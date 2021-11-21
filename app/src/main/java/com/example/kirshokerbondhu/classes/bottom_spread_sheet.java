package com.example.kirshokerbondhu.classes;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.kirshokerbondhu.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.Random;

public class bottom_spread_sheet extends BottomSheetDialogFragment implements View.OnClickListener {

    private AutoCompleteTextView edit_crop_name, edit_area;
    private TextView text_area, text_reason_tag;
    private TypeWriter text_verdict, text_reason;
    private RadioButton radio_paddy, radio_others;
    private SharedPrefs sharedPrefs;
    private ConstraintLayout constraint_container;
    private Button button_check;
    private String Rain = "";
    private int counter = 0;
    private double obtained_local_temp = 0.0, obtained_chances_of_rain = 0.0, L_Temp = 0.0, U_Temp = 0.0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.bottom_spread_sheet, container, false);
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        edit_area = v.findViewById(R.id.edit_area);
        edit_crop_name = v.findViewById(R.id.edit_crop_name);
        text_area = v.findViewById(R.id.text_area);
        text_reason = v.findViewById(R.id.text_reason);
        text_verdict = v.findViewById(R.id.text_verdict);
        text_reason_tag = v.findViewById(R.id.text_reason_tag);
        radio_paddy = v.findViewById(R.id.radio_paddy);
        radio_others = v.findViewById(R.id.radio_others);
        constraint_container = v.findViewById(R.id.constraint_container);
        button_check = v.findViewById(R.id.button_check);


        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        constraint_container.setLayoutTransition(layoutTransition);


        radio_paddy.setChecked(true);
        button_check.setOnClickListener(this);

        sharedPrefs = new SharedPrefs();
        addAdapterToLocationACTV();
        addAdapterToCropNameACTV("rice");
        setListenersToRadioButtons(radio_paddy);
        setListenersToRadioButtons(radio_others);


        return v;
    }

    private void setListenersToRadioButtons(RadioButton radioButton) {
        radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (radioButton.isChecked()) {
                if (radioButton == radio_paddy) {
                    addAdapterToCropNameACTV("rice");
                    return;
                }
                addAdapterToCropNameACTV("other products");
            }

        });
    }

    private void addAdapterToCropNameACTV(String crop) {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, sharedPrefs.getArrayList(requireContext(), crop));

        edit_crop_name.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void addAdapterToLocationACTV() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, sharedPrefs.getArrayList(requireContext(), "locations"));

        edit_area.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public static int getRandomNumber(int min, int max) {
        return (new Random()).nextInt((max - min) + 1) + min;
    }

    @Override
    public void onClick(View v) {
        if (v == button_check) {
            text_reason.setText("");
            text_reason_tag.setText("");
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) text_verdict.getLayoutParams();
            params.horizontalBias = 0.5f; // here is one modification for example. modify anything else you want :)
            text_verdict.setLayoutParams(params); // request the view to use the new modified params
            text_verdict.setVisibility(View.VISIBLE);
            text_verdict.setLetterSpacing(0.3f);
            text_verdict.setCharacterDelay(401);
            text_verdict.animationCompleteCallBack = null;
            text_verdict.setAnimationCompleteListener(new Handler(msg -> {
                new Handler().postDelayed(() -> text_verdict.animateText("•••"), 401);

                return false;
            }));
            text_verdict.animateText("•••");

            if (counter > 0) {
                new Handler().postDelayed(() -> {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    getLocationTenperature(databaseReference);
                }, 2000);
                return;
            }
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            getLocationTenperature(databaseReference);

        }
    }

    private void getLocationTenperature(DatabaseReference databaseReference) {
        databaseReference.child("Location Temperatures").child(getTextsFromEditText(edit_area))
                .child(String.valueOf(getRandomNumber(1, 7))).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        obtained_local_temp = Double.parseDouble(Objects.requireNonNull(snapshot.getValue()).toString());
                        getChancesOfRain(databaseReference);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Snackbar.make(edit_area, error.getMessage(), Snackbar.LENGTH_SHORT).show();

                    }
                }
        );
    }

    private void getChancesOfRain(DatabaseReference databaseReference) {

        databaseReference.child("Chances Of Rainfall").child(getTextsFromEditText(edit_area))
                .child(String.valueOf(getRandomNumber(1, 5))).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            obtained_chances_of_rain += Double.parseDouble(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                        }
                        obtained_chances_of_rain /= snapshot.getChildrenCount();
                        getCropDetails(databaseReference);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Snackbar.make(edit_area, error.getMessage(), Snackbar.LENGTH_SHORT).show();

                    }
                }
        );
    }

    private void getCropDetails(DatabaseReference databaseReference) {
        if (radio_paddy.isChecked()) {
            databaseReference.child("Requirements").child("Paddy").child(getTextsFromEditText(edit_crop_name))
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            L_Temp = Double.parseDouble(Objects.requireNonNull(snapshot
                                    .child("L-Temp").getValue()).toString());
                            U_Temp = Double.parseDouble(Objects.requireNonNull(snapshot
                                    .child("U-Temp").getValue()).toString());
                            Rain = Objects.requireNonNull(snapshot
                                    .child("Rain").getValue()).toString();

                            formulateResult();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Snackbar.make(edit_area, error.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }
                    });
        } else {
            databaseReference.child("Requirements").child("Other Products").child(getTextsFromEditText(edit_crop_name))
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            L_Temp = Double.parseDouble(Objects.requireNonNull(snapshot
                                    .child("L-Temp").getValue()).toString());
                            U_Temp = Double.parseDouble(Objects.requireNonNull(snapshot
                                    .child("U-Temp").getValue()).toString());
                            Rain = Objects.requireNonNull(snapshot
                                    .child("Rain").getValue()).toString();

                            formulateResult();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Snackbar.make(edit_area, error.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void formulateResult() {
        String rain_state = "";
        if (Rain.equals("A")) {
            rain_state = "above 50%";
        } else {
            rain_state = "below 50%";
        }
        String verdict = "";
        if (obtained_local_temp >= L_Temp && obtained_local_temp <= U_Temp && ((obtained_chances_of_rain > 50.0 && Rain.equals("A"))
                || (obtained_chances_of_rain <= 50.0 && Rain.equals("B")))) {

            verdict = "sounds great!";
            text_reason.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
        } else {
            verdict = "bad idea!";
            text_reason.setTextColor(ContextCompat.getColor(requireContext(), R.color.red));
        }
        String reason = "your location: " + getTextsFromEditText(edit_area).toLowerCase() +
                "\n" + getTextsFromEditText(edit_crop_name).toLowerCase() + "'s max. temperature: "
                + U_Temp + "°C\n" + getTextsFromEditText(edit_crop_name).toLowerCase() + "'s lowest temperature: "
                + L_Temp + "°C\n" + "required rainfall chances: " + rain_state + "\n" + "local avg. temperature: " + obtained_local_temp
                + "°C\n" + "local chances of rain: " + obtained_chances_of_rain + "%\n";
        giveResults(verdict, reason);
    }

    private void giveResults(String verdict, String reason) {
        counter = 1;
        text_verdict.setText("");
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) text_verdict.getLayoutParams();
        params.horizontalBias = 0.0f; // here is one modification for example. modify anything else you want :)
        text_verdict.setLayoutParams(params); // request the view to use the new modified params
        text_verdict.animationCompleteCallBack = null;
        text_verdict.setLetterSpacing(0f);
        text_verdict.setCharacterDelay(51);
        text_verdict.removeCallbacks(() -> text_verdict.animateText("•••"));
        new Handler().postDelayed(() -> {
            text_reason_tag.setText(getString(R.string.reason));
            text_reason.setCharacterDelay(25);
            text_reason.animateText(reason);
            text_reason.setText(reason);
        }, 1000);
        text_verdict.setAnimationCompleteListener(new Handler(msg -> {
            text_verdict.setText(verdict);
            return false;
        }));
        text_verdict.animateText(verdict);

    }

    private String getTextsFromEditText(AutoCompleteTextView edittext) {
        return edittext.getText().toString().trim();
    }
}