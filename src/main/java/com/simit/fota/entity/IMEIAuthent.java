package com.simit.fota.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IMEIAuthent {
    private Integer ID;
    private Long IMEIID;
    private Long RequestTs;
    private String state;
}
