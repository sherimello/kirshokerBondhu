package com.example.kirshokerbondhu.activities;

import static android.content.res.Resources.getSystem;

import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.example.kirshokerbondhu.R;
import com.example.kirshokerbondhu.classes.OnSwipeTouchListener;
import com.example.kirshokerbondhu.classes.SharedPrefs;
import com.example.kirshokerbondhu.classes.TypeWriter;
import com.example.kirshokerbondhu.classes.bottom_spread_sheet;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private Button button_signout;
    private ImageView image_bg;
    public ImageView image_menu, image_dp;
    private LinearLayout linear_info;
    private CardView card_info, card_weather, card_main, card_menu;
//    private LinearLayout  linear_crop_recommendation, linear_budget_formulationtion;
    private View swipe_view;
    private TextView text_logout, text_mail, text_profile;
    private TypeWriter text_privacy;
    private CollapsingToolbarLayout collapsing_toolbar;
    private ConstraintLayout constraint_soil_detection, constraint_budget_formulation, constraint_disease_detection, constraint_menu_items, constraint_crop_recommendation;
    private Bitmap mIcon_val;
    private SwitchCompat aSwitch;
    private URL newurl = null;
    private int counter = 0, make_info_visible = 0;
    public static final int NOTIFICATION_CHANNEL_ID = 10001 ;
    private final static String default_notification_channel_id = "default" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");

        if (type.equals("notification")) {
            startActivity(new Intent(getApplicationContext(), Weather.class));
            finish();
        }

        button_signout = findViewById(R.id.button_signout);
        image_menu = findViewById(R.id.image_menu);
//        image_bg = findViewById(R.id.image_bg);
        linear_info = findViewById(R.id.linear_info);
        aSwitch = findViewById(R.id.switch_notification);
        image_dp = findViewById(R.id.image_dp);
        card_main = findViewById(R.id.card_main);
        card_weather = findViewById(R.id.card_weather);
        card_menu = findViewById(R.id.card_menu);
        card_info = findViewById(R.id.card_info);
        swipe_view = findViewById(R.id.swipe_view);
        text_logout = findViewById(R.id.text_logout);
        text_mail = findViewById(R.id.text_mail);
        text_privacy = findViewById(R.id.text_privacy);
        text_profile = findViewById(R.id.text_profile);
        collapsing_toolbar = findViewById(R.id.collapsing_toolbar);
        constraint_menu_items = findViewById(R.id.constraint_menu_items);
        constraint_disease_detection = findViewById(R.id.constraint_disease_detection);
        constraint_crop_recommendation = findViewById(R.id.constraint_crop_recommendation);
        constraint_budget_formulation = findViewById(R.id.constraint_budget_formulation);
        constraint_soil_detection = findViewById(R.id.constraint_soil_detection);
//        linear_crop_recommendation = findViewById(R.id.card_crop_recommendation);
//        linear_budget_formulationtion = findViewById(R.id.card_budget_formulation);

        setUpHeaderInfo();

//        Glide.with(this)
//                .load(ResourcesCompat.getDrawable(getResources(), R.drawable.grass, null))
//                .disallowHardwareConfig()
//                .into(image_bg);

        button_signout.setOnClickListener(view -> signOut());

            aSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
                if (card_main.getAlpha() != 1)
                if (compoundButton.isChecked()) {
                    Toast.makeText(MainActivity.this, "on", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(this::notification, 3000);
                }
            });


        image_menu.setOnClickListener(this);
//        linear_crop_recommendation.setOnClickListener(this);
        card_weather.setOnClickListener(this);
        card_info.setOnClickListener(this);
        card_menu.setOnClickListener(this);
        constraint_crop_recommendation.setOnClickListener(this);
        constraint_disease_detection.setOnClickListener(this);
        constraint_budget_formulation.setOnClickListener(this);
        constraint_soil_detection.setOnClickListener(this);
