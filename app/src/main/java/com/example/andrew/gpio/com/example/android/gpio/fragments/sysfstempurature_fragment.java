package com.example.andrew.gpio.com.example.android.gpio.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.andrew.gpio.R;


/**
 * Created by andrew on 11/21/15.
 */
public class sysfstempurature_fragment extends Fragment {

    TextView tvZone0;
    TextView tvZone1;
    public sysfstempurature_fragment() {
        super();

//        tvZone0 = findViewById()

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_sysfstempurature, container, false);


        tvZone0 = (TextView) rootView.findViewById(R.id.textViewThermalZone0);
        tvZone1 = (TextView) rootView.findViewById(R.id.textViewThermalZone1);

        float f0 = getsysfstempurature(0);
        String szone0 = f0 > 0 ? String.format("Thermal Zone0: %4.2f C", f0) : "Thermal Zone0: NA";
        tvZone0.setText(szone0);

        float f1 = getsysfstempurature(1);
        String szone1 = f1 > 0 ? String.format("Thermal Zone1: %4.2f C", f1) : "Thermal Zone1: NA";
        tvZone1.setText(szone1);

        return  rootView;
    }


    public native float getsysfstempurature(int zone);

}
