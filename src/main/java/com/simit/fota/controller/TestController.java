package com.simit.fota.controller;

import com.alibaba.fastjson.JSON;
import com.simit.fota.security.DefaultPasswordEncoder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.ArrayList;

@RestController
public class TestController {
    @RequestMapping("/test/hello")
    public String hello(){
        return "hello";
    }

    @Autowired
    private DefaultPasswordEncoder passwordEncoder = new DefaultPasswordEncoder();

    @Test
    public void test(){
        System.out.println(passwordEncoder.encode("123456"));
        ArrayList<String> imeis = new ArrayList<>();
        imeis.add("112356");
        System.out.println(JSON.toJSON(imeis));
    }
}
