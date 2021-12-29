package com.simit.fota.controller;

import com.simit.fota.annotation.LoginRequired;
import com.simit.fota.dao.DeviceMapper;
import com.simit.fota.entity.*;
import com.simit.fota.redis.RedisService;
import com.simit.fota.result.*;
import com.simit.fota.service.DeviceService;
import com.simit.fota.service.HardwareService;
import com.simit.fota.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fota/api")
@Slf4j
public class HardwareController {

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private HardwareService hardwareService;

    @Autowired
    private TaskService taskService;

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
        result.setMessage("");
        log.info(loginRes.get("message"));
        hardwareService.reportAuth(result);
        Integer IMEIID = taskService.findTaskByIMEI(IMEI);
        if (IMEIID != null){

            taskService.updateStatus(IMEIID,"02");
        }
        return result;
    }

    @LoginRequired
    @GetMapping("/check")
    public CheckResult checkVersion(CheckVo checkVo, @RequestParam("product_name") String productName,
                                    @RequestParam("product_version") String productVersion) throws Exception {
        log.error("开始check");
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

        result.setContent(res.get("content") == null ? "":res.get("content"));

        result.setCurrentVersion(res.get("current_version"));

        if (res.get("date") != null){
            result.setDate(Long.parseLong(res.get("date")));
        }

        result.setUrl(res.get("url"));
//        result.setUrl("http://fotafile1.ranchip.com:7788/7d45b2c4b96941c98b4e8862c52e78bf.pack");

        result.setFileMd5(res.get("file_md5"));

        return result;
    }



    @LoginRequired
    @PostMapping("/report")
    public BaseResult reportUpload(ReportVo reportVo,@RequestParam("product_name") String productName,@RequestParam("upgrade_result") String upgradeResult){
        //imei=865602040244987&&version=N23-R03-ZKY-05B&&company=neoway&&project=N23&&token=bb34aba5562b56c97411a72f44d399f1 HTTP/1.1\r\n]
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
        result.setMessage(res.get("message"));
        return result;
    }

    @LoginRequired
    @PostMapping("/update")
    public BaseResult updatetUpload(UpdateVo updateVo){
        //imei=865602040244987&&version=N23-R03-ZKY-05B&&company=neoway&&project=N23&&token=bb34aba5562b56c97411a72f44d399f1 HTTP/1.1\r\n]
        log.error(updateVo.toString());
        BaseResult result = new BaseResult();
        result.setAction("update");
        result.setIMEI(updateVo.getImei());
        result.setState("ok");
        Map<String,String> res = hardwareService.updateUpload(updateVo);
        result.setMessage(res.get("message"));
        return result;
    }


//    @GetMapping("/checkRecord/list")
//    public Result<CheckPage<CheckRecord>> checkRecordPage(CheckPage page,String IMEI){
//        List<CheckRecord> data = null;
//        if(IMEI == null || IMEI.length() < 10){
//            data = hardwareService.findCheckRecords(page);
//        }else {
//            data = hardwareService.findCheckRecordsByImei(page,IMEI);
//        }
//        page.setDataList(data);
//        return Result.success(page,"check");
//    }

    @GetMapping("/checkRecord/list/{IMEI}")
    public Result<CheckPage<CheckRecord>> checkRecordPage(CheckPage page,@PathVariable("IMEI") String IMEI){
        List<CheckRecord> data = null;
        if(IMEI == null || IMEI.length() < 10){
            data = hardwareService.findCheckRecords(page);
        }else {
            data = hardwareService.findCheckRecordsByImei(page,IMEI);
        }
        page.setDataList(data);
        return Result.success(page,"check");
    }
    @GetMapping("/checkRecord/list/")
    public Result<CheckPage<CheckRecord>> checkRecordPage(CheckPage page){
        List<CheckRecord> data = null;
        data = hardwareService.findCheckRecords(page);
        page.setDataList(data);
        return Result.success(page,"check");
    }


}

