package com.example.kirshokerbondhu.activities;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;

import com.example.kirshokerbondhu.R;

import java.util.Locale;


public class SplashTest extends AppCompatActivity {

    private TextView go;
    private CardView logo;
    Locale locale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_test);

        go = findViewById(R.id.go);
        logo = findViewById(R.id.logo);
        logo.setScaleX(0);
        logo.setScaleY(0);
        logo.setAlpha(0);
        logo.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        logo.animate().scaleY(1).scaleX(1).alpha(1).rotation(360).setInterpolator(new OvershootInterpolator()).setDuration(1207);

        go.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        go.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(2000).setInterpolator(new OvershootInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                go.setLayerType(View.LAYER_TYPE_NONE, null);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        go.setOnClickListener(v -> {

            changeLang(lang);
            Intent intent = new Intent(getApplicationContext(), OnboardingTest.class);
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(SplashTest.this, logo, "continue");
            startActivity(intent, options.toBundle());
        });
    }

    String lang= "bn";

    public void changeLang(String lang) {
        Configuration config = getBaseContext().getResources().getConfiguration();
        if (!"".equals(lang) && !config.locale.getLanguage().equals(lang)) {

            locale = new Locale(lang);
            Locale.setDefault(locale);
            Configuration conf = new Configuration(config);
            conf.locale = locale;
            getBaseContext().getResources().updateConfiguration(conf, getBaseContext().getResources().getDisplayMetrics());
        }
    }

}