package com.simit.fota.service;

import com.alibaba.fastjson.JSONObject;
import com.simit.fota.dao.DeviceMapper;
import com.simit.fota.dao.HardWareMapper;
import com.simit.fota.entity.*;
import com.simit.fota.redis.HardwarePrefix;
import com.simit.fota.redis.RedisService;
import com.simit.fota.result.FotaResult;
import com.simit.fota.util.FotaUtil;
import com.simit.fota.util.MD5;
import com.simit.fota.util.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class HardwareService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private VersionService versionService;

    @Autowired
    private ProjectService projectService;

    @Value("${innos}")
    private String innos;

    @Value("${file.downloadParam}")
    private String downloadParam;

    @Autowired
    private BrandService brandService;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private HardWareMapper hardWareMapper;

    @Autowired
    private TaskService taskService;

    @Value("${domain}")
    private String domain;

    public Map<String, String> hardwareLogin(Device device) {
        Map<String, String> res = new HashMap<>();
        res.put("message", "Get auth ticket success");
        if (device == null) {
            res.put("message", "company or projectName error");
            return res;
        }

        String ticket = FotaUtil.generateNumber(6);
        res.put("ticket", ticket);
        String token = MD5.getMD5Str(device.getIMEI() + innos + ticket);
        redisService.set(HardwarePrefix.getById, device.getIMEI(), token);
        return res;
    }

    public Map<String, String> doCheck(CheckVo checkVo) throws Exception {
        Map<String, String> res = new HashMap<>();
        CheckRecord checkRecord = new CheckRecord(checkVo);
        redisService.insertZset(RedisKeyUtil.getCheckZsetKey(), JSONObject.toJSONString(checkRecord), (double) System.currentTimeMillis());
        redisService.pushList(RedisKeyUtil.getCheckListKey(checkVo.getId()),JSONObject.toJSONString(checkRecord));
        String IMEI = checkVo.getId();
        Device device = deviceMapper.findByIMEI(IMEI);
        if (device == null) {
            res.put("message", "post param error");
            return res;
        }
        Integer IMEIID = taskService.findTaskByIMEI(IMEI);
        Task task = taskService.findTaskByIMEIID(IMEIID);
        if (task == null){
            res.put("message","no new version");
            return res;
        }


        String projectName = checkVo.getProductName();
        FotaProject projectByName = projectService.findProjectByName(projectName);
        ManufacturerBrand company = brandService.findManufacturerBrandByName(checkVo.getCompany());
        if (projectByName == null || company == null) {
            res.put("message", "company or projectName error");
            return res;
        }

//        String version = checkVo.getProductVersion();
//        if (StringUtils.isEmpty(version)) {
//            res.put("message", "post param error");
//            return res;
//        }
//
//        Version preVersion = versionService.findVersionByPidVname(projectByName.getID(), version);
//        if (preVersion == null) {
//            res.put("message", "no version file");
//            return res;
//        }

//        String recordVersion = device.getSWRlse();
//        device.setSWRlse(preVersion.getVersionName());
//        if (!recordVersion.equals(preVersion)) {
//            deviceMapper.updateDevice(device);
//        }

//        String curVersion = versionService.curVersion(projectByName.getID());
//        Version versionDetail = versionService.findVersionByPidVname(projectByName.getID(), curVersion);
//        if (preVersion.equals(curVersion)) {
//            res.put("message", "no new version");
//            return res;
//        }
        log.error(checkVo.toString());
        Version versionById = versionService.findVersionById(task.getFotaProjectID(), task.getVersionID());

        res.put("message", "new version");
        res.put("update", "true");
        res.put("current_version", versionById.getVersionName());
        res.put("content", versionById.getDescription());
        res.put("date", versionById.getCreateTs() + "");

//        List<VersionFiles> versionFilesList = versionService.findVersionFile(task.getFotaProjectID(),task.getVersionID());
        VersionFiles versionFiles = versionService.findFileByType(task.getVersionID(), task.getFotaProjectID(), task.getFileType());
        taskService.updateStatus(IMEIID,"03");
//        if (versionFilesList.size() >= 2){
//            for (VersionFiles cur:versionFilesList){
//                if ("1".equals(cur.getFileType())){
//                    versionFiles = cur;
//                }
//            }
//            if (versionFiles == null){
//                versionFiles = versionFilesList.get(0);
//            }
//        }else{
//            versionFiles = versionFilesList.get(0);
//        }
        if (versionFiles.getFileType().equals("1")){
            res.put("message","new full ver");
        }else {
            res.put("message","new differ ver");
        }
        if (versionFiles != null) {
            res.put("url", "http://fotafile1.ranchip.com:7788/" + versionFiles.getFileURL());
            res.put("file_md5", versionFiles.getFileMD5());
        }
        return res;
    }

    public Map<String, String> reportUpload(ReportVo reportVo) {

        Map<String, String> res = new HashMap<>();

        String IMEI = reportVo.getId();

        Device device = deviceMapper.findByIMEI(IMEI);

        if (device == null) {
            res.put("message", "post param error");
            return res;
        }

        log.error(IMEI);
        log.error(reportVo.getProductName());
        log.error(reportVo.getCompany());

        String upgradeResult = reportVo.getUpgradeResult();
        String result = null;

        if ("0".equals(upgradeResult)){
            log.error("升级结果未知");
            result = "05";
        }else if ("1".equals(upgradeResult)){
            log.error("升级成功");
            Version version = versionService.findVersionByPidVname(projectService.findProjectByName(reportVo.getProductName()).getID(), reportVo.getVersion());
            if (version != null){
                device.setSWRlse(version.getVersionName());
                deviceMapper.updateDevice(device);
            }
            result = "04";
        }else {
            result = "06";
            log.error("升级失败");
        }
        if (!StringUtils.isEmpty(result) && "04".equals(result)){
            taskService.updateStatus(device.getId(),result);
        }
        res.put("message","report imei info ok ");
        return res;
    }

    public void reportAuth(FotaResult result) {
        IMEIAuthent authent = new IMEIAuthent();
        authent.setIMEIID(Long.valueOf(result.getIMEI()));
        authent.setState(result.getState());
        authent.setRequestTs(System.currentTimeMillis());
        hardWareMapper.insertIMEIAuth(authent);
    }

    public Map<String, String> updateUpload(UpdateVo updateVo) {

        Map<String, String> res = new HashMap<>();

        String IMEI = updateVo.getImei();

        Device device = deviceMapper.findByIMEI(IMEI);

        if (device == null) {
            res.put("message", "post param error");
            return res;
        }

        String curVersion = updateVo.getVersion();
        String project = updateVo.getProject();

        Integer taskByIMEI = taskService.findTaskByIMEI(IMEI);
        Task task = taskService.findTaskByIMEIID(taskByIMEI);
        Integer projectID = task.getFotaProjectID();
        Integer versionID = task.getVersionID();
        Version version1 = versionService.findVersionById(projectID, versionID);
        String result = null;

        if (version1.getVersionName().equals(curVersion)){

            result = "04";
                device.setSWRlse(curVersion);
                deviceMapper.updateDevice(device);
        }else {
            result = "06";
        }

        if (!StringUtils.isEmpty(result) && "04".equals(result)){
            taskService.updateStatus(device.getId(),result);
        }
        res.put("message","update imei info ok ");
        return res;
    }

    public List<CheckRecord> findCheckRecords(CheckPage page) {
        page.setTotalCount(redisService.getZsetCount(RedisKeyUtil.getCheckZsetKey()));
        List<CheckRecord> data = redisService.getZRange(RedisKeyUtil.getCheckZsetKey(), page.getStartRow(), (int) page.getPageSize(), CheckRecord.class);
        return data;
    }

    public List<CheckRecord> findCheckRecordsByImei(CheckPage page, String imei) {
        page.setTotalCount(redisService.getListCount(RedisKeyUtil.getCheckListKey(imei)));
        List<CheckRecord> data = redisService.getListByKey(RedisKeyUtil.getCheckListKey(imei), CheckRecord.class,page.getStartRow(), (int) page.getPageSize());
        return data;
    }
}
