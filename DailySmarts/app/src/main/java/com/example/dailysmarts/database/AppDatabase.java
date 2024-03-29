package com.example.dailysmarts.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {SmartEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SmartDao smartDao();
}
