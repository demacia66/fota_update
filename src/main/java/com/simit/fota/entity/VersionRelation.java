package com.simit.fota.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 记录版本之间的关系
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VersionRelation {

    private Integer ID;

    private Integer FotaProjectID;

    private Integer VersionID;

    private Integer PrevVersionID;

    private Integer NextVersionID;

    private String delTag;
}
