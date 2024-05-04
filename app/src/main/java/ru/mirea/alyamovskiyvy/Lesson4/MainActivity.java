package ru.mirea.alyamovskiyvy.Lesson4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import ru.mirea.alyamovskiyvy.Lesson4.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MusicPlayerFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        fragment = new MusicPlayerFragment();
        setContentView(binding.getRoot());

        binding.titleTextView.setText("Музыкальный плеер № 1");

        getSupportFragmentManager().
                beginTransaction().
                replace(binding.fragmentContainer.getId(), fragment).
                commit();

    }
}