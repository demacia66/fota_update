package com.simit.fota.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class JedisConfig {

    @Autowired
    private RedisConfig redisConfig;

    //配置redis连接池信息
    @Bean
    public JedisPool jedisPoolFactory(){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(redisConfig.getPoolMaxActive());
        poolConfig.setMaxWaitMillis(redisConfig.getPoolMaxWait() * 1000);
        poolConfig.setMaxIdle(redisConfig.getPoolMaxIdle());
        return new JedisPool(poolConfig,redisConfig.getHost(), redisConfig.getPort(), redisConfig.getTimeout() * 1000,redisConfig.getPassword());
    }
}
