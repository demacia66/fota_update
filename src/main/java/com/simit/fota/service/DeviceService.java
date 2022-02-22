package com.simit.fota.service;

import com.simit.fota.dao.*;
import com.simit.fota.entity.*;
import com.simit.fota.exception.GlobalException;
import com.simit.fota.result.CodeMsg;
import com.simit.fota.result.Page;
import com.simit.fota.result.Result;
import com.simit.fota.util.DateFormatUtil;
import com.simit.fota.util.ExcelRead;
import com.sun.org.apache.bcel.internal.classfile.Code;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sun.rmi.runtime.Log;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DeviceService {


    @Autowired
    private BrandService brandService;

    @Autowired
    private UDPMapper udpMapper;

    @Autowired
    private NetworkTypeService typeService;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private VersionService versionService;

    public void createNewDevice(Device device) {
        if (StringUtils.isEmpty(device.getIMEI())) {
            //IMEI号为空
            throw new GlobalException(CodeMsg.IMEI_EMPTY);
        }

        if (device.getIMEI().length() != 15){
            throw new GlobalException(CodeMsg.IMEI_LENGTH_ERROR);
        }

        Device device1 = deviceMapper.findByIMEI(device.getIMEI());
        if (device1 != null) {
            throw new GlobalException(CodeMsg.IMEI_DUPLICATE);
        }

        if (StringUtils.isEmpty(device.getProject())) {
            //项目为空
            throw new GlobalException(CodeMsg.PROJECT_EMPTY);
        }

        if (StringUtils.isEmpty(device.getSWRlse())) {
            throw new GlobalException(CodeMsg.SW_RLSE_NOT_EXIST);
        }

        //project检查
//        FotaProject project = projectMapper.findProjectByName(device.getProject());
//        if (project == null) {
//            throw new GlobalException(CodeMsg.PROJECT_NOT_EXIST);
//        }

//        int swlrse = versionService.findSwlrse(device.getSWRlse());
//        if (swlrse <= 0){
//            throw new GlobalException(CodeMsg.SW_RLSE_NOT_EXIST);
//        }

        //检查网络类型
        if (!StringUtils.isEmpty(device.getNetworkType())) {
            NetworkType type = typeService.findNetworkTypeByName(device.getNetworkType());
            if (type == null) {
                device.setNetworkType("Unknown");
                device.setNetworkTypeID(17);
            } else {
                device.setNetworkTypeID(type.getID());
            }
        } else {
            device.setNetworkTypeID(17);
            device.setNetworkType("Unknown");
        }

        if (StringUtils.isEmpty(device.getManufacturerBrand())) {
            device.setManufacturerBrand("neoway");
            device.setManufacturerBrandID(1);
        } else {
            ManufacturerBrand brand = brandService.findManufacturerBrandByName(device.getManufacturerBrand());
            if (brand == null) {
                throw new GlobalException(CodeMsg.BRAND_NOT_EXIST);
            }
            device.setManufacturerBrandID(brand.getID());
        }
        //设置创建时间
        device.setCreateTs(System.currentTimeMillis());

        deviceMapper.insertDevice(device);


    }


    public List<Device> getDevices() {
        List<Device> devices = deviceMapper.getDevices();
        return devices;
    }

    public Device findDeviceByIMEI(String IMEI) {
        if (StringUtils.isEmpty(IMEI)) {
            throw new GlobalException(CodeMsg.IMEI_EMPTY);
        }
        Device device = deviceMapper.findByIMEI(IMEI);
        if (device == null) {
            throw new GlobalException(CodeMsg.DEVICE_NOT_EXIST);
        }
        if (device.getCreateTs() != null){
            device.setCreate_ts(DateFormatUtil.formatDate(device.getCreateTs()));
        }
        return device;
    }

    public Page<Device> getDeviceList(Page page) {
        int totalCount = deviceMapper.findDeviceCount();
        if (page == null) {
            page = new Page();
        }else if (page.getOrderField() == null){
            page = new Page(totalCount, page.getCurrentPage(), page.getPageSize());
        }else {
            page = new Page(totalCount,page.getOrderType(),page.getOrderField(),page.getCurrentPage(),page.getPageSize());
        }

        List<Device> devices = deviceMapper.findAllDevices(page);

        for (Device device : devices) {
            if (device.getLastTs() != null) {
                device.setLast_ts(DateFormatUtil.formatDate(device.getLastTs()));
            }
            device.setCreate_ts(DateFormatUtil.formatDate(device.getCreateTs()));
        }
        page.setDataList(devices);
        return page;
    }

    //更新设备
    public void doUpdate(String IMEI, Device device) {

        if (device == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }

        if (!StringUtils.isEmpty(device.getIMEI()) && !device.getIMEI().equals(IMEI)) {
            throw new GlobalException(CodeMsg.PARAM_ERROR);
        }

        Device device1 = deviceMapper.findByIMEI(IMEI);
        if (device1 == null) {
            throw new GlobalException(CodeMsg.DEVICE_NOT_EXIST);
        }
//        if (device.getSWRlse() != null){
//            FotaProject name = projectMapper.findProjectByName(device1.getProject());
//            Version version = versionService.findVersionByPidVname(name.getID(), device.getSWRlse());
//            if (version == null){
//                throw new GlobalException(CodeMsg.SW_RLSE_NOT_EXIST);
//            }
//        }
        if (device.getNetworkType() == null && device.getManufacturerBrand() == null && device.getSWRlse() == null) {
            return;
        }
        if (!StringUtils.isEmpty(device.getManufacturerBrand())) {
            ManufacturerBrand brand = brandService.findManufacturerBrandByName(device.getManufacturerBrand());
            if (brand == null) {
                throw new GlobalException(CodeMsg.BRAND_NOT_EXIST);
            }
            device.setManufacturerBrandID(brand.getID());
        }
        if (!StringUtils.isEmpty(device.getNetworkType())) {
            NetworkType type = typeService.findNetworkTypeByName(device.getNetworkType());
            if (type == null) {
                throw new GlobalException(CodeMsg.TYPE_NOT_EXIST);
            }
            device.setNetworkTypeID(type.getID());
        }
        deviceMapper.updateDevice(device);
    }

    @Transactional
    public void deleteDevices(List<String> imeis) {
        List<String> delete = deviceMapper.findDelete(imeis);
        if (delete == null || delete.isEmpty()) {
            return;
        }
        deviceMapper.deleteDevices(imeis);
        deviceMapper.deleteDevicesInKVL(imeis);
    }

    public List<Device> importDevices(MultipartFile file) throws IllegalAccessException, IntrospectionException, IOException, InstantiationException, InvocationTargetException, NoSuchFieldException {
        List<Object> res = ExcelRead.readExcelByPOJO(file, Device.class);
        List<Device> deviceList = new ArrayList<>();
        for (Object cur : res) {

            if (cur instanceof Device) {
                Device device = (Device) cur;
                if (!StringUtils.isEmpty(device.getIMEI()) && !"IMEI".equals(device.getIMEI())) {
                    if (!(device.getIMEI().length() == 15)){
                        throw new GlobalException(CodeMsg.IMEI_LENGTH_ERROR);
                    }
                    deviceList.add(device);
                }
            }

        }
        insertOrUpdateDevice(deviceList);
        return deviceList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertOrUpdateDevice(List<Device> deviceList) {
        for (Device device : deviceList) {

            //看看是否以前存在这个设备
            try {
                Device cur = deviceMapper.findDeviceWithDelete(device);
                if (cur == null) {
                    createNewDevice(device);
                } else {
                    doUpdate(device.getIMEI(), device);
                }
            } catch (GlobalException e) {
                log.error(e.getCodeMsg().getMessage());
                continue;
            }

        }
    }

    public Page<IMEIKV> getDeviceReport(String imei,Page page) {

        int totalCount = deviceMapper.findReportCount(imei);

        if (page == null) {
            page = new Page();
            page.setOrderField("ts");
            page.setOrderType("desc");
        }else if (page.getOrderField() == null || page.getOrderField().equals("ID")){
            page = new Page(totalCount, page.getCurrentPage(), page.getPageSize());
            page.setOrderField("ts");
            page.setOrderType("desc");
        }else {
            page = new Page(totalCount,page.getOrderType(),page.getOrderField(),page.getCurrentPage(),page.getPageSize());
        }

        List<IMEIKV> pageData = deviceMapper.findAllReports(imei,page);
        for (IMEIKV cur : pageData) {
            if (cur.getTs() != null) {
                cur.setReportTs(DateFormatUtil.formatDate(cur.getTs()));
            }
        }

        page.setDataList(pageData);

        return page;
    }


    public List<Integer> findIMEI(List<String> imeis) {
        return deviceMapper.findIMEIS(imeis);
    }

    public Page<Device> getDeviceList(Page page, String imei) {
        int totalCount = deviceMapper.findDeviceCountIMEI(imei);
        if (page == null) {
            page = new Page();
        }else if (page.getOrderField() == null){
            page = new Page(totalCount, page.getCurrentPage(), page.getPageSize());
        }else {
            page = new Page(totalCount,page.getOrderType(),page.getOrderField(),page.getCurrentPage(),page.getPageSize());
        }

        List<Device> devices = deviceMapper.findAllDevicesIMEI(page,imei);

        for (Device device : devices) {
            if (device.getLastTs() != null) {
                device.setLast_ts(DateFormatUtil.formatDate(device.getLastTs()));
            }
            device.setCreate_ts(DateFormatUtil.formatDate(device.getCreateTs()));
        }
        page.setDataList(devices);
        return page;
    }

    public Page<UDPReport> getUdpDeviceReport(String imei, Page page) {
        int totalCount = udpMapper.findUDPReportCount(imei);

        if (page == null) {
            page = new Page();
            page.setOrderField("ts");
            page.setOrderType("desc");
        }else if (page.getOrderField() == null || page.getOrderField().equals("ID")){
            page = new Page(totalCount, page.getCurrentPage(), page.getPageSize());
            page.setOrderField("ts");
            page.setOrderType("desc");
        }else {
            page = new Page(totalCount,page.getOrderType(),page.getOrderField(),page.getCurrentPage(),page.getPageSize());
        }


        List<UDPReport> pageData = udpMapper.findUDPReportByIMEI(imei,page);
//        for (UDPReport cur : pageData) {
//            if (cur.getTs() != null) {
//                cur.setReportTs(DateFormatUtil.formatDate(cur.getTs()));
//            }
//        }

        page.setDataList(pageData);

        return page;
    }
}
