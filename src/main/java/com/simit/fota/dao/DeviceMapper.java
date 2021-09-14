package com.simit.fota.dao;

import com.simit.fota.entity.Device;
import com.simit.fota.entity.IMEIKV;
import com.simit.fota.result.Page;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DeviceMapper {

    @Insert("insert into IMEI_Attribute(IMEI,SN,Project,Manufacturer_Brand_ID,Network_Type_ID,SW_rlse,Create_ts,Location,delTag) " +
            "values(#{IMEI},#{SN},#{Project},#{ManufacturerBrandID},#{NetworkTypeID},#{SWRlse},#{CreateTs},#{Location},#{delTag})")
    int insertDevice(Device device);

    @Select("select ia.ID ID,IMEI,SN,Project,Manufacturer_Brand,Name Network_Type,SW_rlse,Create_ts from IMEI_Attribute ia,Network_Type nt,Manufacturer_Brand mb where ia.Manufacturer_Brand_ID = mb.ID and nt.ID = Network_Type_ID and IMEI = #{imei} and delTag != '1' ")
    Device findByIMEI(@Param("imei") String imei);

    @Select("select *  from IMEI_Attribute where delTag != 1")
    List<Device> getDevices();

    //找到是否以前存在这个设备
    @Select("select * from IMEI_Attribute where IMEI = #{IMEI}")
    Device findDeviceWithDelete(Device device);

    @Select("select count(*) from IMEI_Attribute where delTag != '1' ")
    int findDeviceCount();

    @Select("select ID,ia.IMEI,ia.SW_rlse,DeviceID,Project,ts last_ts,RSSI from IMEI_KV_latest ikl Right join IMEI_Attribute ia on ikl.IMEI = ia.IMEI and ia.delTag != '1' order by last_ts desc limit #{startRow},#{pageSize}")
    List<Device> findAllDevices(Page page);


    void updateDevice(Device device);

    int deleteDevices(@Param("imeis") List<String> imeis);

    //找到应该删除的设备
    List<String> findDelete(@Param("imeis") List<String> imeis);

    int deleteDevicesInKVL(@Param("imeis") List<String> imeis);

    int deleteDevicesInKV(@Param("imeis") List<String> imeis);

    @Select("select count(*) from IMEI_KV where IMEI = #{IMEI}")
    int findReportCount(@Param("IMEI") String IMEI);

    @Select("select IMEI,RSSI,ts,DeviceID,ICCID,SW_rlse from IMEI_KV where IMEI = #{IMEI} limit #{startRow},#{pageSize}")
    List<IMEIKV> findAllReports(String imei, Page page);
}
