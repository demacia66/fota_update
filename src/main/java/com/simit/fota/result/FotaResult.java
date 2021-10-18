package com.simit.fota.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FotaResult {

    private String state;

    private String action;

    private String message;

    @JsonProperty("id")
    private String IMEI;

    private String ticket;
}
