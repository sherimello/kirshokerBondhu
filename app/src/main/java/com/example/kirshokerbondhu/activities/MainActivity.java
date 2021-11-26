package com.example.kirshokerbondhu.activities;

import static android.content.res.Resources.getSystem;

import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
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
import com.example.kirshokerbondhu.classes.TypeWriter;
import com.example.kirshokerbondhu.classes.bottom_spread_sheet;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private Button button_signout;
    public ImageView image_menu, image_dp;
    private CardView card_main, card_crop_recommendation, card_budget_formulation;
    private View swipe_view;
    private TextView text_logout, text_mail, text_profile;
    private TypeWriter text_privacy;
    private CollapsingToolbarLayout collapsing_toolbar;
    private ConstraintLayout constraint_menu_items, constraint_crop_recommendation, constraint_budget_formulation;
    private Bitmap mIcon_val;
    private URL newurl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        button_signout = findViewById(R.id.button_signout);
        image_menu = findViewById(R.id.image_menu);
        image_dp = findViewById(R.id.image_dp);
        card_main = findViewById(R.id.card_main);
        swipe_view = findViewById(R.id.swipe_view);
        text_logout = findViewById(R.id.text_logout);
        text_mail = findViewById(R.id.text_mail);
        text_privacy = findViewById(R.id.text_privacy);
        text_profile = findViewById(R.id.text_profile);
        collapsing_toolbar = findViewById(R.id.collapsing_toolbar);
        constraint_menu_items = findViewById(R.id.constraint_menu_items);
        constraint_crop_recommendation = findViewById(R.id.constraint_crop_recommendation);
        constraint_budget_formulation = findViewById(R.id.constraint_budget_formulation);
        card_crop_recommendation = findViewById(R.id.card_crop_recommendation);
        card_budget_formulation = findViewById(R.id.card_budget_formulation);

        setUpHeaderInfo();

        button_signout.setOnClickListener(view -> signOut());
        image_menu.setOnClickListener(this);
        card_crop_recommendation.setOnClickListener(this);
        constraint_crop_recommendation.setOnClickListener(this);
//        constraint_budget_formulation.setOnClickListener(this);
        card_budget_formulation.setOnClickListener(this);
        text_logout.setOnClickListener(this);
        text_profile.setOnClickListener(this);

        swipe_view.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                resetMainCardFromMenuExpansionAnimation();
            }
        });

    }

    private void resetMainCardFromMenuExpansionAnimation() {


        ObjectAnimator animator = ObjectAnimator
                .ofFloat(card_main, "radius", 0, convertToPx(0));
        animator.setDuration(500);
        animator.start();
        swipe_view.setVisibility(View.GONE);
        card_main.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        card_main.animate().rotation(0).translationY(0).translationX(0).scaleX(1).scaleY(1).setDuration(500).setInterpolator(new OvershootInterpolator());
        constraint_menu_items.animate().translationX(-convertToPx(200)).rotation(19).setDuration(500).setInterpolator(new OvershootInterpolator());
        constraint_menu_items.setLayerType(View.LAYER_TYPE_NONE, null);

    }
    private void setUpHeaderInfo() {
        setUserPicture();
        collapsing_toolbar.setTitle("welcome " + Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName());
        text_mail.setText(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());
        text_privacy.setCharacterDelay(21);
        text_privacy.animateText(getString(R.string.privacy_text));
    }

    private void setUserPicture() {
        new Thread(() -> {
            try {
                newurl = new URL(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getPhotoUrl()).toString());
                mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            runOnUiThread(() -> {
                image_dp.setScaleType(ImageView.ScaleType.CENTER_CROP);
                image_dp.setImageBitmap(mIcon_val);
            });

        }).start();
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
        if (v == text_profile) {
            final ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    Pair.create(card_main, getResources().getString(R.string.card)));

            startActivity(new Intent(getApplicationContext(), Profile.class), options.toBundle());
            new Handler().postDelayed(this::finish, 1000);
        }
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
        if (v == constraint_budget_formulation) {
            Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
            new bottom_spread_sheet().show(getSupportFragmentManager(), "budget formation");
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

    public int convertToPx(int dp) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (dp * scale + 0.5f);
    }

    @Override
    public void onBackPressed() {
        if (swipe_view.getVisibility() == View.VISIBLE) {
            resetMainCardFromMenuExpansionAnimation();
        } else
            finishAffinity();
    }
}