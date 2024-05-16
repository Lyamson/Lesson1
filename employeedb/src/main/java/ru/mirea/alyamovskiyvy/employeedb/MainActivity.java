package ru.mirea.alyamovskiyvy.employeedb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import ru.mirea.alyamovskiyvy.employeedb.DB.SuperHero;
import ru.mirea.alyamovskiyvy.employeedb.DB.SuperHeroDao;
import ru.mirea.alyamovskiyvy.employeedb.DB.SuperHeroDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SuperHeroDatabase db = App.getInstance().getDatabase();
        SuperHeroDao dao = db.superHeroDao();

        List<SuperHero> superHeroes = new ArrayList<>(Arrays.asList(
                new SuperHero("Джон", "Гравимен", "Управление гравитацией"),
                new SuperHero("Джек", "Мегамозг", "Сверхскоростные мозговые активности")
        ));
        for (SuperHero superHero :
                superHeroes) {
            dao.insert(superHero);
        }

        List<SuperHero> getHeroes = dao.getAll();
        List<HashMap<String, Object>> values = new ArrayList<>();
        for (SuperHero hero :
                getHeroes) {
            values.add(new HashMap<String, Object>(){
                {
                    put("id", hero.id);
                    put("realName", hero.real_name);
                    put("heroName", hero.hero_name);
                    put("ability", hero.abilities);
                }
            });
        }
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                values,
                R.layout.super_hero_item,
                new String[]{"id", "realName", "heroName", "ability"},
                new int[]{R.id.idTextView, R.id.realNameTextView, R.id.heroNameTextView, R.id.abilityTextView}
        );
        ListView listView = (ListView) findViewById(R.id.heroesListView);
        listView.setAdapter(adapter);
    }
}