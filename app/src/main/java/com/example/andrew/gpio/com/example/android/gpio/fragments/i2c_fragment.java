package com.example.andrew.gpio.com.example.android.gpio.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.andrew.gpio.R;

/**
 * Created by andrew on 11/30/15.
 */
public class i2c_fragment extends Fragment {


    TextView tvTemp;
    TextView tvPres;
    SeekBar humidityBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_i2c, container, false);

        tvTemp = (TextView) rootView.findViewById(R.id.textViewi2cTemp);
        tvPres = (TextView) rootView.findViewById(R.id.textViewi2cPressure);
        humidityBar = (SeekBar) rootView.findViewById(R.id.seekBari2cHumidity);

        UpdateValues();

        Button updateBtn = (Button) rootView.findViewById(R.id.buttoni2cUpdateSensors);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateValues();
            }
        });


        return  rootView;
    }


    public void UpdateValues() {
        updateSensors();
        float temp = getTemperature();
        float pres = getPressure();
        float hum = getHumidity();

        tvTemp.setText(String.format("Tempurature: %4.2f *C", temp));
        tvPres.setText(String.format("Pressure: %4.2f hPa", pres));
        humidityBar.setProgress((int)(Math.ceil(hum)));

    }

    // Native Methods
    // -------------------------------------------------------------------
    public native void updateSensors();

    public native float getTemperature();

    public native float getHumidity();

    public native float getPressure();

}