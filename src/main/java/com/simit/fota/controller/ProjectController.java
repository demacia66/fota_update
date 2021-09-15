package com.simit.fota.controller;

import com.simit.fota.entity.FotaProject;
import com.simit.fota.entity.ProjectListVo;
import com.simit.fota.entity.ProjectVo;
import com.simit.fota.exception.GlobalException;
import com.simit.fota.result.CodeMsg;
import com.simit.fota.result.Page;
import com.simit.fota.result.Result;
import com.simit.fota.service.ProjectService;
import com.simit.fota.util.JWTTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/fota/api/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    /**
     * 创建项目
     * @param projectVo 项目名和初始版本以及描述信息
     * @param request
     * @return
     */
    @PostMapping("/add")
    public Result<CodeMsg> createProject(@RequestBody ProjectVo projectVo, HttpServletRequest request) {
        String token = request.getHeader("token");
        projectService.createProject(projectVo,token);
        return Result.success(CodeMsg.PROJECT_ADD_SUCCESS, "version");
    }

    /**
     * 删除项目
     * @param projectId
     * @return
     */
    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/del/{Fota_Project_ID}")
    public Result<Boolean> delProject(@PathVariable("Fota_Project_ID") Integer projectId) {
        if (projectId == null) {
            throw new GlobalException(CodeMsg.PROJECT_ID_EMPTY);
        }
        projectService.deleteProject(projectId);
        return Result.success(true, "device");
    }

    /**
     * 获取当前所有的项目名
     * @return
     */
    @GetMapping("/projectName")
    public Result<List<String>> projectName(){
        List<String> res = projectService.findAllProject();
        return Result.success(res,"project_name");
    }

    /**
     * 项目版本
     * @param page 显示页面的参数
     * @return
     */
    @GetMapping("/list")
    public Result<Page<ProjectListVo>> projectList( Page page){
        Page<ProjectListVo> res = projectService.findProjectsByPage(page);
        return Result.success(res,"list");
    }


    /**
     * 更新项目信息
     * @param FotaProjectID
     * @param project
     * @return
     */
    @PutMapping("/edit/{Fota_Project_ID}")
    public Result<Boolean> updateProject(@PathVariable("Fota_Project_ID") Integer FotaProjectID , @RequestBody ProjectVo project){
        if (project.getFotaProjectID() == null){
            project.setFotaProjectID(FotaProjectID);
        }
        projectService.updateProject(project);
        return Result.success(true,"edit");
    }

}
