package com.simit.fota.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    private String IMEI;

    private String SN;

    private String Project;


}
