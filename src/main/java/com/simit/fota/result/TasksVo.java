package com.simit.fota.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TasksVo {
    private Integer ID;

    private Integer taskId;
    private Integer IMEIID;
    private String IMEI;

    private String CreatorType;
    private String TaskName;

    private String Creator;


    @JsonProperty("Fota_Project_Name")
    private String FotaProjectName;

    @JsonProperty("Fota_Project_ID")
    private Integer FotaProjectID;

    private Integer VersionID;

    @JsonProperty("Version_Name")
    private String versionName;

    private String Status;

    private Long UpdateStatusTs;

    private String updateTs;

    @JsonProperty("Create_Ts")
    private Long CreateTs;

    @JsonProperty("CreateTs")
    private String cts;

    private String delTag;
}
