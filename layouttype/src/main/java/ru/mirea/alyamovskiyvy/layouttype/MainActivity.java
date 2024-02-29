package ru.mirea.alyamovskiyvy.layouttype;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = findViewById(R.id.textView);
        TextView tv2 = (TextView) findViewById(R.id.textView2);
        tv2.setText("Text has been changed successfully");
        Button button = findViewById(R.id.button);
        button.setText("Some button text");
        View.OnClickListener oclBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("Button clicked");
            }
        };
        button.setOnClickListener(oclBtn);
    }
}