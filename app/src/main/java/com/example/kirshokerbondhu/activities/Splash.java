package com.example.kirshokerbondhu.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.example.kirshokerbondhu.R;
import com.example.kirshokerbondhu.classes.DoubleClickListener;
import com.example.kirshokerbondhu.classes.SharedPrefs;
import com.example.kirshokerbondhu.classes.TypeWriter;
import com.google.firebase.auth.FirebaseAuth;

public class Splash extends AppCompatActivity {

    private TypeWriter text_krishoker, text_bondhu;
    private ImageView image_bg;
    private static final String ON_BOARDING_SEEN_STATE = "isOnboardingSeen";
    private SharedPrefs sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        text_krishoker = findViewById(R.id.text_krishoker);
        text_bondhu = findViewById(R.id.text_bondhu);
        image_bg = findViewById(R.id.image_bg);

        sharedPrefs = new SharedPrefs();

        Glide.with(this)
                .load(ResourcesCompat.getDrawable(getResources(), R.drawable.splashbg, null))
                .into(image_bg);

        animateTexts();
        changeActivity();

        image_bg.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {

                sharedPrefs.onboardingPref(getApplicationContext(), 1);

            }

            @Override
            public void onDoubleClick(View v) {
                sharedPrefs.onboardingPref(getApplicationContext(), 0);
            }
        });

    }

    private void changeActivity() {
        new Handler().postDelayed(() -> {

            SharedPreferences prefs = getSharedPreferences(ON_BOARDING_SEEN_STATE, MODE_PRIVATE);
            if (prefs.getInt("status", 0) == 1) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    final ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                            this,
                            Pair.create(text_krishoker, getResources().getString(R.string.krishoker)),
                            Pair.create(text_bondhu, getResources().getString(R.string.bondhu)));

                    startActivity(new Intent(getApplicationContext(), SignIn.class), options.toBundle());
                    new Handler().postDelayed(this::finish, 2000);
                } else {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            } else {
                Intent intent = new Intent(getApplicationContext(), OnboardingTest.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(this, image_bg, "continue");
                startActivity(intent, options.toBundle());
                finish();
            }

        }, 3000);
    }

    private void animateTexts() {
        new Handler().postDelayed(() -> {
            text_krishoker.setCharacterDelay(100);
            text_krishoker.animateText(getResources().getString(R.string.krishoker));
        }, 500);
        new Handler().postDelayed(() -> {
            text_bondhu.setCharacterDelay(100);
            text_bondhu.animateText(getResources().getString(R.string.bondhu));
        }, getResources().getString(R.string.krishoker).length() * 100L + 500);

    }
}