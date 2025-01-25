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
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
    private static final Gson gson = new Gson(); // Gson למטרת המרה ל-JSON וממנו

    @TypeConverter
    public static String fromList(List<String> list) {
        if (list == null || list.isEmpty()) return null;
        return String.join(",", list); // ממיר רשימה למחרוזת עם פסיקים
    }

    @TypeConverter
    public static List<String> fromString(String value) {
        if (value == null || value.isEmpty()) return null;
        return Arrays.asList(value.split(",")); // ממיר מחרוזת לרשימה
    }

    @TypeConverter
    public static String fromDate(Date date) {
        return date == null ? null : formatter.format(date);
    }

    @TypeConverter
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
    public static String fromCategoryList(List<Category> categories) {
        if (categories == null || categories.isEmpty()) return null;
        return gson.toJson(categories); // המרה ל-JSON
    }

    @TypeConverter
    public static List<Category> toCategoryList(String value) {
        if (value == null || value.isEmpty()) return null;
        Type listType = new TypeToken<List<Category>>() {}.getType();
        return gson.fromJson(value, listType); // המרה חזרה ל-List<Category>
    }
}

