package com.simit.fota.dao;

import com.simit.fota.entity.Version;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface VersionMapper {
    @Select("select ID,Fota_Project_ID,Version_Name,Create_ts,Description,delTag from Version_Library where Version_Name = #{versionName} and Fota_Project_ID = #{Fota_Project_ID}")
    Version findVersionByNameId(@Param("versionName") String versionName,@Param("projectId") Integer projectId);

    @Update("update Version_Library set delTag = '0',Create_ts = #{CreateTs},Description = #{Description} where Version_Name = #{VersionName} and Fota_Project_ID = #{FotaProjectID}")
    int recoverVersion(Version version);

    @Insert("inser into Version_Library(Fota_Project_ID,Version_Name,Create_ts,Description,delTag) values(#{FotaProjectID},#{VersionName},#{CreateTs},#{Description},#{delTag})")
    int insertVersion(Version version);
}
