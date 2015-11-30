package com.example.andrew.gpio;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.andrew.gpio.com.example.android.gpio.fragments.adc_fragment;
import com.example.andrew.gpio.com.example.android.gpio.fragments.gpio_fragment;
import com.example.andrew.gpio.com.example.android.gpio.fragments.i2c_fragment;
import com.example.andrew.gpio.com.example.android.gpio.fragments.main_fragment;
import com.example.andrew.gpio.com.example.android.gpio.fragments.pwm_fragment;
import com.example.andrew.gpio.com.example.android.gpio.fragments.sysfstempurature_fragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame, new main_fragment()).commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        Fragment fragment = null;

        if (id == R.id.nav_main) {
            setTitle("GPIO Map");
            fragment = new main_fragment();
        } else if (id == R.id.nav_gpio) {
            setTitle("GPIO");
            fragment = new gpio_fragment();
        } else if (id == R.id.nav_adc) {
            setTitle("ADC values");
            fragment = new adc_fragment();
        } else if (id == R.id.nav_pwm) {
            setTitle("PWM pin");
            fragment = new pwm_fragment();

            // SYSFS Options
        } else if (id == R.id.nav_sysfstemp) {
            setTitle("sysfs Tempurature");
            fragment = new sysfstempurature_fragment();
        } else if (id == R.id.nav_i2c) {
            setTitle("I2C External Sensors");
            fragment = new i2c_fragment();
        }

        if(fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void SetTitle(String title) {
        try {
            getActionBar().setTitle(title);
        } catch(Exception e) {
            //Log
        }
    }

    public native void gpio_init();

    static {
        System.loadLibrary("cgpio");
    }


}
