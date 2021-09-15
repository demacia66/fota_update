package com.simit.fota.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VersionUpload {

    private String VersionName;

    private Integer FotaProjectID;

    private String preVersion;

    private String Description;

    private MultipartFile fullFile;

    private MultipartFile differFile;

}
