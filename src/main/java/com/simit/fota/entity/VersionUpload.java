package com.simit.fota.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VersionUpload {


    private String Version_Name;

    private Integer FotaProjectID;

    private String preVersion;

    private String Description;

    private MultipartFile fullFile;

    private MultipartFile differFile;

}
