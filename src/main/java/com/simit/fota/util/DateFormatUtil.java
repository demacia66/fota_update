package com.simit.fota.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {
    public static String formatDate(long date){
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("yyyy年MM月dd日 HH:mm:ss");
        return format.format(new Date(date));
    }
}
