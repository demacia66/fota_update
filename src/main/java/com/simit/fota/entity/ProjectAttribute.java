package com.simit.fota.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectAttribute {
    private String curVersion;
    @JsonProperty("Fota_Project_ID")
    private Integer ID;
    @JsonProperty("Fota_Project_Name")
    private String FotaProjectName;
    @JsonProperty("Creat_ts")
    private Long CreateTs;
    private String Creator;
    @JsonProperty("CreatTs")
    private String ts;
}
