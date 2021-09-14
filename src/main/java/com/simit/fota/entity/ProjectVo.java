package com.simit.fota.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectVo {
     //初始版本
    @JsonProperty("initial_version")
    private String initialVersion;

    //项目名
    @JsonProperty("Fota_Project_Name")
    private String FotaProjectName;

    //项目描述
    private String Description;
}
