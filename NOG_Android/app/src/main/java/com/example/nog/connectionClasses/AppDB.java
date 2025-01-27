package com.example.nog.connectionClasses;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.nog.Converters.Converters;

import com.example.nog.ObjectClasses.Category;
import com.example.nog.ObjectClasses.Movie;
import com.example.nog.ObjectClasses.User;


@Database(entities = {User.class, Movie.class, Category.class}, version = 3, exportSchema = false)
@TypeConverters({Converters.class}) //  TypeConverters
public abstract class AppDB extends RoomDatabase{
    public abstract MovieDao movieDao();
    public abstract CategoryDao categoryDao();
    public abstract UserDao userDao();
}
