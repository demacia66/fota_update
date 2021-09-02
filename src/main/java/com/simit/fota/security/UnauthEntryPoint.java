package com.simit.fota.security;

import com.simit.fota.result.CodeMsg;
import com.simit.fota.result.Result;
import com.simit.fota.util.ResponseUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//未授权统一处理类
public class UnauthEntryPoint implements AuthenticationEntryPoint {
    //没有授权的时候执行这个方法
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        ResponseUtil.out(httpServletResponse, Result.error(CodeMsg.NO_AUTHENTICATION.fillArgs("权限不足或未登录")));
    }
}

