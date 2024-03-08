package ru.mirea.alyamovskiyvy.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class MyProgressDialogFragment extends DialogFragment {
    private String TAG = MyProgressDialogFragment.class.getSimpleName();
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMax(10);
        pd.setTitle("Some title of ProgressDialog");
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        return pd;
    }

    @Override
    public void onStart() {
        super.onStart();
        ProgressDialog pd = (ProgressDialog) getDialog();
        Handler handle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                pd.incrementProgressBy(1);
                Log.i(TAG, "Progress: " + pd.getProgress() + "/" + pd.getMax());
            }
        };
        Log.i(TAG, "Dialog: " + pd);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while (pd.getProgress() < pd.getMax()){
                        Thread.sleep(500);
                        handle.sendMessage(handle.obtainMessage());
                        if (pd.getProgress() == pd.getMax()){
                            pd.dismiss();
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
