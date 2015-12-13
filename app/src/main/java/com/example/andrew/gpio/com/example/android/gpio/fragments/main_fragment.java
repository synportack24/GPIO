package com.example.andrew.gpio.com.example.android.gpio.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.andrew.gpio.R;

/**
 * Created by andrew on 11/21/15.
 */
public class main_fragment extends Fragment {

    Intent externalGPIOIntent = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Button btnStart = (Button) rootView.findViewById(R.id.buttonStartService);
        btnStart.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startService(externalGPIOIntent);
            }
        });

        Button btnStop = (Button) rootView.findViewById(R.id.buttonStopService);
        btnStop.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (externalGPIOIntent != null){
                    //stopService
                }
            }
        });

        return  rootView;
    }

}
