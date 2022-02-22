package com.simit.fota.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author ycy
 * @email 615336738@qq.com
 * @create 2021-08-15 3:45 下午
 */
@Service
public class RedisService {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 获取对象
     */
    public <T> T get(KeyPrefix prefix,String key,Class<T> clazz){
        try (Jedis jedis = jedisPool.getResource()){
            //生成key
            jedis.select(3);
            String realKey = prefix.getPrefix() + key;
            String str = jedis.get(realKey);
            T t = stringToBean(str,clazz);
            return t;
        }
    }
    public List<String> get(KeyPrefix prefix, String key){
        try (Jedis jedis = jedisPool.getResource()){
            //生成key
            jedis.select(3);
            String realKey = prefix.getPrefix() + key;
            String str = jedis.get(realKey);
            return stringToList(str);
        }
    }


    /**
     * 设置对象
     */
    public <T> boolean set(KeyPrefix prefix,String key,T value){
        try (Jedis jedis = jedisPool.getResource()){
            jedis.select(3);
            String str = beanToString(value);
            if (str == null || str.length() <= 0){
                return false;
            }
            String realKey = prefix.getPrefix() + key;
            //过期时间
            int seconds = prefix.expiredSeconds();
            if (seconds <= 0){
                jedis.set(realKey,str);
            }else {
                jedis.setex(realKey,seconds,str);
            }
            return true;
        }
    }

    public <T> boolean remove(KeyPrefix prefix,String key){
        try (Jedis jedis = jedisPool.getResource()){
            jedis.select(3);
            String realKey = prefix.getPrefix() + key;
            //过期时间
            jedis.del(realKey);
            return true;
        }
    }
    public boolean insertZset(String key,String value,Double score){
        try (Jedis jedis = jedisPool.getResource()){
            jedis.select(3);
            //过期时间
            jedis.zadd(key,score,value);

            return true;
        }
    }

    public <T> List<T> getZRange(String key,int startRow,int limit,Class<T> clazz){
        try (Jedis jedis = jedisPool.getResource()){
            jedis.select(3);
            Set<String> zrange = jedis.zrange(key, startRow, startRow + limit - 1);
            List<T> res = getFromSet(zrange,clazz);
            return res;
        }
    }

    public long getZsetCount(String key){
        try (Jedis jedis = jedisPool.getResource()){
            jedis.select(3);
            return jedis.zcard(key);
        }
    }


    public <T> List<T> getListByKey(String key,Class<T> clazz,int startRow,int size){
        try(Jedis jedis = jedisPool.getResource()) {
            jedis.select(3);
            List<String> lrange = jedis.lrange(key, startRow, startRow + size - 1);
            return getFromList(lrange,clazz);
        }
    }

    private <T> List<T> getFromSet(Set<String> zrange, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        for (String cur : zrange){
            list.add(JSONObject.parseObject(cur,clazz));
        }
        return list;
    }

    public boolean pushList(String key,String value){
        try(Jedis jedis = jedisPool.getResource()) {
            jedis.select(3);
            jedis.lpush(key,value);
            if (jedis.llen(key) > 30){
                for (int i = 0; i < 10; i++) {
                    jedis.rpop(key);
                }
            }
            return true;
        }
    }

    private <T> List<T> getFromList(List<String> zrange, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        for (String cur : zrange){
            list.add(JSONObject.parseObject(cur,clazz));
        }
        return list;
    }

    public Long getListCount(String key){
        try(Jedis jedis = jedisPool.getResource()) {
            jedis.select(3);
            return jedis.llen(key);
        }
    }


    private <T> String beanToString(T value) {
        if (value == null){
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == Integer.class){
            return "" + value;
        }else if (clazz == String.class){
            return (String) value;
        }else if(clazz == Long.class){
            return "" + value;
        }
        return JSON.toJSONString(value);
    }

    private <T> T stringToBean(String str,Class<T> clazz) {

        if (str == null || str.length() <= 0||clazz == null){
            return null;
        }

        if (clazz == Integer.class){
            return (T) Integer.valueOf(str);
        }else if (clazz == String.class){
            return (T) str;
        }else if(clazz == Long.class){
            return (T) Long.valueOf(str);
        }
        return JSON.toJavaObject(JSON.parseObject(str),clazz);
    }

    private List<String> stringToList(String str) {

        if (str == null || str.length() <= 0){
            return null;
        }

        List<String> res = new ArrayList<>();

        String a = str.substring(1,str.length() - 1);
        String[] strs = a.split(",");

        for (String cur:strs){
            res.add(cur.replace("\"",""));
        }

        return res;
    }

    /**
     * 判断是否存在

     */
    public <T> boolean exists(KeyPrefix prefix,String key){
        try(Jedis jedis = jedisPool.getResource()){
            //生成key
            String realKey = prefix.getPrefix() + key;
            return jedis.exists(realKey);
        }
    }

    /**
     * 增加
     */
    public <T> Long incr(KeyPrefix prefix,String key){
        try(Jedis jedis = jedisPool.getResource()){
            //生成key
            String realKey = prefix.getPrefix() + key;
             return jedis.incr(realKey);
        }
    }

    /**
     * 减少
     */
    public <T> Long decr(KeyPrefix prefix,String key){
        try(Jedis jedis = jedisPool.getResource()){
            //生成key
            String realKey = prefix.getPrefix() + key;
             return jedis.decr(realKey);
        }
    }

}
