package com.simit.fota.dao;

import com.simit.fota.entity.IMEIAuthent;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface HardWareMapper {

    @Insert("insert into IMEI_Authent(IMEI_ID,Request_ts,state) values(#{imeiAuthent.IMEIID},#{imeiAuthent.RequestTs},#{imeiAuthent.state})")
    int insertIMEIAuth(@Param("imeiAuthent") IMEIAuthent imeiAuthent);
}
