package ru.mirea.alyamovskiyvy.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class MyTimeDialogFragment extends DialogFragment {
    private String TAG = MyTimeDialogFragment.class.getSimpleName();
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), myCallback, 0, 0, true);
        return timePickerDialog;
    }

    TimePickerDialog.OnTimeSetListener myCallback = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Log.i(TAG, "Time is " + hourOfDay + " hours and " + minute + " minutes");
        }
    };
}
