package com.simit.fota.dao;

import com.simit.fota.entity.IMEIKV;
import com.simit.fota.entity.UDPReport;
import com.simit.fota.result.Page;
import com.simit.fota.result.TasksVo;
import org.apache.catalina.LifecycleState;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UDPMapper {
    @Insert("insert into IMEI_KV(IMEI,RSSI,ts,DeviceID,ICCID,SW_rlse) values(#{imeikv.IMEI},#{imeikv.RSSI},#{imeikv.ts},#{imeikv.DeviceID},#{imeikv.ICCID},#{imeikv.SWRlse})")
    int insertIMEI(@Param("imeikv") IMEIKV imeikv);
    @Select("select * from IMEI_KV_latest where IMEI = #{imeikv.IMEI}")
    IMEIKV findIMEI(@Param("imeikv") IMEIKV imeikv);

    @Insert("insert into IMEI_KV_latest(IMEI,RSSI,ts,DeviceID,ICCID,SW_rlse,delTag) values(#{imeikv.IMEI},#{imeikv.RSSI},#{imeikv.ts},#{imeikv.DeviceID},#{imeikv.ICCID},#{imeikv.SWRlse},'0')")
    int insertIMEILast(@Param("imeikv") IMEIKV imeikv);

    @Update("update IMEI_KV_latest set RSSI = #{imeikv.RSSI},ts = #{imeikv.ts},DeviceID = #{imeikv.DeviceID},ICCID = #{imeikv.ICCID},SW_rlse = #{imeikv.SWRlse} where IMEI = #{imeikv.IMEI}")
    void updateIMEILast(@Param("imeikv") IMEIKV imeikv);

    @Insert("insert into UDPReport(DeviceID,Model,SWVersion,ProductVersion,IMEI,ICCID,IMSI,EARFCN,CELLID,PCI,RSRP,RSRQ,RSSI,SNR,ECL,CSQ,RRCSUCESS,RRCSUM,RRCFAILReason1,RRCFAILReason2,RRCFAILReason3,RRCFAILReason4,RRCFAILReason5,RRCFAILReason6,RRCFAILReason7," +
            "SubCarrSpacing,ToneNum,Pwr,PreambleRep,OperationMode,MultiCarrier,WorkTimes,WorkHours,CurrentSIM,Operato1,OP1_ARFCN1,OP1_RSRP1,OP1_SNR1,OP1_ARFCN2,OP1_RSRP2,OP1_SNR2,OP1_ARFCN3,OP1_RSRP3,OP1_SNR3,Operato2,OP2_ARFCN1,OP2_RSRP1,OP2_SNR1,OP2_ARFCN2,OP2_RSRP2,OP2_SNR2,OP2_ARFCN3,OP2_RSRP3,OP2_SNR3,ResultKey,ts)" +
            "values(#{udpReport.DeviceID},#{udpReport.Model},#{udpReport.SWVersion},#{udpReport.ProductVersion},#{udpReport.IMEI},#{udpReport.ICCID},#{udpReport.IMSI},#{udpReport.EARFCN},#{udpReport.CELLID},#{udpReport.PCI},#{udpReport.RSRP},#{udpReport.RSRQ},#{udpReport.RSSI},#{udpReport.SNR},#{udpReport.ECL},#{udpReport.CSQ},#{udpReport.RRCSUCESS},#{udpReport.RRCSUM},#{udpReport.RRCFAILReason1},#{udpReport.RRCFAILReason2},#{udpReport.RRCFAILReason3},#{udpReport.RRCFAILReason4},#{udpReport.RRCFAILReason5},#{udpReport.RRCFAILReason6},#{udpReport.RRCFAILReason7}" +
            ",#{udpReport.SubCarrSpacing},#{udpReport.ToneNum},#{udpReport.Pwr},#{udpReport.PreambleRep},#{udpReport.OperationMode},#{udpReport.MultiCarrier},#{udpReport.WorkTimes},#{udpReport.WorkHours},#{udpReport.CurrentSIM},#{udpReport.Operato1}" +
            ",#{udpReport.OP1ARFCN1},#{udpReport.OP1RSRP1},#{udpReport.OP1SNR1},#{udpReport.OP1ARFCN2},#{udpReport.OP1RSRP2},#{udpReport.OP1SNR2},#{udpReport.OP1ARFCN3},#{udpReport.OP1RSRP3},#{udpReport.OP1SNR3},#{udpReport.Operato2},#{udpReport.OP2ARFCN1},#{udpReport.OP2RSRP1},#{udpReport.OP2SNR1},#{udpReport.OP2ARFCN2},#{udpReport.OP2RSRP2},#{udpReport.OP2SNR2},#{udpReport.OP2ARFCN3},#{udpReport.OP2RSRP3},#{udpReport.OP2SNR3},#{udpReport.ResultKey},#{udpReport.ts})")
    void insertUDPReport(@Param("udpReport") UDPReport udpReport);

    @Select("select * from UDPReport where IMEI = #{imei}  order by ${page.orderField} ${page.orderType} limit #{page.startRow},#{page.pageSize}")
    List<UDPReport> findUDPReportByIMEI(@Param("imei") String imei,@Param("page") Page page);


    @Select("select t.ID ID,ti.Creator,Creator_Type,Task_ID,Task_Name,ti.Create_ts,IMEI_ID,t.Fota_Project_ID,t.Version_ID,t.Status,t.UpdateStatus_ts,t.delTag,Fota_Project_Name,Version_Name,IMEI from Task t join IMEI_Attribute i " +
            "on IMEI_ID = i.ID join Fota_Project f on f.ID = t.Fota_Project_ID join Version_Library v on t.Version_ID = v.ID and t.Fota_Project_ID = v.Fota_Project_ID join Task_Info ti on t.Task_ID = ti.ID and t.delTag != '1' and t.IMEI_ID = #{id} " +
            " order by ${page.orderField} ${page.orderType} limit #{page.startRow},#{page.pageSize}")
    List<TasksVo> findTasksWithIMEI(@Param("page") Page page, @Param("id") Integer id);

    @Select("select count(*) from UDPReport where IMEI = #{imei}")
    int findUDPReportCount(@Param("imei") String imei);
}
