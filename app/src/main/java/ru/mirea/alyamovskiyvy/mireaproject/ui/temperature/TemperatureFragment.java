package ru.mirea.alyamovskiyvy.mireaproject.ui.temperature;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ru.mirea.alyamovskiyvy.mireaproject.R;
import ru.mirea.alyamovskiyvy.mireaproject.Suggestion;
import ru.mirea.alyamovskiyvy.mireaproject.databinding.FragmentTemperatureBinding;

public class TemperatureFragment extends Fragment
                                    implements SensorEventListener {
    public TemperatureFragment() {
        // Required empty public constructor
    }
    private String TAG = TemperatureFragment.class.getSimpleName();
    private FragmentTemperatureBinding binding;
    private final List<Suggestion> suggestions = Arrays.asList(
            new Suggestion(15, 130, "Одеться полегче"),
            new Suggestion(11, 20, "Надеть ветровку"),
            new Suggestion(-130, 10, "Надеть свитер"),
            new Suggestion(-130, 5, "Надеть шапку"),
            new Suggestion(-19, -5, "Надеть тёплую куртку"),
            new Suggestion(-130, -20, "Надеть шубу")
    );
    private SensorManager sensorManager;
    private Sensor temperatureSensor;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTemperatureBinding.inflate(inflater, container, false);

        Log.d(TAG, "onCreateView Callback");

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE){
            int temp = Math.round(event.values[0]);
            binding.temperatureTextView.setText(String.valueOf(temp));
            ArrayList<String> items = new ArrayList<>();

            for (Suggestion suggestion : suggestions) {
                if (suggestion.isInRange(temp)){
                    items.add(suggestion.SuggestionString);
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this.requireContext(), R.layout.dot_list_item, R.id.text1, items);
            binding.recommendationsListView.setAdapter(adapter);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}