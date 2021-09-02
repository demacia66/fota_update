package com.simit.fota.redis;

/**
 * @author ycy
 * @email 615336738@qq.com
 * @create 2021-08-15 4:53 下午
 */
public interface KeyPrefix {
    int expiredSeconds();

    String getPrefix();
}
