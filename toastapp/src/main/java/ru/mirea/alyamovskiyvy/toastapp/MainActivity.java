package ru.mirea.alyamovskiyvy.toastapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editTextText);
    }

    public void onClickShowToast(View view) {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Студент №1 Группа БСБО-09-21 Количество символов - " + editText.getText().toString().length(),
                Toast.LENGTH_SHORT);
        toast.show();
    }
}