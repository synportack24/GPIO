package com.example.andrew.gpio.com.example.android.gpio.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.andrew.gpio.MainActivity;
import com.example.andrew.gpio.R;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andrew on 11/30/15.
 */
public class i2c_fragment extends Fragment {



    ListView i2cListView;
    SeekBar humidityBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_i2c, container, false);

//        DataOutputStream os = new DataOutputStream(MainActivity.mProcess.getOutputStream());
//        try {
//            os.writeBytes("chmod 666 /dev/i2c-1\n");
//        } catch (IOException e){
//        }

        humidityBar = (SeekBar) rootView.findViewById(R.id.seekBari2cHumidity);
        i2cListView = (ListView) rootView.findViewById(R.id.listViewI2C);

        setupI2CPermissions();

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

    private void setupI2CPermissions(){
        if(MainActivity.mProcess != null) {
            DataOutputStream os = new DataOutputStream(MainActivity.mProcess.getOutputStream());
            try {
                os.writeBytes("chmod 666 /dev/i2c-1\n");
                os.flush();
            } catch (IOException e) {
//           Toast.makeText(this, "Error chmoding i2c-1", Toast.LENGTH_LONG).show();
            }
        }
    }


    public void UpdateValues() {
        updateSensors();
        float temp = getTemperature();
        float pres = getPressure();
        float hum = getHumidity();
        float uv = getUV();
        float lux = getVisableLUX();

        //tvTemp.setText(String.format("Tempurature: %4.2f *C", temp));
        //tvPres.setText(String.format("Pressure: %4.2f hPa", pres));
        //tvLum.setText(String.format("Luminosity: %4.2f", lum));
        humidityBar.setProgress((int) (Math.ceil(hum)));



        List<Map<String, String>> i2clist = new ArrayList<Map<String, String>>();

        String themerature = String.format("%4.2f C", temp);
        Map<String, String> t0m = new HashMap<>();
        t0m.put("Name", "Temperature");
        t0m.put("Value", themerature);
        i2clist.add(t0m);

        String pressure = String.format("%4.2f hPa", pres);
        Map<String, String> t0p = new HashMap<>();
        t0p.put("Name", "Pressure");
        t0p.put("Value", pressure);
        i2clist.add(t0p);

        String humidity = String.format("%4.2f %%", hum);
        Map<String, String> t0h = new HashMap<>();
        t0h.put("Name", "Humidity");
        t0h.put("Value", humidity);
        i2clist.add(t0h);

        String ultraV = String.format("%4.2f lux", uv);
        Map<String, String> t0uv = new HashMap<>();
        t0uv.put("Name", "Ultra Violet");
        t0uv.put("Value", ultraV);
        i2clist.add(t0uv);

        String vlux = String.format("%4.2f lux", lux);
        Map<String, String> t0lx = new HashMap<>();
        t0lx.put("Name", "Visable Light");
        t0lx.put("Value", vlux);
        i2clist.add(t0lx);

//        String vlux = String.format("%4.2f lux", lux);
//        Map<String, String> t0lx = new HashMap<>();
//        t0lx.put("Name", "Visable LUX");
//        t0lx.put("Value", vlux);
//        i2clist.add(t0lx);


        Context ctext = getActivity().getBaseContext();  // getContext not valid for API 19 =(
        SimpleAdapter adapter = new SimpleAdapter(ctext, i2clist,  android.R.layout.simple_list_item_2, new String[] {"Name", "Value"}, new int[] {android.R.id.text1, android.R.id.text2});
        //SimpleAdapter adapter = new SimpleAdapter(attachedContext, syslist,  android.R.layout.simple_list_item_2, new String[] {"Name", "Value"}, new int[] {android.R.id.text1, android.R.id.text2});
        i2cListView.setAdapter(adapter);



    }

    // Native Methods
    // -------------------------------------------------------------------
    public native void updateSensors();

    public native float getTemperature();

    public native float getHumidity();

    public native float getPressure();

    public native float getUV();

    public native float getVisableLUX();

    public native float getIRLUX();

}