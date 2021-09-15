package com.simit.fota.dao;

import com.simit.fota.entity.Version;
import com.simit.fota.entity.VersionVo;
import com.simit.fota.result.Page;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface VersionMapper {
    /**
     * 通过项目和版本名获取版本
     * @param versionName
     * @param projectId
     * @return
     */
    @Select("select ID,Fota_Project_ID,Version_Name,Create_ts,Description,delTag from Version_Library where Version_Name = #{versionName} and Fota_Project_ID = #{projectId}")
    Version findVersionByNameId(@Param("versionName") String versionName,@Param("projectId") Integer projectId);

    /**
     * 恢复版本
     * @param version
     * @return
     */
    @Update("update Version_Library set delTag = '0',Create_ts = #{CreateTs},Description = #{Description} where Version_Name = #{VersionName} and Fota_Project_ID = #{FotaProjectID}")
    int recoverVersion(Version version);

    /**
     * 新建版本
     * @param version
     * @return
     */
    @Insert("insert into Version_Library(Fota_Project_ID,Version_Name,Create_ts,Description,delTag) values(#{FotaProjectID},#{VersionName},#{CreateTs},#{Description},#{delTag})")
    int insertVersion(Version version);

    /**
     * 获取项目当前的版本
     * @param projectId
     * @return
     */
    @Select("select Version_Name,ID from Version_Library where Fota_Project_ID = #{projectId} and delTag != '1' order by ID desc limit 1")
    Version findLatestVersion(Integer projectId);

    /**
     * 删除版本
     * @param version
     */
    @Update("update Version_Library set delTag = '1' where ID = #{ID} and Fota_Project_ID = #{FotaProjectID}")
    void delVersion(Version version);

    /**
     * 删除版本相关的关系
     * @param version
     */
    @Update("update Version_Relation set delTag = '1' where Fota_Project_ID = #{FotaProjectID} and (Version_ID = #{ID} or Prev_Version_ID = #{ID} or Next_Version_ID = #{ID})")
    void delVersionRelation(Version version);

    /**
     * 通过版本id找到版本
     * @param version
     * @return
     */
    @Select("select * from Version_Library where ID = #{ID} and Fota_Project_ID = #{FotaProjectID} and delTag != '1' ")
    Version findVersionByVId(Version version);

    /**
     *项目的版本数量
     * @param fotaProjectId
     * @return
     */
    @Select("select count(*) from Version_Library where Fota_Project_ID = #{fotaProjectId} and delTag != '1' ")
    int findVersionCount(@Param("fotaProjectId") Integer fotaProjectId);

    /**
     * 找到项目的版本列表
     * @param fotaProjectId
     * @return
     */
    @Select("select * from Version_Library where Fota_Project_ID = #{fotaProjectId} and delTag != '1' order by ID DESC limit #{page.startRow},#{page.pageSize}")
    List<VersionVo> findVersionsByPId(@Param("fotaProjectId") Integer fotaProjectId,@Param("page") Page page);

    @Select("select * from Version_Library where Fota_Project_ID = #{fotaProjectID} order by ID limit 1")
    Version findInitialVersion(@Param("fotaProjectID") Integer fotaProjectID);

    @Update("update Version_Library set Version_Name = #{VersionName},Description = #{Description} where ID = #{ID} and delTag != '1' ")
    void updateVersionById(Version version);
}
