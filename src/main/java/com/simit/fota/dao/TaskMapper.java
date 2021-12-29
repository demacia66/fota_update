package com.simit.fota.dao;

import com.simit.fota.entity.Task;
import com.simit.fota.entity.TaskDetail;
import com.simit.fota.entity.TaskInfo;
import com.simit.fota.result.Page;
import com.simit.fota.result.TasksVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface TaskMapper {

    @Insert("insert into Task(Task_ID,IMEI_ID,Fota_Project_ID,Version_ID,Status,UpdateStatus_ts,delTag,File_Type) values(#{task.TaskID},#{task.IMEIID},#{task.FotaProjectID},#{task.VersionID},#{task.Status},#{task.UpdateStatusTs},#{task.delTag},#{task.FileType})")
    public int insertTask(@Param("task")Task task);

    @Insert("insert into Task_Info(Fota_Project_ID,Version_ID,Creator,Creator_Type,Status,Task_Name,Create_ts,UpdateStatus_ts,delTag) values(#{taskInfo.FotaProjectID},#{taskInfo.VersionID},#{taskInfo.Creator},#{taskInfo.CreatorType},#{taskInfo.Status},#{taskInfo.TaskName},#{taskInfo.CreateTs},#{taskInfo.UpdateStatusTs},#{taskInfo.delTag})")
    @Options(useGeneratedKeys = true,keyProperty ="ID",keyColumn = "ID")
    public int insertTaskInfo(@Param("taskInfo") TaskInfo taskInfo);


    int deleteTask(@Param("tasks") List<Integer> tasks);

    int deleteTaskInfo(@Param("tasks") List<Integer> tasks);

    @Select("select count(*) from Task where delTag != '1' and Status != '04' ")
    int findTaskCount();

    @Select("select t.ID ID,ti.Creator,Creator_Type,Task_ID,Task_Name,ti.Create_ts,IMEI_ID,t.Fota_Project_ID,t.Version_ID,t.Status,t.UpdateStatus_ts,t.delTag,Fota_Project_Name,Version_Name,IMEI from Task t join IMEI_Attribute i " +
            "on IMEI_ID = i.ID join Fota_Project f on f.ID = t.Fota_Project_ID join Version_Library v on t.Version_ID = v.ID and t.Fota_Project_ID = v.Fota_Project_ID join Task_Info ti on t.Task_ID = ti.ID and t.delTag != '1' and t.Status != '04' " +
            " order by ${page.orderField} ${page.orderType} limit #{page.startRow},#{page.pageSize}")
    List<TasksVo> findTasks(@Param("page") Page page);

    @Select("select ID from IMEI_Attribute where IMEI = #{imei}")
    Integer findIMEIID(@Param("imei") String imei);

    @Select("select * from Task where IMEI_ID = #{imeiid} and delTag != '1' and status != '04' ")
    Task findTaskByIMEIID(Integer imeiid);

    @Select("select t.ID ID,t.Fota_Project_ID,t.Version_ID,Status,UpdateStatus_ts,t.delTag,Fota_Project_Name,Version_Name ,t.Creator,t.Creator_Type,Task_Name,t.Create_ts from Task_Info t join" +
            " Fota_Project f on f.ID = t.Fota_Project_ID join Version_Library v on t.Version_ID = v.ID and t.Fota_Project_ID = v.Fota_Project_ID where t.delTag != '1' and t.ID = #{taskId}" )
    TaskDetail findTaskDetailByTaskId(@Param("taskId") Integer taskId);


    @Update("update Task set Status = #{result} ,UpdateStatus_ts = #{updateTs} where ID = #{id}")
    void updateTaskStatus(@Param("id") Integer id, @Param("updateTs") long updateTs, @Param("result") String result);

    @Update("update Task_Info set Status = #{result} ,UpdateStatus_ts = #{updateTs} where ID = #{taskID}")
    void updateTaskInfoTaskStatus(@Param("taskID") Integer taskID, @Param("updateTs") long updateTs, @Param("result") String result);

    @Select("select * from Task where IMEI_ID = #{id} and delTag != '1' and Status != '04' order by UpdateStatus_ts desc limit 1")
    Task findNewTask(@Param("id") Integer id);

    @Select("select count(*) from Task where delTag != '1' and Status = ${status} ")
    int findTaskCountWithStatus(@Param("status") String status);

    @Select("select t.ID ID,ti.Creator,Creator_Type,Task_ID,Task_Name,ti.Create_ts,IMEI_ID,t.Fota_Project_ID,t.Version_ID,t.Status,t.UpdateStatus_ts,t.delTag,Fota_Project_Name,Version_Name,IMEI from Task t join IMEI_Attribute i " +
            "on IMEI_ID = i.ID join Fota_Project f on f.ID = t.Fota_Project_ID join Version_Library v on t.Version_ID = v.ID and t.Fota_Project_ID = v.Fota_Project_ID join Task_Info ti on t.Task_ID = ti.ID and t.delTag != '1' and t.Status = ${status} " +
            " order by ${page.orderField} ${page.orderType} limit #{page.startRow},#{page.pageSize}")
    List<TasksVo> findTasksWithStatus(@Param("page") Page page, @Param("status") String status);

    @Select("select count(*) from Task where delTag != '1' and IMEI_ID = #{imei} ")
    int findTaskCountWithIMEI(@Param("imei") Integer imei);

    @Select("select t.ID ID,ti.Creator,Creator_Type,Task_ID,Task_Name,ti.Create_ts,IMEI_ID,t.Fota_Project_ID,t.Version_ID,t.Status,t.UpdateStatus_ts,t.delTag,Fota_Project_Name,Version_Name,IMEI from Task t join IMEI_Attribute i " +
            "on IMEI_ID = i.ID join Fota_Project f on f.ID = t.Fota_Project_ID join Version_Library v on t.Version_ID = v.ID and t.Fota_Project_ID = v.Fota_Project_ID join Task_Info ti on t.Task_ID = ti.ID and t.delTag != '1' and t.IMEI_ID = #{id} " +
            " order by ${page.orderField} ${page.orderType} limit #{page.startRow},#{page.pageSize}")
    List<TasksVo> findTasksWithIMEI(@Param("page") Page page, @Param("id") Integer id);
}
