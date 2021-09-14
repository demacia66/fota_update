package com.simit.fota.controller;

import com.simit.fota.entity.FotaProject;
import com.simit.fota.entity.ProjectVo;
import com.simit.fota.exception.GlobalException;
import com.simit.fota.result.CodeMsg;
import com.simit.fota.result.Result;
import com.simit.fota.service.ProjectService;
import com.simit.fota.util.JWTTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/fota/api/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;


    @PostMapping("/add")
    public Result<CodeMsg> createProject(@RequestBody ProjectVo projectVo, HttpServletRequest request) {

        String token = request.getHeader("token");
        projectService.createProject(projectVo,token);
        return Result.success(CodeMsg.PROJECT_ADD_SUCCESS, "version");
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/del/{Fota_Project_ID}")
    public Result<Boolean> delProject(@PathVariable("Fota_Project_ID") Integer projectId) {
        if (projectId == null) {
            throw new GlobalException(CodeMsg.PROJECT_ID_EMPTY);
        }
        projectService.deleteProject(projectId);
        return Result.success(true, "device");
    }

}
