package com.simit.fota.controller;

import com.simit.fota.annotation.LoginRequired;
import com.simit.fota.dao.DeviceMapper;
import com.simit.fota.entity.CheckVo;
import com.simit.fota.entity.Device;
import com.simit.fota.entity.ReportVo;
import com.simit.fota.redis.RedisService;
import com.simit.fota.result.BaseResult;
import com.simit.fota.result.CheckResult;
import com.simit.fota.result.FotaResult;
import com.simit.fota.service.DeviceService;
import com.simit.fota.service.HardwareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.Map;

@RestController
@RequestMapping("/fota/api")
public class HardwareController {

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private HardwareService hardwareService;

    @GetMapping("/authent")
    public FotaResult deviceLogin(String id, String version){
        String IMEI = id;
        FotaResult result = new FotaResult();
        result.setState("ok");
        result.setIMEI(IMEI);
        result.setAction("authent");
        if (IMEI == null){
            result.setMessage("post param error");
            result.setState("error");
        }
        if (IMEI.length() != 15 ){
            result.setMessage("imei length error");
            result.setState("error");
        }
        try {
            if (IMEI != null && IMEI.length() == 15 ){
                Long.parseLong(IMEI);
            }
        }catch (Exception e){
            result.setState("error");
            result.setMessage("post param error");
        }
        Device device = deviceMapper.findByIMEI(IMEI);
        Map<String, String> loginRes = hardwareService.hardwareLogin(device);
        if (loginRes.get("ticket") == null){
            result.setState("error");
        }else {
            result.setTicket(loginRes.get("ticket"));
        }
        result.setMessage(loginRes.get("message"));
        hardwareService.reportAuth(result);
        return result;
    }

    @LoginRequired
    @GetMapping("/check")
    public CheckResult checkVersion(CheckVo checkVo, @RequestParam("product_name") String productName,
                                    @RequestParam("product_version") String productVersion) throws Exception {
        CheckResult result = new CheckResult();
        result.setAction("check");
        result.setIMEI(checkVo.getId());
        result.setCompany(checkVo.getCompany());

        checkVo.setProductName(productName);
        checkVo.setProductVersion(productVersion);
        result.setProductVersion(productVersion);
        String IMEI = checkVo.getId();
        result.setState("ok");
        if (IMEI == null){
            result.setMessage("post param error");
            result.setState("error");
            return result;
        }
        if (IMEI.length() != 15 ){
            result.setMessage("imei length error");
            result.setState("error");
            return result;
        }
        try {
            if (IMEI != null && IMEI.length() == 15 ){
                Long.parseLong(IMEI);
            }
        }catch (Exception e){
            result.setState("error");
            result.setMessage("post param error");
            return result;
        }
        Map<String,String> res = hardwareService.doCheck(checkVo);

        result.setMessage(res.get("message"));
        String update = res.get("update");

        if (update != null){
            result.setUpdate(true);
        }else {
            result.setUpdate(false);
        }

        result.setContent(res.get("content"));

        result.setCurrentVersion(res.get("current_version"));

        result.setDate(Long.parseLong(res.get("date")));

        result.setUrl(res.get("url"));

        result.setFileMd5(res.get("file_md5"));

        return result;
    }



    @LoginRequired
    @PostMapping("/report")
    public BaseResult reportUpload(ReportVo reportVo,@RequestParam("product_name") String productName,@RequestParam("upgrade_result") String upgradeResult){
        BaseResult result = new BaseResult();
        reportVo.setProductName(productName);
        reportVo.setUpgradeResult(upgradeResult);

        result.setAction("report");
        result.setIMEI(reportVo.getId());
        String IMEI = reportVo.getId();
        result.setState("ok");

        if (IMEI == null){
            result.setMessage("post param error");
            result.setState("error");
            return result;
        }
        if (IMEI.length() != 15 ){
            result.setMessage("imei length error");
            result.setState("error");
            return result;
        }
        try {
            if (IMEI != null && IMEI.length() == 15 ){
                Long.parseLong(IMEI);
            }
        }catch (Exception e){
            result.setState("error");
            result.setMessage("post param error");
            return result;
        }
        Map<String,String> res = hardwareService.reportUpload(reportVo);
        return result;
    }
}

