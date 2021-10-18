package com.simit.fota.controller;

import com.simit.fota.annotation.LoginRequired;
import com.simit.fota.entity.*;
import com.simit.fota.exception.GlobalException;
import com.simit.fota.result.CodeMsg;
import com.simit.fota.result.Page;
import com.simit.fota.result.Result;
import com.simit.fota.service.ProjectService;
import com.simit.fota.service.VersionService;
import org.apache.commons.lang3.StringUtils;
import org.apache.xmlbeans.impl.xb.ltgfmt.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/fota/api/ver")
public class VersionController {

    @Autowired
    private VersionService versionService;

    @Autowired
    private ProjectService projectService;

    @Value("${file.upload-dir}")
    private String uploadPath;

    /**
     * 删除版本
     *
     * @param version 封装项目id和版本id
     * @return
     */
    @DeleteMapping("/del/{Fota_Project_ID}/{Version_ID}")
    public Result<Boolean> deleteVersion(@RequestBody Version version,@PathVariable("Fota_Project_ID") Integer fotaProjectId,@PathVariable("Version_ID") Integer versionId) {
        if (version == null) {
            throw new GlobalException(CodeMsg.PARAM_ERROR);
        }
        version.setFotaProjectID(fotaProjectId);
        version.setID(versionId);
        if (version.getFotaProjectID() == null) {
            throw new GlobalException(CodeMsg.PROJECT_ID_EMPTY);
        }
        if (version.getID() == null) {
            throw new GlobalException(CodeMsg.VERSIONID_EMPTY);
        }
        versionService.deleteVersion(version);
        return Result.success(true, "version");
    }

    /**
     * @param fotaProjectName
     * @return
     */
    @GetMapping("/versions/{fotaProjectName}")
    public Result<List<String>> versions(@PathVariable("fotaProjectName") String fotaProjectName) {
        FotaProject project = projectService.findProjectByName(fotaProjectName);
        List<String> res = new ArrayList<>();
        if (project != null) {
            res = versionService.findVersionsByPid(project.getID());
        }
        if (res == null) {
            res = new ArrayList<>();
        }
        return Result.success(res, "versions");
    }

    /**
     * 项目的版本列表
     *
     * @param page
     * @param fotaProjectId 项目id
     * @return
     */
    @GetMapping("/list")
    public Result<Page<Version>> versionList(Page page, @RequestParam("Fota_Project_ID") Integer fotaProjectId) {
        if (fotaProjectId == null) {
            throw new GlobalException(CodeMsg.PROJECT_ID_EMPTY);
        }
        Page<Version> res = versionService.findVersionByPage(page, fotaProjectId);
        return Result.success(res, "list");
    }

    /**
     * 上传文件版本
     *
     * @param versionUpload 封装版本信息
     * @return
     */
    @PostMapping("/add")
    public Result<Boolean> createVersion(VersionUpload versionUpload, Integer Fota_Project_ID) {


        if (Fota_Project_ID != null) {
            versionUpload.setFotaProjectID(Fota_Project_ID);
        }

        //判断参数是否合法
        //项目id和版本名是否存在
        if (versionUpload.getFotaProjectID() == null) {
            return Result.error(CodeMsg.PROJECT_ID_EMPTY, "version");
        }
        if (StringUtils.isEmpty(versionUpload.getVersionName())) {
            return Result.error(CodeMsg.VERSION_NAME_EMPTY, "version");
        }
        if (StringUtils.isEmpty(versionUpload.getPreVersion())) {
            return Result.error(CodeMsg.PREVERSION_EMPTY, "version");
        }

        //项目是否存在
        FotaProject projectById = projectService.findProjectById(versionUpload.getFotaProjectID());
        if (projectById == null || projectById.getDelTag().equals("1")) {
            return Result.error(CodeMsg.PROJECT_NOT_EXIST, "version");
        }

        //之前的版本是否存在
        Version preVersion = versionService.findVersionByPidVname(versionUpload.getFotaProjectID(), versionUpload.getPreVersion());
        if (preVersion == null) {
            return Result.error(CodeMsg.PREVERSION_NOT_EXIST, "version");
        }

        //文件是否均为空
        if (versionUpload.getDifferFile() == null && versionUpload.getFullFile() == null) {
            return Result.error(CodeMsg.NOT_ALLOWED_EMPTY_FILE, "version");
        }

        //文件合法性判断
        String suffix1 = null;
        String suffix2 = null;
        if (versionUpload.getFullFile() != null) {
            String fileName = versionUpload.getFullFile().getOriginalFilename();
            suffix1 = fileName.substring(fileName.lastIndexOf("."));
        }
        if (versionUpload.getDifferFile() != null) {
            String fileName = versionUpload.getDifferFile().getOriginalFilename();
            suffix2 = fileName.substring(fileName.lastIndexOf("."));
        }
        if (StringUtils.isEmpty(suffix1) && StringUtils.isEmpty(suffix2)) {
            return Result.error(CodeMsg.FILE_FORMAT_IS_INCORRECT, "version");
        }

        //增加相应版本
        versionService.addVersion(versionUpload);
        return Result.success(CodeMsg.VERSION_ADD_SUCCESS, "version");
    }

    @GetMapping("/download")
    @LoginRequired
    public Result<Boolean> downloadFile(String fileName, HttpServletResponse response) {
        System.out.println(fileName);
        System.out.println(uploadPath + "\\" + fileName);
        File file = new File(uploadPath + "\\" + fileName);
        if (file.exists()) {
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                ServletOutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new GlobalException(CodeMsg.FILE_NOT_EXIST);
        }
        return Result.success(true, "download");
    }


    @PutMapping("/edit/{Fota_Project_ID}/{Version_ID}")
    public Result<Boolean> updateVersion(VersionUpload versionUpload,@PathVariable("Fota_Project_ID") Integer projectId,@PathVariable("Version_ID") Integer versionId){
        if (projectId == null){
            throw new GlobalException(CodeMsg.PROJECT_ID_EMPTY);
        }
        if (versionId == null){
            throw new GlobalException(CodeMsg.VERSIONID_EMPTY);
        }
        versionService.doUpdate(versionUpload,projectId,versionId);
        return Result.success(true,"version");
    }


    @GetMapping("/edit/{Fota_Project_ID}/{Version_ID}")
    public Result<Version> getVersionAttribute(@PathVariable("Fota_Project_ID") Integer projectId,@PathVariable("Version_ID") Integer versionId){
        if (projectId == null){
            throw new GlobalException(CodeMsg.PROJECT_ID_EMPTY);
        }
        if (versionId == null){
            throw new GlobalException(CodeMsg.VERSIONID_EMPTY);
        }
        Version version = versionService.findVersionById(projectId,versionId);
        return Result.success(version,"version");
    }
}
