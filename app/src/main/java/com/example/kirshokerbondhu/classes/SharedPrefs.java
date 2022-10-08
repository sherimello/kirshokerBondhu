package com.example.kirshokerbondhu.classes;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPrefs {

    private static final String ON_BOARDING_SEEN_STATE = "isOnboardingSeen", PREF_NAME = "is_data_downloaded";

    public void onboardingPref(Context context, int i) {
        SharedPreferences.Editor editor = context.getSharedPreferences(ON_BOARDING_SEEN_STATE, MODE_PRIVATE).edit();
        editor.putInt("status", i);
        editor.apply();
    }
    public void sharedDataAlreadyDownloadedStatus(Context context, int i) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("status", i);
        editor.apply();
    }

    public void saveArrayList(Context context, ArrayList<String> list, String key) {
        SharedPreferences prefs = context.getSharedPreferences("Data Pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }
    public void saveIntArrayList(Context context, ArrayList<Integer> list, String key) {
        SharedPreferences prefs = context.getSharedPreferences("Data Pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }

    public ArrayList<String> getArrayList(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences("Data Pref", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public ArrayList<Integer> getIntArrayList(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences("Data Pref", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
}
