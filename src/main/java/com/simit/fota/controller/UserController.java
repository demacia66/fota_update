package com.simit.fota.controller;

import com.simit.fota.exception.GlobalException;
import com.simit.fota.result.CodeMsg;
import com.simit.fota.result.Result;
import com.simit.fota.security.DefaultPasswordEncoder;
import com.simit.fota.service.UserService;
import com.simit.fota.util.JWTTokenUtil;
import com.simit.fota.util.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/fota/api/user")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private DefaultPasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public Result<Boolean> signup(String username,String password,@RequestParam("Phone_Number") String phoneNum){
        if (StringUtils.isEmpty(username)){
            throw new GlobalException(CodeMsg.USER_EMPTY);
        }
        if (StringUtils.isEmpty(password)){
            throw new GlobalException(CodeMsg.PASSWORD_EMPTY);
        }

        if (!ValidatorUtil.isMobile(phoneNum)){
            throw new GlobalException(CodeMsg.MOBILE_ERROR);
        }

        password = passwordEncoder.encode(password);

        userService.doSignUp(username,password,phoneNum);

        return Result.success(true,"sign");
    }

    @PutMapping("/changepassword")
    public Result<Boolean> changePassword(String username, String password, HttpServletRequest request){
        if (StringUtils.isEmpty(username)){
            throw new GlobalException(CodeMsg.USER_EMPTY);
        }

        String token = request.getHeader("token");

        String username1 = JWTTokenUtil.getUserInfoFromToken(token);

        //防止修改到别人的密码
        if (!username.equals(username1)){
            throw new GlobalException(CodeMsg.USER_INFO_ERROR);
        }

        if (StringUtils.isEmpty(password)){
            throw new GlobalException(CodeMsg.PASSWORD_EMPTY);
        }
        userService.doUpdatePassword(username,password);
        return Result.success(true,"password");
    }
}
