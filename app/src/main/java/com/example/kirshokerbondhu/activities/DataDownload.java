package com.example.kirshokerbondhu.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kirshokerbondhu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

public class DataDownload extends AppCompatActivity {

    private ArrayList<String> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_download);
        locations = new ArrayList<>();

        getLocationDataSet();

    }

    private void getLocationDataSet() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren())
//                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    locations.add(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                    if (locations.size() == snapshot.getChildrenCount()) {
                        saveArrayList(locations, "locations");
                    }
                }
//                Toast.makeText(getApplicationContext(), String.valueOf(snapshot.getChildrenCount()), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void saveArrayList(ArrayList<String> list, String key) {
        Toast.makeText(getApplicationContext(), "in", Toast.LENGTH_SHORT).show();
        SharedPreferences prefs = getSharedPreferences("Location Data Pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

}