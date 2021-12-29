package com.simit.fota.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//@Configuration
//public class AccessControlAllowOriginFilter extends WebMvcConfigurerAdapter {
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**").allowedOrigins(new String[]{"*"}).allowedMethods(new String[]{"PUT", "DELETE", "GET", "POST", "OPTIONS"}).allowedHeaders(new String[]{"*"}).exposedHeaders(new String[]{"access-control-allow-headers", "access-control-allow-methods", "access-control-allow-origin", "access-control-max-age", "X-Frame-Options"}).allowCredentials(true).maxAge(3600L);
//    }
//}