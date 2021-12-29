package com.simit.fota.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"state","id","action","hobbies","message","update","product_version","company","file_md5","content","current_version","date","url"})
public class CheckResult {

    private Boolean update;

    private String state;

    private String action;

    private String message;
    @JsonProperty("id")
    private String IMEI;


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
