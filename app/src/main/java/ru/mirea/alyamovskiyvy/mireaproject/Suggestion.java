package ru.mirea.alyamovskiyvy.mireaproject;

public class Suggestion {
    public int MinTemp, MaxTemp;
    public String SuggestionString;
    public Suggestion(int minTemp, int maxTemp, String suggestionString){
        MinTemp = minTemp;
        MaxTemp = maxTemp;
        SuggestionString = suggestionString;
    }

    public boolean isInRange(int temp){
        return MinTemp <= temp && temp <= MaxTemp;
    }
}
