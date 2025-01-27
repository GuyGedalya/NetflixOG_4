package com.example.nog.ObjectClasses;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

public class CategoryWithMovies {

    @SerializedName("categories")
    private Map<String, List<Movie>> categories;

    public Map<String, List<Movie>> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, List<Movie>> categories) {
        this.categories = categories;
    }
}
