package ru.mirea.alyamovskiyvy.mireaproject.ui.voicenotes;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import ru.mirea.alyamovskiyvy.mireaproject.GetTextLineDialogFragment;
import ru.mirea.alyamovskiyvy.mireaproject.R;
import ru.mirea.alyamovskiyvy.mireaproject.databinding.FragmentVoiceNotesBinding;

public class VoiceNotesFragment extends Fragment {
    public VoiceNotesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private FragmentVoiceNotesBinding binding;
    private boolean isWork;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private boolean isRecording = false;
    private int playingId = -1;
    private String TAG = VoiceNotesFragment.class.getSimpleName();
    private int new_id = 0;
    private VoiceNotesAdapter adapter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVoiceNotesBinding.inflate(inflater, container, false);

        ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted ->
                    isWork = isGranted
        );

        adapter = new VoiceNotesAdapter(requireContext(), new ArrayList<>(), view -> {
            Log.d(VoiceNotesAdapter.class.getSimpleName(), String.format("Clicked note with id: %d & text: %s",
                    adapter.getVoiceNoteById((Integer) view.getTag()).getId(),
                    adapter.getVoiceNoteById((Integer) view.getTag()).getText()));
            if (isRecording) return;
            int id = (Integer)view.getTag();
            VoiceNote note = adapter.getVoiceNoteById(id);
            if (playingId < 0){
                binding.recordButton.setEnabled(false);
                note.setImageResource(android.R.drawable.ic_media_pause);

                startPlaying(id);
            } else if(playingId == id) {
                binding.recordButton.setEnabled(true);
                note.setImageResource(android.R.drawable.ic_media_play);
                stopPlaying();
            } else {
                VoiceNote playingVoiceNote = adapter.getVoiceNoteById(playingId);
                playingVoiceNote.setImageResource(android.R.drawable.ic_media_play);
                stopPlaying();

                note.setImageResource(android.R.drawable.ic_media_pause);
                startPlaying(id);
            }
        });

        binding.listView.setAdapter(adapter);

        binding.recordButton.setOnClickListener(view ->{
            int audioPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO);
            if (audioPermission == PackageManager.PERMISSION_GRANTED){
                isWork = true;
            } else {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
            }
            if (isWork){
                if (playingId >= 0){
                    VoiceNote playingVoiceNote = adapter.getVoiceNoteById(playingId);
                    playingVoiceNote.setImageResource(android.R.drawable.ic_media_play);
                    stopPlaying();
                }
                if (!isRecording){
                    binding.recordButton.setText("Остановить запись");
                    startRecording();
                } else {
                    binding.recordButton.setText("Добавить запись");
                    stopRecording();
                }
                isRecording = !isRecording;
            }
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private String getFilePath(int id){
        return (new File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC), String.format(Locale.ENGLISH, "/audiorecordertest_%d.3gp", id))).getAbsolutePath();
    }

    private void startRecording(){
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(getFilePath(new_id));
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

        GetTextLineDialogFragment dialog = new GetTextLineDialogFragment(this);
        dialog.show(requireActivity().getSupportFragmentManager(), "mireaproject");
    }

    public void onOkClicked(String text){
        adapter.addVoiceNote(new VoiceNote(new_id, text));
        new_id++;
    }

    private void startPlaying(int id){
        player = new MediaPlayer();
        try{
            player.setOnCompletionListener(player -> {
                adapter.getVoiceNoteById(playingId).setImageResource(android.R.drawable.ic_media_play);
                stopPlaying();
                binding.recordButton.setEnabled(true);
            });
            player.setDataSource(getFilePath(id));
            player.prepare();
            player.start();
            playingId = id;
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }
    }

    private void stopPlaying(){
        player.stop();
        player.release();
        player = null;
        playingId = -1;
    }
}