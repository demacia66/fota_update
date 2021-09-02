package com.simit.fota.controller;

import com.simit.fota.security.DefaultPasswordEncoder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    }
}
