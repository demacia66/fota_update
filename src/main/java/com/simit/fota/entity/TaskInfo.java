package com.simit.fota.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskInfo {
    @JsonProperty("taskId")
    private Integer ID;
    private Integer FotaProjectID;
    private Integer VersionID;
    private String Creator;
    private String CreatorType;
    private String Status;
    private String TaskName;
    private Long CreateTs;
    private Long UpdateStatusTs;
    private String delTag;
}