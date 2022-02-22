package com.simit.fota.service;

import com.simit.fota.dao.VersionMapper;
import com.simit.fota.entity.*;
import com.simit.fota.exception.GlobalException;
import com.simit.fota.result.CodeMsg;
import com.simit.fota.result.Page;
import com.simit.fota.util.DateFormatUtil;
import com.simit.fota.util.FotaUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
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

    @Value("${fileUploadPath}")
    private String fileUploadPath;

    @Value("${ftpHost}")
    private String ftpHost;

    @Value("${ftpPort}")
    private int ftpPort;

    @Value("${ftpUsername}")
    private String ftpUsername;

    @Value("${ftpPassword}")
    private String ftpPassword;

    public void deleteProjectVersion(Integer projectId) {
        versionMapper.delVersionByPid(projectId);
        versionMapper.delVersionRelationByPid(projectId);
    }

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
//        Version preVersion = versionMapper.findVersionByNameId(versionName, projectId);
//                throw new GlobalException(CodeMsg.VERSION_DUPLICATE);
        version.setFotaProjectID(projectId);
        version.setDelTag("0");
        version.setVersionName(versionName);
        version.setDescription(description);
        version.setCreateTs(System.currentTimeMillis());
        versionMapper.insertVersion(version);
        if (initialVersion == null) {
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
            Version initialVersion = findInitialVersion(version1.getFotaProjectID());
            if (!(initialVersion == null) && initialVersion.getID().equals(version1.getID())) {
                throw new GlobalException(CodeMsg.DELETE_INTIAL_VERSION);
            }
            String curVersion = curVersion(version1.getFotaProjectID());
            if (!curVersion.equals(version1.getVersionName())) {
                throw new GlobalException(CodeMsg.DELETE_CURVERSION);
            }
            versionMapper.delVersion(version);
            versionMapper.delVersionRelation(version);
            versionMapper.updateNextVersion(version);
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
            page.setOrderType("desc");
        } else if (page.getOrderField() == null) {
            page = new Page(totalCount, page.getCurrentPage(), page.getPageSize());
            page.setOrderType("desc");
        }
        page.setTotalCount(totalCount);

        //找到项目的版本列表
        Version initialVersion = findInitialVersion(fotaProjectId);
        Version latestVersion = versionMapper.findLatestVersion(fotaProjectId);
        List<VersionVo> versionList = versionMapper.findVersionsByPId(fotaProjectId, page);
        if (versionList == null) {
            versionList = new ArrayList<>();
        }
        for (VersionVo cur : versionList) {
            cur.setTs(DateFormatUtil.formatDate(cur.getCreateTs()));
            if (cur.getID().equals(initialVersion.getID())) {
                cur.setFlag("1");
            } else if (cur.getID().equals(latestVersion.getID())) {
                cur.setFlag("2");
            } else {
                cur.setFlag("0");
            }
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


    public int findSwlrse(String version) {
        return versionMapper.findVersionName(version);
    }

    /**
     * 创建新版本并存放文件
     *
     * @param versionUpload
     */
    @Transactional(rollbackFor = Exception.class)
    public void addVersion(VersionUpload versionUpload) {
        int versionId = createVersion(versionUpload.getFotaProjectID(), versionUpload.getVersion_Name(), versionUpload.getDescription());
        MultipartFile fullFile = versionUpload.getFullFile();
        if (fullFile != null) {
            VersionFiles versionFiles = uploadFile(fullFile);
            versionFiles.setFileType("1");
            versionFiles.setFotaProjectID(versionUpload.getFotaProjectID());
            versionFiles.setVersionID(versionId);
            versionMapper.insertVersionFile(versionFiles);
        }

        MultipartFile differFile = versionUpload.getDifferFile();
        if (differFile != null) {
            VersionFiles versionFiles = uploadFile(differFile);
            versionFiles.setFileType("0");
            versionFiles.setFotaProjectID(versionUpload.getFotaProjectID());
            versionFiles.setVersionID(versionId);
            versionMapper.insertVersionFile(versionFiles);
        }
//        Version preVersion = versionMapper.findVersionByNameId(versionUpload.getPreVersion(), versionUpload.getFotaProjectID());
        Version preVersion = findVersionById(versionUpload.getFotaProjectID(),versionUpload.getPreVersionId());
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
//        File dest = new File(uploadPath + "/" + fileName);
        try {
            InputStream is = file.getInputStream();
//            file.transferTo(dest);
            uploadFile(fileName, is);
            is.mark(0);
            String md5 = DigestUtils.md5DigestAsHex(is);
            VersionFiles versionFiles = new VersionFiles();
            versionFiles.setFileMD5(md5);
            versionFiles.setFileName(fileName);
            versionFiles.setUploadTs(System.currentTimeMillis());
            versionFiles.setFileURL(fileName);
            return versionFiles;
        } catch (IOException e) {
            e.printStackTrace();
            throw new GlobalException(CodeMsg.FILE_UPLOAD_ERROR);
        }
    }

    public List<Version> findVersionsByPid(Integer id) {

        return versionMapper.findVersionNamesByPId(id);
    }

    public List<String> findVersionsByPid1(Integer id) {

        return versionMapper.findVersionNamesByPId1(id);
    }

    public List<VersionFiles> findVersionFile(Integer fotaProjectID, Integer id) {
        return versionMapper.findVersionFile(id, fotaProjectID);
    }

    @Transactional(rollbackFor = Exception.class)

    public void doUpdate(VersionUpload versionUpload, Integer projectId, Integer versionId) {
        Version version = new Version();
        version.setID(versionId);
        version.setFotaProjectID(projectId);
        Version findVersion = versionMapper.findVersionByVId(version);

        if (findVersion == null) {
            throw new GlobalException(CodeMsg.VERSION_NOT_EXIST);
        }

        if (StringUtils.isEmpty(versionUpload.getVersion_Name())) {
            version.setVersionName(findVersion.getVersionName());
        } else {
            version.setVersionName(versionUpload.getVersion_Name());
        }

        if (StringUtils.isEmpty(versionUpload.getDescription())) {
            version.setDescription(findVersion.getDescription());
        } else {
            version.setDescription(versionUpload.getDescription());
        }


        versionMapper.updateVersionById(version);


        MultipartFile fullFile = versionUpload.getFullFile();
        if (fullFile != null) {
            VersionFiles versionFiles = uploadFile(fullFile);
            versionFiles.setFileType("1");
            versionFiles.setFotaProjectID(versionUpload.getFotaProjectID());
            versionFiles.setVersionID(versionId);
            VersionFiles versionFile = versionMapper.findfullFile(versionId, projectId);
            if (versionFile == null) {
                versionMapper.insertVersionFile(versionFiles);
            } else {
                versionMapper.updateVersionFile(versionFiles);
            }
        }

        MultipartFile differFile = versionUpload.getDifferFile();
        if (differFile != null) {
            VersionFiles versionFiles = uploadFile(differFile);
            versionFiles.setFileType("0");
            versionFiles.setFotaProjectID(versionUpload.getFotaProjectID());
            VersionFiles versionFile = versionMapper.findDifferFile(versionId, projectId);
            if (versionFile == null) {
                versionMapper.insertVersionFile(versionFiles);
            } else {
                versionMapper.updateVersionFile(versionFiles);
            }
        }
    }

    public Version findVersionById(Integer projectId, Integer versionId) {
        Version version = new Version();
        version.setID(versionId);
        version.setFotaProjectID(projectId);
        Version versionByVId = versionMapper.findVersionByVId(version);
        if (versionByVId == null) {
            throw new GlobalException(CodeMsg.VERSION_NOT_EXIST);
        }
        versionByVId.setTs(DateFormatUtil.formatDate(versionByVId.getCreateTs()));
        return versionByVId;
    }

    public VersionFiles findFileByType(Integer versionId, Integer projectId, String fileType) {
        VersionFiles versionFiles = null;
        if (StringUtils.isEmpty(fileType)) {
            throw new GlobalException(CodeMsg.FILE_TYPE_EMPTY);
        }
        if (fullFile1.equals(fileType)) {
            return versionMapper.findfullFile(versionId, projectId);
        } else {
            return versionMapper.findDifferFile(versionId, projectId);
        }
    }

    public boolean uploadFile(String filename, InputStream input) {
        boolean success = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(ftpHost);// 连接FTP服务器
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftp.login(ftpUsername, ftpPassword);// 登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return success;
            }
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftp.makeDirectory(fileUploadPath);
            ftp.changeWorkingDirectory(fileUploadPath);
            //设置被动模式
            ftp.enterLocalPassiveMode();
            ftp.storeFile(filename, input);
//            input.close();
            ftp.logout();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return success;
    }


}
