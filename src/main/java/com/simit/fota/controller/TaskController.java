package com.simit.fota.controller;

import com.simit.fota.entity.*;
import com.simit.fota.exception.GlobalException;
import com.simit.fota.result.CodeMsg;
import com.simit.fota.result.Page;
import com.simit.fota.result.Result;
import com.simit.fota.result.TasksVo;
import com.simit.fota.service.TaskService;
import com.simit.fota.util.JWTTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/fota/api/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    //新建任务
    @PostMapping("/add")
    public Result<Boolean> createTask(@RequestBody TaskVo taskVo, HttpServletRequest request){
        log.error(taskVo.getVersionId().toString());
        String token = (String) request.getAttribute("token");
        taskService.createTask(taskVo,"01", JWTTokenUtil.getUserInfoFromToken(token));
        return Result.success(true,"task");
    }

    //删除任务，按照传过来的list删除
    @DeleteMapping("/del")
    public Result<Boolean> deleteTask(@RequestBody TaskDelete taskDelete){
        List<Integer> tasks = taskDelete.getTasks();
        taskService.deleteTask(tasks);
        return Result.success(true,"task");
    }

    //删除一条任务
    @DeleteMapping("/del/{taskid}")
    public Result<Boolean> deleteTaskById(@PathVariable("taskid") Integer taskId){
        List<Integer> tasks = new ArrayList<>();
        tasks.add(taskId);
        taskService.deleteTask(tasks);
        return Result.success(true,"task");
    }


    //获取任务列表
    @GetMapping("/list/")
    public Result<Page<TasksVo>> getTasks(Page page){
        Page<TasksVo> res = taskService.findTasks(page);
        return Result.success(res, "list");
    }


    public Result<Page<TasksVo>> getTasksByStatus(@PathVariable("status") String status,Page page){
        Page<TasksVo> res = taskService.findTasks(page,status);
        return Result.success(res,"list");
    }

    //获取任务列表，按imei
    @GetMapping("/list/{imei}")
    public Result<Page<TasksVo>> getTasksByIMEI(@PathVariable("imei") String imei,Page page){
        if (StringUtils.isEmpty(imei) || (imei.length() != 2 && imei.length() != 15)){
            return getTasks(page);
        }
        if (imei.length() == 2){
            return getTasksByStatus(imei, page);
        }

        try {
            Long.valueOf(imei);
        }catch (Exception e){
            return getTasks(page);
        }
        Page<TasksVo> res = taskService.findTasksByIMEI(page,imei);
        return Result.success(res,"list");
    }


    //获取任务详情
    @GetMapping("/detail")
    public Result<TaskDetail> getTaskDetail(Integer taskId){
        TaskDetail taskDetail = taskService.findTaskDetailByTaskId(taskId);
        return Result.success(taskDetail,"task");
    }
}
