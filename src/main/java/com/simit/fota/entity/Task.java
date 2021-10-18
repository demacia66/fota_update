package com.simit.fota.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private Integer ID;
    private Integer TaskID;
    private Integer IMEIID;
    private Integer FotaProjectID;
    private Integer VersionID;
    private String Status;
    private Long UpdateStatusTs;
    private String delTag;
}
