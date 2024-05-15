package ru.mirea.alyamovskiyvy.employeedb.DB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Employee.class}, version = 1)
public abstract class EmployeeDatabase extends RoomDatabase {
    public abstract EmployeeDao employeeDao();
}
