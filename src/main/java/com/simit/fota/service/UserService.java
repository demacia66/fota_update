package com.simit.fota.service;

import com.simit.fota.dao.UserMapper;
import com.simit.fota.entity.OldUser;
import com.simit.fota.entity.User;
import com.simit.fota.exception.GlobalException;
import com.simit.fota.result.CodeMsg;
import com.simit.fota.result.Page;
import com.simit.fota.result.Result;
import com.simit.fota.security.DefaultPasswordEncoder;
import com.simit.fota.util.JWTTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DefaultPasswordEncoder passwordEncoder;

    //注册
    public void doSignUp(String username,String password,String phoneNum){
        User user = userMapper.findUserByUsername(username);
        if (user != null){
            throw new GlobalException(CodeMsg.USERNAME_EXIST);
        }
        User user1 = new User(username,password,phoneNum);
        user1.setEnable("0");
        userMapper.insertUser(user1);
    }

    //更换密码
    public void doUpdatePassword(OldUser oldUser,String username, String password) {
        User user = userMapper.findUserByUsername(username);
        if (user == null){
            throw new GlobalException(CodeMsg.USER_ERROR);
        }
        if (!user.getPassword().equals(passwordEncoder.encode(oldUser.getOldPassword()))){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        password = passwordEncoder.encode(password);
        user.setPassword(password);
        userMapper.updateUser(user);
    }

    //获取用户信息
    public Map<String, Object> getUserInfo(String token) {
        String username = null;
        try {
            username = JWTTokenUtil.getUserInfoFromToken(token);
        }catch (Exception e){
            throw new GlobalException(CodeMsg.TOKEN_INVALID);
        }
        User user = userMapper.findUserByUsername(username);
        System.out.println(user);
        if (user == null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        Map<String,Object> res = new HashMap<>();
        res.put("username",user.getUsername());
        res.put("mobile",user.getPhoneNumber());
        return res;
    }

    //激活用户
    public void activateUser(Integer userId) {
        User user = userMapper.findUserByUserId(userId);
        if (user == null){
            throw new GlobalException(CodeMsg.USER_NOT_EXIST);
        }
        if ("1".equals(user.getEnable())){
            throw new GlobalException(CodeMsg.ACTIVATE_DUPLICATE);
        }
        if ("2".equals(user.getEnable())){
            throw new GlobalException(CodeMsg.USER_NOT_EXIST);
        }
        userMapper.activateUser(userId);
    }

    public Page<User> getUserList(Page page) {
        int totalCount = userMapper.userCount();
        if (page == null){
            page = new Page();
        }
        if (page.getCurrentPage() == null){
            page.setCurrentPage(1);
        }
        if (page.getPageSize() == null){
            page.setPageSize(10);
        }
        page.setTotalPage(totalCount / page.getPageSize() + 1);
        page.setTotalCount(totalCount);

        if (page.getCurrentPage() <= 0){
            page.setCurrentPage(1);
        }
        if (page.getPageSize() * (page.getCurrentPage() - 1) > totalCount){
            page.setCurrentPage(totalCount);
        }
        page.setStartRow((page.getCurrentPage() - 1) * page.getPageSize());
        page.setDataList(userMapper.findAllUsers(page));
        return page;
    }

    public void deleteUser(Integer userId) {
        User user = userMapper.findUserByUserId(userId);
        if (user == null){
            throw new GlobalException(CodeMsg.USER_NOT_EXIST);
        }
        userMapper.deleteUser(userId);
    }
}
