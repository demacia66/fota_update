package com.simit.fota.service;

import com.simit.fota.dao.ProjectMapper;
import com.simit.fota.entity.Device;
import com.simit.fota.entity.FotaProject;
import com.simit.fota.entity.ProjectListVo;
import com.simit.fota.entity.ProjectVo;
import com.simit.fota.exception.GlobalException;
import com.simit.fota.result.CodeMsg;
import com.simit.fota.result.Page;
import com.simit.fota.util.DateFormatUtil;
import com.simit.fota.util.JWTTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {
    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private VersionService versionService;

    //新建项目
    @Transactional
    public void createProject(ProjectVo projectVo,String token){
        FotaProject project = new FotaProject();
        if (StringUtils.isEmpty(projectVo.getFotaProjectName())){
            throw new GlobalException(CodeMsg.PROJECT_NAME_EMPTY);
        }
        if (StringUtils.isEmpty(projectVo.getInitialVersion())){
            throw new GlobalException(CodeMsg.INTIAL_VERSION_EMPTY);
        }
        FotaProject projectByName = projectMapper.findProjectByName(projectVo.getFotaProjectName());
        if (projectByName != null){
            throw new GlobalException(CodeMsg.PROJECT_DUPLICATE);
        }
        project.setFotaProjectName(projectVo.getFotaProjectName());
        project.setCreator(JWTTokenUtil.getUserInfoFromToken(token));
        project.setDelTag("0");
        project.setCreateTs(System.currentTimeMillis());
        projectMapper.insertFotaProject(project);
        versionService.createVersion(project.getID(),projectVo.getInitialVersion(),projectVo.getDescription());
    }


    public void deleteProject(Integer projectId){
        projectMapper.delProjectById(projectId);
    }

    public List<String> findAllProject() {
        return projectMapper.findProjects();
    }

    public FotaProject findProjectById(Integer projectId) {
        if (projectId == null){
            return null;
        }
        return projectMapper.findProjectById(projectId);
    }

    public Page<ProjectListVo> findProjectsByPage(Page page) {

        int totalCount = projectMapper.findProjectCount();
        if (page == null) {
            page = new Page();
        }else {
            page = new Page(totalCount, page.getCurrentPage(), page.getPageSize());
        }

        List<FotaProject> projects = projectMapper.findAllProjects(page);
        if (projects == null || projects.size() == 0){
            page.setDataList(new ArrayList());
            return page;
        }

        List<ProjectListVo> res = new ArrayList<>();
        for (FotaProject cur : projects) {
            ProjectListVo vo = new ProjectListVo();
            vo.setCreateTs(DateFormatUtil.formatDate(cur.getCreateTs()));
            vo.setCurVersion(versionService.curVersion(cur.getID()));
            vo.setFotaProjectName(cur.getFotaProjectName());
            vo.setID(cur.getID());
            res.add(vo);
        }
        page.setDataList(res);
        return page;
    }
}
