package ru.mirea.alyamovskiyvy.mireaproject.ui.webview;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import ru.mirea.alyamovskiyvy.mireaproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WebViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebViewFragment extends Fragment {
    private String TAG = WebViewFragment.class.getSimpleName();

    public WebViewFragment() {
        // Required empty public constructor
    }
    public static WebViewFragment newInstance() {
        WebViewFragment fragment = new WebViewFragment();
        return fragment;
    }

    private WebView web;
    private boolean isWork;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);

        int internetPermission = ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.INTERNET);
        if (internetPermission == PackageManager.PERMISSION_GRANTED){
            isWork = true;
        } else{
            ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    isGranted -> isWork = isGranted
            );
            requestPermissionLauncher.launch(Manifest.permission.INTERNET);
        }
        web = view.findViewById(R.id.webView);
        if (isWork) {
            web.getSettings().setJavaScriptEnabled(true);
            web.setWebViewClient(new MyWebViewClient());
            web.loadUrl("https://www.google.com/");
        }
        else
            web.setEnabled(false);
        return view;
    }
}