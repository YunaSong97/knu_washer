package com.example.washer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Loading extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(android.R.style.Theme);
        setContentView(R.layout.activity_loading);
        startLoading();
    }
    private void startLoading() {
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                finish();
//            }
//        }, 2000);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                //run after delay
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        }, 2000);//delay ( 이 이후에 코드 넣으면 바로 그 코드 실행한다. delay 주기 위해서는 run 안에 넣어줘야함

    }
}