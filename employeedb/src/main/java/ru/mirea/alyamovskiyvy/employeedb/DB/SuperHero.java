package ru.mirea.alyamovskiyvy.employeedb.DB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SuperHero {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String real_name;
    public String hero_name;
    public String abilities;
    public SuperHero(){
    }
    public SuperHero(String real_name, String hero_name, String abilities){
        this.real_name = real_name;
        this.hero_name = hero_name;
        this.abilities = abilities;
    }
}
