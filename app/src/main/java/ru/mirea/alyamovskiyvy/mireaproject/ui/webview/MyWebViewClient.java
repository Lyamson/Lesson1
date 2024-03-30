package ru.mirea.alyamovskiyvy.mireaproject.ui.webview;

import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {
    private String TAG = MyWebViewClient.class.getSimpleName();
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        Log.i(TAG, "Requested url: " + request.getUrl());
        return false;
    }
}
