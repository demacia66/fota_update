package com.simit.fota.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Version {
    @JsonProperty("Version_ID")
    private Integer ID;
    @JsonProperty("Fota_Project_ID")
    private Integer FotaProjectID;

    private String VersionName;

    @JsonProperty("create_ts")
    private String ts;

    @JsonIgnore
    private Long CreateTs;

    private String Description;

    private String delTag = "0";
}
