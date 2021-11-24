package com.example.kirshokerbondhu.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;

import com.example.kirshokerbondhu.R;

public class Profile extends AppCompatActivity {

    private CardView card_dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        card_dp = findViewById(R.id.card_dp);

    }

    @Override
    public void onBackPressed() {
        final ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                Pair.create(card_dp, getResources().getString(R.string.card)));

        startActivity(new Intent(getApplicationContext(), MainActivity.class), options.toBundle());
    }
}