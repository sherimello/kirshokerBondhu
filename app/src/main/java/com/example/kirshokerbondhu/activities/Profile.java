package com.example.kirshokerbondhu.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.kirshokerbondhu.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    private CardView card_dp, card_save;
    private TextView text_username, text_email, text_save;
    private ImageView image_back, image_dp;
    private EditText edit_phone;
    private AutoCompleteTextView text_location;
    private Button button_logout, button_add_phone, button_add_address;
    private FirebaseAuth mAuth;
    private Bitmap mIcon_val;
    private URL newURL = null;
    private String current_phone_num = "", current_location = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        card_dp = findViewById(R.id.card_dp);
        card_save = findViewById(R.id.card_save);
        text_username = findViewById(R.id.text_username);
        text_email = findViewById(R.id.text_email);
        text_save = findViewById(R.id.text_save);
        image_back = findViewById(R.id.image_back);
        image_dp = findViewById(R.id.image_dp);
        edit_phone = findViewById(R.id.edit_phone);
        text_location = findViewById(R.id.text_location);
        button_logout = findViewById(R.id.button_logout);
        button_add_phone = findViewById(R.id.button_add_phone);
        button_add_address = findViewById(R.id.button_add_address);

        assignDataToViews();
        current_phone_num = getTextFromEditTexts(edit_phone);
        current_location = getTextFromEditTexts(text_location);

        setListenerToEditTexts(edit_phone);
        setListenerToEditTexts(text_location);

        button_logout.setOnClickListener(this);
        button_add_phone.setOnClickListener(this);
        button_add_address.setOnClickListener(this);
        image_back.setOnClickListener(this);

    }

    private String getTextFromEditTexts(EditText view) {
        return view.getText().toString();
    }

    @Override
    public void onClick(View view) {
        if (view == image_back) {
            onBackPressed();
        }
        if (view == button_logout) {
            signOut();
        }
        if (view == button_add_phone) {
            assert button_add_phone != null;
            button_add_phone.setVisibility(View.GONE);
            edit_phone.setVisibility(View.VISIBLE);
        }
        if (view == button_add_address) {
            assert button_add_address != null;
            button_add_address.setVisibility(View.GONE);
            text_location.setVisibility(View.VISIBLE);
        }
    }

    private void setListenerToEditTexts(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (editText == edit_phone) {
                    if (!editText.getText().toString().equals(current_phone_num)) {
                        //code here...
                        if (card_save.getVisibility() == View.GONE) {
                            animateSaveCard();
                            image_back.setVisibility(View.GONE);
                            new Handler().postDelayed(() -> animateSaaveButton(), 500);
                        }
                        return;
                    }
                    if (text_location.getText().toString().equals(current_location)) {
                        image_back.setVisibility(View.VISIBLE);
                        card_save.setVisibility(View.GONE);
                        text_save.setVisibility(View.GONE);
                    }
                    return;
                }

                if (!editText.getText().toString().equals(current_location)) {
                    //code here...
                    if (card_save.getVisibility() == View.GONE) {
                        animateSaveCard();
                        image_back.setVisibility(View.GONE);
                        new Handler().postDelayed(() -> animateSaaveButton(), 500);
                    }
                    return;
                }
                if (edit_phone.getText().toString().equals(current_phone_num)) {
                    image_back.setVisibility(View.VISIBLE);
                    card_save.setVisibility(View.GONE);
                    text_save.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void animateSaveCard() {
        card_save.setAlpha(0);
        card_save.setScaleX(0);
        card_save.setScaleY(0);
        card_save.setVisibility(View.VISIBLE);
        card_save.animate().alpha(1).scaleX(1).scaleY(1).setDuration(500)
                .setInterpolator(new OvershootInterpolator());
    }

    private void animateSaaveButton() {
        text_save.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> text_save.setVisibility(View.GONE), 1500);
    }

    private void assignDataToViews() {
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        text_username.setText(user.getDisplayName());
        text_email.setText(user.getEmail());
        setUserPicture();
    }

    private void setUserPicture() {
        new Thread(() -> {
            try {
                newURL = new URL(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getPhotoUrl()).toString());
                mIcon_val = BitmapFactory.decodeStream(newURL.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            runOnUiThread(() -> {
                image_dp.setScaleType(ImageView.ScaleType.CENTER_CROP);
                image_dp.setImageBitmap(mIcon_val);
            });

        }).start();
    }

    @Override
    public void onBackPressed() {
        final ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                Pair.create(card_dp, getResources().getString(R.string.card)));

        startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("type", "usual"), options.toBundle());
        new Handler().postDelayed(this::finish, 1500);
    }

    private void signOut() {
        GoogleSignInOptions gso = new
                GoogleSignInOptions.
                        Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth.signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                task -> {
                    Intent intent = new Intent(getApplicationContext(), SignIn.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                });
    }
}