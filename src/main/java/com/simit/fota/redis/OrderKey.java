package com.simit.fota.redis;

/**
 * @author ycy
 * @email 615336738@qq.com
 * @create 2021-08-15 5:09 下午
 */
public class OrderKey extends BasePrefix {

    public OrderKey(int expireSeconds,String prefix){
        super(expireSeconds, prefix);
    }
}
