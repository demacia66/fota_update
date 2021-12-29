package com.simit.fota.service;

import com.simit.fota.dao.TaskMapper;
import com.simit.fota.entity.*;
import com.simit.fota.exception.GlobalException;
import com.simit.fota.result.CodeMsg;
import com.simit.fota.result.Page;
import com.simit.fota.result.TasksVo;
import com.simit.fota.util.DateFormatUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private VersionService versionService;

    @Autowired
    private DeviceService deviceService;

    @Value("${fullfile}")
    private String fullFile;

    @Value("${differfile}")
    private String differFile;

    @Autowired
    private TaskMapper taskMapper;

    public void createTask(TaskVo taskVo,String creatorType,String creator) {
        String fotaProjectName = taskVo.getFotaProjectName();
        FotaProject projectByName = projectService.findProjectByName(fotaProjectName);
        if (projectByName == null){
            throw new GlobalException(CodeMsg.PROJECT_NOT_EXIST);
        }
        String versionName = taskVo.getVersionName();
        if (StringUtils.isEmpty(versionName)){
            throw new GlobalException(CodeMsg.VERSION_NAME_EMPTY);
        }
        Version version = versionService.findVersionByPidVname(projectByName.getID(), versionName);
        if (version == null){
            throw new GlobalException(CodeMsg.VERSION_NOT_EXIST);
        }
        String fileType = taskVo.getFileType();
        if (StringUtils.isEmpty(fileType)){
            throw new GlobalException(CodeMsg.FILE_TYPE_EMPTY);
        }
        VersionFiles versionFiles = null;
        versionFiles = versionService.findFileByType(version.getID(),version.getFotaProjectID(),fileType);
        if (versionFiles == null){
            throw new GlobalException(CodeMsg.VERSION_NOT_EXIST);
        }
        List<String> imeis = taskVo.getImeis();
        if (imeis == null){
            throw new GlobalException(CodeMsg.IMEI_EMPTY);
        }
        List<Integer> imeiIds = deviceService.findIMEI(imeis);
        createTasks(imeiIds,projectByName.getID(),version.getID(),creatorType,creator,taskVo.getFileType());
    }


    @Transactional(rollbackFor = Exception.class)
    public void createTasks(List<Integer> imeiIds,Integer projectId,Integer versionId,String creatorType,String creator,String fileType){
        if (imeiIds == null || imeiIds.isEmpty()){
            return;
        }
        Task task = null;
        TaskInfo taskInfo = null;
        for (Integer imeiId:imeiIds){

            Task preTask = taskMapper.findNewTask(imeiId);
            if (preTask != null){
                String status = preTask.getStatus();
                if (("01".equals(status) || "02".equals(status) || "03".equals(status))){
                    continue;
                }
            }


            task = new Task();
            taskInfo = new TaskInfo();

            //设置taskInfo
            long curTime = System.currentTimeMillis();

            taskInfo.setCreateTs(curTime);
            taskInfo.setStatus("01");
            taskInfo.setCreatorType(creatorType);
            taskInfo.setTaskName("task" + curTime);
            taskInfo.setCreator(creator);
            taskInfo.setFotaProjectID(projectId);
            taskInfo.setVersionID(versionId);
            taskInfo.setDelTag("0");
            taskInfo.setUpdateStatusTs(curTime);
            taskMapper.insertTaskInfo(taskInfo);

            task.setDelTag("0");
            task.setFotaProjectID(projectId);
            task.setIMEIID(imeiId);
            task.setUpdateStatusTs(curTime);
            task.setVersionID(versionId);
            task.setStatus("01");
            task.setTaskID(taskInfo.getID());
            task.setFileType(fileType);
            taskMapper.insertTask(task);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteTask(List<Integer> tasks) {
        taskMapper.deleteTask(tasks);
        taskMapper.deleteTaskInfo(tasks);
    }

    public Page<TasksVo> findTasks(Page page) {
        //处理页面信息
        int totalCount = taskMapper.findTaskCount();
        if (page == null) {
            page = new Page();
            page.setOrderType("desc");
        }else if (page.getOrderField() == null){
            page = new Page(totalCount, page.getCurrentPage(), page.getPageSize());
            page.setOrderType("desc");
        }


        String orderField = null;

        if (page.getOrderField().equalsIgnoreCase("CreateTs")){
            page.setOrderField("Create_ts");
        }

        if (page.getOrderField().equalsIgnoreCase("Create_ts")){
            orderField = page.getOrderField();
            page.setOrderField("ti." + page.getOrderField());
        }


        page.setTotalCount(totalCount);
        page.setStartRow((page.getCurrentPage() - 1) * page.getPageSize());
        //找到项目的版本列表
        List<TasksVo> taskList = taskMapper.findTasks(page);
        if (taskList == null) {
            taskList = new ArrayList<>();
        }
        for (TasksVo cur:taskList){
            cur.setUpdateTs(DateFormatUtil.formatDate(cur.getUpdateStatusTs()));
            cur.setCts(DateFormatUtil.formatDate(cur.getCreateTs()));
            String creatorType = cur.getCreatorType();
            if ("00".equals(creatorType)){
                creatorType = "系统";
            }else if ("01".equals(creatorType)){
                creatorType = "用户";
            }else if ("02".equals(creatorType)){
                creatorType = "其他";
            }
            cur.setCreatorType(creatorType);
        }
        if (orderField != null){
            page.setOrderField(orderField);
        }
        page.setDataList(taskList);
        return page;
    }

    public Integer findTaskByIMEI(String imei) {
        return taskMapper.findIMEIID(imei);
    }

    public Task findTaskByIMEIID(Integer imeiid) {
        return taskMapper.findTaskByIMEIID(imeiid);
    }

    public TaskDetail findTaskDetailByTaskId(Integer taskId) {
        if (taskId == null){
            throw new GlobalException(CodeMsg.TASKID_EMPTY);
        }
        TaskDetail taskDetail = taskMapper.findTaskDetailByTaskId(taskId);
        if (taskDetail != null){
            taskDetail.setUTs(DateFormatUtil.formatDate(taskDetail.getUpdateStatusTs()));
            taskDetail.setCTs(DateFormatUtil.formatDate(taskDetail.getCreateTs()));
            String creatorType = taskDetail.getCreatorType();
            if ("00".equals(creatorType)){
                creatorType = "系统";
            }else if ("01".equals(creatorType)){
                creatorType = "用户";
            }else if ("02".equals(creatorType)){
                creatorType = "其他";
            }
            taskDetail.setCreatorTypeA(creatorType);
        }
        return taskDetail;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Integer id, String result) {
        Task task = taskMapper.findNewTask(id);
        if (task == null){
            return;
        }
        long updateTs = System.currentTimeMillis();
        taskMapper.updateTaskStatus(task.getID(),updateTs,result);
        taskMapper.updateTaskInfoTaskStatus(task.getTaskID(),updateTs,result);
    }

    public Page<TasksVo> findTasks(Page page, String status) {
        if (StringUtils.isEmpty(status)){
            return findTasks(page);
        }
        int totalCount = taskMapper.findTaskCountWithStatus(status);
        if (page == null) {
            page = new Page();
            page.setOrderType("desc");
        }else if (page.getOrderField() == null){
            page = new Page(totalCount, page.getCurrentPage(), page.getPageSize());
            page.setOrderType("desc");
        }


        String orderField = null;

        if (page.getOrderField().equalsIgnoreCase("Create_ts")){
            orderField = page.getOrderField();
            page.setOrderField("ti." + page.getOrderField());
        }

        page.setTotalCount(totalCount);
        page.setStartRow((page.getCurrentPage() - 1) * page.getPageSize());
        //找到项目的版本列表
        List<TasksVo> taskList = taskMapper.findTasksWithStatus(page,status);
        if (taskList == null) {
            taskList = new ArrayList<>();
        }
        for (TasksVo cur:taskList){
            cur.setUpdateTs(DateFormatUtil.formatDate(cur.getUpdateStatusTs()));
            cur.setCts(DateFormatUtil.formatDate(cur.getCreateTs()));
            String creatorType = cur.getCreatorType();
            if ("00".equals(creatorType)){
                creatorType = "系统";
            }else if ("01".equals(creatorType)){
                creatorType = "用户";
            }else if ("02".equals(creatorType)){
                creatorType = "其他";
            }
            cur.setCreatorType(creatorType);
        }
        if (orderField != null){
            page.setOrderField(orderField);
        }
        page.setDataList(taskList);
        return page;
    }

    public Page<TasksVo> findTasksByIMEI(Page page, String imei) {

        Device device2 = deviceService.findDeviceByIMEI(imei);

        int totalCount = taskMapper.findTaskCountWithIMEI(device2.getId());
        if (page == null) {
            page = new Page();
            page.setOrderType("desc");
        }else if (page.getOrderField() == null){
            page = new Page(totalCount, page.getCurrentPage(), page.getPageSize());
            page.setOrderType("desc");
        }


        String orderField = null;

        if (page.getOrderField().equalsIgnoreCase("CreateTs")){
            page.setOrderField("Create_ts");
        }

        if (page.getOrderField().equalsIgnoreCase("Create_ts")){
            orderField = page.getOrderField();
            page.setOrderField("ti." + page.getOrderField());
        }

        page.setTotalCount(totalCount);
        page.setStartRow((page.getCurrentPage() - 1) * page.getPageSize());
        //找到项目的版本列表
        List<TasksVo> taskList = taskMapper.findTasksWithIMEI(page,device2.getId());
        if (taskList == null) {
            taskList = new ArrayList<>();
        }
        for (TasksVo cur:taskList){
            cur.setUpdateTs(DateFormatUtil.formatDate(cur.getUpdateStatusTs()));
            cur.setCts(DateFormatUtil.formatDate(cur.getCreateTs()));
            String creatorType = cur.getCreatorType();
            if ("00".equals(creatorType)){
                creatorType = "系统";
            }else if ("01".equals(creatorType)){
                creatorType = "用户";
            }else if ("02".equals(creatorType)){
                creatorType = "其他";
            }
            cur.setCreatorType(creatorType);
        }
        if (orderField != null){
            page.setOrderField(orderField);
        }
        page.setDataList(taskList);
        return page;
    }
}
