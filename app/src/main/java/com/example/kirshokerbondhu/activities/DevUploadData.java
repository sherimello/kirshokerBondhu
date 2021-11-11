package com.example.kirshokerbondhu.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kirshokerbondhu.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DevUploadData extends AppCompatActivity {

    private Button button_upload;
    private int counter = 1;
    private String[] list = {"Dhaka",
            "Faridpur",
            "Gazipur",
            "Gopalganj",
            "Jamalpur",
            "Kishoreganj",
            "Madaripur",
            "Manikganj",
            "Munshiganj",
            "Mymensingh",
            "Narayanganj",
            "Narsingdi",
            "Netrokona",
            "Rajbari",
            "Shariatpur",
            "Sherpur",
            "Tangail",
            "Bogura",
            "Joypurhat",
            "Naogaon",
            "Natore",
            "Nawabganj",
            "Pabna",
            "Rajshahi",
            "Sirajgonj",
            "Dinajpur",
            "Gaibandha",
            "Kurigram",
            "Lalmonirhat",
            "Nilphamari",
            "Panchagarh",
            "Rangpur",
            "Thakurgaon",
            "Barguna",
            "Barishal",
            "Bhola",
            "Jhalokati",
            "Patuakhali",
            "Pirojpur",
            "Bandarban",
            "Brahmanbaria",
            "Chandpur",
            "Chattogram",
            "Cumilla",
            "Cox's Bazar",
            "Feni",
            "Khagrachari",
            "Lakshmipur",
            "Noakhali",
            "Rangamati",
            "Habiganj",
            "Maulvibazar",
            "Sunamganj",
            "Sylhet",
            "Bagerhat",
            "Chuadanga",
            "Jashore",
            "Jhenaidah",
            "Khulna",
            "Kushtia",
            "Magura",
            "Meherpur",
            "Narail",
            "Satkhira"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_upload_data);

        button_upload = findViewById(R.id.button_upload);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        button_upload.setOnClickListener(v -> {

            for (String s : list) {
                databaseReference.child("Location").child(String.valueOf(counter))
                        .setValue(s);
                counter++;
            }
            counter = 1;
            Toast.makeText(getApplicationContext(), "uploaded!", Toast.LENGTH_SHORT).show();
        });
    }
}