package com.simit.fota.redis;

/**
 * @author ycy
 * @email 615336738@qq.com
 * @create 2021-08-15 5:08 下午
 */
public class UserKey extends BasePrefix {

    private UserKey(String prefix){
        super( prefix);
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");




}
