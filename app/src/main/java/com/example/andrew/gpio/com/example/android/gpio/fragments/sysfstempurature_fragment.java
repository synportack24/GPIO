package com.example.andrew.gpio.com.example.android.gpio.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.andrew.gpio.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by andrew on 11/21/15.
 */
public class sysfstempurature_fragment extends Fragment {

    ListView ls;
    public sysfstempurature_fragment() {
        super();

//        tvZone0 = findViewById()

    }

    private Context attachedContext;

    @Override
    public void onAttach(Context context) {
        attachedContext = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_sysfstempurature, container, false);

        List<Map<String, String>> syslist = new ArrayList<Map<String, String>>();

        float f0 = getsysfstempurature(0);
        String thermalZone0 = f0 > 0 ? String.format("%4.2f C", f0) : "NA";
        Map<String, String> t0m = new HashMap<>();
        t0m.put("Name", "Thermal Zone 0");
        t0m.put("Value", thermalZone0);
        syslist.add(t0m);

        float f1 = getsysfstempurature(1);
        String thermalZone1 = f1 > 0 ? String.format("%4.2f C", f1) : "NA";
        Map<String, String> t1m = new HashMap<>();
        t1m.put("Name", "Thermal Zone 1");
        t1m.put("Value", thermalZone1);
        syslist.add(t1m);

        String lversion = getLinuxVersion();
        if(lversion == null)
            lversion = "Not Found";
        Map<String, String> v1m = new HashMap<>();
        v1m.put("Name", "Linux Version");
        v1m.put("Value", lversion);
        syslist.add(v1m);

        float b1 = getTimeSinceBoot();
        String bootTime = b1 > 0 ? String.format("%6.2f Seconds", b1) : "NA";
        Map<String, String> b1m = new HashMap<>();
        b1m.put("Name", "Time Since Boot");
        b1m.put("Value", bootTime);
        syslist.add(b1m);

        float b2 = getTimeSinceIdle();
        String idleTime = b2 > 0 ? String.format("%6.2f Seconds", b2) : "NA";
        Map<String, String> b2m = new HashMap<>();
        b2m.put("Name", "Time Since Idle");
        b2m.put("Value", idleTime);
        syslist.add(b2m);



        ls = (ListView) rootView.findViewById(R.id.listViewSysfsInfo);

        Context ctext = getActivity().getBaseContext();  // getContext not valid for API 19 =(
        SimpleAdapter adapter = new SimpleAdapter(ctext, syslist,  android.R.layout.simple_list_item_2, new String[] {"Name", "Value"}, new int[] {android.R.id.text1, android.R.id.text2});
        //SimpleAdapter adapter = new SimpleAdapter(attachedContext, syslist,  android.R.layout.simple_list_item_2, new String[] {"Name", "Value"}, new int[] {android.R.id.text1, android.R.id.text2});
        ls.setAdapter(adapter);


        return  rootView;
    }


    public native float getsysfstempurature(int zone);

    public native String getLinuxVersion();

    public native float getTimeSinceBoot();

    public native float getTimeSinceIdle();
}
