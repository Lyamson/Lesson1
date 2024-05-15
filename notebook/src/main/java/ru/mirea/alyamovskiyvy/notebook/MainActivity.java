package ru.mirea.alyamovskiyvy.notebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import ru.mirea.alyamovskiyvy.notebook.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION = 100;
    private ActivityMainBinding binding;
    private boolean isWork;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION);
        }

        binding.saveButton.setOnClickListener(view -> {
            String fileName = binding.titleEditText.getText().toString() + ".txt";

            writeFileToExternalStorage(fileName, binding.quoteEditText.getText().toString());
        });

        binding.loadButton.setOnClickListener(view -> {
            String fileName = binding.titleEditText.getText().toString() + ".txt";

            List<String> lines = readFileFromExternalStorage(fileName);
            if (lines == null) return;
            binding.quoteEditText.setText(String.join(
                    "\n",
                            lines
            ));
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            isWork = grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }

    public List<String> readFileFromExternalStorage(String fileName) {
        File file = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                fileName
        );
        FileInputStream stream = null;
        List<String> lines = null;
        try {
            stream = new FileInputStream(file.getAbsoluteFile());
            InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            lines = new ArrayList<>();
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            while (line != null) {
                lines.add(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            Log.e("ExternalStorage", "Error reading " + file, e);
        } finally {
            try {
                if (stream != null)
                    stream.close();
            } catch (IOException e) {
                Log.e("ExternalStorage", "Error closing", e);
            }
        }
        return lines;
    }
    public void writeFileToExternalStorage(String fileName, String data) {
        File file = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                fileName
        );
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file.getAbsoluteFile());
            OutputStreamWriter writer = new OutputStreamWriter(stream);

            writer.write(data);

            writer.close();
        } catch (IOException e) {
            Log.e("ExternalStorage", "Error writing " + file, e);
        } finally {
            try {
                if (stream != null)
                    stream.close();
            } catch (IOException e) {
                Log.e("ExternalStorage", "Error closing", e);
            }
        }
    }
    public boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
}