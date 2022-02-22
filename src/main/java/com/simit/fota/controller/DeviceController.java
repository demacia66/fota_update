package com.simit.fota.controller;

import com.simit.fota.entity.*;
import com.simit.fota.exception.GlobalException;
import com.simit.fota.result.CodeMsg;
import com.simit.fota.result.Page;
import com.simit.fota.result.Result;
import com.simit.fota.service.*;
import com.simit.fota.util.ExcelRead;


import com.simit.fota.util.JWTTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.xml.stream.Location;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

@RestController
@RequestMapping("/fota/api/device")
@Slf4j
@Api(description = "设备操作接口")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private NetworkTypeService typeService;

    @Autowired
    private VersionService versionService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;


    @PostMapping("/add")
    @ApiOperation("添加设备")
    @ApiImplicitParam(name = "device",value = "设备实体device类",required = true,dataType = "Device")
    public Result<CodeMsg> addDevice(@RequestBody Device device){
        deviceService.createNewDevice(device);
        return Result.success(CodeMsg.DEVICE_ADD_SUCCESS,"device");
    }

    @GetMapping("/attribute/{IMEI}")
    @ApiOperation("设备详情")
    public Result<Device> deviceAttribute(@PathVariable("IMEI") String IMEI){
        Device device = deviceService.findDeviceByIMEI(IMEI);
        return Result.success(device,"attribute");
    }

    //设备列表
    @GetMapping("/list/")
    public Result<Page<Device>> deviceList(Page page){
        Page<Device> result = deviceService.getDeviceList(page);
        return Result.success(result,"list");
    }

    @GetMapping("/list/{imei}")
    public Result<Page<Device>> deviceList(Page page,@PathVariable("imei") String imei){
        Page<Device> result = deviceService.getDeviceList(page,imei);
        return Result.success(result,"list");
    }

    //设备属性
    @PutMapping("/attribute/{IMEI}")
    public Result<Boolean> updateDevice(@PathVariable("IMEI") String IMEI,@RequestBody Device device){
        deviceService.doUpdate(IMEI,device);
        return Result.success(true,"attribute");
    }

    //删除设备，参数为所有删除的imei号
    @DeleteMapping("/del")
    public Result<Boolean> deleteDevices(@RequestBody List<String> imeis){
        if (!(imeis == null || imeis.isEmpty())){
            deviceService.deleteDevices(imeis);
        }
        return Result.success(true,"device");
    }

    //删除设备
    @DeleteMapping("/del/{IMEI}")
    public Result<Boolean> deleteDevice(@PathVariable("IMEI") String imei){
        if (!(imei == null || imei.isEmpty())){
            List<String> imeis = new ArrayList<>();
            imeis.add(imei);
            deviceService.deleteDevices(imeis);
        }
        return Result.success(true,"device");
    }

    //导入设备
    @RequestMapping(value = "/import",method = RequestMethod.POST)
    public Result<Boolean> importDevices( MultipartFile file) throws IllegalAccessException, IntrospectionException, IOException, InstantiationException, InvocationTargetException, NoSuchFieldException, ServletException {
        if (file == null){
            throw new GlobalException(CodeMsg.NOT_ALLOWED_EMPTY_FILE);
        }
        deviceService.importDevices(file);
        return Result.success(true,"csv");
    }

    //导入设备并创建任务
    @RequestMapping(value = "/import/{flag}",method = RequestMethod.POST)
    public Result<Boolean> importDevicesTask(MultipartFile file, ImportInfo importInfo,@PathVariable("flag") boolean flag,HttpServletRequest request) throws IllegalAccessException, IntrospectionException, IOException, InstantiationException, InvocationTargetException, NoSuchFieldException, ServletException {

        String token = (String) request.getAttribute("token");


        if(!flag){
            return importDevices(file);
        }

        if (file == null){
            throw new GlobalException(CodeMsg.NOT_ALLOWED_EMPTY_FILE);
        }

        if (importInfo == null){
            throw new GlobalException(CodeMsg.PARAM_ERROR);
        }

        if (StringUtils.isEmpty(importInfo.getFota_Project_Name())||StringUtils.isEmpty(importInfo.getFota_Project_Name())||StringUtils.isEmpty(importInfo.getFile_Type())){
            throw new GlobalException(CodeMsg.PARAM_ERROR);
        }

        FotaProject project = projectService.findProjectByName(importInfo.getFota_Project_Name());
        if (project == null){
            throw new GlobalException(CodeMsg.PROJECT_NOT_EXIST);
        }

        Version version = versionService.findVersionById(project.getID(),importInfo.getVersion_Id());

        if (version == null){
            throw new GlobalException(CodeMsg.VERSION_NOT_EXIST);
        }
        VersionFiles fileByType = versionService.findFileByType(version.getID(), project.getID(), importInfo.getFile_Type());
        if (fileByType == null){
            throw new GlobalException(CodeMsg.FILE_NOT_EXIST);
        }

        List<Device> deviceList = deviceService.importDevices(file);

        TaskVo taskVo = new TaskVo();
        taskVo.setFileType(importInfo.getFile_Type());
        taskVo.setFotaProjectName(importInfo.getFota_Project_Name());
        taskVo.setVersionName(importInfo.getVersion_Name());
        taskVo.setVersionId(importInfo.getVersion_Id());
        List<String> list = new ArrayList<>();
        for (Device cur : deviceList){
            list.add(cur.getIMEI());
        }
        taskVo.setImeis(list);
        taskService.createTask(taskVo,"01", JWTTokenUtil.getUserInfoFromToken(token));

        return Result.success(true,"csv");
    }

    @GetMapping("/report/{IMEI}")
    //获取IMEI_KV
    public Result<Page<IMEIKV>> getReports(@PathVariable("IMEI") String IMEI, Page page){
        Device deviceByIMEI = deviceService.findDeviceByIMEI(IMEI);
        if (deviceByIMEI == null || "1".equals(deviceByIMEI.getDelTag())){
            throw new GlobalException(CodeMsg.DEVICE_NOT_EXIST);
        }
        return Result.success(deviceService.getDeviceReport(IMEI,page),"report");
    }

    @GetMapping("/udpreport/{IMEI}")
    //udp上报数据
    public Result<Page<UDPReport>> getUDPReports(@PathVariable("IMEI") String IMEI, Page page){
        Device deviceByIMEI = deviceService.findDeviceByIMEI(IMEI);
        if (deviceByIMEI == null || "1".equals(deviceByIMEI.getDelTag())){
            throw new GlobalException(CodeMsg.DEVICE_NOT_EXIST);
        }
        return Result.success(deviceService.getUdpDeviceReport(IMEI,page),"udpreport");
    }

    @GetMapping("/brands")
    //获取所有的公司
    public Result<List<String>> getBrands(){
        List<String> brands = brandService.findAllManufacturerBrands();
        return Result.success(brands,"brands");
    }
    @GetMapping("/types")
    //获取所有的运营类别
    public Result<List<String>> getTypes(){
        List<String> types = typeService.findTypes();
        return Result.success(types,"types");
    }


}
