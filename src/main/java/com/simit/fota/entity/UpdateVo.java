package com.simit.fota.entity;

import lombok.Data;

@Data
public class UpdateVo {
    private String imei;
    private String version;
    private String company;
    private String project;
}
