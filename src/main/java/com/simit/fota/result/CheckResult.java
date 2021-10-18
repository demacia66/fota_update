package com.simit.fota.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;

@Data
public class CheckResult extends BaseResult{

    private Boolean update;

    @JsonProperty("product_version")
    private String productVersion;
    @JsonProperty("current_version")
    private String currentVersion;
    @JsonProperty("company")
    private String company;
    private String content;
    @JsonProperty("file_md5")
    private String fileMd5;
    private long date;
    private String url;
}
