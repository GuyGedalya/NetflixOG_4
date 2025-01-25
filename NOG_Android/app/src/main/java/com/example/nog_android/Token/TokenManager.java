package com.example.nog_android.Token;

public class TokenManager {
    private static TokenManager instance;
    private String token;

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

    // Set the token
    public void setToken(String token) {
        this.token = token;
    }

    // Clear the token
    public void clearToken() {
        this.token = null;
    }
}

