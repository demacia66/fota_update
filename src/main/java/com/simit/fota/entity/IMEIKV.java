package com.simit.fota.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IMEIKV {

    @JsonProperty("IMEI")
    private String IMEI;

    @JsonProperty("RSSI")
    private String RSSI;

    @JsonProperty("report_ts")
    private String reportTs;

    private Long ts;

    @JsonProperty("DeviceID")
    private String DeviceID;

    @JsonProperty("ICCID")
    private String ICCID;

    @JsonProperty("SW_rlse")
    private String SWRlse;
}
