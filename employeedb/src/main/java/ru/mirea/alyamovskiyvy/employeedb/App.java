package ru.mirea.alyamovskiyvy.employeedb;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import ru.mirea.alyamovskiyvy.employeedb.DB.SuperHeroDatabase;

public class App extends Application {
    public static App instance;
    private SuperHeroDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, SuperHeroDatabase.class, "database")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }
    public static App getInstance() {
        return instance;
    }
    public SuperHeroDatabase getDatabase() {
        return database;
    }
}