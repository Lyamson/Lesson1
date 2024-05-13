package ru.mirea.alyamovskiyvy.mireaproject.ui.myprofile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.mirea.alyamovskiyvy.mireaproject.databinding.FragmentMyProfileBinding;

public class MyProfileFragment extends Fragment {
    public MyProfileFragment() {
        // Required empty public constructor
    }
    private FragmentMyProfileBinding binding;
    private boolean isWork;
    private Uri imageUri;
    private String TAG = MyProfileFragment.class.getSimpleName();
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false);

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
                try {
                    File file = createImageFile();
                    String authorities = requireActivity().getApplicationContext().getPackageName() + ".fileprovider";
                    imageUri = FileProvider.getUriForFile(requireActivity(), authorities, file);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    cameraActivityResultLauncher.launch(cameraIntent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";

        File storageDirectory = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return  File.createTempFile(imageFileName, ".jpg", storageDirectory);
    }
}