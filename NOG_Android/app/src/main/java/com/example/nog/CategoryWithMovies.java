package com.example.nog;

import com.example.nog_android.Movie;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

public class CategoryWithMovies {

    @SerializedName("categories") // המיפוי לשמות הקטגוריות בשרת
    private Map<String, List<Movie>> categories;

    public Map<String, List<Movie>> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, List<Movie>> categories) {
        this.categories = categories;
    }
}
