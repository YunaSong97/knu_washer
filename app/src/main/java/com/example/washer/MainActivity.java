package com.example.washer;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
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

    private View header;

    Button washerBtn1,washerBtn2,washerBtn3,washerBtn4;
    static final int washerNum = 4;
    static final int dormNum = 4;

    int dormId = 1;
    Washer[][] washers = new Washer[dormNum][washerNum];
    TextView[] dormButton = new TextView[dormNum];
    String usr_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String usr_id_from_login = intent.getStringExtra("usr_id");

        //기숙사 이름 메인에 띄우기
        String dorm_name = intent.getStringExtra("usr_dorm");
        TextView usr_dorm = (TextView)findViewById(R.id.usr_dorm);
        usr_dorm.setText(dorm_name);

        //OOO님 환영합니다!
        if(!TextUtils.isEmpty(usr_id_from_login)){
            String usr_password = intent.getStringExtra("usr_password");
            Toast.makeText(getApplicationContext(), usr_id_from_login+"님, 환영합니다!", Toast.LENGTH_LONG).show();
            usr_id = usr_id_from_login;

        }
        else{
            Toast.makeText(getApplicationContext(), usr_id+"님, 재접속!", Toast.LENGTH_LONG).show();
        }



        /*intent에서 안넘어온 값을 get할시에 int -> default값, String -> null*/

        /*InputTime에서 세탁기 시간을 설정했을 경우*/
        int hour = intent.getIntExtra("hour", -1);
        int minute = intent.getIntExtra("minute", -1);
        final int washerId = intent.getIntExtra("washerId", -1);
        dormId = intent.getIntExtra("dormId", 1);//1번을 default로 해야함. -1같은걸로 하면 팅김
        TextView textView = findViewById(R.id.usr_dorm);
        textView.setText("기숙사" + String.valueOf(dormId));

        for (int i = 0; i < washerNum; i++){
            dormButton[i] = (TextView) findViewById(getResources().getIdentifier("dorm" + String.valueOf(i+1), "id", getPackageName()));
            final int finalI = i;//익명함수에서는 로컬변수와 함수파라미터에 접근 못하므로 final로 고쳐야한다
            //참조 :  https://dreamaz.tistory.com/259
            Log.d(TAG, "i is " + String.valueOf(i));
//            ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_main, container, false);
            dormButton[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dormId = finalI + 1;
                    Toast.makeText(getApplicationContext(), "dorm " + String.valueOf(finalI+1) + " 선택", Toast.LENGTH_SHORT).show();
//                    setContentView(R.layout.activity_main);
                    DrawerLayout sideLayout = (DrawerLayout) findViewById(R.id.drawer_layout);//내가 지정한 id가 아님 그냥 drawer_layout가 기본인듯
                    TextView textView = findViewById(R.id.usr_dorm);
                    textView.setText("기숙사" + String.valueOf(dormId));
                    sideLayout.closeDrawers();
                }
            });
        }
        //header안해도 되는데?
