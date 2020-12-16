package com.example.washer;

import android.app.Application;

public class GlobalObject extends Application {
    static final int washerNum = 4;
    static final int dormNum = 4;

    static int dormId = 1;
    static Washer[][] washers = new Washer[dormNum][washerNum];
    static String usr_id = "";
    static String dorm_name = "";

    static int test = 0;

    @Override

    public void onCreate() {

        super.onCreate();

    }




    @Override

    public void onTerminate() {

        super.onTerminate();

    }

    public static int getWasherNum() {
        return washerNum;
    }

    public static int getDormNum() {
        return dormNum;
    }

    public int getDormId() {
        return dormId;
    }

    public void setDormId(int dormId) {
        this.dormId = dormId;
    }
    public void setWasher(Washer washer, int i, int j) throws CloneNotSupportedException {
        this.washers[i][j] = (Washer) washer.clone();
    }
    public Washer getWasher(int i, int j){
        return this.washers[i][j];
    }
//    public Washer[][] getWashers() {
//        return washers;
//    }
//
//    public void setWashers(Washer[][] washers) {
//        this.washers = washers;
//    }
}
