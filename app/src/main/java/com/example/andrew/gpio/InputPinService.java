package com.example.andrew.gpio;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.os.Process;
import android.support.annotation.Nullable;
import android.widget.Toast;

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


    public void toastmessage(){
        Toast.makeText(this, "Button Press", Toast.LENGTH_LONG).show();
    }

    public native Boolean setup_Interrupt(int pin);



    Thread background = new Thread(new Runnable() {


        // After call for background.start this run method call
        public void run() {
            Boolean IRQ_Loop = true;
            while(IRQ_Loop) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                IRQ_Loop = setup_Interrupt(33);

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
            }

        };

    });



}
