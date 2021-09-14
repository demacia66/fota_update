package com.simit.fota.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
//读取excel文件，value对应excel中的列名
public @interface excelRescoure {
    String value() default "";//默认为空
}