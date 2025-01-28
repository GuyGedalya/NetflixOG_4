package com.example.nog.ObjectClasses;

import com.google.gson.annotations.SerializedName;

public class TokenManager {
    private static TokenManager instance;
    @SerializedName("token")
    private String token;
    @SerializedName("user")
    private User user;

    // Private constructor to enforce Singleton pattern
    private TokenManager() {}

    // Get the single instance of TokenManager
    public static synchronized TokenManager getInstance() {
        if (instance == null) {
            instance = new TokenManager();
        }
        return instance;
    }

    // Get the token
    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    // Set the token
    public void setToken(String token) {
        this.token = token;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Clear the token
    public void clearToken() {
        this.token = null;
        this.user = null;
    }

}
