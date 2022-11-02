package com.example.kirshokerbondhu.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.kirshokerbondhu.R;

public class Subscription extends AppCompatActivity implements View.OnClickListener {

    private ImageView image_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        image_back = findViewById(R.id.image_back);

        image_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == image_back) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("type", ""));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("type", ""));
        finish();
    }
}