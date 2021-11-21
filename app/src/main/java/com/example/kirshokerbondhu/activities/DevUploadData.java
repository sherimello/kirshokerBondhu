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
    private int counter = 1, chance_counter = 1;
    private double[] temps = {15.67, 34.44, 21.00, 35.10, 14.77, 27.1, 30.04}, chances_of_rainfall = {21.3, 36.7, 50.0, 63.2, 90.5, 23.1, 33.95, 50.0, 60.3, 91.0, 10.0, 27.7, 50.0, 57.0, 99.0, 30.0, 40.0, 50.0, 65.0, 65.0, 9.0, 13.0, 39.95, 64.13, 70.0};
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
            uploadMonthlyChancesOfRainfallPerLocation(databaseReference);
        });
    }

    private void uploadMonthlyChancesOfRainfallPerLocation(DatabaseReference databaseReference) {
        for (String s : list) {
            for (double d2 : chances_of_rainfall) {
                databaseReference.child("Chances Of Rainfall").child(s)
                        .child(String.valueOf(chance_counter))
                        .child(String.valueOf(counter)).setValue(d2);
                if (counter == 5) {
                    counter = 1;
                    chance_counter++;
                } else
                    counter++;
            }
            counter = 1;
            chance_counter = 1;
        }
    }

    private void uploadTemperatures(DatabaseReference databaseReference) {
        for (String s : list) {
            for (double d : temps) {
                databaseReference.child("Temperatures").child(s).child(String.valueOf(counter)).setValue(d);
                counter++;
            }
            counter = 1;
        }
    }

    private void uploadLocation(DatabaseReference databaseReference) {
        for (String s : list) {
            databaseReference.child("Location").child(String.valueOf(counter))
                    .setValue(s);
            counter++;
        }
        counter = 1;
        Toast.makeText(getApplicationContext(), "uploaded!", Toast.LENGTH_SHORT).show();
    }
}