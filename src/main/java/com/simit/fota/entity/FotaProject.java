package com.simit.fota.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FotaProject {
    //项目id
    @JsonProperty("Fota_Project_ID")
    private Integer ID;
    //项目名
    @JsonProperty("Fota_Project_Name")
    private String FotaProjectName;
    //创建时间
    private Long CreateTs;
    //项目拥有者
    private String Creator;
    //默认0存在，1删除
    private String delTag;


}
