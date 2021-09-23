package com.simit.fota.service;

import com.simit.fota.dao.ProjectMapper;
import com.simit.fota.entity.*;
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
    @Transactional(rollbackFor = Exception.class)
    public void createProject(ProjectVo projectVo,String token){
        FotaProject project = new FotaProject();

        //检查参数是否正确
        if (StringUtils.isEmpty(projectVo.getFotaProjectName())){
            throw new GlobalException(CodeMsg.PROJECT_NAME_EMPTY);
        }
        if (StringUtils.isEmpty(projectVo.getInitialVersion())){
            throw new GlobalException(CodeMsg.INTIAL_VERSION_EMPTY);
        }
        //查看是否已经存在这个项目
        FotaProject projectByName = projectMapper.findProjectByName(projectVo.getFotaProjectName());
        if (projectByName != null){
            throw new GlobalException(CodeMsg.PROJECT_DUPLICATE);
        }
        //创建项目
        project.setFotaProjectName(projectVo.getFotaProjectName());
        project.setCreator(JWTTokenUtil.getUserInfoFromToken(token));
        project.setDelTag("0");
        project.setCreateTs(System.currentTimeMillis());
        projectMapper.insertFotaProject(project);
        //创建版本
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

    /**
     * 获取项目信息列表
     * @param page
     * @return
     */
    public Page<ProjectListVo> findProjectsByPage(Page page) {

        //处理页面信息
        int totalCount = projectMapper.findProjectCount();
        if (page == null) {
            page = new Page();
        }else {
            page = new Page(totalCount, page.getCurrentPage(), page.getPageSize());
        }

        //项目信息
        List<FotaProject> projects = projectMapper.findAllProjects(page);
        if (projects == null || projects.size() == 0){
            page.setDataList(new ArrayList());
            return page;
        }

        List<ProjectListVo> res = new ArrayList<>();
        //处理显示的信息
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

    public FotaProject findProjectByName(String name){
        return projectMapper.findProjectByName(name);
    }

    public void updateProject(ProjectVo project) {
        if(project.getFotaProjectID() == null){
            throw new GlobalException(CodeMsg.PROJECT_ID_EMPTY);
        }
        FotaProject preProject = projectMapper.findProjectById(project.getFotaProjectID());
        if (!StringUtils.isEmpty(project.getFotaProjectName()) && !preProject.getFotaProjectName().equals(project.getFotaProjectName())){
            FotaProject projectByName = projectMapper.findProjectByName(project.getFotaProjectName());
            if (projectByName != null){
                throw new GlobalException(CodeMsg.PROJECT_DUPLICATE);
            }
        }
        if (StringUtils.isEmpty(project.getFotaProjectName())){
            project.setFotaProjectName(preProject.getFotaProjectName());
        }

        projectMapper.updateProName(project);

        if (project.getDescription() == null){
            return;
        }

        Version version = versionService.findInitialVersion(project.getFotaProjectID());
        if (version.getDescription() != null && version.getDescription().equals(project.getDescription())){
            return;
        }
        version.setDescription(project.getDescription());
        versionService.updateInitialVersion(version);
    }
}
