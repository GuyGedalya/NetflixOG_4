package com.example.nog_android;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Movie.class, Category.class}, version = 1)
public abstract class AppDB extends RoomDatabase{
    public abstract MovieDao movieDao();
    public abstract CategoryDao categoryDao();
    public abstract UserDao userDao();
}
