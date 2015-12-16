package com.example.andrew.gpio;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.os.Process;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class InputPinService extends Service {

    private int pin = 0;
    private Boolean isRunning = false;



    public InputPinService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
    }


    private void setupPins() {
        if(MainActivity.mProcess != null) {
            DataOutputStream os = new DataOutputStream(MainActivity.mProcess.getOutputStream());
            try {
                // Export pin
                os.writeBytes("echo " + pin + " > /sys/class/gpio/export\n");
                // set direction
                os.writeBytes("chmod 666 /sys/class/gpio/gpio" + pin + "/direction\n");
                os.writeBytes("echo in > /sys/class/gpio/gpio" + pin + "/direction\n");
                // Edge Type
                os.writeBytes("chmod 666 /sys/class/gpio/gpio" + pin + "/edge\n");
                os.writeBytes("echo falling > /sys/class/gpio/gpio" + pin + "/edge\n");
                // Allow Access To value
                os.writeBytes("chmod 666 /sys/class/gpio/gpio" + pin + "/value\n");
                os.flush();
            } catch (IOException e) {
//            Toast.makeText(this, "Error Exporting Pins", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        pin = intent.getIntExtra("Pin", -1);

        if(pin == -1){
            Toast.makeText(this, "Pin Value not sent in intent", Toast.LENGTH_SHORT).show();
            return super.onStartCommand(intent, flags, startId);
        }

        setupPins();

        background.start();
        //Interrput();

        Toast.makeText(this, "Started: Input Pin Service", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.isRunning = false;
        Toast.makeText(this, "Stopping: Input Pin Service", Toast.LENGTH_SHORT).show();
    }


    // Can not be accessed from Device only emulator
    public void toastmessage(){
        Toast.makeText(this, "Button Press", Toast.LENGTH_LONG).show();
    }

    public native void setupIRQ(int pin);

    public native float getTempurature();


    Thread background = new Thread(new Runnable() {


        // After call for background.start this run method call
        public void run() {
            int pin = 33;

            Boolean IRQ_Loop = true;
            //FileListener fl = new FileListener("/sys/class/gpio/gpio" + pin + "/");
            String pastValue = "1";
//            edgeChange();
            try {
                while(IRQ_Loop) {

                    Thread.sleep(250);
                    BufferedReader br = new BufferedReader(new FileReader("/sys/class/gpio/gpio" + pin + "/value"));
                    String sl = br.readLine();
                    if( sl != null ) {
                        if(sl.equalsIgnoreCase(pastValue) == false && pastValue.equalsIgnoreCase("0")){
                            edgeChange();
                        }
                        pastValue = sl;
                    }


                //setupIRQ(pin);

                //edgeChange();

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e){
                e.printStackTrace();
            } catch(IOException e) {

            }
            // Code only goes here if something bad happened
            String testPoint = "WOAH!";
        }

        public void edgeChange(){
            Message msgObj = handler.obtainMessage();
            Bundle b = new Bundle();
            b.putString("message", "Button Pressed!");
            msgObj.setData(b);
            handler.sendMessage(msgObj);
        }

        private void threadMsg(String msg) {

            if (!msg.equals(null) && !msg.equals("")) {
                Message msgObj = handler.obtainMessage();
                Bundle b = new Bundle();
                b.putString("message", msg);
                msgObj.setData(b);
                handler.sendMessage(msgObj);
            }
        }



        private final Handler handler = new Handler() {

            public void handleMessage(Message msg) {
                String toastText = msg.getData().getString("message");

                if ((null != toastText)) {
                    Toast.makeText(getBaseContext(), toastText, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "Error No message text", Toast.LENGTH_SHORT).show();
                }



                // Write to DB
                sqlHelper db = new sqlHelper(getBaseContext());
                float tempf = getTempurature();
                double tempd = tempf;
                i2cValue v = new i2cValue(tempd);
                db.addTempuraturestoTable(v);
                db.close();
            }



        };

    });



}
