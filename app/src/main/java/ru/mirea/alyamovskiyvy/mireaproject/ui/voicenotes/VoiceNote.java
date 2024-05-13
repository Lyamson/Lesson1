package ru.mirea.alyamovskiyvy.mireaproject.ui.voicenotes;

import android.widget.ImageButton;

public class VoiceNote {
    private int id;
    private String text;
    private ImageButton imageButton;
    public VoiceNote(int id, String text){
        this.id = id;
        this.text = text;
    }
    public int getId(){
        return id;
    }

    public String getText(){
        return text;
    }

    public void setImageButton(ImageButton imageButton){
        this.imageButton = imageButton;
    }

    public void setImageResource(int resId){
        if (imageButton != null){
            imageButton.setImageResource(resId);
        }
    }
}
