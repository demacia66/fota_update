package com.simit.fota.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VersionFiles {
    private Integer ID;

    private String FileType;

    private Integer VersionID;

    private Integer FotaProjectID;

    private String FileName;

    private String FileURL;

    private String FileMD5;

    private Long UploadTs;
}
