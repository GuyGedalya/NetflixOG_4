package com.example.nog.connectionClasses;
import com.google.gson.annotations.SerializedName;


public class UserResponse {

    @SerializedName("Admin")
    private Boolean isAdmin;

    public Boolean getAdmin() {
        return isAdmin;
    }
}
