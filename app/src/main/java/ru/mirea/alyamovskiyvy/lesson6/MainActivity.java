package ru.mirea.alyamovskiyvy.lesson6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import ru.mirea.alyamovskiyvy.lesson6.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private final String GROUP_KEY = "group";
    private final String NUMBER_KEY = "number";
    private final String TITLE_KEY = "title";
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences prefs = getSharedPreferences("Lesson6", Context.MODE_PRIVATE);

        binding.groupEditText.setText(prefs.getString(GROUP_KEY, ""));
        binding.numberEditText.setText(prefs.getString(NUMBER_KEY, ""));
        binding.titleEditText.setText(prefs.getString(TITLE_KEY, ""));

        binding.saveButton.setOnClickListener(view -> {
            SharedPreferences.Editor editor = prefs.edit();

            editor.putString(GROUP_KEY, binding.groupEditText.getText().toString());
            editor.putString(NUMBER_KEY, binding.numberEditText.getText().toString());
            editor.putString(TITLE_KEY, binding.titleEditText.getText().toString());

            editor.apply();
            Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show();
        });
    }
}