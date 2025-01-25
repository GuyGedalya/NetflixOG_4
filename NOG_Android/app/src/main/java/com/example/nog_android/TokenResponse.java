package com.example.nog_android;

import com.google.gson.annotations.SerializedName;

public class TokenResponse {
    @SerializedName("token")
    private String token;
    @SerializedName("user")
    private User user;

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}

