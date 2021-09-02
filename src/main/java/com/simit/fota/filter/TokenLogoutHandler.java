package com.simit.fota.filter;

import com.simit.fota.redis.RedisService;
import com.simit.fota.redis.UserKey;
import com.simit.fota.result.Result;
import com.simit.fota.util.JWTTokenUtil;
import com.simit.fota.util.ResponseUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//退出处理器
@AllArgsConstructor
public class TokenLogoutHandler implements LogoutHandler {


    private RedisService redisService;


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        //1 从header里面获取token
        //2 token不为空，移除token，从redis删除token
        String token = request.getHeader("token");
        if(token != null) {
            //移除
            JWTTokenUtil.removeToken(token);
            //从token获取用户名
            String username = JWTTokenUtil.getUserInfoFromToken(token);
            redisService.remove(UserKey.getByName,username);
        }
        ResponseUtil.out(response, Result.success(null,"/fota/api/user/logout"));
    }
}
