package com.simit.fota.controller;

import com.simit.fota.entity.Device;
import com.simit.fota.entity.IMEIKV;
import com.simit.fota.entity.User;
import com.simit.fota.exception.GlobalException;
import com.simit.fota.result.CodeMsg;
import com.simit.fota.result.Page;
import com.simit.fota.result.Result;
import com.simit.fota.service.DeviceService;
import com.simit.fota.util.ExcelRead;
import com.sun.istack.internal.Nullable;

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

    @GetMapping("/list")
    public Result<Page<Device>> deviceList(@RequestParam(required = false) Page page){
        Page<Device> result = deviceService.getDeviceList(page);
        return Result.success(result,"list");
    }

    @PutMapping("/attribute/{IMEI}")
    public Result<Boolean> updateDevice(@PathVariable("IMEI") String IMEI,@RequestBody Device device){
        deviceService.doUpdate(IMEI,device);
        return Result.success(true,"attribute");
    }

    @DeleteMapping("/del")
    public Result<Boolean> deleteDevices(@RequestBody List<String> imeis){
        if (!(imeis == null || imeis.isEmpty())){
            deviceService.deleteDevices(imeis);
        }
        return Result.success(true,"device");
    }


    @RequestMapping(value = "/import",method = RequestMethod.POST)
    public Result<Boolean> importDevices( MultipartFile file) throws IllegalAccessException, IntrospectionException, IOException, InstantiationException, InvocationTargetException, NoSuchFieldException, ServletException {
        if (file == null){
            throw new GlobalException(CodeMsg.NOT_ALLOWED_EMPTY_FILE);
        }
        deviceService.importDevices(file);
        return Result.success(true,"csv");
    }

    @GetMapping("/report/{IMEI}")
    public Result<Page<IMEIKV>> getReports(@PathVariable("IMEI") String IMEI,@RequestParam(required = false) Page page){
        Device deviceByIMEI = deviceService.findDeviceByIMEI(IMEI);
        if (deviceByIMEI == null || "1".equals(deviceByIMEI.getDelTag())){
            throw new GlobalException(CodeMsg.DEVICE_NOT_EXIST);
        }
        return Result.success(deviceService.getDeviceReport(IMEI,page),"report");
    }


}
