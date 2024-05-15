package ru.mirea.alyamovskiyvy.securesharedpreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;

import android.content.SharedPreferences;
import android.os.Bundle;

import java.io.IOException;
import java.security.GeneralSecurityException;

import ru.mirea.alyamovskiyvy.securesharedpreferences.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private final String mainKeyAlias = "MainKeyAlias";
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try {
            SharedPreferences secureSharedPreferences = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    mainKeyAlias,
                    getBaseContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            secureSharedPreferences.edit().putString("secure", "А. С. Пушкин").apply();

            binding.poetNameTextView.setText(secureSharedPreferences.getString("secure", "Тут будет имя вашего любимого поэта"));

        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}