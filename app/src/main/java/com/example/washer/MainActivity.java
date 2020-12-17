package com.example.washer;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private View header;

    Button washerBtn1,washerBtn2,washerBtn3,washerBtn4;
//    static final int washerNum = 4;
//    static final int dormNum = 4;

    int currentDormId = 1;
//    Washer[][] washers = new Washer[dormNum][washerNum];
    TextView[] dormButton = new TextView[GlobalObject.dormNum];
    String usr_id = "";
    GlobalObject g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        final GlobalObject GlobalObject = (GlobalObject) getApplicationContext();

        Button listbtn = (Button) findViewById(R.id.list_btn);
        listbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LogActivity.class);
                startActivity(intent);
            }
        });

        GlobalObject.test += 1;
        Log.d("==========test=========", String.valueOf(GlobalObject.test));
        Intent intent = getIntent();
        String usr_id_from_login = intent.getStringExtra("usr_id");


        /*InputTime에서 세탁기 시간을 설정했을 경우*/
        int hour = intent.getIntExtra("hour", -1);
        int minute = intent.getIntExtra("minute", -1);
        final int washerId = intent.getIntExtra("washerId", -1);
        currentDormId = intent.getIntExtra("dormId", 1);//1번을 default로 해야함. -1같은걸로 하면 팅김


        출처: https://peekaf.tistory.com/28 [To Be Continued...]

        //OOO님 환영합니다!
        if(!TextUtils.isEmpty(usr_id_from_login)){
            GlobalObject.usr_id = usr_id_from_login;
            GlobalObject.dorm_name = intent.getStringExtra("usr_dorm");
            String usr_password = intent.getStringExtra("usr_password");
            Toast.makeText(getApplicationContext(), usr_id_from_login+"님, 환영합니다!", Toast.LENGTH_LONG).show();

            for (int i = 0; i < GlobalObject.dormNum; i++){
                for (int j = 0; j<GlobalObject.washerNum;j++){
                    GlobalObject.washers[i][j] = new Washer(i + 1, j+1);
//                    boolean getImformSuccess = GlobalObject.washers[i][j].getImformFromDatabase();
//                    if (!getImformSuccess) {
//                        Toast.makeText(getApplicationContext(), "정보를 불러오는데 실패하였습니다. ", Toast.LENGTH_LONG).show();
//                    }
                }
                g = new GlobalObject();
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                g.updateImformFromDatabase(queue);
            }

        }
        else{
            Toast.makeText(getApplicationContext(), GlobalObject.usr_id+"님, 재접속!", Toast.LENGTH_LONG).show();

        }

        //기숙사 이름 설정
        TextView dormText = findViewById(R.id.usr_dorm);

        dormText.setText(GlobalObject.dorm_name + " " + String.valueOf(currentDormId)+"층");
        

        //버튼 설정
       for(int i = 0; i< GlobalObject.washerNum;i++) {
           final int finalI = i;//익명함수에서는 로컬변수와 함수파라미터에 접근 못하므로 final로 고쳐야한다
           //참조 :  https://dreamaz.tistory.com/259
           Log.d(TAG, "i is " + String.valueOf(i));
//            ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_main, container, false);
//            final int finalJ = j;
           GlobalObject.washers[currentDormId - 1][i].setButton((Button) findViewById(getResources().getIdentifier("washer" + String.valueOf(i + 1), "id", getPackageName())));
           GlobalObject.washers[currentDormId - 1][i].setButtonListener(new View.OnClickListener() {
               public void onClick(View v) {
                   onclick_washer(v, currentDormId, GlobalObject.washers[currentDormId - 1][finalI].getId());
               }
           });
       }


        /*intent에서 안넘어온 값을 get할시에 int -> default값, String -> null*/



        for (int i = 0; i < GlobalObject.washerNum; i++){
            dormButton[i] = (TextView) findViewById(getResources().getIdentifier("dorm" + String.valueOf(i+1), "id", getPackageName()));
            final int finalI = i;//익명함수에서는 로컬변수와 함수파라미터에 접근 못하므로 final로 고쳐야한다
            //참조 :  https://dreamaz.tistory.com/259
            Log.d(TAG, "i is " + String.valueOf(i));
//            ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_main, container, false);
            dormButton[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    currentDormId = finalI + 1;
                    Toast.makeText(getApplicationContext(), "dorm " + String.valueOf(finalI+1) + " 선택", Toast.LENGTH_SHORT).show();
//                    setContentView(R.layout.activity_main);
                    DrawerLayout sideLayout = (DrawerLayout) findViewById(R.id.drawer_layout);//내가 지정한 id가 아님 그냥 drawer_layout가 기본인듯
                    TextView dormText = findViewById(R.id.usr_dorm);
                    dormText.setText(GlobalObject.dorm_name + String.valueOf(currentDormId));
                    sideLayout.closeDrawers();
                }
            });
        }



        if (hour != -1 && minute != -1){
//            for(int i = 0; i< washerNum; i++){
//                Log.d(TAG, "busy check" + String.valueOf(i+1) + String.valueOf(washers[i].isBusy()));
//            }
            //Login Activity에서 넘어오는 것을 대비하여 체크해줘야한다
            //세탁기 정보가 설정됨
            int left_minute = hour * 60 + minute;
            long destiny_time_millis = System.currentTimeMillis() + left_minute * 60 *1000;
//            GlobalObject.washers[currentDormId -1][washerId-1].getImformFromDatabase();
//            g = new GlobalObject();
//            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
//            g.updateImformFromDatabase(queue);
            if (GlobalObject.washers[currentDormId -1][washerId-1].isBusy()){
                //세탁기 이미 돌아가고 있는중
                Toast.makeText(getApplicationContext(), "이미 다른 사용자가 사용중인 세탁기입니다.", Toast.LENGTH_LONG).show();
            }
            else {
                //세탁기 목표 시간 설정
                {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    //Toast.makeText(getApplicationContext(), "insert success", Toast.LENGTH_SHORT).show();
                                    // Intent intent = new Intent(InputTime.this, MainActivity.class);
                                    // startActivity(intent);
                                } else {
                                    //Toast.makeText(getApplicationContext(), "insert fail", Toast.LENGTH_SHORT).show();
//                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    setTimeRequest settime = new setTimeRequest("d" + Integer.toString(currentDormId) + "w" + Integer.toString(washerId), Long.toString(destiny_time_millis), "busy", responseListener);
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    queue.add(settime);
                }

                {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    //Toast.makeText(getApplicationContext(), "insert success", Toast.LENGTH_SHORT).show();
                                    // Intent intent = new Intent(InputTime.this, MainActivity.class);
                                    // startActivity(intent);
                                } else {
                                    //Toast.makeText(getApplicationContext(), "insert fail", Toast.LENGTH_SHORT).show();
//                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    inputLogRequest setlog = new inputLogRequest(GlobalObject.usr_id, "d" + Integer.toString(currentDormId) + "w" + Integer.toString(washerId), responseListener);
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    queue.add(setlog);
                }


//                boolean success = GlobalObject.washers[currentDormId -1][washerId-1].updateImformToDatabase(true, destiny_time_millis, GlobalObject.usr_id, false);
                TextView changed_washer_time = (TextView) findViewById(getResources().getIdentifier("washerLeftTime" + String.valueOf(washerId), "id", getPackageName()));
                Log.d(TAG, String.valueOf(washerId));
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
        }
        else{
            //LoginActivity에서 넘어옴

        }

        final Handler handler = new Handler(){
            @SuppressLint("HandlerLeak")
            public void handleMessage(Message msg){
                // 원래 하려던 동작 (UI변경 작업 등)
                //타이머 갱신
                Log.d(TAG, String.valueOf(System.currentTimeMillis()));


                g = new GlobalObject();
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                g.updateImformFromDatabase(queue);
                for(int i = 0; i<GlobalObject.dormNum; i++) {
                    for (int j = 0; j < GlobalObject.washerNum; j++) {
                        Log.d(TAG, "is wash done" + String.valueOf(i + 1) + String.valueOf(GlobalObject.washers[i][j].isWashDone()));
                        long left_time_sec = (GlobalObject.washers[i][j].getDestiny_millis_time() - System.currentTimeMillis()) / 1000;
                        int left_hour = (int) left_time_sec / 3600;
                        int left_minute = (int) left_time_sec / 60 - left_hour * 60;
                        int left_sec = (int) left_time_sec - left_hour * 3600 - left_minute * 60;
                        if (GlobalObject.washers[i][j].isBusy()) {
                            Log.d(TAG, String.valueOf(i + 1) + "is busy");

                            Log.d(TAG, "현재 : " + String.valueOf(System.currentTimeMillis()) + "  destiny : " + GlobalObject.washers[i][j].getDestiny_millis_time() + "남은 sec : " + left_time_sec);
                            if (left_time_sec < 0) {
                                //사용완료 전송
                                {
                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                boolean success = jsonObject.getBoolean("success");
                                                if (success) {
//                                                    Toast.makeText(getApplicationContext(), "insert success", Toast.LENGTH_SHORT).show();
                                                    // Intent intent = new Intent(InputTime.this, MainActivity.class);
                                                    // startActivity(intent);
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "insert fail", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };
                                    long wash_done_over_millis = GlobalObject.washers[i][j].getDestiny_millis_time();//1분지나면 다시 사용완료하게 할건데 그냥 여기선 그대로 넣고 및에서 leftsec < -60이런식으로 처리할거임
                                    setTimeRequest settime = new setTimeRequest("d" + Integer.toString(i+1) + "w" + Integer.toString(j+1), Long.toString(wash_done_over_millis), "done", responseListener);
                                    RequestQueue queue_ = Volley.newRequestQueue(MainActivity.this);
                                    queue_.add(settime);
                                }
                            }
                            else {
                                if(i == currentDormId -1) {
                                    String left_time_str = "남은시간 : " + String.valueOf(left_hour) + ":" + String.valueOf(left_minute) + ":" + String.valueOf(left_sec);
                                    TextView changed_washer_time = (TextView) findViewById(getResources().getIdentifier("washerLeftTime" + String.valueOf(j + 1), "id", getPackageName()));
                                    changed_washer_time.setText(left_time_str);
                                }
                            }
                        }
                        else if (GlobalObject.washers[i][j].isWashDone()) {
                            //busy하지 않고 wash가 done이면
                            if(i == currentDormId -1) {
                                long over_time_sec = -left_time_sec;
                                int over_hour = (int) over_time_sec / 3600;
                                int over_minute = (int) over_time_sec / 60 - over_hour * 60;
                                int over_sec = (int) over_time_sec - over_hour * 3600 - over_minute * 60;

                                if (over_time_sec>60){
                                    //다시 사용가능으로 변경
                                    {
                                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    boolean success = jsonObject.getBoolean("success");
                                                    if (success) {
//                                                        Toast.makeText(getApplicationContext(), "insert success", Toast.LENGTH_SHORT).show();
                                                        // Intent intent = new Intent(InputTime.this, MainActivity.class);
                                                        // startActivity(intent);
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "insert fail", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        };
//                                        long wash_done_over_millis = GlobalObject.washers[i][j].getDestiny_millis_time() + 60*1000;//1분지나면 다시 사용완료
                                        setTimeRequest settime = new setTimeRequest("d" + Integer.toString(i+1) + "w" + Integer.toString(j+1), Long.toString(0), "able", responseListener);
                                        RequestQueue queue_ = Volley.newRequestQueue(MainActivity.this);
                                        queue_.add(settime);
                                    }
                                }
                                else{
                                    String over_time_str = "세탁완료 (+" + String.valueOf(over_hour) + ":" + String.valueOf(over_minute) + ":" + String.valueOf(over_sec) + ")";
                                    TextView changed_washer_time = (TextView) findViewById(getResources().getIdentifier("washerLeftTime" + String.valueOf(j + 1), "id", getPackageName()));
                                    //Button btn = (Button)findViewById(R.id.OK_bt);
                                    //btn.setEnabled(true);

                                    changed_washer_time.setText(over_time_str);
                                }
                            }
                        }
                        else{
                            if(i == currentDormId -1){
                                TextView changed_washer_time = (TextView) findViewById(getResources().getIdentifier("washerLeftTime" + String.valueOf(j + 1), "id", getPackageName()));
                                //Button btn = (Button)findViewById(R.id.OK_bt);
                                //btn.setEnabled(true);
                                changed_washer_time.setText("사용가능");
                            }
                        }

                    }
                }
            }
        };

        TimerTask validate_washer_time = new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG,"timer run");
                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);

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
