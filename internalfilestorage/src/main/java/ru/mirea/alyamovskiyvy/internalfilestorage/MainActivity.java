package ru.mirea.alyamovskiyvy.internalfilestorage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import ru.mirea.alyamovskiyvy.internalfilestorage.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private String fileName = "mirea.txt";
    private ActivityMainBinding binding;
    Runnable loadText = new Runnable() {
        @Override
        public void run() {
            binding.savedTextTextView.post(() ->
                    binding.savedTextTextView.setText(getTextFromFile()));
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new Thread(loadText).start();

        binding.saveButton.setOnClickListener(view ->
                new Thread(() -> {
                    saveTextToFile();
                    loadText.run();
                }
        ).start());
    }
    public String getTextFromFile(){
        FileInputStream stream = null;
        try {
            stream = openFileInput(fileName);
            byte[] bytes = new byte[stream.available()];
            stream.read(bytes);
            String text = new String(bytes);
            Log.d(TAG, text);
            return text;
        } catch (IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (stream != null)
                    stream.close();
            } catch (IOException ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return null;
    }
    public void saveTextToFile(){
        FileOutputStream stream = null;
        try {
            stream = openFileOutput(fileName, MODE_PRIVATE);
            stream.write(binding.dateInfoTextEditMultiline.getText().toString().getBytes());
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null)
                    stream.close();
            } catch (IOException ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}