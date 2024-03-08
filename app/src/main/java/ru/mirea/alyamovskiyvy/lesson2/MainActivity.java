package ru.mirea.alyamovskiyvy.lesson2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
    }

    public void onClick(View sender) {
        String TAG = MainActivity.class.getSimpleName();
        Log.i(TAG, "Log here");
        textView.setText("Hello world!");
    }
}