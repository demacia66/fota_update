package com.simit.fota.controller;

import com.alibaba.fastjson.JSONArray;
import com.simit.fota.entity.UDPReport;
import com.simit.fota.service.UDPService;
import com.simit.fota.udpjson.JsonRootBean;
import com.simit.fota.udpjson.ResultData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//获取解析udp数据
@RestController
@RequestMapping("/fota/api/udp")
public class UDPController {

    @Autowired
    private UDPService udpService;

//    @RequestMapping("/report")
    //处理解析后的数据
    public void report(UDPReport udpReport){
       if (StringUtils.isEmpty(udpReport.getIMEI()) || StringUtils.isEmpty(udpReport.getTs().toString())){
           return;
       }
       udpService.report(udpReport);
    }

    //udp数据解析
    @RequestMapping("/report")
    public void test(@RequestBody JsonRootBean bean) {
        System.out.println(bean);
        JSONArray resultData = bean.getResultData();
        List<ResultData> resultData1 = resultData.toJavaList(ResultData.class);
        for (ResultData cur : resultData1) {
            UDPReport udpReport = new UDPReport();
            udpReport.setDeviceID(cur.getDeviceIdKey());
            udpReport.setModel(cur.getModuleKey().getModelKey());
            udpReport.setSWVersion(cur.getModuleKey().getSWVersionKey());
            udpReport.setProductVersion(cur.getModuleKey().getProductVersionKey());
            udpReport.setIMEI(cur.getModuleKey().getIMEIKey() + "");
            udpReport.setICCID(cur.getUIMKey().getICCIDKey() + "");
            udpReport.setIMSI(cur.getUIMKey().getIMSIKey() + "");
            udpReport.setEARFCN(cur.getRadioKey().getRadioInfoKey().getEARFCNKey() + "");
            udpReport.setCELLID(cur.getRadioKey().getRadioInfoKey().getCELLIDKey() + "");
            udpReport.setCSQ(cur.getRadioKey().getRadioInfoKey().getCSQKey() + "");
            udpReport.setPCI(cur.getRadioKey().getRadioInfoKey().getPCIKey() + "");
            udpReport.setRSRP(cur.getRadioKey().getRadioInfoKey().getRSRPKey() + "");
            udpReport.setRSRQ(cur.getRadioKey().getRadioInfoKey().getRSRQKey() + "");
            udpReport.setRSSI(cur.getRadioKey().getRadioInfoKey().getRSSIKey() + "");
            udpReport.setSNR(cur.getRadioKey().getRadioInfoKey().getSNRKey() + "");
            udpReport.setECL(cur.getRadioKey().getRadioInfoKey().getECLKey() + "");
            udpReport.setRRCSUCESS(cur.getRadioKey().getRRCFALLReasonKey().getRRCSUCESSKey() + "");
            udpReport.setRRCSUM(cur.getRadioKey().getRRCFALLReasonKey().getRRCSUMKey() + "");
            udpReport.setRRCFAILReason1(cur.getRadioKey().getRRCFALLReasonKey().getRRCFAILReason1Key() + "");
            udpReport.setRRCFAILReason2(cur.getRadioKey().getRRCFALLReasonKey().getRRCFAILReason2Key() + "");
            udpReport.setRRCFAILReason3(cur.getRadioKey().getRRCFALLReasonKey().getRRCFAILReason3Key() + "");
            udpReport.setRRCFAILReason4(cur.getRadioKey().getRRCFALLReasonKey().getRRCFAILReason4Key() + "");
            udpReport.setRRCFAILReason5(cur.getRadioKey().getRRCFALLReasonKey().getRRCFAILReason5Key() + "");
            udpReport.setRRCFAILReason6(cur.getRadioKey().getRRCFALLReasonKey().getRRCFAILReason6Key() + "");
            udpReport.setRRCFAILReason7(cur.getRadioKey().getRRCFALLReasonKey().getRRCFAILReason7Key()+ "");
            udpReport.setSubCarrSpacing(cur.getRadioKey().getMultiToneKey().getSubCarrSpacingKey() + "");
            udpReport.setToneNum(cur.getRadioKey().getMultiToneKey().getToneNumKey() + "");
            udpReport.setPwr(cur.getRadioKey().getMultiToneKey().getPwrKey() + "");
            udpReport.setPreambleRep(cur.getRadioKey().getMultiToneKey().getPreambleRepKey() + "");
            udpReport.setOperationMode(cur.getRadioKey().getMultiToneKey().getOperationModeKey() + "");
            udpReport.setMultiCarrier(cur.getRadioKey().getMultiToneKey().getMultiCarrierKey() + "");
            udpReport.setWorkTimes(cur.getRadioKey().getWorkStatisticsKey().getWorkTimesKey() + "");
            udpReport.setWorkHours(cur.getRadioKey().getWorkStatisticsKey().getWorkHoursKey() + "");
            udpReport.setCurrentSIM(cur.getNetworkInfoKey().getCurrentSIMKey() + "");
            udpReport.setOperato1(cur.getNetworkInfoKey().getOperato1Key() + "");
            udpReport.setOP1ARFCN1(cur.getNetworkInfoKey().getOP1_ARFCN1Key() + "");
            udpReport.setOP1RSRP1(cur.getNetworkInfoKey().getOP1_RSRP1Key() + "");
            udpReport.setOP1SNR1(cur.getNetworkInfoKey().getOP1_SNR1Key() + "");
            udpReport.setOP1ARFCN2(cur.getNetworkInfoKey().getOP1_ARFCN2Key() + "");
            udpReport.setOP1RSRP2(cur.getNetworkInfoKey().getOP1_RSRP2Key() + "");
            udpReport.setOP1SNR2(cur.getNetworkInfoKey().getOP1_SNR2Key() + "");
            udpReport.setOP1ARFCN3(cur.getNetworkInfoKey().getOP1_ARFCN3Key() + "");
            udpReport.setOP1RSRP3(cur.getNetworkInfoKey().getOP1_RSRP3Key() + "");
            udpReport.setOP1SNR3(cur.getNetworkInfoKey().getOP1_SNR3Key() + "");
            udpReport.setOperato2(cur.getNetworkInfoKey().getOperato2Key() + "");
            udpReport.setOP2ARFCN1(cur.getNetworkInfoKey().getOP2_ARFCN1Key() + "");
            udpReport.setOP2RSRP1(cur.getNetworkInfoKey().getOP2_RSRP1Key() + "");
            udpReport.setOP2SNR1(cur.getNetworkInfoKey().getOP2_SNR1Key() + "");
            udpReport.setOP2ARFCN2(cur.getNetworkInfoKey().getOP2_ARFCN2Key() + "");
            udpReport.setOP2RSRP2(cur.getNetworkInfoKey().getOP2_RSRP2Key() + "");
            udpReport.setOP2SNR2(cur.getNetworkInfoKey().getOP2_SNR2Key() + "");
            udpReport.setOP2ARFCN3(cur.getNetworkInfoKey().getOP2_ARFCN3Key() + "");
            udpReport.setOP2RSRP3(cur.getNetworkInfoKey().getOP2_RSRP3Key() + "");
            udpReport.setOP2SNR3(cur.getNetworkInfoKey().getOP2_SNR3Key() + "");
            udpReport.setResultKey(cur.getSIMChangeResultKey().getResultKey());
            udpReport.setTs(System.currentTimeMillis());
            udpService.report(udpReport);
        }

//        List<ResultData> resultData = bean.getResultData();
//        for (ResultData cur : resultData) {
//            System.out.println(cur.toString());
//        }
    }
}
