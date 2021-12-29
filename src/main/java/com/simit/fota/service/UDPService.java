package com.simit.fota.service;

import com.simit.fota.dao.UDPMapper;
import com.simit.fota.entity.IMEIKV;
import com.simit.fota.entity.UDPReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UDPService {

    @Autowired
    private UDPMapper udpMapper;

    public void report(UDPReport udpReport) {
        IMEIKV imeikv = new IMEIKV();
//        imeikv.setTs(Long.parseLong(udpReport.getTs()));
//        imeikv.setSWRlse(udpReport.getProduct_version());
        imeikv.setICCID(udpReport.getICCID());
        imeikv.setRSSI(udpReport.getRSSI());
        imeikv.setIMEI(udpReport.getIMEI());
        imeikv.setDeviceID(udpReport.getDeviceID());
    }

    @Transactional(rollbackFor = Exception.class)
    public void doReport(IMEIKV imeikv){
        udpMapper.insertIMEI(imeikv);
    }
}
