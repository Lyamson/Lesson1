package ru.mirea.alyamovskiyvy.thread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import ru.mirea.alyamovskiyvy.thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new	Thread(new	Runnable()	{
                    public void run()	{
                        Log.d(MainActivity.class.getSimpleName(), binding.countEditText.getText().toString());
                        int count = Integer.valueOf(binding.countEditText.getText().toString());
                        int days = Integer.valueOf(binding.daysEditText.getText().toString());

                        int result = count / days;
                        binding.resultTextView.post(new Runnable() {
                            @Override
                            public void run() {
                                binding.resultTextView.setText(String.valueOf(result));
                            }
                        });
                    }
                }).start();
            }
        });

        setContentView(binding.getRoot());
    }
}