//        header = getLayoutInflater().inflate(R.layout.side_navi_main, null, false);
//        TextView textView = findViewById(R.id.dorm1);
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "dorm 1 선택", Toast.LENGTH_LONG).show();
////                DrawerLayout drawer = findViewById(R.id.drawerView);
////                drawer.closeDrawer(GravityCompat.START);
//                setContentView(R.layout.activity_main);
//            }
//        });

        //Main Activity로 돌아오면 이게 작동돼서 new로 washer가 초기화된다.
        //DB랑 연동해야함
        for (int i = 0; i < dormNum; i++){
            for(int j = 0; j<washerNum;j++ ) {
                washers[i][j] = new Washer(i + 1, j+1, (Button) findViewById(getResources().getIdentifier("washer" + String.valueOf(j + 1), "id", getPackageName())));
                final int finalI = i;//익명함수에서는 로컬변수와 함수파라미터에 접근 못하므로 final로 고쳐야한다
                //참조 :  https://dreamaz.tistory.com/259
                Log.d(TAG, "i is " + String.valueOf(i));
//            ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_main, container, false);
                final int finalJ = j;
                washers[i][j].setButtonListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        onclick_washer(v, dormId, washers[finalI][finalJ].getId());
                    }
                });
                boolean getImformSuccess = washers[i][j].getImformFromDatabase();
                if (!getImformSuccess) {
                    Toast.makeText(getApplicationContext(), "정보를 불러오는데 실패하였습니다. ", Toast.LENGTH_LONG).show();
                }
            }
        }



        if (hour != -1 && minute != -1){
//            for(int i = 0; i< washerNum; i++){
//                Log.d(TAG, "busy check" + String.valueOf(i+1) + String.valueOf(washers[i].isBusy()));
//            }
            //Login Activity에서 넘어오는 것을 대비하여 체크해줘야한다
            //세탁기 정보가 설정됨
            int left_minute = hour * 60 + minute;
            long destiny_time_millis = System.currentTimeMillis() + left_minute * 60 *1000;
            washers[dormId-1][washerId-1].getImformFromDatabase();
            if (washers[dormId-1][washerId-1].isBusy()){
                //세탁기 이미 돌아가고 있는중
                Toast.makeText(getApplicationContext(), "이미 다른 사용자가 사용중인 세탁기입니다.", Toast.LENGTH_LONG).show();
            }
            else {
                //세탁기 목표 시간 설정
                boolean success = washers[dormId-1][washerId-1].updateImformToDatabase(true, destiny_time_millis, usr_id, false);
                TextView changed_washer_time = (TextView) findViewById(getResources().getIdentifier("washerLeftTime" + String.valueOf(washerId), "id", getPackageName()));
                Log.d(TAG, String.valueOf(washerId));
                if (success) {
                    if (hour != 0) {
                        Toast.makeText(getApplicationContext(), String.valueOf(washerId) +
                                "번 세탁기에서 " + String.valueOf(hour) + "시간" + String.valueOf(minute) +
                                " 분 실행", Toast.LENGTH_LONG).show();

                        changed_washer_time.setText("남은시간 : " + String.valueOf(hour) + ":" + String.valueOf(minute));
                    } else {
                        Toast.makeText(getApplicationContext(), String.valueOf(washerId) +
                                "번 세탁기에서 " + String.valueOf(minute) +
                                " 분 실행", Toast.LENGTH_LONG).show();
                        changed_washer_time.setText("남은시간 : " + "0:" + String.valueOf(minute));
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "업데이트 오류 : 누군가가 이미 사용중이거나 네트워크 오류", Toast.LENGTH_LONG).show();
                }
            }
        }
        else{
            //LoginActivity에서 넘어옴

        }
        TimerTask validate_washer_time = new TimerTask() {
            @Override
            public void run() {
                //타이머 갱신
                //Log.d("MainActivity", String.valueOf(System.currentTimeMillis()));
                for(int i = 0; i<dormNum; i++) {
                    for (int j = 0; j < washerNum; j++) {
                        Log.d(TAG, "is wash done" + String.valueOf(i + 1) + String.valueOf(washers[i][j].isWashDone()));
                        if (washers[i][j].isBusy()) {
                            Log.d(TAG, String.valueOf(i + 1) + "is busy");
                            long left_time_sec = (washers[i][j].getDestiny_millis_time() - System.currentTimeMillis()) / 1000;
                            if (left_time_sec < 0) {

                                washers[i][j].updateImformToDatabase(false, 0, null, true);
                            }
                            else {
                                if(i == dormId-1) {
                                    int left_hour = (int) left_time_sec / 3600;
                                    int left_minute = (int) left_time_sec / 60 - left_hour * 60;
                                    int left_sec = (int) left_time_sec - left_hour * 3600 - left_minute * 60;
                                    String left_time_str = "남은시간 : " + String.valueOf(left_hour) + ":" + String.valueOf(left_minute) + ":" + String.valueOf(left_sec);
                                    TextView changed_washer_time = (TextView) findViewById(getResources().getIdentifier("washerLeftTime" + String.valueOf(j + 1), "id", getPackageName()));
                                    changed_washer_time.setText(left_time_str);
                                }
                            }
                        }
                        else if (washers[i][j].isWashDone()) {
                            //busy하지 않고 wash가 done이면
                            if(i == dormId-1) {
                                TextView changed_washer_time = (TextView) findViewById(getResources().getIdentifier("washerLeftTime" + String.valueOf(j + 1), "id", getPackageName()));
                                changed_washer_time.setText("세탁완료");
                            }
                        }
                        else{
                            if(i == dormId-1){
                                TextView changed_washer_time = (TextView) findViewById(getResources().getIdentifier("washerLeftTime" + String.valueOf(j + 1), "id", getPackageName()));
                                changed_washer_time.setText("사용가능");
                            }
                        }

                    }
                }

            }
        };
        Timer timer = new Timer();
        timer.schedule(validate_washer_time, 0, 1000);

    }
    public void onclick_washer(View view, int dormId, int washerId) {
        Intent InputTimeIntent = new Intent(MainActivity.this, InputTime.class);
        InputTimeIntent.putExtra("washerId",washerId); /*송신*/
        InputTimeIntent.putExtra("dormId",dormId); /*송신*/
        startActivity(InputTimeIntent);
    }
    public int getResId(Context ctx, String id) {
        Resources res = ctx.getResources();
        //name엔 뭘 넣어야 하는거지
        return res.getIdentifier("", "id", ctx.getPackageName());
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알림");
        builder.setMessage("종료하시겠습니까?");
        builder.setNegativeButton("취소",null);
        builder.setPositiveButton("종료", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveTaskToBack(true);                  // 태스크를 백그라운드로 이동
                finishAndRemoveTask();                  // 액티비티 종료 + 태스크 리스트에서 지우기
                android.os.Process.killProcess(android.os.Process.myPid());   // 앱 프로세스 종료
            }
        });
        builder.show();
    }

    //git push test
    
    //git push on site test

}
