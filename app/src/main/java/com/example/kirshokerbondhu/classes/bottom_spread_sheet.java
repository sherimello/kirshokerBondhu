package com.example.kirshokerbondhu.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kirshokerbondhu.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class bottom_spread_sheet extends BottomSheetDialogFragment {

    private Data mData;
    private ArrayList<String> locations;
    private AutoCompleteTextView edit_crop_name, edit_area;

    private TextView text_area;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_spread_sheet, container, false);
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        edit_area = v.findViewById(R.id.edit_area);
        text_area = v.findViewById(R.id.text_area);
//        locs = (String[]) getArrayList("locations").toArray();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, getArrayList("locations"));

        edit_area.setAdapter(adapter);
        adapter.notifyDataSetChanged();
//        edit_area.showDropDown();
        Toast.makeText(requireActivity(), String.valueOf(getArrayList("locations").size()), Toast.LENGTH_SHORT).show();
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mData = (Data) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement Data Interface.");
        }
    }

    public ArrayList<String> getArrayList(String key) {
        SharedPreferences prefs = requireContext().getSharedPreferences("Location Data Pref", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public interface Data {
        void getArrayList(ArrayList<String> locations);
    }
}
