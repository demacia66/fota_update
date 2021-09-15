package com.simit.fota.service;

import com.simit.fota.dao.VersionMapper;
import com.simit.fota.entity.FotaProject;
import com.simit.fota.entity.Version;
import com.simit.fota.entity.VersionVo;
import com.simit.fota.exception.GlobalException;
import com.simit.fota.result.CodeMsg;
import com.simit.fota.result.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 获取当前版本名，用于项目列表
     * @param projectId 项目id
     * @return
     */
    public String curVersion(Integer projectId){

        FotaProject project = projectService.findProjectById(projectId);
        if (project == null){
            throw new GlobalException(CodeMsg.PROJECT_NOT_EXIST);
        }
        Version version = versionMapper.findLatestVersion(projectId);
        return version.getVersionName();
    }

    /**
     * 删除版本
     * @param version
     */
    public void deleteVersion(Version version) {
        Version version1 = versionMapper.findVersionByVId(version);
        if (version1 != null && !"1".equals(version1.getDelTag())){
            //删除版本和版本的关系
            versionMapper.delVersion(version);
            versionMapper.delVersionRelation(version);
        }else {
            throw new GlobalException(CodeMsg.VERSION_NOT_EXIST);
        }
    }

    /**
     * 项目的版本列表
     * @param page 页面信息
     * @param fotaProjectId 项目 id
     * @return
     */
    public Page<Version> findVersionByPage(Page page, Integer fotaProjectId) {

        //处理页面信息
        int totalCount = versionMapper.findVersionCount(fotaProjectId);
        if (page == null) {
            page = new Page();
        }else {
            page = new Page(totalCount, page.getCurrentPage(), page.getPageSize());
        }

        //找到项目的版本列表
        List<VersionVo> versionList = versionMapper.findVersionsByPId(fotaProjectId,page);
        if (versionList == null){
            versionList = new ArrayList<>();
        }
        page.setDataList(versionList);
        return page;
    }

    public Version findInitialVersion(Integer fotaProjectID) {
        return versionMapper.findInitialVersion(fotaProjectID);
    }

    public void updateInitialVersion(Version version) {
        versionMapper.updateVersionById(version);
    }
}
