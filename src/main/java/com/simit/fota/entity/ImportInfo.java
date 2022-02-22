package com.simit.fota.entity;

import lombok.Data;

@Data
public class ImportInfo {
    private String Fota_Project_Name;
    private Integer Version_Id;
    private String Version_Name;
    private String File_Type;
}
