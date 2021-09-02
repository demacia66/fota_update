package com.simit.fota.util;

import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;


public class JWTTokenUtil {

    //token有效时长
    @Value("${tokenEcpiration}")
    private static long tokenEcpiration = 24*60*60*1000;
    //秘钥
    @Value("${innosPerson}")
    private static String tokenSignKey = "aabbcc";

    //通过用户名生成token
    public static String createToken(String username){
        String token = Jwts.builder().setSubject(username).setExpiration(new Date(System.currentTimeMillis() + tokenEcpiration))
                .signWith(SignatureAlgorithm.HS512, tokenSignKey).compressWith(CompressionCodecs.GZIP).compact();
        return token;
    }

    //从token中获取用户信息
    public static String getUserInfoFromToken(String token){
        String username = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token).getBody().getSubject();
        return username;
    }

    public static void removeToken(String token) {
    }
}
