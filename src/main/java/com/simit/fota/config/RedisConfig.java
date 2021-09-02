package com.simit.fota.config;



import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.Duration;




// 能够读取配置文件中redis打头的
@ConfigurationProperties(prefix = "spring.redis")
@Component
@Data
public class RedisConfig {

    private String host;

    private int port;

    private int timeout;

    @Value("${spring.redis.lettuce.pool.max-idle}")
    private int poolMaxIdle;

    @Value("${spring.redis.lettuce.pool.max-wait}")
    private int poolMaxWait;
    //秒


    @Value("${spring.redis.lettuce.pool.max-active}")
    private int poolMaxActive;
}