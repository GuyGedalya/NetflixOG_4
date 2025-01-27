package com.example.nog.ObjectClasses;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity
public class User {
    public void setId(int id) {
        this.id = id;
    }

    public void setMongoId(String mongoId) {
        this.mongoId = mongoId;
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

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

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

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
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

