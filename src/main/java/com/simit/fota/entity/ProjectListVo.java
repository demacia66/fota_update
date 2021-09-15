package com.simit.fota.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ycy
 * @email 615336738@qq.com
 * @create 2021-09-15 9:20 上午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
//用于显示项目列表
public class ProjectListVo {

    @JsonProperty("Fota_Project_ID")
    private Integer ID;

    //项目名
    @JsonProperty("Fota_Project_Name")
    private String FotaProjectName;

    private String curVersion;

    @JsonProperty("Create_ts")
    private String CreateTs;

}
