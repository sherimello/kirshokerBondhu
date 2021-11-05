package com.example.kirshokerbondhu.activities;

import static android.content.res.Resources.getSystem;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.kirshokerbondhu.R;
import com.example.kirshokerbondhu.classes.OnSwipeTouchListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private Button button_signout;
    private ImageView image_menu;
    private CardView card_main;
    private View swipe_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        button_signout = findViewById(R.id.button_signout);
        image_menu = findViewById(R.id.image_menu);
        card_main = findViewById(R.id.card_main);
        swipe_view = findViewById(R.id.swipe_view);

        button_signout.setOnClickListener(view -> signOut());
        image_menu.setOnClickListener(this);

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
        if (v == image_menu) {
            animate_main_card();
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
        card_main.animate().rotation(-15).translationY(-width * .3f).translationX(width * .5f).scaleX(.7f).scaleY(.7f).setDuration(700).setInterpolator(new OvershootInterpolator());
    }

    public int convertToPx(int dp) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (dp * scale + 0.5f);
    }

}