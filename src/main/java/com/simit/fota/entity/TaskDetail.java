package com.simit.fota.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDetail {
    @JsonIgnore
    private Integer taskId;
    @JsonProperty("taskId")
    private Integer ID;
    private String Creator;
    @JsonIgnore
    private String CreatorType;
    @JsonProperty("Creator_Type")
    private String CreatorTypeA;
    private String Status;
    @JsonProperty("Task_Name")
    private String TaskName;
    @JsonProperty("CreateTs")
    private Long CreateTs;
    @JsonProperty("create_ts")
    private String cTs;
    @JsonProperty("UpdateStatusTs")
    private Long UpdateStatusTs;
    @JsonProperty("UpdateStatus_ts")
    private String uTs;
    private String delTag;
    @JsonProperty("Fota_Project_Name")
    private String FotaProjectName;

    @JsonProperty("Fota_Project_ID")
    private Integer FotaProjectID;

    private Integer VersionID;

    @JsonProperty("Version_Name")
    private String versionName;
}
