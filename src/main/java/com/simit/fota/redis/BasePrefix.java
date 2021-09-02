package com.simit.fota.redis;

/**
 * @author ycy
 * @email 615336738@qq.com
 * @create 2021-08-15 4:55 下午
 */
public abstract class BasePrefix implements KeyPrefix{


    private int expireSeconds;
    //存放过期时间

    private String prefix;

    public BasePrefix(int expireSeconds,String prefix){
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    public BasePrefix(String prefix) {
        //0代表不过期
        this(0,prefix);
    }

    @Override
    public int expiredSeconds() {
        //默认0永不过期
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        //获取类名当作前缀
        String className = getClass().getSimpleName();

        return className + ":" + prefix;
    }
}
