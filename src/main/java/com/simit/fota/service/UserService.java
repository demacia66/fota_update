package com.simit.fota.service;

import com.simit.fota.dao.UserMapper;
import com.simit.fota.entity.User;
import com.simit.fota.exception.GlobalException;
import com.simit.fota.result.CodeMsg;
import com.simit.fota.security.DefaultPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DefaultPasswordEncoder passwordEncoder;

    public void doSignUp(String username,String password,String phoneNum){
        User user = userMapper.findUserByUsername(username);
        if (user != null){
            throw new GlobalException(CodeMsg.USERNAME_EXIST);
        }
        User user1 = new User(username,password,phoneNum);
        userMapper.insertUser(user1);
    }

    public void doUpdatePassword(String username, String password) {
        User user = userMapper.findUserByUsername(username);
        if (user == null){
            throw new GlobalException(CodeMsg.USER_ERROR);
        }
        password = passwordEncoder.encode(password);
        user.setPassword(password);
        userMapper.updateUser(user);
    }
}
