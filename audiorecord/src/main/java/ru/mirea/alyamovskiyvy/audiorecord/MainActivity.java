package ru.mirea.alyamovskiyvy.audiorecord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.IOException;

import ru.mirea.alyamovskiyvy.audiorecord.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION = 200;
    private boolean isWork;
    private ActivityMainBinding binding;
    private final String TAG = MainActivity.class.getSimpleName();
    private String filePath = null;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    boolean isRecording = false;
    boolean isPlaying = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.playRecordButton.setEnabled(false);
        filePath = (new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "/audiorecordertest.3gp")).getAbsolutePath();

        int audioRecordPermissionStatus = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);
        int storagePermissionStatus = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (audioRecordPermissionStatus == PackageManager.PERMISSION_GRANTED &&
            storagePermissionStatus == PackageManager.PERMISSION_GRANTED){
            isWork = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case REQUEST_CODE_PERMISSION:
                isWork = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!isWork) finish();
    }

    public void startRecordButton_onClick(View view) {
        if (!isRecording){
            binding.startRecordButton.setText("Stop recording");
            binding.playRecordButton.setEnabled(false);
            startRecording();
        } else {
            binding.startRecordButton.setText("Start recording");
            binding.playRecordButton.setEnabled(true);
            stopRecording();
        }
        isRecording = !isRecording;
    }

    public void playRecordButton_onClick(View view) {
        if (!isPlaying){
            binding.playRecordButton.setText("Stop playing");
            binding.startRecordButton.setEnabled(false);
            startPlaying();
        } else {
            binding.playRecordButton.setText("Play");
            binding.startRecordButton.setEnabled(true);
            stopPlaying();
        }
        isPlaying = !isPlaying;
    }

    private void startRecording(){
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(filePath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try{
            recorder.prepare();
        } catch (IOException e){
            Log.e(TAG, "prepare() failed");
        }
        recorder.start();
    }

    private void stopRecording(){
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    private void startPlaying(){
        player = new MediaPlayer();
        try{
            player.setDataSource(filePath);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }
    }

    private void stopPlaying(){
        player.stop();
        player.release();
        player = null;
    }
}