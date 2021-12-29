package com.simit.fota.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckRecord {

    @JsonProperty("IMEI")
    private String imei;
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("product_version")
    private String productVersion;
    @JsonProperty("check_ts")
    private Long checkTs;

    public CheckRecord(CheckVo checkVo){
        imei = checkVo.getId();
        productName = checkVo.getProductName();
        productVersion = checkVo.getProductVersion();
        checkTs = System.currentTimeMillis();
    }
}
