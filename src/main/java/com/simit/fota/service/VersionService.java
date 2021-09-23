package com.simit.fota.service;

import com.simit.fota.dao.VersionMapper;
import com.simit.fota.entity.*;
import com.simit.fota.exception.GlobalException;
import com.simit.fota.result.CodeMsg;
import com.simit.fota.result.Page;
import com.simit.fota.util.FotaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class VersionService {
    @Autowired
    private VersionMapper versionMapper;

    @Autowired
    private ProjectService projectService;

    @Value("${file.upload-dir}")
    private String uploadPath;

    @Value("${differfile}")
    private String differFile1;

    @Value("${fullfile}")
    private String fullFile1;

    /**
     * 新建版本
     *
     * @param projectId
     * @param versionName
     * @param description
     */
    public int createVersion(int projectId, String versionName, String description) {
        Version initialVersion = versionMapper.findInitialVersion(projectId);

        Version version = new Version();
        Version preVersion = versionMapper.findVersionByNameId(versionName, projectId);
        if (preVersion != null) {
            if ("0".equals(preVersion.getDelTag())) {
                throw new GlobalException(CodeMsg.VERSION_DUPLICATE);
            }
            preVersion.setCreateTs(System.currentTimeMillis());
            preVersion.setDescription(description);
            versionMapper.recoverVersion(preVersion);
        }else {
            version.setFotaProjectID(projectId);
            version.setDelTag("0");
            version.setVersionName(versionName);
            version.setDescription(description);
            version.setCreateTs(System.currentTimeMillis());
            versionMapper.insertVersion(version);
        }
        if (preVersion != null){
            version.setID(preVersion.getID());
        }
        if (initialVersion == null){
            VersionRelation relation = new VersionRelation();
            relation.setFotaProjectID(projectId);
            relation.setDelTag("0");
            relation.setNextVersionID(0);
            relation.setPrevVersionID(0);
            relation.setVersionID(version.getID());
            versionMapper.insertInitialVersionRelation(relation);
        }
        return version.getID();
    }

    /**
     * 获取当前版本名，用于项目列表
     *
     * @param projectId 项目id
     * @return
     */
    public String curVersion(Integer projectId) {

        FotaProject project = projectService.findProjectById(projectId);
        if (project == null) {
            throw new GlobalException(CodeMsg.PROJECT_NOT_EXIST);
        }
        Version version = versionMapper.findLatestVersion(projectId);
        return version.getVersionName();
    }

    /**
     * 删除版本
     *
     * @param version
     */
    public void deleteVersion(Version version) {
        Version version1 = versionMapper.findVersionByVId(version);
        if (version1 != null && !"1".equals(version1.getDelTag())) {
            //删除版本和版本的关系
            versionMapper.delVersion(version);
            versionMapper.delVersionRelation(version);
        } else {
            throw new GlobalException(CodeMsg.VERSION_NOT_EXIST);
        }
    }

    /**
     * 项目的版本列表
     *
     * @param page          页面信息
     * @param fotaProjectId 项目 id
     * @return
     */
    public Page<Version> findVersionByPage(Page page, Integer fotaProjectId) {

        //处理页面信息
        int totalCount = versionMapper.findVersionCount(fotaProjectId);
        if (page == null) {
            page = new Page();
        } else {
            page = new Page(totalCount, page.getCurrentPage(), page.getPageSize());
        }

        //找到项目的版本列表
        List<VersionVo> versionList = versionMapper.findVersionsByPId(fotaProjectId, page);
        if (versionList == null) {
            versionList = new ArrayList<>();
        }
        page.setDataList(versionList);
        return page;
    }

    public Version findVersionByPidVname(Integer projectId, String versionName) {
        Version version = new Version();
        version.setVersionName(versionName);
        version.setFotaProjectID(projectId);
        return versionMapper.findVersionByVName(version);
    }

    public Version findInitialVersion(Integer fotaProjectID) {
        return versionMapper.findInitialVersion(fotaProjectID);
    }

    public void updateInitialVersion(Version version) {
        versionMapper.updateVersionById(version);
    }

    /**
     * 创建新版本并存放文件
     *
     * @param versionUpload
     */
    @Transactional(rollbackFor = Exception.class)
    public void addVersion(VersionUpload versionUpload) {
        int versionId = createVersion(versionUpload.getFotaProjectID(), versionUpload.getVersionName(), versionUpload.getDescription());
        MultipartFile fullFile = versionUpload.getFullFile();
        if (fullFile != null) {
            VersionFiles versionFiles = uploadFile(fullFile);
            versionFiles.setFileType("1");
            versionFiles.setFotaProjectID(versionUpload.getFotaProjectID());
            versionFiles.setVersionID(versionId);
            versionMapper.insertVersionFile(versionFiles);
        }

        MultipartFile differFile = versionUpload.getDifferFile();
        if (differFile != null){
            VersionFiles versionFiles = uploadFile(differFile);
            versionFiles.setFileType("0");
            versionFiles.setFotaProjectID(versionUpload.getFotaProjectID());
            versionMapper.insertVersionFile(versionFiles);
        }
        Version preVersion = versionMapper.findVersionByNameId(versionUpload.getPreVersion(),versionUpload.getFotaProjectID());
        VersionRelation relation = new VersionRelation();
        relation.setNextVersionID(0);
        relation.setVersionID(versionId);
        relation.setPrevVersionID(preVersion.getID());
        relation.setDelTag("0");
        relation.setFotaProjectID(versionUpload.getFotaProjectID());
        versionMapper.insertInitialVersionRelation(relation);


        //更新先前的版本关系
        versionMapper.updateRelation(relation);

    }


    public VersionFiles uploadFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        fileName = FotaUtil.generateUUID() + fileType;
        File dest = new File(uploadPath + "/" + fileName);
        try {
            InputStream is = file.getInputStream();
            file.transferTo(dest);
            is.mark(0);
            String md5 = DigestUtils.md5DigestAsHex(is);
            VersionFiles versionFiles = new VersionFiles();
            versionFiles.setFileMD5(md5);
            versionFiles.setFileName(fileName);
            versionFiles.setUploadTs(System.currentTimeMillis());
            versionFiles.setFileURL("/fota/api/ver/download?fileName=" + fileName);
            return versionFiles;
        } catch (IOException e) {
            e.printStackTrace();
            throw new GlobalException(CodeMsg.FILE_UPLOAD_ERROR);
        }
    }

    public List<String> findVersionsByPid(Integer id) {

        return versionMapper.findVersionNamesByPId(id);
    }
}