//        linear_budget_formulationtion.setOnClickListener(this);
        text_logout.setOnClickListener(this);
        text_profile.setOnClickListener(this);

        swipe_view.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                resetMainCardFromMenuExpansionAnimation();
            }
        });

    }

    private void notification(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("disaster notification", "disaster notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        Intent resultIntent = new Intent(this, MainActivity.class).putExtra("type", "notification");
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "disaster notification");
        builder.setContentTitle("প্রাকৃতিক দূর্যোগ");
        builder.setContentText("আজ রাতে টর্নেডোর সম্ভাবনা আছে...");
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setAutoCancel(true);
        builder.setContentIntent(resultPendingIntent);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
        managerCompat.notify(1, builder.build());
    }

    private void resetMainCardFromMenuExpansionAnimation() {


        ObjectAnimator animator = ObjectAnimator
                .ofFloat(card_main, "radius", 0, convertToPx(0));
        animator.setDuration(500);
        animator.start();
        swipe_view.setVisibility(View.GONE);
        card_main.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        card_main.animate().alpha(1).rotationY(0).translationY(0).translationX(0).scaleX(1).scaleY(1).setDuration(500).setInterpolator(new OvershootInterpolator());
        constraint_menu_items.animate().alpha(0).translationX(-convertToPx(200)).rotationY(-15).setDuration(500).setInterpolator(new OvershootInterpolator());
        constraint_menu_items.setLayerType(View.LAYER_TYPE_NONE, null);

    }

    private void setUpHeaderInfo() {
        setUserPicture();
        collapsing_toolbar.setTitle("স্বাগতম " + Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName());
        text_mail.setText("স্বাগতম " + Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName());
//        text_mail.setText(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());
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

        if (v == card_info) {
            if (linear_info.getAlpha() == 0) {
                linear_info.setVisibility(View.VISIBLE);
                linear_info.animate().alpha(1).setDuration(500);
            }
        }

        if (v == card_weather) {
            startActivity(new Intent(getApplicationContext(), Weather.class));
            finish();
        }
        if (v == text_profile) {
            if (card_main.getAlpha() != 1) {
                final ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                        this,
                        Pair.create(card_main, getResources().getString(R.string.card)));

                startActivity(new Intent(getApplicationContext(), Profile.class), options.toBundle());
                new Handler().postDelayed(this::finish, 1000);
            }
        }
        if (v == text_logout) {
            if (card_main.getAlpha() != 1) {
                signOut();
            }
        }
        if (v == card_menu) {
            animate_main_card();
        }
        if (v == image_menu) {
            animate_main_card();
        }
        if (v == constraint_crop_recommendation) {
            
            new bottom_spread_sheet().show(getSupportFragmentManager(), "crop recommendation");
        }
        if (v == constraint_disease_detection) {
            counter++;
            
            new bottom_spread_sheet().show(getSupportFragmentManager(), "disease detection" + counter);
            if (counter == 2) {
                counter = 0;
            }
        }
        if (v == constraint_soil_detection) {
            counter++;
            
            new bottom_spread_sheet().show(getSupportFragmentManager(), "soil detection" + counter);
            if (counter == 2) {
                counter = 0;
            }
        }
        if (v == constraint_budget_formulation) {
            
            new bottom_spread_sheet().show(getSupportFragmentManager(), "budget formation");
        }
    }

    private void animate_main_card() {
        swipe_view.setVisibility(View.VISIBLE);
        ObjectAnimator animator = ObjectAnimator
                .ofFloat(card_main, "radius", 0, convertToPx(17));
        animator.setDuration(500);
        animator.start();
        card_main.setCardElevation(11);
        card_main.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        float width = getSystem().getDisplayMetrics().widthPixels;
        float height = getSystem().getDisplayMetrics().heightPixels;
        card_main.animate().alpha(0).rotationY(-15).translationY(0).translationX(-width * .5f).scaleX(.97f).scaleY(.97f).setDuration(500).setInterpolator(new OvershootInterpolator());
//        card_main.animate().rotation(-19).translationY(-width * .2f).translationX(width * .3f).scaleX(.8f).scaleY(.8f).setDuration(500).setInterpolator(new OvershootInterpolator());
        constraint_menu_items.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        constraint_menu_items.animate().alpha(1).translationX(0).rotationY(0).setDuration(500).setInterpolator(new OvershootInterpolator());
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
        }
        if (linear_info.getVisibility() == View.VISIBLE) {
            linear_info.animate().alpha(0).setDuration(500);
        }
        else
            finishAffinity();
    }
}