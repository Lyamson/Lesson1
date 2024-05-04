package ru.mirea.alyamovskiyvy.cryptoloader;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MyLoader extends AsyncTaskLoader<String> {
    public static final String ARG_WORD = "word";
    private String decryptText;
    public MyLoader(@NonNull Context context, Bundle args) {
        super(context);
        if (args != null){
            byte[] keyBytes = args.getByteArray("KEY");
            byte[] cryptText = args.getByteArray(ARG_WORD);

            SecretKey key = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
            decryptText = MainActivity.decryptMsg(cryptText, key);
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
        SystemClock.sleep(3000);
        return decryptText;
    }
}
