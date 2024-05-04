package ru.mirea.alyamovskiyvy.workmanager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

public class UploadWorker extends Worker {
    static final String TAG = UploadWorker.class.getSimpleName();
    public UploadWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params
            ){
        super(context, params);
    }
    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork Callback: started");
        try{
            TimeUnit.SECONDS.sleep(10);
        } catch(InterruptedException e){
            e.printStackTrace();
        }

        Log.d(TAG, "doWork Callback: ended");
        return Result.success();
    }
}
