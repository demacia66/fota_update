package com.simit.fota.controller;

import com.alibaba.fastjson.JSON;
import com.simit.fota.dao.DeviceMapper;
import com.simit.fota.entity.Device;
import com.simit.fota.exception.GlobalException;
import com.simit.fota.result.CodeMsg;
import com.simit.fota.result.Result;
import com.simit.fota.security.DefaultPasswordEncoder;
import com.simit.fota.service.DeviceService;
import com.simit.fota.service.NetworkTypeService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RestController
public class TestController {

    @Autowired
    private DeviceMapper deviceMapper;

    @RequestMapping("/test/hello")
    public String hello(){
        return "hello";
    }

    @Autowired
    private DefaultPasswordEncoder passwordEncoder = new DefaultPasswordEncoder();

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private NetworkTypeService typeService;

    @Value("${file.upload-dir}")
    private String uploadPath;

    @RequestMapping("/test/insert/device")
    public void insertDevices() {
        List<String> types = typeService.findTypes();
        for (int i = 0; i < 1000; i++) {
            int cur = 200000 + i;
            Device device = new Device();
            device.setCreateTs(System.currentTimeMillis());
            device.setIMEI("" + cur);
            device.setManufacturerBrand("neoway");
            device.setManufacturerBrandID(1);
            int rand = new Random().nextInt(types.size() - 1);
            device.setNetworkTypeID(rand);
            device.setSWRlse("initial");
            device.setProject("first");
            device.setSN(UUID.randomUUID().toString().replaceAll("-",""));
            device.setDelTag("0");
            deviceMapper.insertDevice(device);
        }
    }

    @Test
    public void test(){
        System.out.println(passwordEncoder.encode("123456"));
        ArrayList<String> imeis = new ArrayList<>();
        imeis.add("112356");
        System.out.println(JSON.toJSON(imeis));
    }

    @GetMapping("/test/download")
    public Result<Boolean> downloadFile(String fileName, HttpServletResponse response){
        System.out.println(fileName);
        System.out.println(uploadPath + "\\" + fileName);
        File file = new File(uploadPath + "\\" + fileName);
        if (file.exists()){
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition","attachment;fileName="+fileName);
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try{
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                ServletOutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1){
                    os.write(buffer,0,i);
                    i = bis.read(buffer);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            throw new GlobalException(CodeMsg.FILE_NOT_EXIST);
        }
        return Result.success(true,"download");
    }
}
