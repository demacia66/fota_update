package com.simit.fota.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskVo {
    private List<String> imeis;
    @JsonProperty("Fota_Project_Name")
    private String FotaProjectName;
    @JsonProperty("Version_Name")
    private String versionName;
    @JsonProperty("Version_Id")
    private Integer versionId;
    @JsonProperty("File_Type")
    private String FileType;
}
