package ru.mirea.alyamovskiyvy.mireaproject.ui.voicenotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import ru.mirea.alyamovskiyvy.mireaproject.R;

public class VoiceNotesAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<VoiceNote> voiceNotes;

    public VoiceNotesAdapter(Context context, ArrayList<VoiceNote> items, View.OnClickListener onClickListener){
        this.context = context;
        voiceNotes = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.onClickListener = onClickListener;
    }

    @Override
    public int getCount() {
        return voiceNotes.size();
    }

    @Override
    public Object getItem(int position) {
        return voiceNotes.get(position);
    }

    public Object getItemById(int id){
        VoiceNote note = null;
        for (VoiceNote voiceNote :
                voiceNotes) {
            if (voiceNote.getId() == id){
                note = voiceNote;
                break;
            }
        }
        return note;
    }

    VoiceNote getVoiceNoteById(int id){
        return ((VoiceNote) getItemById(id));
    }

    public void addVoiceNote(VoiceNote note){
        voiceNotes.add(note);
        notifyDataSetChanged();
    }

    VoiceNote getVoiceNote(int position){
        return ((VoiceNote) getItem(position));
    }

    @Override
    public long getItemId(int position) {
        return voiceNotes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = inflater.inflate(R.layout.voice_note_item, parent, false);
        }

        VoiceNote note = getVoiceNote(position);
        ((TextView)view.findViewById(R.id.voiceNoteTextTextView)).setText(note.getText());
        ImageButton imageButton = view.findViewById(R.id.voiceNotePlayButton);
        imageButton.setTag(note.getId());
        imageButton.setOnClickListener(onClickListener);

        note.setImageButton(imageButton);

        return view;
    }

    View.OnClickListener onClickListener;
}
