package ru.mirea.alyamovskiyvy.mireaproject.ui.fileswork;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.security.crypto.EncryptedSharedPreferences;

import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import ru.mirea.alyamovskiyvy.mireaproject.R;
import ru.mirea.alyamovskiyvy.mireaproject.databinding.FragmentFilesWorkBinding;

public class FilesWorkFragment extends Fragment {
    public FilesWorkFragment() {
        // Required empty public constructor
    }
    private final String mainKeyAlias = "MainKeyAlias";
    FragmentFilesWorkBinding binding;
    SecretKey key;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFilesWorkBinding.inflate(inflater, container, false);


        SharedPreferences secureSharedPreferences = null;
        try {
            secureSharedPreferences = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    mainKeyAlias,
                    requireActivity().getBaseContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            String encodedKey = secureSharedPreferences.getString("key", null);
            if (encodedKey != null) {
                byte[] decodedKey = Base64.decode(encodedKey, Base64.DEFAULT);
                key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
            } else {
                key = generateKey();
                secureSharedPreferences.edit().putString("key", Base64.encodeToString(key.getEncoded(), Base64.DEFAULT)).apply();
            }
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

        ActivityResultLauncher<Intent> encryptFileActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getData() != null) {
                        Uri uri = result.getData().getData();
                        encryptAndSaveFile(uri);
                    }
                }
        );

        ActivityResultLauncher<Intent> decryptFileActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getData() != null) {
                        Uri uri = result.getData().getData();
                        decryptAndSaveFile(uri);
                    }
                }
        );

        binding.floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("text/plain");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            encryptFileActivityResultLauncher.launch(intent);
        });

        binding.decryptButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("*/*");
            //intent.addCategory(Intent.CATEGORY_OPENABLE);
            decryptFileActivityResultLauncher.launch(intent);
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void decryptAndSaveFile(Uri uri){
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = requireActivity().getContentResolver().openInputStream(uri);
            File decryptedFile = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                    "decrypted.txt"
            );

            outputStream = new FileOutputStream(decryptedFile);

            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);

            byte[] decryptedBytes = decryptMsg(bytes, key);
            outputStream.write(decryptedBytes);

            Toast.makeText(getContext(),"File decrypted at " + decryptedFile.getPath(), Toast.LENGTH_SHORT).show();
        } catch (IOException ex) {
            Log.e(FilesWorkFragment.class.getSimpleName(), "Error on decrypt and save: " + ex.getMessage());
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException ex) {
                Log.e(FilesWorkFragment.class.getSimpleName(), "Error on decrypt and save: " + ex.getMessage());
            }
        }
    }

    private void encryptAndSaveFile(Uri uri){
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = requireActivity().getContentResolver().openInputStream(uri);
            File encryptedFile = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                    "encrypted.enc"
            );

            outputStream = new FileOutputStream(encryptedFile);

            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);

            byte[] encryptedBytes = encryptMsg(bytes, key);
            outputStream.write(encryptedBytes);

            Toast.makeText(getContext(),"File encrypted at " + encryptedFile.getPath(), Toast.LENGTH_SHORT).show();
        } catch (IOException ex) {
            Log.e(FilesWorkFragment.class.getSimpleName(), "Error on encrypt and save: " + ex.getMessage());
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException ex) {
                Log.e(FilesWorkFragment.class.getSimpleName(), "Error on encrypt and save: " + ex.getMessage());
            }
        }
    }

    public static SecretKey generateKey() {
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed("any	data used as random seed".getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(256, sr);
            return new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encryptMsg(byte[] message, SecretKey secret) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            return cipher.doFinal(message);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decryptMsg(byte[] cipherText, SecretKey secret) {
        /*	Decrypt	the	message	*/
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secret);
            return cipher.doFinal(cipherText);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
                 | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}