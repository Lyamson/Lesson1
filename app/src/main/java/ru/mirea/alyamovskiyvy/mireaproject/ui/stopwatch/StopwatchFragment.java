package ru.mirea.alyamovskiyvy.mireaproject.ui.stopwatch;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.TimeUnit;

import ru.mirea.alyamovskiyvy.mireaproject.R;
import ru.mirea.alyamovskiyvy.mireaproject.databinding.FragmentStopwatchBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StopwatchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StopwatchFragment extends Fragment {

    public StopwatchFragment() {
        // Required empty public constructor
    }
    public static StopwatchFragment newInstance() {
        return new StopwatchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    FragmentStopwatchBinding binding;
    int minutes = 0, seconds = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStopwatchBinding.inflate(inflater, container, false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        binding.minutesTextView.post(new Runnable() {
                            @Override
                            public void run() {
                                binding.minutesTextView.setText(String.valueOf(minutes));
                            }
                        });
                        binding.secondsTextView.post(new Runnable() {
                            @Override
                            public void run() {
                                binding.secondsTextView.setText(String.valueOf(seconds));
                            }
                        });
                        TimeUnit.SECONDS.sleep(1);
                        seconds++;
                        if (seconds >= 60){
                            seconds = 0;
                            minutes++;
                        }
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return binding.getRoot();
    }
}