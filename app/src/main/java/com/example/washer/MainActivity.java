package com.example.washer;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    Button washerBtn1,washerBtn2,washerBtn3,washerBtn4;
    static final int washerNum = 4;

    Washer[] washers = new Washer[washerNum];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int i = 0; i < washerNum; i++){
            washers[i] = new Washer(i+1, (Button) findViewById(getResources().getIdentifier("washer" + String.valueOf(i+1), "id", getPackageName())));
            final int finalI = i;//익명함수에서는 로컬변수와 함수파라미터에 접근 못하므로 final로 고쳐야한다
            //참조 :  https://dreamaz.tistory.com/259
            Log.d(TAG, "i is " + String.valueOf(i));
//            ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_main, container, false);
            washers[i].setButtonListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onclick_washer(v, washers[finalI].getId());
                }
            });
        }




        //버튼 설정
//
//        washerBtn1 = (Button) findViewById(R.id.washer1);
//        washerBtn1.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                onclick_washer(v, 1);
//            }
//        });
//        washerBtn2 = (Button) findViewById(R.id.washer2);
//        washerBtn2.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                onclick_washer(v, 2);
//            }
//        });
//        washerBtn3 = (Button) findViewById(R.id.washer3);
//        washerBtn3.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                onclick_washer(v, 3);
//            }
//        });
//        washerBtn4 = (Button) findViewById(R.id.washer4);
//        washerBtn4.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//                    onclick_washer(v, 4);
//                }
//            });

        Intent intent = getIntent();
        String usr_id = intent.getStringExtra("usr_id");
        //OOO님 환영합니다!
        String usr_password = intent.getStringExtra("usr_password");
        Toast.makeText(getApplicationContext(), usr_id+"님, 환영합니다!", Toast.LENGTH_LONG).show();

        /*intent에서 안넘어온 값을 get할시에 int -> default값, String -> null*/
        int hour = intent.getIntExtra("hour", -1);
        int minute = intent.getIntExtra("minute", -1);
        final int washerId = intent.getIntExtra("washerId", -1);
        if (hour != -1 && minute != -1){
            //Login Activity에서 넘어오는 것을 대비하여 체크해줘야한다
            //세탁기 정보가 설정됨
            int left_minute = hour * 60 + minute;
            long destiny_time_millis = System.currentTimeMillis() + left_minute * 60 *1000;
            washers[washerId-1].setDestiny_millis_time(destiny_time_millis);
            TextView changed_washer_time = (TextView) findViewById(getResources().getIdentifier("washerLeftTime" + String.valueOf(washerId), "id", getPackageName()));
            Log.d(TAG, String.valueOf(washerId));
            washers[washerId-1].setBusy(true);
            if (hour != 0) {
                Toast.makeText(getApplicationContext(), String.valueOf(washerId) +
                        "번 세탁기에서 " + String.valueOf(hour) + "시간" + String.valueOf(minute) +
                        " 분 실행", Toast.LENGTH_LONG).show();

                changed_washer_time.setText("남은시간 : " + String.valueOf(hour) + ":" + String.valueOf(minute));
            }
            else{
                Toast.makeText(getApplicationContext(), String.valueOf(washerId) +
                        "번 세탁기에서 "+ String.valueOf(minute) +
                        " 분 실행", Toast.LENGTH_LONG).show();
                changed_washer_time.setText("남은시간 : " + "0:" + String.valueOf(minute));
            }
        }
        else{
            //LoginActivity에서 넘어옴

        }
        TimerTask validate_washer_time = new TimerTask() {
            @Override
            public void run() {
                //타이머 갱신
                Log.d("MainActivity", String.valueOf(System.currentTimeMillis()));
                for(int i = 0; i<washerNum; i++){
                    if (washers[i].isBusy()) {
                        long left_time_sec = (washers[i].getDestiny_millis_time() - System.currentTimeMillis()) / 1000;
                        int left_hour = (int) left_time_sec / 3600;
                        int left_minute = (int) left_time_sec / 60 - left_hour * 60;
                        int left_sec = (int) left_time_sec - left_hour * 3600 - left_minute * 60;
                        String left_time_str = "남은시간 : " + String.valueOf(left_hour) + ":" + String.valueOf(left_minute) + ":" + String.valueOf(left_sec);
                        TextView changed_washer_time = (TextView) findViewById(getResources().getIdentifier("washerLeftTime" + String.valueOf(i + 1), "id", getPackageName()));
                        changed_washer_time.setText(left_time_str);
                    }
                }

            }
        };
        Timer timer = new Timer();
        timer.schedule(validate_washer_time, 0, 1000);
        /*Timer[] timer = new Timer[washerNum];
        for(int i=0;i<4;i++){
            timer[i].schedule(validate_washer_time, 0, 1000);
        }*/
    }
    public void onclick_washer(View view, int washerId) {
        Intent InputTimeIntent = new Intent(MainActivity.this, InputTime.class);
        InputTimeIntent.putExtra("washerId",washerId); /*송신*/
        startActivity(InputTimeIntent);
    }
    public int getResId(Context ctx, String id) {
        Resources res = ctx.getResources();
        //name엔 뭘 넣어야 하는거지
        return res.getIdentifier("", "id", ctx.getPackageName());
    }

}
