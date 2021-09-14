package com.simit.fota.service;

import com.simit.fota.dao.UserMapper;
import com.simit.fota.entity.SecurityUser;
import com.simit.fota.entity.User;
import com.simit.fota.exception.GlobalException;
import com.simit.fota.result.CodeMsg;
import com.simit.fota.security.DefaultPasswordEncoder;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("userDetailsService")
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private DefaultPasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    //进行登录
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //判断
//        if (username == null || !"mary".equals(username)){
//            //数据库中没有用户名，认证失败
//            throw new UsernameNotFoundException("用户名不存在！");
//        }
//        List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList("admins,ROLE_sale");

        User curUser = userMapper.findUserByUsername(username);

        if (curUser == null){
            throw new GlobalException(CodeMsg.USER_ERROR);
        }
        //用户没有激活
        if ("0".equals(curUser.getEnable())){
            throw new GlobalException(CodeMsg.USER_NO_ACTIVATION);
        }

        //根据用户名查询数据
//        User user = userService.selectByUsername(username);
        //判断

        List<String> permission = new ArrayList<>();
        permission.add("admin");
        permission.add("user");
        //根据用户查询用户权限列表
        List<String> permissionValueList = permission;
        SecurityUser securityUser = new SecurityUser();
        securityUser.setCurrentUserInfo(curUser);
        securityUser.setPermissionValueList(permissionValueList);
        return securityUser;

    }

    @Test
    public void test(){
        DefaultPasswordEncoder defaultPasswordEncoder = new DefaultPasswordEncoder();
        System.out.println(defaultPasswordEncoder.encode("111111"));
    }
}
