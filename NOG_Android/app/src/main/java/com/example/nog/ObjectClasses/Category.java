package com.example.nog.ObjectClasses;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class Category implements Serializable {
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
    final private boolean promoted;

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
