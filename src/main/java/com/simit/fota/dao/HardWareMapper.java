package com.simit.fota.dao;

import com.simit.fota.entity.CheckRecord;
import com.simit.fota.entity.IMEIAuthent;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface HardWareMapper {

    @Insert("insert into IMEI_Authent(IMEI_ID,Request_ts,state) values(#{imeiAuthent.IMEIID},#{imeiAuthent.RequestTs},#{imeiAuthent.state})")
    int insertIMEIAuth(@Param("imeiAuthent") IMEIAuthent imeiAuthent);

    @Insert("insert into checkRecords(IMEI,value,ts) values(#{check.imei},#{s},#{check.checkTs})")
    void insertCheckRecord(@Param("check") CheckRecord checkRecord, @Param("s") String s);

    @Select("select value from checkRecords order by ts desc limit #{startRow},#{pageSize}")
    List<String> findCheckRecords(@Param("startRow") int startRow,@Param("pageSize")  int pageSize);

    @Select("select count(*) from checkRecords")
    int findCheckRecordCount();
}
