package com.example.kirshokerbondhu.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.example.kirshokerbondhu.R;
import com.example.kirshokerbondhu.classes.UserInfoClass;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class SignIn extends AppCompatActivity {

    private ImageView image_bg;
    private Button button_signin;
    private GoogleSignInClient mGoogleSignInClient;
    private String token, default_web_client_ID;
    private FirebaseAuth mAuth;
    private String user_name;
    private static String PREF_NAME = "is_data_downloaded";
    private FirebaseUser user;
    private ArrayList<UserInfoClass> userInfoClasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        userInfoClasses = new ArrayList<>();

        image_bg = findViewById(R.id.image_bg);
        button_signin = findViewById(R.id.button_signin);

        mAuth = FirebaseAuth.getInstance();

        Glide.with(this)
                .load(ResourcesCompat.getDrawable(getResources(), R.drawable.splashbg, null))
                .disallowHardwareConfig()
                .into(image_bg);

        button_signin.setOnClickListener(view -> getToken());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == Integer.parseInt(token)) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                showSnackBarMessage("activity result error: " + e.getLocalizedMessage());
                // Google Sign In failed, update UI appropriately
            }
        }
    }

    private void getToken() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Sign In Request Token").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                token = Objects.requireNonNull(snapshot.getValue()).toString();
                init_GSO();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                showSnackBarMessage("Token retrieving error: " + error.getMessage());

            }
        });
    }

    private void init_GSO() {

        // Configure Google Sign In
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Default Web Client ID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                default_web_client_ID = Objects.requireNonNull(snapshot.getValue()).toString();
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(default_web_client_ID)
                        .requestEmail()
                        .build();

                mGoogleSignInClient = GoogleSignIn.getClient(SignIn.this, gso);

                init_Gmail_Prompt();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                showSnackBarMessage(error.getMessage());

            }
        });

    }

    private void init_Gmail_Prompt() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, Integer.parseInt(token));
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        checkIfDataIsDownloaded();
                        user = mAuth.getCurrentUser();
                        if (Objects.requireNonNull(task.getResult().getAdditionalUserInfo()).isNewUser()) {
                            if (user != null) {
                                createUserNode();
                            }

                        } else {
                            checkIfDataIsDownloaded();
                        }

                    } else {
                        showSnackBarMessage(String.valueOf(task.getResult().describeContents()));
                    }
                });
    }

    private void checkIfDataIsDownloaded() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        if (prefs.contains("status") && prefs.getInt("status", 0) == 1) {
            changeActivity(new MainActivity());
            return;
        }
        int status = prefs.getInt("status", 0);
        if (status == 0) {
            changeActivity(new DataDownload());
        }
    }

    private void createUserNode() {
        new Thread(() -> {
            userInfoClasses.add(new UserInfoClass("null", "null"));
            user_name = user.getDisplayName();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Users").child(user_name).setValue(userInfoClasses.get(0))
                    .addOnCompleteListener(task -> {
                        checkIfDataIsDownloaded();
                    }).addOnCanceledListener(() -> showSnackBarMessage("User creation cancelled. Try again shortly..."))
                    .addOnFailureListener(e -> showSnackBarMessage(e.getMessage()));

        }).start();
    }

    private void showSnackBarMessage(String msg) {
        Snackbar.make(image_bg, msg, Snackbar.LENGTH_SHORT).show();
    }

    private void changeActivity(Object c) {
        startActivity(new Intent(getApplicationContext(), c.getClass()).putExtra("type", "usual"));
        finish();
    }
}