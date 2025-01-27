package com.example.nog.Activities;

import android.content.Context;

import androidx.room.Room;

import com.example.nog.connectionClasses.AppDB;

public class MyDataBase {
    private static volatile AppDB instance;

    // Private constructor to prevent instantiation
    private MyDataBase() {}

    // Singleton method to get the database instance
    public static AppDB getInstance(Context context) {
        if (instance == null) {
            synchronized (MyDataBase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context, AppDB.class, "my_database")
                            .fallbackToDestructiveMigration()
                            .build();

                }
            }
        }
        return instance;
    }
}
