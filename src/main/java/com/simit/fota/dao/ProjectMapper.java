package com.simit.fota.dao;

import com.simit.fota.entity.FotaProject;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ProjectMapper {
    @Select("select * from Fota_Project where Fota_Project_Name = #{project} and delTag != 1")
    FotaProject findProjectByName(String project);

    @Insert("insert into Fota_Project(Fota_Project_Name,Create_ts,Creator,delTag) values(#{Fota_Project_Name}," +
            "#{CreateTs},#{Creator},#{delTag})")
    @Options(useGeneratedKeys = true,keyProperty ="ID",keyColumn = "ID")
    int insertFotaProject(FotaProject project);

    @Update("update Fota_Project set delTag = 1 where ID = #{projectId}")
    int delProjectById(Integer projectId);
}
