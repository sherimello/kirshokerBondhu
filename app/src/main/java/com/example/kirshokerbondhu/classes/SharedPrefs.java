package com.example.kirshokerbondhu.classes;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {

    private static final String ON_BOARDING_SEEN_STATE = "isOnboardingSeen";

    public void onboardingPref(Context context, int i) {
        SharedPreferences.Editor editor = context.getSharedPreferences(ON_BOARDING_SEEN_STATE, MODE_PRIVATE).edit();
        editor.putInt("status", i);
        editor.apply();
    }

}
