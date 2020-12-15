package com.example.washer;

import android.view.View;
import android.widget.Button;

public class Washer {
    private boolean busy = false;
    private int left_second = 0;
    private int id = 0;
    private long destiny_millis_time = 0;
    private final Button button;

    public Washer(int id, Button button) {
        this.id = id;
        this.button = button;

    }
    //=================================
    //========getter and setter========
    //=================================

    public void setButtonListener(View.OnClickListener listener){
        this.button.setOnClickListener(listener);
    }

    public long getDestiny_millis_time() {
        return destiny_millis_time;
    }

    public void setDestiny_millis_time(long destiny_millis_time) {
        this.destiny_millis_time = destiny_millis_time;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public int getLeft_second() {
        return left_second;
    }

    public void setLeft_second(int left_second) {
        this.left_second = left_second;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
