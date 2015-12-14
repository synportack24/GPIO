package com.example.andrew.gpio;

import android.graphics.Path;
import android.os.*;
/**
 * Created by andrew on 12/13/15.
 */
public class FileListener extends FileObserver {

    protected String path;

    public FileListener(String path) {
        super(path, FileObserver.ALL_EVENTS);

        this.path = path;
    }

    @Override
    public void onEvent(int event, String path) {
        if ((FileObserver.MODIFY & event)!=0) {
            String stop = "asdf";
        }
    }
}
