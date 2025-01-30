package com.example.nog.ObjectClasses;


import androidx.annotation.NonNull;

import com.example.nog.connectionClasses.ApiClient;
import com.example.nog.connectionClasses.ApiService;
import com.example.nog.connectionClasses.UserResponse;
import com.google.gson.annotations.SerializedName;

import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public void isAdmin(Consumer<Boolean> callback){
        ApiService apiService = ApiClient.getApiService();
        String userId = this.user.getMongoId();
        Call<UserResponse> call = apiService.getUser(userId);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Boolean isAdmin = response.body().getAdmin();
                    callback.accept(isAdmin);
                } else {
                    callback.accept(false); // Default value in case of an error
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                callback.accept(false); // Default value in case of an error
            }
        });
    }

}
