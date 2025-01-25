package com.example.nog_android;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.nog.Converters.Converters;

@Database(entities = {User.class, Movie.class, Category.class}, version = 1)
@TypeConverters({Converters.class}) // הוספת TypeConverters
public abstract class AppDB extends RoomDatabase{
    public abstract MovieDao movieDao();
    public abstract CategoryDao categoryDao();
    public abstract UserDao userDao();
}
