package com.simit.fota.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Integer id;

    private String username;

    private String password;

    private String phoneNum;

    public User(String username, String password, String phoneNum) {
        this.username = username;
        this.password = password;
        this.phoneNum = phoneNum;
    }
}
