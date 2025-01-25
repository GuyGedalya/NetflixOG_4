package com.example.nog_android.connectionClasses;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.nog_android.Entities.Category;
import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM Category")
    List<Category> index();

    @Query("SELECT * FROM Category WHERE id = :id")
    Category get(int id);

    @Insert
    void insert(Category... categories);
    @Update
    void update(Category... categories);
    @Delete
    void delete(Category... categories);
}