package com.simit.fota.util;

public class RedisKeyUtil {


    private static String checkZset = "checkZset";

    private static String CHECK_LIST_PREFIX = "checkList:";

    public static String getCheckZsetKey(){
        return checkZset;
    }

    public static String getCheckListKey(String imei){
        return (CHECK_LIST_PREFIX + imei);
    }

}
