package com.example.kirshokerbondhu.classes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kirshokerbondhu.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class bottom_spread_sheet extends BottomSheetDialogFragment {

    private AutoCompleteTextView edit_crop_name, edit_area;
    private TextView text_area;
    private RadioButton radio_paddy, radio_others;
    private SharedPrefs sharedPrefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_spread_sheet, container, false);
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        edit_area = v.findViewById(R.id.edit_area);
        edit_crop_name = v.findViewById(R.id.edit_crop_name);
        text_area = v.findViewById(R.id.text_area);
        radio_paddy = v.findViewById(R.id.radio_paddy);
        radio_others = v.findViewById(R.id.radio_others);

        radio_paddy.setChecked(true);

        sharedPrefs = new SharedPrefs();
        addAdapterToLocatioACTV();
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

    private void addAdapterToLocatioACTV() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, sharedPrefs.getArrayList(requireContext(), "locations"));

        edit_area.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }



}
