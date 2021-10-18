package com.simit.fota.entity;

import lombok.Data;

@Data
public class ReportVo {

    private String id;

    private String version;

    private String company;

    private String upgradeResult;

    private String productName;
}
