package com.simit.fota.filter;

import com.simit.fota.exception.GlobalException;
import com.simit.fota.redis.RedisService;
import com.simit.fota.redis.UserKey;
import com.simit.fota.result.CodeMsg;
import com.simit.fota.util.JWTTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

public class TokenAuthFilter extends BasicAuthenticationFilter {



    private RedisService redisService;

    public TokenAuthFilter(AuthenticationManager authenticationManager,RedisService redisService) {
        super(authenticationManager);
        this.redisService = redisService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //获取当前认证成功用户权限信息
        UsernamePasswordAuthenticationToken authRequest = null;
//        System.out.println(request.getRequestURL());
        try {
            authRequest = getAuthentication(request);
        } catch (Exception e) {
            e.printStackTrace();

        }
        //判断如果有权限信息，放到权限上下文中
        if(authRequest != null) {
            SecurityContextHolder.getContext().setAuthentication(authRequest);
        }
        chain.doFilter(request,response);
    }

    //获取用户信息
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        //从header获取token
        Enumeration<String> headerNames = request.getHeaderNames();

        String token = request.getHeader("token");
        String token1 = request.getHeader("authorization");


        if (StringUtils.isEmpty(token)){
            token = token1;
        }

        if(token != null) {
            request.setAttribute("token",token);
            //从token获取用户名
            String username = JWTTokenUtil.getUserInfoFromToken(token);
//            System.out.println(username);
            //从redis获取对应权限列表
            List<String> permissionValueList = redisService.get(UserKey.getByName,username);

            if (permissionValueList == null){
                return null;
            }

            Collection<GrantedAuthority> authority = new ArrayList<>();

            for(String permissionValue : permissionValueList) {
                SimpleGrantedAuthority auth = new SimpleGrantedAuthority(permissionValue);
                authority.add(auth);
            }
            return new UsernamePasswordAuthenticationToken(username,token,authority);
        }
        return null;
    }

}
