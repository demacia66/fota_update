package com.simit.fota.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VersionVo {

    @JsonProperty("Version_ID")
    private Integer ID;

    @JsonProperty("Version_Name")
    private String VersionName;

    @JsonProperty("Description")
    private String Description;

    @JsonProperty("CreateTs")
    private String ts;

    @JsonProperty("Create_ts")
    private Long CreateTs;

    @JsonProperty("flag")
    private String flag;
}
