package com.simit.fota.entity;

import lombok.Data;

@Data
public class OldUser extends User{
    //旧密码
    private String oldPassword;
}
