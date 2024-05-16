package ru.mirea.alyamovskiyvy.employeedb.DB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {SuperHero.class}, version = 3)
public abstract class SuperHeroDatabase extends RoomDatabase {
    public abstract SuperHeroDao superHeroDao();
}
