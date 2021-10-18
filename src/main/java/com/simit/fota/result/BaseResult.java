package com.simit.fota.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BaseResult {

    private String state;

    private String action;

    private String message;
    @JsonProperty("id")
    private String IMEI;
}
