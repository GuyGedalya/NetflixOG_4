package com.example.nog_android.ObjectClasses;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.List;

@Entity
public class Movie {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("Title")
    private String title;

    @SerializedName("ReleaseDate")
    private Date releaseDate;

    @SerializedName("Image")
    private String imagePath;

    @SerializedName("Film")
    private String filmPath;

    @SerializedName("Categories")
    private List<Category> categories;

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setFilmPath(String filmPath) {
        this.filmPath = filmPath;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getFilmPath() {
        return filmPath;
    }

    public List<Category> getCategories() {
        return categories;
    }
}
