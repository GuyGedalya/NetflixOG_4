package com.example.nog.Converters;

import androidx.room.TypeConverter;

import com.example.nog_android.Category;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Converters {
    // Formatter used to convert Date objects to a specific string format
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());

    // Gson instance for JSON serialization and deserialization
    private static final Gson gson = new Gson();

    @TypeConverter
    // Converts a List of Strings into a single comma-separated String
    public static String fromList(List<String> list) {
        if (list == null || list.isEmpty()) return null;
        return String.join(",", list);
    }

    @TypeConverter
    // Converts a comma-separated String into a List of Strings
    public static List<String> fromString(String value) {
        if (value == null || value.isEmpty()) return null;
        return Arrays.asList(value.split(","));
    }

    @TypeConverter
    // Converts a Date object into a formatted String
    public static String fromDate(Date date) {
        return date == null ? null : formatter.format(date);
    }

    @TypeConverter
    // Converts a formatted String into a Date object
    public static Date toDate(String dateString) {
        if (dateString == null) return null;
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @TypeConverter
    // Converts a List of Category objects into a JSON string
    public static String fromCategoryList(List<Category> categories) {
        if (categories == null || categories.isEmpty()) return null;
        return gson.toJson(categories);
    }

    @TypeConverter
    // Converts a JSON string into a List of Category objects
    public static List<Category> toCategoryList(String value) {
        if (value == null || value.isEmpty()) return null;
        Type listType = new TypeToken<List<Category>>() {}.getType();
        return gson.fromJson(value, listType);
    }
}
