package com.example.kirshokerbondhu.classes;

import static android.app.Activity.RESULT_OK;

import android.animation.LayoutTransition;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.example.kirshokerbondhu.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class bottom_spread_sheet extends BottomSheetDialogFragment implements View.OnClickListener {

    private AutoCompleteTextView edit_crop_name, edit_area, edit_land_area;
    private TextView text_reason_tag, text_desc;
    private ImageView image_selected_picture, image_selected_picture_round;
    private CardView card_open_camera, card_open_gallery, card_selected_picture, card_check, card_check2;
    private View view_gallery_left, view_gallery_right, view_camera_left, view_camera_right;
    private TypeWriter text_verdict, text_reason, text_disease_name, text_suggestions;
    private RadioButton radio_paddy, radio_others;
    private SharedPrefs sharedPrefs;
    private ConstraintLayout constraint_container;
    private RelativeLayout relative_selected_picture;
    private Button button_check, button_check2;
    private String Rain = "", tag = "";
    private int counter = 0, simulation_count = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1, PICK_IMAGE = 2;
    private final static String CROP_SUGGESTION = "crop recommendation", BUDGET_FORMATION = "budget formation",
            DISEASE_IDENTIFICATION = "disease detection", SOIL_IDENTIFICATION = "soil detection";
    private double obtained_local_temp = 0.0, obtained_chances_of_rain = 0.0, obtained_chances_of_flood = 0.0, L_Temp = 0.0, U_Temp = 0.0;
    private int nMaxScreenHeight;


    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        super.show(manager, tag);
        this.tag = tag;
        assert tag != null;
        if (tag.contains("1")) {
            simulation_count = 1;
        }
        if (tag.contains("2")) {
            simulation_count = 2;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (tag.equals(CROP_SUGGESTION))
            return assignCropSuggestionUI(inflater, container);
        if (tag.equals(DISEASE_IDENTIFICATION + "1") || tag.equals(DISEASE_IDENTIFICATION + "2") ||
                tag.equals(SOIL_IDENTIFICATION + "1") || tag.equals(SOIL_IDENTIFICATION + "2")) {
            return assignPhotoUI(inflater, container);
        }

        return assignBudgetUI(inflater, container);
    }

    private View assignBudgetUI(LayoutInflater inflater, ViewGroup container) {

        View v = inflater.inflate(R.layout.budget_formulation_layout, container, false);
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);

        edit_area = v.findViewById(R.id.edit_area);
        edit_land_area = v.findViewById(R.id.edit_land_area);
        edit_crop_name = v.findViewById(R.id.edit_crop_name);
        text_reason = v.findViewById(R.id.text_reason);
        text_verdict = v.findViewById(R.id.text_verdict);
        text_desc = v.findViewById(R.id.text_desc);
        text_reason_tag = v.findViewById(R.id.text_reason_tag);
        radio_paddy = v.findViewById(R.id.radio_paddy);
        radio_others = v.findViewById(R.id.radio_others);
        constraint_container = v.findViewById(R.id.constraint_container);
        card_check2 = v.findViewById(R.id.card_check);
        card_check = v.findViewById(R.id.card_check);

        text_desc.setText(getResources().getText(R.string.budget_formulation_desc));


        setRootLayoutAnimation();
        radio_paddy.setChecked(true);
        card_check2.setOnClickListener(this);

        sharedPrefs = new SharedPrefs();
        addAdapterToLocationACTV();
        addAdapterToCropNameACTV("rice");
        setListenersToRadioButtons(radio_paddy);
        setListenersToRadioButtons(radio_others);

        return v;
    }

    private View assignPhotoUI(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.bottom_spread_sheet_for_photo_taking_ui, container, false);
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);

        updateUsableScreenSize();

        card_open_camera = v.findViewById(R.id.card_open_camera);
        card_open_gallery = v.findViewById(R.id.card_open_gallery);
        view_camera_left = v.findViewById(R.id.view_camera_left);
        view_camera_right = v.findViewById(R.id.view_camera_right);
        view_gallery_left = v.findViewById(R.id.view_gallery_left);
        view_gallery_right = v.findViewById(R.id.view_gallery_right);
        image_selected_picture = v.findViewById(R.id.image_selected_picture);
        image_selected_picture_round = v.findViewById(R.id.image_selected_picture_round);
        relative_selected_picture = v.findViewById(R.id.relative_selected_picture);
        text_suggestions = v.findViewById(R.id.text_suggestions);
        text_desc = v.findViewById(R.id.text_desc);
        text_disease_name = v.findViewById(R.id.text_disease_name);
        card_selected_picture = v.findViewById(R.id.card_selected_picture);

        view_camera_left.setOnClickListener(this);
        view_camera_right.setOnClickListener(this);
        view_gallery_left.setOnClickListener(this);
        view_gallery_right.setOnClickListener(this);

        if (tag.equals("soil detection1") || tag.equals("soil detection2")) {
            text_desc.setText(getResources().getText(R.string.soil_identification_desc));
        }
        else {
            text_desc.setText(getResources().getText(R.string.disease_diagnosis_desc));
        }

        return v;
    }

    private View assignCropSuggestionUI(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.bottom_spread_sheet_for_crop_suggestion_and_budget, container, false);
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        edit_area = v.findViewById(R.id.edit_area);
        edit_crop_name = v.findViewById(R.id.edit_crop_name);
        text_reason = v.findViewById(R.id.text_reason);
        text_verdict = v.findViewById(R.id.text_verdict);
        text_desc = v.findViewById(R.id.text_desc);
        text_reason_tag = v.findViewById(R.id.text_reason_tag);
        radio_paddy = v.findViewById(R.id.radio_paddy);
        radio_others = v.findViewById(R.id.radio_others);
        constraint_container = v.findViewById(R.id.constraint_container);
        card_check = v.findViewById(R.id.card_check);

        text_desc.setText(getResources().getText(R.string.crop_recommendation_desc));

        setRootLayoutAnimation();


        radio_paddy.setChecked(true);
        card_check.setOnClickListener(this);

        sharedPrefs = new SharedPrefs();
        addAdapterToLocationACTV();
        addAdapterToCropNameACTV("rice");
        setListenersToRadioButtons(radio_paddy);
        setListenersToRadioButtons(radio_others);
        return v;
    }

    private void setRootLayoutAnimation() {
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        constraint_container.setLayoutTransition(layoutTransition);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            card_open_camera.setVisibility(View.GONE);
            card_open_gallery.setVisibility(View.GONE);
            card_selected_picture.setVisibility(View.VISIBLE);
            text_disease_name.setVisibility(View.VISIBLE);
            text_disease_name.setLetterSpacing(0.3f);
            text_disease_name.setCharacterDelay(401);
            text_disease_name.animationCompleteCallBack = null;
            text_disease_name.setAnimationCompleteListener(new Handler(msg -> {
                new Handler().postDelayed(() -> text_disease_name.animateText("•••"), 401);

                return false;
            }));
            text_disease_name.animateText("•••");
            if (requestCode == PICK_IMAGE) {

                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = requireActivity().getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    image_selected_picture.setImageBitmap(selectedImage);
                    image_selected_picture_round.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
                image_selected_picture.requestLayout();
                image_selected_picture.getLayoutParams().height
                        = nMaxScreenHeight;
                new Handler().postDelayed(() -> {
                    if (tag.equals("soil detection1") || tag.equals("soil detection2")) {

                        text_disease_name.setLetterSpacing(0f);
                        text_disease_name.setCharacterDelay(41);
                        text_suggestions.setVisibility(View.VISIBLE);
                        text_suggestions.setCharacterDelay(21);

                        if (Objects.equals(direction, "left")) {
                            text_disease_name.setAnimationCompleteListener(new Handler(msg -> {
                                text_disease_name.setText(getText(R.string.doash_mati));
                                text_suggestions.setAnimationCompleteListener(new Handler(
                                        msg1 -> {
                                            text_suggestions.setText(R.string.doash_mati_info);
                                            return false;
                                        }));
                                text_suggestions.animateText(getText(R.string.doash_mati_info));
                                return false;
                            }));
                            text_disease_name.animateText(getText(R.string.doash_mati));
                        } else {
                            text_disease_name.setAnimationCompleteListener(new Handler(msg -> {
                                text_disease_name.setText(getText(R.string.sandy_soil));
                                text_suggestions.setAnimationCompleteListener(new Handler(
                                        msg1 -> {
                                            text_suggestions.setText(R.string.sandy_soil_info);
                                            return false;
                                        }));
                                text_suggestions.animateText(getText(R.string.sandy_soil_info));
                                return false;
                            }));
                            text_disease_name.animateText(getText(R.string.sandy_soil));
                        }
                    }

                    if (tag.equals("disease detection1") || tag.equals("disease detection2")) {
                        text_reason_tag.setVisibility(View.GONE);
                        text_suggestions.setVisibility(View.VISIBLE);
                        text_disease_name.setLetterSpacing(0f);
                        text_disease_name.setCharacterDelay(41);
                        text_suggestions.setCharacterDelay(21);
                        if (Objects.equals(direction, "left")) {
                            text_disease_name.setAnimationCompleteListener(new Handler(msg -> {
                                text_disease_name.setText(R.string.hispa);
                                return false;
                            }));
                            text_suggestions.setAnimationCompleteListener(new Handler(msg -> {
                                text_suggestions.setText(R.string.hispa_suggestion);
                                return false;
                            }));
                            text_disease_name.animateText(getText(R.string.hispa));
                            text_suggestions.animateText(getString(R.string.hispa_suggestion));
                        } else {
                            text_disease_name.setAnimationCompleteListener(new Handler(msg -> {
                                text_disease_name.setText(R.string.brownspot);
                                return false;
                            }));
                            text_suggestions.setAnimationCompleteListener(new Handler(msg -> {
                                text_suggestions.setText(R.string.brownspot_suggestion);
                                return false;
                            }));
                            text_disease_name.animateText(getText(R.string.brownspot));
                            text_suggestions.animateText(getString(R.string.brownspot_suggestion));
                        }
                    }
                }, 3000);
            }
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                image_selected_picture.setImageBitmap(imageBitmap);
                image_selected_picture_round.setImageBitmap(imageBitmap);
                image_selected_picture.requestLayout();
                image_selected_picture.getLayoutParams().height
                        = nMaxScreenHeight;
                new Handler().postDelayed(() -> {
                    if (tag.equals("soil detection1") || tag.equals("soil detection2")) {

                        text_disease_name.setLetterSpacing(0f);
                        text_disease_name.setCharacterDelay(41);
                        text_suggestions.setVisibility(View.VISIBLE);
                        text_suggestions.setCharacterDelay(21);

                        if (Objects.equals(direction, "left")) {
                            text_disease_name.setAnimationCompleteListener(new Handler(msg -> {
                                text_disease_name.setText(getText(R.string.doash_mati));
                                text_suggestions.setAnimationCompleteListener(new Handler(
                                        msg1 -> {
                                            text_suggestions.setText(R.string.doash_mati_info);
                                            return false;
                                        }));
                                text_suggestions.animateText(getText(R.string.doash_mati_info));
                                return false;
                            }));
                            text_disease_name.animateText(getText(R.string.doash_mati));
                        } else {
                            text_disease_name.setAnimationCompleteListener(new Handler(msg -> {
                                text_disease_name.setText(getText(R.string.sandy_soil));
                                text_suggestions.setAnimationCompleteListener(new Handler(
                                        msg1 -> {
                                            text_suggestions.setText(R.string.sandy_soil_info);
                                            return false;
                                        }));
                                text_suggestions.animateText(getText(R.string.sandy_soil_info));
                                return false;
                            }));
                            text_disease_name.animateText(getText(R.string.sandy_soil));
                        }
                    }

                    if (tag.equals("disease detection1") || tag.equals("disease detection2")) {
                        text_suggestions.setVisibility(View.VISIBLE);
                        text_disease_name.setLetterSpacing(0f);
                        text_disease_name.setCharacterDelay(41);
                        text_suggestions.setCharacterDelay(21);
                        if (Objects.equals(direction, "left")) {
                            text_disease_name.setAnimationCompleteListener(new Handler(msg -> {
                                text_disease_name.setText(R.string.hispa);
                                return false;
                            }));
                            text_suggestions.setAnimationCompleteListener(new Handler(msg -> {
                                text_suggestions.setText(R.string.hispa_suggestion);
                                return false;
                            }));
                            text_disease_name.animateText(getText(R.string.hispa));
                            text_suggestions.animateText(getString(R.string.hispa_suggestion));
                        } else {
                            text_disease_name.setAnimationCompleteListener(new Handler(msg -> {
                                text_disease_name.setText(R.string.brownspot);
                                return false;
                            }));
                            text_suggestions.setAnimationCompleteListener(new Handler(msg -> {
                                text_suggestions.setText(R.string.brownspot_suggestion);
                                return false;
                            }));
                            text_disease_name.animateText(getText(R.string.brownspot));
                            text_suggestions.animateText(getString(R.string.brownspot_suggestion));
                        }
                    }
                }, 3000);
            }
        }

    }

    public final void updateUsableScreenSize() {
        nMaxScreenHeight = this.getResources().getDisplayMetrics().heightPixels;
    }

    private void setListenersToRadioButtons(RadioButton radioButton) {
        radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (radioButton.isChecked()) {
                edit_crop_name.setText("");
                if (radioButton == radio_paddy) {
                    addAdapterToCropNameACTV("rice");
                    changeEditTextHint(edit_crop_name, getString(R.string.hint_crop_name_aus));
                    return;
                }
                changeEditTextHint(edit_crop_name, getString(R.string.hint_crop_name_strawberry));
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

    String direction;

    @Override
    public void onClick(View v) {
        if (v == view_camera_left) {
            dispatchTakePictureIntent();
            direction = "left";
        }

        if (v == view_camera_right) {
            dispatchTakePictureIntent();
            direction = "right";
        }

        if (v == view_gallery_right) {
            dispatchTakeGalleryIntent();
            direction = "right";
        }

        if (v == view_gallery_left) {
            dispatchTakeGalleryIntent();
            direction = "left";
        }

        if (v == card_check) {
            checkIfInputsAreOK();
        }

        if (v == card_check2) {
            checkIfInputsAreOK();
        }

        if (v == card_open_camera) {
            dispatchTakePictureIntent();
        }
        if (v == card_open_gallery) {
            dispatchTakeGalleryIntent();
        }
    }

    private void dispatchTakeGalleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            snackbarMessage(card_open_camera, e.getMessage());
            // display error state to the user
        }
    }

    private void initiateCropSuggestionCheck() {
        text_reason.setText("");
        text_reason_tag.setText("");
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) text_verdict.getLayoutParams();
        params.horizontalBias = 0.5f; // here is one modification for example. modify anything else you want :)
        text_verdict.setLayoutParams(params); // request the view to use the new modified params
        text_verdict.setVisibility(View.VISIBLE);
        text_verdict.setTextColor(Color.parseColor("#009688"));
        text_verdict.setLetterSpacing(0.3f);
        text_verdict.setCharacterDelay(401);
        text_verdict.animationCompleteCallBack = null;
        text_verdict.setAnimationCompleteListener(new Handler(msg -> {
            new Handler().postDelayed(() -> text_verdict.animateText("•••"), 401);

            return false;
        }));
        text_verdict.animateText("•••");

        getCleanLocation();

        if (tag.equals(BUDGET_FORMATION)) {

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Cost Per Acre");
            String finalLoc = getCleanLocation();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String val = Objects.requireNonNull(snapshot.child(finalLoc).getValue()).toString();
                    String acres = edit_land_area.getText().toString().trim();
                    DecimalFormat f = new DecimalFormat("##.00");
                    double acres_num = Double.parseDouble(acres);
                    int val_num_unit = Integer.parseInt(val);
                    double val_num = val_num_unit * acres_num;
                    new Handler().postDelayed(() -> {
                        text_verdict.setText("");
                        text_verdict.animationCompleteCallBack = null;
                        text_verdict.setLetterSpacing(0f);
                        text_verdict.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        text_reason.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        text_verdict.setCharacterDelay(51);
                        text_reason.setCharacterDelay(21);
                        text_verdict.removeCallbacks(() -> text_verdict.animateText("•••"));
                        text_verdict.setAnimationCompleteListener(new Handler(
                                msg -> {
                                    text_verdict.setText(MessageFormat.format("মোট: {0}৳", f.format(val_num)));
                                    text_reason.setAnimationCompleteListener(new Handler(
                                            msg1 -> {
                                                text_reason.setText(MessageFormat.format("পারিশ্রমিক বাবদ: {0}৳\nচারা তোলা বাবদ: {1}৳\nরোপন: {2}৳\nসেচ বাবদ: {3}৳\nসার বাবদ: {4}৳\nনিরানি বাবদ: {5}৳\nকীটনাশক: {6}৳\nফসল কাঁটা বাবদ: {7}৳\nপরিবহন: {8}৳\nঅন্যান্য: {9}৳\n\n\n", f.format(val_num * 0.0474), f.format(val_num * 0.0107), f.format(val_num * 0.0685), f.format(val_num * 0.0213), f.format(val_num * 0.0870), f.format(val_num * 0.1227), f.format(val_num * 0.0202), f.format(val_num * 0.1183), f.format(val_num * 0.0544), f.format(val_num * 0.0214)));
                                                return false;
                                            }));
                                    text_reason.animateText(MessageFormat.format("পারিশ্রমিক বাবদ: {0}৳\nচারা তোলা বাবদ: {1}৳\nরোপন: {2}৳\nসেচ বাবদ: {3}৳\nসার বাবদ: {4}৳\nনিরানি বাবদ: {5}৳\nকীটনাশক: {6}৳\nফসল কাঁটা বাবদ: {7}৳\nপরিবহন: {8}৳\nঅন্যান্য: {9}৳\n\n\n", f.format(val_num * 0.0474), f.format(val_num * 0.0107), f.format(val_num * 0.0685), f.format(val_num * 0.0213), f.format(val_num * 0.0870), f.format(val_num * 0.1227), f.format(val_num * 0.0202), f.format(val_num * 0.1183), f.format(val_num * 0.0544), f.format(val_num * 0.0214)));
                                    return false;
                                }
                        ));
                        text_verdict.animateText("মোট: " + f.format(val_num) + "৳");
                    }, 0);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            return;
        }

        giveCropSuggestion(getCleanLocation());
    }

    private String getCleanLocation() {
        String loc = edit_area.getText().toString().trim();

        if (loc.contains(" "))
            loc = edit_area.getText().toString().trim().substring(0, edit_area.getText().toString().trim().indexOf(" "));

        if (loc.equals("বরিশাল")) {
            loc = "Barisal";
        }
        if (loc.equals("চট্টগ্রাম")) {
            loc = "Chattogram";
        }
        if (loc.equals("ঢাকা")) {
            loc = "Dhaka";
        }
        if (loc.equals("খুলনা")) {
            loc = "Khulna";
        }
        if (loc.equals("রাজশাহী")) {
            loc = "Rajshahi";
        }
        if (loc.equals("সিলেট")) {
            loc = "Sylhet";
        }
        return loc;
    }

    String storm_verdict, max_temp, av_rainfall, min_temp, humidity, storm, suggestion_verdict;
    private void giveCropSuggestion(String loc) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Weather Info");
        databaseReference.child(loc).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                max_temp = String.valueOf(snapshot.child("max temp").getValue());
                av_rainfall= String.valueOf(snapshot.child("average rainfall").getValue());
                min_temp = String.valueOf(snapshot.child("min temp").getValue());
                humidity = String.valueOf(snapshot.child("humidity").getValue());
                storm = Objects.requireNonNull(snapshot.child("storm").getValue()).toString();

