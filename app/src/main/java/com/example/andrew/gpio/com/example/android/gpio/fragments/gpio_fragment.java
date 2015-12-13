package com.example.andrew.gpio.com.example.android.gpio.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.andrew.gpio.MainActivity;
import com.example.andrew.gpio.R;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by andrew on 11/21/15.
 */
public class gpio_fragment extends Fragment {

    int pins[] = {28, 30, 31};




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_gpio, container, false);

        setupPins();

        final CheckBox cbTest28 = (CheckBox) rootView.findViewById(R.id.checkBox28);
        cbTest28.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                int writeHight = 0; // LOW
                if (cbTest28.isChecked()) {
                    writeHight = 1;
                }
                pinWrite(28, writeHight);
            }
        });

        final CheckBox cbTest30 = (CheckBox) rootView.findViewById(R.id.checkBox30);
        cbTest30.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                int writeHight = 0; // LOW
                if(cbTest30.isChecked()){
                    writeHight = 1;
                }
                pinWrite(30, writeHight);
            }
        });

        final CheckBox cbTest31 = (CheckBox) rootView.findViewById(R.id.checkBox31);
        cbTest31.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                int writeHight = 0; // LOW
                if(cbTest31.isChecked()){
                    writeHight = 1;
                }
                pinWrite(31, writeHight);
            }
        });

        return  rootView;
    }

    private void setupPins() {
        if(MainActivity.mProcess != null) {
            DataOutputStream os = new DataOutputStream(MainActivity.mProcess.getOutputStream());
            try {
                for (int pin : pins) {
                    os.writeBytes("echo " + pin + " > /sys/class/gpio/export\n");
                    os.writeBytes("chmod 666 /sys/class/gpio/gpio" + pin + "/direction\n");
                    os.writeBytes("echo out > /sys/class/gpio/gpio" + pin + "/direction\n");
                    os.writeBytes("chmod 666 /sys/class/gpio/gpio" + pin + "/value\n");
                }
                os.flush();
            } catch (IOException e) {
//            Toast.makeText(this, "Error Exporting Pins", Toast.LENGTH_LONG).show();
            }
        }
    }

    public native void exportPin(int Pin);

    public native void setPinMode(int Pin, int direction);

    public native void pinWrite(int Pin, int value);
}
