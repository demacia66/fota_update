package com.simit.fota.controller;

import com.simit.fota.entity.UDPReport;
import com.simit.fota.service.UDPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fota/api/udp")
public class UDPController {

    @Autowired
    private UDPService udpService;

    @RequestMapping("/report")
    public void report(String udpReport){
        UDPReport udpReport1 = new UDPReport();
//        udpService.report(udpReport);
    }
}
