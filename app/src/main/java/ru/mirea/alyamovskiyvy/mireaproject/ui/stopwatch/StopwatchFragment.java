package ru.mirea.alyamovskiyvy.mireaproject.ui.stopwatch;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Console;
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
    Thread thread;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStopwatchBinding.inflate(inflater, container, false);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Log.d(StopwatchFragment.class.getSimpleName(), "I'm working");
                        binding.minutesTextView.post(new Runnable() {
                            @Override
                            public void run() {
                                binding.minutesTextView.setText(AddZeroIfNeeded(minutes));
                            }
                        });
                        binding.secondsTextView.post(new Runnable() {
                            @Override
                            public void run() {
                                binding.secondsTextView.setText(AddZeroIfNeeded(seconds));
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
                        return;
                    }
                }
            }
        });
        thread.start();
        return binding.getRoot();
    }

    private String AddZeroIfNeeded(int number){
        if (number < 10 && number >= 0)
            return String.format("0%d", number);
        else
            return String.valueOf(number);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        thread.interrupt();
    }
}