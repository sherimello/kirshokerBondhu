package com.example.kirshokerbondhu.activities;

import static android.content.res.Resources.getSystem;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.kirshokerbondhu.R;
import com.example.kirshokerbondhu.classes.OnSwipeTouchListener;
import com.example.kirshokerbondhu.classes.bottom_spread_sheet;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, bottom_spread_sheet.Data {

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private Button button_signout;
    private ImageView image_menu;
    private CardView card_main, card_crop_recommendation;
    private View swipe_view;
    private TextView text_logout;
    private ConstraintLayout constraint_menu_items, constraint_crop_recommendation;
    private ArrayList<String> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        locations = new ArrayList<>();
        locations = getArrayList("locations");
//        Toast.makeText(getApplicationContext(), String.valueOf(getArrayList("locations").size()), Toast.LENGTH_SHORT).show();

        button_signout = findViewById(R.id.button_signout);
        image_menu = findViewById(R.id.image_menu);
        card_main = findViewById(R.id.card_main);
        swipe_view = findViewById(R.id.swipe_view);
        text_logout = findViewById(R.id.text_logout);
        constraint_menu_items = findViewById(R.id.constraint_menu_items);
        constraint_crop_recommendation = findViewById(R.id.constraint_crop_recommendation);
        card_crop_recommendation = findViewById(R.id.card_crop_recommendation);

        button_signout.setOnClickListener(view -> signOut());
        image_menu.setOnClickListener(this);
        card_crop_recommendation.setOnClickListener(this);
        constraint_crop_recommendation.setOnClickListener(this);
        text_logout.setOnClickListener(this);

        swipe_view.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                ObjectAnimator animator = ObjectAnimator
                        .ofFloat(card_main, "radius", 0, convertToPx(0));
                animator.setDuration(500);
                animator.start();
                swipe_view.setVisibility(View.GONE);
                card_main.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                card_main.animate().rotation(0).translationY(0).translationX(0).scaleX(1).scaleY(1).setDuration(700).setInterpolator(new OvershootInterpolator());
                constraint_menu_items.animate().translationX(-convertToPx(200)).rotation(19).setDuration(700).setInterpolator(new OvershootInterpolator());
                constraint_menu_items.setLayerType(View.LAYER_TYPE_NONE, null);

//                    animateMenuItems(0, 0, 1, 10, 0);

            }
        });

    }

    private void signOut() {
        GoogleSignInOptions gso = new
                GoogleSignInOptions.
                        Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth.signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                task -> {
                    Intent intent = new Intent(getApplicationContext(), SignIn.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                });
    }

    @Override
    public void onClick(View v) {
        if (v == text_logout) {
            signOut();
        }
        if (v == image_menu) {
            animate_main_card();
        }
        if (v == constraint_crop_recommendation) {
            Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
            new bottom_spread_sheet().show(getSupportFragmentManager(), "crop recommendation");
        }
    }

    private void animate_main_card() {
        swipe_view.setVisibility(View.VISIBLE);
        ObjectAnimator animator = ObjectAnimator
                .ofFloat(card_main, "radius", 0, convertToPx(17));
        animator.setDuration(500);
        animator.start();
        card_main.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        float width = getSystem().getDisplayMetrics().widthPixels;
        card_main.animate().rotation(-19).translationY(-width * .2f).translationX(width * .3f).scaleX(.8f).scaleY(.8f).setDuration(500).setInterpolator(new OvershootInterpolator());
        constraint_menu_items.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        constraint_menu_items.animate().translationX(0).rotation(0).setDuration(500).setInterpolator(new OvershootInterpolator());
    }

    public ArrayList<String> getArrayList(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public int convertToPx(int dp) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (dp * scale + 0.5f);
    }

    @Override
    public void getArrayList(ArrayList<String> locations) {

    }
}