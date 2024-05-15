package ru.mirea.alyamovskiyvy.employeedb;

import android.app.Application;

import androidx.room.Room;

import ru.mirea.alyamovskiyvy.employeedb.DB.EmployeeDatabase;

public class App extends Application {
    public static App instance;
    private EmployeeDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, EmployeeDatabase.class, "database")
                .allowMainThreadQueries()
                .build();
    }
    public static App getInstance() {
        return instance;
    }
    public EmployeeDatabase getDatabase() {
        return database;
    }
}