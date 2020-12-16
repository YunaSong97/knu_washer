package com.example.washer;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.util.Log;
import android.view.Display;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class GlobalObject extends Application {
    static final int washerNum = 4;
    static final int dormNum = 4;

    static int dormId = 1;
    static Washer[][] washers = new Washer[dormNum][washerNum];
    static String usr_id = "";
    static String dorm_name = "";

    private static final String TAG = "GlobalObject";

    static int test = 0;

    @Override

    public void onCreate() {

        super.onCreate();

    }

    public boolean updateImformFromDatabase(RequestQueue queue){
        final String[] washer_id = new String[100];
        final String[] washer_state = new String[100];
        final String[] destinyTime = new String[100];
        boolean getImformSuccess = true;

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d(TAG,String.valueOf(response));
                        JSONArray jsonArray = new JSONArray(response);


//                        JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                        for(int i=0; i<jsonArray.length(); i++){
                            JSONObject one_jsonObject = jsonArray.getJSONObject(i);
                            washer_id[i] = one_jsonObject.getString("washer_id")+i;
                            Log.d(TAG, one_jsonObject.getString("washer_id")+i);
                            washer_state[i] = one_jsonObject.getString("washer_state");
                            destinyTime[i] = one_jsonObject.getString("destinyTime");
                        }
                        for (int i = 0; i < dormNum * washerNum; i++){
                            Log.d(TAG, washer_state[i].substring(0,4));
                            int d = Integer.parseInt(washer_id[i].substring(1,2));
                            int w = Integer.parseInt(washer_id[i].substring(3,4));
                            washers[d-1][w-1].setState(washer_state[i].substring(0,4));
                            washers[d-1][w-1].setDestiny_millis_time(Long.parseLong(destinyTime[i]));


                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        getTimeRequest GetTimeRequest = new getTimeRequest(responseListener);
//        RequestQueue queue = Volley.newRequestQueue(getTime.this);
        queue.add(GetTimeRequest);
        //이거하고 바로 하면 안됨 시간이 걸리기 때문에 처리는 전부 안에서 해줘야된다

//        //**업데이트 필요!!  this.id를 통해 DB에서 데이터 받아온다
//        boolean getImformSuccess = true;
////        this.destiny_millis_time = 0;
////        this.setBusy(false);
////        this.usingUserId = "default";
////        this.washDone = false;

        return getImformSuccess;
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
