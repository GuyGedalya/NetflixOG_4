package com.example.nog_android.connectionClasses;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.nog_android.Entities.Category;
import com.example.nog_android.Entities.Movie;
import com.example.nog_android.Entities.User;


@Database(entities = {User.class, Movie.class, Category.class}, version = 1)
public abstract class AppDB extends RoomDatabase{
    public abstract MovieDao movieDao();
    public abstract CategoryDao categoryDao();
    public abstract UserDao userDao();
}
