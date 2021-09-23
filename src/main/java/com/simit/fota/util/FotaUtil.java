package com.simit.fota.util;

import java.util.Random;
import java.util.UUID;

public class FotaUtil {

    /**
     * 生成随机字符串
     * @return
     */
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
