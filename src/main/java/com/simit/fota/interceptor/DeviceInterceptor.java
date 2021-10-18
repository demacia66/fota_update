package com.simit.fota.interceptor;


import com.simit.fota.annotation.LoginRequired;
import com.simit.fota.redis.HardwarePrefix;
import com.simit.fota.redis.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class DeviceInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        if (handler instanceof HandlerMethod){

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            if (loginRequired != null){
                String token = httpServletRequest.getParameter("token");
                if (StringUtils.isEmpty(token)){
                    return false;
                }
                String id = httpServletRequest.getParameter("id");
                if (StringUtils.isEmpty(id) || id.length() != 15){
                    return false;
                }
                String redisToken = redisService.get(HardwarePrefix.getById, id, String.class);
                if (StringUtils.isEmpty(redisToken) || !redisToken.equals(token)){
                    return false;
                }
                return true;
            }
            return true;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
