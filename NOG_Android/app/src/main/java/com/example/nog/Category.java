package com.example.nog;

import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("name")
    private String name;

    @SerializedName("promoted")
    private boolean promoted;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPromoted() {
        return promoted;
    }

    public void setPromoted(boolean promoted) {
        this.promoted = promoted;
    }
}
