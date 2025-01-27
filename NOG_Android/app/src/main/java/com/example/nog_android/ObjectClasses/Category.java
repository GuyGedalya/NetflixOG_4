package com.example.nog_android.ObjectClasses;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

@Entity
public class Category {
    public void setId(int id) {
        this.id = id;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    @SerializedName("name")
    private String name;

    @SerializedName("promoted")
    private boolean promoted;

    public Category(String name, boolean promoted) {
        this.name = name;
        this.promoted = promoted;
    }

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

}
