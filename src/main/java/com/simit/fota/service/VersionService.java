package com.simit.fota.service;

import com.simit.fota.dao.VersionMapper;
import com.simit.fota.entity.FotaProject;
import com.simit.fota.entity.Version;
import com.simit.fota.exception.GlobalException;
import com.simit.fota.result.CodeMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VersionService {
    @Autowired
    private VersionMapper versionMapper;

    @Autowired
    private ProjectService projectService;

    /**
     * 新建版本
     * @param projectId
     * @param versionName
     * @param description
     */
    public void createVersion(int projectId,String versionName,String description){
        Version version = new Version();
        Version preVersion = versionMapper.findVersionByNameId(versionName, projectId);
        if (preVersion != null){
            if ("0".equals(preVersion.getDelTag())){
                throw new GlobalException(CodeMsg.VERSION_DUPLICATE);
            }
            preVersion.setCreateTs(System.currentTimeMillis());
            preVersion.setDescription(description);
            versionMapper.recoverVersion(preVersion);
            return;
        }
        version.setFotaProjectID(projectId);
        version.setDelTag("0");
        version.setVersionName(versionName);
        version.setDescription(description);
        version.setCreateTs(System.currentTimeMillis());
        versionMapper.insertVersion(version);
    }

    public String curVersion(Integer projectId){

        FotaProject project = projectService.findProjectById(projectId);
        if (project == null){
            throw new GlobalException(CodeMsg.PROJECT_NOT_EXIST);
        }
        Version version = versionMapper.findLatestVersion(projectId);
        return version.getVersionName();
    }

}
