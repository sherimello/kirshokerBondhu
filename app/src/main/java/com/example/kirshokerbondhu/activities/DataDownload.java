package com.example.kirshokerbondhu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kirshokerbondhu.R;
import com.example.kirshokerbondhu.classes.SharedPrefs;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class DataDownload extends AppCompatActivity {

    private ArrayList<String> locations, rice, other_products;
    private ProgressBar progressBar;
    private SharedPrefs sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_download);

        progressBar = findViewById(R.id.progressBar);

        locations = new ArrayList<>();
        rice = new ArrayList<>();
        other_products = new ArrayList<>();
        sharedPrefs = new SharedPrefs();


        getLocationDataSet();

    }

    private void getLocationDataSet() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    locations.add(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                    if (locations.size() == snapshot.getChildrenCount()) {
                        getRiceDataSet();
                        sharedPrefs.saveArrayList(getApplicationContext(), locations, "locations");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar.make(progressBar, error.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void getRiceDataSet() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Rice").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    rice.add(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                    if (rice.size() == snapshot.getChildrenCount()) {
                        getOtherProductsDataSet();
                        sharedPrefs.saveArrayList(getApplicationContext(), rice, "rice");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar.make(progressBar, error.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void getOtherProductsDataSet() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Other Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    other_products.add(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                    if (other_products.size() == snapshot.getChildrenCount()) {
                        sharedPrefs.sharedDataAlreadyDownloadedStatus(getApplicationContext(), 1);
                        sharedPrefs.saveArrayList(getApplicationContext(), other_products, "other products");
                        changeActivity();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar.make(progressBar, error.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void changeActivity() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

}