package com.simit.fota.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Integer userId;

    private String username;

    private String password;

    @JsonProperty("Phone_Number")
    private String PhoneNumber;

    private String enable;

    public User(String username, String password, String phoneNum) {
        this.username = username;
        this.password = password;
        this.PhoneNumber = phoneNum;
    }
}
