package com.simit.fota.controller;

import com.simit.fota.entity.ProjectListVo;
import com.simit.fota.entity.Version;
import com.simit.fota.entity.VersionUpload;
import com.simit.fota.entity.VersionVo;
import com.simit.fota.exception.GlobalException;
import com.simit.fota.result.CodeMsg;
import com.simit.fota.result.Page;
import com.simit.fota.result.Result;
import com.simit.fota.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fota/api/ver")
public class VersionController {

    @Autowired
    private VersionService versionService;

    /**
     * 删除版本
     * @param version 封装项目id和版本id
     * @return
     */
    @DeleteMapping("/del")
    public Result<Boolean> deleteVersion(@RequestBody Version version){
        if (version == null){
            throw new GlobalException(CodeMsg.PARAM_ERROR);
        }
        if (version.getFotaProjectID() == null){
            throw new GlobalException(CodeMsg.PROJECT_ID_EMPTY);
        }
        if (version.getID() == null){
            throw new GlobalException(CodeMsg.VERSIONID_EMPTY);
        }
        versionService.deleteVersion(version);
        return Result.success(true,"version");
    }


    /**
     * 项目的版本列表
     * @param page
     * @param fotaProjectId 项目id
     * @return
     */
    @GetMapping("/list")
    public Result<Page<Version>> versionList(Page page,@RequestParam("Fota_Project_ID") Integer fotaProjectId){
        if (fotaProjectId == null){
            throw new GlobalException(CodeMsg.PROJECT_ID_EMPTY);
        }
        Page<Version> res = versionService.findVersionByPage(page,fotaProjectId);
        return Result.success(res,"list");
    }

    @PostMapping("/add")
    public Result<Boolean> createVersion(@RequestBody VersionUpload versionUpload){
        return Result.success(true,"version");
    }
}
