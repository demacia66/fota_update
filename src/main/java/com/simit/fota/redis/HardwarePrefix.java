package com.simit.fota.redis;

public class HardwarePrefix extends BasePrefix{

    public HardwarePrefix(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public HardwarePrefix(String prefix) {
        super(2 * 60 *60,prefix);
    }

    public static HardwarePrefix getById = new HardwarePrefix("id");
}
