package ru.mirea.alyamovskiyvy.mireaproject.ui.myprofile;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import ru.mirea.alyamovskiyvy.mireaproject.databinding.FragmentMyProfileBinding;

public class MyProfileFragment extends Fragment {
    private final String NAME_KEY = "name";
    private final String SURNAME_KEY = "surname";
    private final String AGE_KEY = "age";
    private final String fileName = "avatar";
    public MyProfileFragment() {
        // Required empty public constructor
    }
    private FragmentMyProfileBinding binding;
    private boolean isWork;
    private Uri imageUri;
    private String TAG = MyProfileFragment.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false);

        File file = new File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                fileName + ".jpg"
        );
        String authorities = requireActivity().getApplicationContext().getPackageName() + ".fileprovider";
        imageUri = FileProvider.getUriForFile(requireActivity(), authorities, file);

        sharedPreferences = requireActivity().getSharedPreferences("profile", Context.MODE_PRIVATE);

        loadSaved();

        ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> isWork = isGranted
        );

        ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK)
                        binding.avatarImageView.setImageURI(imageUri);
                }
        );

        binding.takePhotoButton.setOnClickListener(view -> {
            int cameraPermission = ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.CAMERA);
            if (cameraPermission == PackageManager.PERMISSION_GRANTED){
                isWork = true;
            } else{
                requestPermissionLauncher.launch(Manifest.permission.CAMERA);
            }
            if (isWork){
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                cameraActivityResultLauncher.launch(cameraIntent);
            }
        });

        binding.saveButton.setOnClickListener(view -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(NAME_KEY, binding.nameEditText.getText().toString());
            editor.putString(SURNAME_KEY, binding.surnameEditText.getText().toString());
            editor.putString(AGE_KEY, binding.ageEditTextNumber.getText().toString());

            editor.apply();
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
    private void loadSaved() {
        binding.avatarImageView.setImageURI(imageUri);

        binding.nameEditText.setText(sharedPreferences.getString(NAME_KEY, ""));
        binding.surnameEditText.setText(sharedPreferences.getString(SURNAME_KEY, ""));
        binding.ageEditTextNumber.setText(sharedPreferences.getString(AGE_KEY, ""));
    }
}