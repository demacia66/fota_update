package com.simit.fota.service;

import com.simit.fota.dao.ProjectMapper;
import com.simit.fota.entity.FotaProject;
import com.simit.fota.entity.ProjectVo;
import com.simit.fota.exception.GlobalException;
import com.simit.fota.result.CodeMsg;
import com.simit.fota.util.JWTTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        FotaProject projectByName = projectMapper.findProjectByName(project.getFotaProjectName());
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

}
