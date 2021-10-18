package com.simit.fota.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.simit.fota.entity.OldUser;
import com.simit.fota.entity.User;
import com.simit.fota.exception.GlobalException;
import com.simit.fota.result.CodeMsg;
import com.simit.fota.result.Page;
import com.simit.fota.result.Result;
import com.simit.fota.security.DefaultPasswordEncoder;
import com.simit.fota.service.UserService;
import com.simit.fota.util.JWTTokenUtil;
import com.simit.fota.util.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/fota/api/user")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private DefaultPasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public Result<Boolean> signup(@RequestBody User user){

        String username = user.getUsername();
        if (StringUtils.isEmpty(username)){
            throw new GlobalException(CodeMsg.USER_EMPTY);
        }
        String password = user.getPassword();

        if (StringUtils.isEmpty(password)){
            throw new GlobalException(CodeMsg.PASSWORD_EMPTY);
        }


        String phoneNum = user.getPhoneNumber();
        if (!ValidatorUtil.isMobile(phoneNum)){
            throw new GlobalException(CodeMsg.MOBILE_ERROR);
        }

        password = passwordEncoder.encode(password);

        userService.doSignUp(username,password,phoneNum);

        return Result.success(CodeMsg.Sign_SUCCESS,"sign");
    }

    @PutMapping("/changepassword")
    public Result<Boolean> changePassword(@RequestBody OldUser olduser, HttpServletRequest request){
        String username = olduser.getUsername();
        String password = olduser.getPassword();
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

        userService.doUpdatePassword(olduser,username,password);
        return Result.success(true,"password");
    }

    @GetMapping("/info")
    public Result<Map<String, Object>> getUserInfo(HttpServletRequest request){
        String token = request.getHeader("token");
        Map<String,Object> data = userService.getUserInfo(token);
        return Result.success(data,"info");
    }

    //激活用户
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/activate/{userId}")
    public Result<Boolean> activate(@PathVariable("userId") Integer userId){
        if (userId == null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        userService.activateUser(userId);
        return Result.success(true,"activate");
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/delete/{userId}")
    public Result<Boolean> deleteUser(@PathVariable("userId") Integer userId){
        if (userId == null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        userService.deleteUser(userId);
        return Result.success(true,"user");
    }

    //获取用户列表
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/list")
    public Result<Page<User>> getUsers(  Page page,HttpServletRequest request){
        Page<User> result = userService.getUserList(page);
        return Result.success(result,"user");
    }
}
