package ru.mirea.alyamovskiyvy.looper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import ru.mirea.alyamovskiyvy.looper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private MyLooper myLooper;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Handler mainThreadHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.d(MainActivity.class.getSimpleName(), "Task execute. This is result: " +	msg.getData().getString("result"));
            }
        };

        myLooper = new MyLooper(mainThreadHandler);
        myLooper.start();

        //binding.editTextMirea.setText("Мой номер по списку №1");
    }

    public void button_OnClick(View view) {
        String work = binding.workEditText.getText().toString();
        Integer age = Integer.valueOf(binding.ageEditTextNumber.getText().toString());
        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putString("KEY", work);
        msg.setData(bundle);
        myLooper.mHandler.sendMessageDelayed(msg, age * 250);
    }
}