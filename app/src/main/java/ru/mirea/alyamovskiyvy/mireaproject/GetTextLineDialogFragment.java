package ru.mirea.alyamovskiyvy.mireaproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ru.mirea.alyamovskiyvy.mireaproject.ui.voicenotes.VoiceNotesFragment;

public class GetTextLineDialogFragment extends DialogFragment {
    private EditText editText;
    private VoiceNotesFragment fragment;
    public GetTextLineDialogFragment(VoiceNotesFragment fragment){
        this.fragment = fragment;
        editText = new EditText(fragment.getContext());
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Введите текст для заметки")
                .setView(editText)
                .setPositiveButton("ОК", (dialog, button) -> {
                    fragment.onOkClicked(editText.getText().toString());
                    dialog.cancel();
                })
                .setNegativeButton("ОТМЕНА", (dialog, button) -> dialog.cancel());
        return builder.create();
    }
}
