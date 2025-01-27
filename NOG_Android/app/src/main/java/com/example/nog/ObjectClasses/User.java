package com.example.nog.ObjectClasses;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;

    public void setMongoId(String mongoId) {
        this.mongoId = mongoId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    @SerializedName("_id")
    private String mongoId;

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

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

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getMongoId() {return mongoId;}
    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public List<String> getMoviesWatched() {
        return moviesWatched;
    }

    public void setMoviesWatched(List<String> moviesWatched) {
        this.moviesWatched = moviesWatched;
    }
}

