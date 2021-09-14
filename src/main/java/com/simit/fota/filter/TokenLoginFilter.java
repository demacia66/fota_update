package com.simit.fota.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simit.fota.entity.SecurityUser;
import com.simit.fota.entity.User;
import com.simit.fota.exception.GlobalException;
import com.simit.fota.redis.RedisService;
import com.simit.fota.redis.UserKey;
import com.simit.fota.result.CodeMsg;
import com.simit.fota.result.Result;
import com.simit.fota.util.JWTTokenUtil;
import com.simit.fota.util.ResponseUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {


    private RedisService redisService;



    private AuthenticationManager authenticationManager;


    public TokenLoginFilter(AuthenticationManager authenticationManager,RedisService redisService) {
        this.redisService = redisService;
        this.authenticationManager = authenticationManager;
        this.setPostOnly(false);
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/fota/api/user/login","POST"));
    }


    //1 获取表单提交用户名和密码
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        //获取表单提交数据
        try {

            String username1 = request.getParameter("username");
            String password1 = request.getParameter("password");
            if (username1 != null && password1 != null){
                return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username1,password1,
                        new ArrayList<>()));
            }

            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            String username = user.getUsername();
            if (StringUtils.isEmpty(username)){
                throw new GlobalException(CodeMsg.USER_EMPTY);
            }
            String password = user.getPassword();
            if (StringUtils.isEmpty(password)){
                throw new GlobalException(CodeMsg.PASSWORD_EMPTY);
            }

//            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword(),
                    new ArrayList<>()));
        } catch (GlobalException e) {
            ResponseUtil.error(response,Result.error(e.getCodeMsg(),"authent"));
        }catch (UsernameNotFoundException e){
            ResponseUtil.error(response,Result.error(CodeMsg.USERNAME_PASSWORD_ERROR,"authent"));
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }



    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //认证成功，得到认证成功之后用户信息
        SecurityUser user = (SecurityUser)authResult.getPrincipal();
        //根据用户名生成token
        String token = JWTTokenUtil.createToken(user.getCurrentUserInfo().getUsername());
        //把用户名称和用户权限列表放到redis
        redisService.set(UserKey.getByName,user.getCurrentUserInfo().getUsername(),user.getPermissionValueList());

        //返回token
        //名字为token
        Map tokenMap = new HashMap<>();
        tokenMap.put("token",token);
        ResponseUtil.out(response, Result.success(tokenMap,"authent"));

    }


    //3 认证失败调用的方法
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        ResponseUtil.out(response, Result.error(CodeMsg.USERNAME_PASSWORD_ERROR,"auth"));
    }


}
