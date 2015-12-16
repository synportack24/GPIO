package com.example.andrew.gpio;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by andrew on 12/14/15.
 */
public class i2cValue {

    private String timeDate;
    public String getTimeDate() {
        return timeDate;
    }
    public void setTimeDate(String timeDate) {
        this.timeDate = timeDate;
    }


    private double tempurature;
    public double getTempurature() {
        return tempurature;
    }
    public void setTempurature(double tempurature) {
        this.tempurature = tempurature;
    }


    public i2cValue(double temp){
        this.tempurature = temp;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        timeDate= sdf.format(date);
    }

    public i2cValue(String date, double temp){
        timeDate = date;
        tempurature = temp;
    }

}
