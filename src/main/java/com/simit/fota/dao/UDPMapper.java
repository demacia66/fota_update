package com.simit.fota.dao;

import com.simit.fota.entity.IMEIKV;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UDPMapper {
    @Insert("insert into IMEI_KV(IMEI,RSSI,ts,DeviceID,ICCID,SW_rlse)")
    int insertIMEI(IMEIKV imeikv);
}
