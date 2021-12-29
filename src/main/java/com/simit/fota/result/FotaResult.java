package com.simit.fota.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({"state","action","message","id","ticket"})
public class FotaResult {

    private String state;

    private String action;

    private String message;

    @JsonProperty("id")
    private String IMEI;

    private String ticket;
}
