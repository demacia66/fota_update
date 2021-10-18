package com.simit.fota.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class CheckVo {
    private String id;
    private String version;
    private String company;
    private String token;
    private String productName;
    private String productVersion;
}