//        Toast.makeText(requireContext(), max_temp, Toast.LENGTH_SHORT).show();

                if (storm.equals("yes")) {
                    storm_verdict = "ঝড়ের সম্ভাবনা আছে";
                }
                if (edit_crop_name.getText().toString().trim().equalsIgnoreCase("aus")) {
                    suggestion_verdict = "ভাল পরিকল্পনা";
                }
                else {
                    suggestion_verdict = "মন্দ পরিকল্পনা\nকারণ: শীতকালিন ফসল";
                }

                String finalStorm_verdict = storm_verdict;
                String finalSuggestion_verdict = suggestion_verdict;
                new Handler().postDelayed(() -> {
                    text_verdict.setText("");
                    text_verdict.animationCompleteCallBack = null;
                    text_verdict.setLetterSpacing(0f);
                    text_verdict.setCharacterDelay(51);
                    text_reason.setCharacterDelay(21);
                    text_verdict.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    text_reason.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    text_verdict.removeCallbacks(() -> text_verdict.animateText("•••"));
                    text_verdict.setAnimationCompleteListener(new Handler(
                            msg -> {
                                text_verdict.setText(finalSuggestion_verdict);
                                text_reason.setAnimationCompleteListener(new Handler(
                                        msg1 -> {
                                            text_reason.setText(MessageFormat.format("গড় বৃষ্টিপাত (মিলিমিটার): {0} মিলিমিটার\nসর্বোচ্চ তাপমাত্রা (ডিগ্রী সেলসিয়াস): {1}° সেলসিয়াস\nসর্বনিম্ন তাপমাত্রা (ডিগ্রী সেলসিয়াস): {2}° সেলসিয়াস\nআদ্রতা (%): {3}%\n{4}\n\n\n",
                                                    av_rainfall,
                                                    max_temp,
                                                    min_temp,
                                                    humidity,
                                                    finalStorm_verdict));
                                            return false;
                                        }));
                                text_reason.animateText(MessageFormat.format("গড় বৃষ্টিপাত (মিলিমিটার): {0} মিলিমিটার\nসর্বোচ্চ তাপমাত্রা (ডিগ্রী সেলসিয়াস): {1}° সেলসিয়াস\nসর্বনিম্ন তাপমাত্রা (ডিগ্রী সেলসিয়াস): {2}° সেলসিয়াস\nআদ্রতা (%): {3}%\n{4}\n\n\n",
                                        av_rainfall,
                                        max_temp,
                                        min_temp,
                                        humidity,
                                        finalStorm_verdict));
                                return false;
                            }
                    ));
                    text_verdict.animateText(finalSuggestion_verdict);
                }, 3001);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void changeEditTextHint(EditText editText, String hint) {
        editText.setHint(hint);
    }

    private void checkIfInputsAreOK() {

        if (edit_crop_name.getText().toString().isEmpty() || edit_area.getText().toString().isEmpty()) {
            snackbarMessage(edit_area, "field(s) cannot be empty!");
            return;
        }
        ArrayList<String> arrayList2 = sharedPrefs.getArrayList(requireContext(), "locations");
        if (radio_paddy.isChecked()) {
            ArrayList<String> arrayList = sharedPrefs.getArrayList(requireContext(), "rice");

            for (int i = 0; i < arrayList.size(); i++) {
                if (edit_crop_name.getText().toString().trim().equals(arrayList.get(i))) {
                    for (int j = 0; j < arrayList2.size(); j++) {
                        if (edit_area.getText().toString().trim().equals(arrayList2.get(j))) {
                            initiateCropSuggestionCheck();
                            return;
                        }
                    }
                    snackbarMessage(edit_area, edit_area.getText().toString().trim() + " is not a valid location!");
                    return;
                }
            }
            snackbarMessage(edit_area, edit_crop_name.getText().toString().trim() + " not found in database!");
            return;
        }
        ArrayList<String> arrayList = sharedPrefs.getArrayList(requireContext(), "other products");

        for (int i = 0; i < arrayList.size(); i++) {
            if (edit_crop_name.getText().toString().trim().equals(arrayList.get(i))) {
                for (int j = 0; j < arrayList2.size(); j++) {
                    if (edit_area.getText().toString().trim().equals(arrayList2.get(j))) {
                        initiateCropSuggestionCheck();
                        return;
                    }
                }
                snackbarMessage(edit_area, edit_area.getText().toString().trim() + " is not a valid location!");
                return;
            }
        }
        snackbarMessage(edit_area, edit_crop_name.getText().toString().trim() + " not found in database!");

    }

    private void snackbarMessage(View view, String s) {
        Snackbar.make(view, s, Snackbar.LENGTH_SHORT).setAnchorView(constraint_container).show();
    }

    private void getLocationTemperature(DatabaseReference databaseReference) {
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

                        snackbarMessage(edit_area, error.getMessage());

                    }
                }
        );
    }

    private void getChancesOfRain(DatabaseReference databaseReference) {

        obtained_chances_of_rain = 0.0;
        databaseReference.child("Chances Of Rainfall").child(getTextsFromEditText(edit_area))
                .child(String.valueOf(getRandomNumber(1, 5))).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            obtained_chances_of_rain += Double.parseDouble(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                        }
                        obtained_chances_of_rain /= 5;
                        getChancesOfFlood(databaseReference);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        snackbarMessage(edit_area, error.getMessage());

                    }
                }
        );
    }

    String s = "";

    private void getChancesOfFlood(DatabaseReference databaseReference) {
        s = "";
        obtained_chances_of_flood = 0.0;
        databaseReference.child("Chances Of Flood").child(getTextsFromEditText(edit_area))
                .child(String.valueOf(getRandomNumber(1, 5))).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            s = MessageFormat.format("{0}{1}", s, Objects.requireNonNull(dataSnapshot.getValue()).toString());
                            obtained_chances_of_flood += Double.parseDouble(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                        }
                        obtained_chances_of_flood /= 5;
                        getCropDetails(databaseReference);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        snackbarMessage(edit_area, error.getMessage());

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
                            snackbarMessage(edit_area, error.getMessage());
                        }
                    });
        } else if (radio_others.isChecked()) {
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
                            snackbarMessage(edit_area, error.getMessage());
                        }
                    });
        }
    }

    private void formulateResult() {
        String rain_state;
        if (Rain.equals("A")) {
            rain_state = "above 50%";
        } else {
            rain_state = "below 50%";
        }
        String verdict;
        if (obtained_local_temp >= L_Temp && obtained_local_temp <= U_Temp && ((obtained_chances_of_rain > 50.0 && Rain.equals("A"))
                || (obtained_chances_of_rain <= 50.0 && Rain.equals("B"))) && obtained_chances_of_flood < 50) {

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
                + "°C\n" + "local chances of rain: " + obtained_chances_of_rain + "%\n"
                + "local chances of flood: " + obtained_chances_of_flood + "%\n";
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
            text_reason.setCharacterDelay(17);
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