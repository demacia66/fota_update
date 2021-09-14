package com.simit.fota.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.simit.fota.annotation.excelRescoure;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    private Integer id;


    @JsonProperty("IMEI")
    @excelRescoure("IMEI")
    private String IMEI;

    @JsonProperty("SN")
    @excelRescoure("SN")
    private String SN;

    @JsonProperty("Project")
    @excelRescoure("Project")
    private String Project;

    private Integer ManufacturerBrandID;

    @JsonProperty("Manufacturer_Brand")
    @excelRescoure("Manufacturer_Brand")
    private String ManufacturerBrand;

    private Integer NetworkTypeID;

    @JsonProperty("Network_Type")
    @excelRescoure("Network_Type")
    private String NetworkType;

    @JsonProperty("SW_rlse")
    @excelRescoure("SW_rlse")
    private String SWRlse;

    private Long CreateTs;

    @JsonProperty("location")
    @excelRescoure("location")
    private String Location;

    private String RSSI;

    private Long lastTs;

    private String delTag = "0";

    @JsonProperty("DeviceID")
    private String DeviceID;

    private String last_ts;
}
