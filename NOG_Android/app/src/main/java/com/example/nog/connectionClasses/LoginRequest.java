package com.example.nog.connectionClasses;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("UserName")
    String userName;
    @SerializedName("Password")
    String password;

    public LoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
