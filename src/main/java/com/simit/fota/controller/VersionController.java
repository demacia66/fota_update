package com.simit.fota.controller;

import com.simit.fota.annotation.LoginRequired;
import com.simit.fota.entity.*;
import com.simit.fota.exception.GlobalException;
import com.simit.fota.result.CodeMsg;
import com.simit.fota.result.Page;
import com.simit.fota.result.Result;
import com.simit.fota.service.ProjectService;
import com.simit.fota.service.VersionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.xmlbeans.impl.xb.ltgfmt.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.rmi.runtime.Log;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fota/api/ver")

@Slf4j
public class VersionController {

    @Autowired
    private VersionService versionService;

    @Autowired
    private ProjectService projectService;

    @Value("${file.upload-dir}")
    private String uploadPath;

    //删除版本
    @DeleteMapping("/del/{Fota_Project_ID}/{Version_ID}")
    public Result<Boolean> deleteVersion(@PathVariable("Fota_Project_ID") Integer fotaProjectId,@PathVariable("Version_ID") Integer versionId) {
        //@RequestBody Version version
        Version version = new Version();
//        if (version == null) {
//            throw new GlobalException(CodeMsg.PARAM_ERROR);
//        }
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


    //对应项目的版本列表
    @GetMapping("/versions/{fotaProjectId}")
    public Result<List<Map<String,String>>> versions(@PathVariable("fotaProjectId") Integer fotaProjectID) {
        FotaProject project = projectService.findProjectById(fotaProjectID);
        List<Version> cur = new ArrayList<>();
        if (project != null) {
            cur = versionService.findVersionsByPid(project.getID());
        }
        if (cur == null) {
            cur = new ArrayList<>();
        }
        List<Map<String,String>> res = new ArrayList<>();
        for(Version version : cur) {
            Map<String,String> map = new HashMap<>();
            map.put("id",version.getID().toString());
            map.put("version",version.getVersionName());
            res.add(map);
        }
        return Result.success(res, "versions");
    }

    //对应项目名的版本列表
    @GetMapping("/versions1/{projectName}")
    public Result<List<Map<String,String>>> versions1(@PathVariable("projectName") String projectName) {
        FotaProject project = projectService.findProjectByName(projectName);
//        List<String> cur = new ArrayList<>();
//        if (project != null) {
//            cur = versionService.findVersionsByPid1(project.getID());
//        }
//        if (cur == null) {
//            cur = new ArrayList<>();
//        }
        List<Version> cur = new ArrayList<>();
        if (project != null) {
            cur = versionService.findVersionsByPid(project.getID());
        }
        if (cur == null) {
            cur = new ArrayList<>();
        }
        List<Map<String,String>> res = new ArrayList<>();
        for(Version version : cur) {
            Map<String,String> map = new HashMap<>();
            map.put("id",version.getID().toString());
            map.put("version",version.getVersionName());
            res.add(map);
        }
        return Result.success(res, "versions");
//
//        return Result.success(cur, "versions");
    }

    /**
     * 项目的版本列表
     *
     * @param page
     * @param fotaProjectId 项目id
     * @return
     */
    @GetMapping("/list/{Fota_Project_ID}")
    public Result<Page<Version>> versionList(Page page, @PathVariable("Fota_Project_ID") Integer fotaProjectId) {
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


        log.error(versionUpload.getPreVersionId().toString());

        if (Fota_Project_ID != null) {
            versionUpload.setFotaProjectID(Fota_Project_ID);
        }

        //判断参数是否合法
        //项目id和版本名是否存在
        if (versionUpload.getFotaProjectID() == null) {
            return Result.error(CodeMsg.PROJECT_ID_EMPTY, "version");
        }

        if (StringUtils.isEmpty(versionUpload.getVersion_Name())) {
            return Result.error(CodeMsg.VERSION_NAME_EMPTY, "version");
        }

//        if (StringUtils.isEmpty(versionUpload.getPreVersion())) {
//            return Result.error(CodeMsg.PREVERSION_EMPTY, "version");
//        }

        //项目是否存在
        FotaProject projectById = projectService.findProjectById(versionUpload.getFotaProjectID());
        if (projectById == null || projectById.getDelTag().equals("1")) {
            return Result.error(CodeMsg.PROJECT_NOT_EXIST, "version");
        }

        //之前的版本是否存在
        Version preVersion = versionService.findVersionById(versionUpload.getFotaProjectID(), versionUpload.getPreVersionId());
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

    @Deprecated
    @GetMapping("/download")
//    @LoginRequired
    public void downloadFile(String fileName, HttpServletResponse response) {
        log.error("开始下载");
        File file = new File(uploadPath + "/" + fileName);
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

    }


    //更改版本
    @PostMapping("/edit/{Fota_Project_ID}/{Version_ID}")
    public Result<Boolean> updateVersion(VersionUpload versionUpload,@PathVariable("Fota_Project_ID") Integer projectId,@PathVariable("Version_ID") Integer versionId){
        if (projectId == null){
            throw new GlobalException(CodeMsg.PROJECT_ID_EMPTY);
        }
        if (versionId == null){
            throw new GlobalException(CodeMsg.VERSIONID_EMPTY);
        }
        versionUpload.setFotaProjectID(projectId);
        versionService.doUpdate(versionUpload,projectId,versionId);
        return Result.success(true,"version");
    }


    //获取版本属性信息
    @GetMapping("/edit/{Fota_Project_ID}/{Version_ID}")
    public Result<Version> getVersionAttribute(@PathVariable("Fota_Project_ID") Integer projectId,@PathVariable("Version_ID") Integer versionId){
        log.error("开始下载");
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
