package com.example.nog_android.ObjectClasses;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("_id")
    private String mongoId;
    @SerializedName("UserName")
    private String userName;

    @SerializedName("Email")
    private String email;

    @SerializedName("Password")
    private String password;

    @SerializedName("Phone")
    private String phone;

    @SerializedName("ProfileImage")
    private String profileImagePath;

    @SerializedName("Admin")
    private Boolean isAdmin;

    @SerializedName("Movies")
    private List<String> moviesWatched;

    public User(String userName, String email, String password, String phone, String profileImagePath) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.profileImagePath = profileImagePath;
    }

    public String getMongoId() {return mongoId;}
    public Boolean getAdmin() {
        return isAdmin;
    }

    public List<String> getMoviesWatched() {
        return moviesWatched;
    }

    public void setMoviesWatched(List<String> moviesWatched) {
        this.moviesWatched = moviesWatched;
    }
}

