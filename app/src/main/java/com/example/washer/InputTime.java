package com.example.washer;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class InputTime extends AppCompatActivity {
    private static final String TAG = "InputTime";
    Button okBtn;
    TextView washerIdTextView;
    EditText hour_edit_text;
    EditText minute_edit_text;
    String hour_str;
    String minute_str;
    int left_hour, left_minute;
    SeekBar seekBar;
    int total_minute;
    int washerId;
    int dormId;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_time);
        //ActionBar ab = getSupportActionBar() ;
        //ab.setTitle("시간 입력");
        //ab.setDisplayHomeAsUpEnabled(true);

        washerIdTextView = (TextView) findViewById(R.id.washer_id_text);

        Intent mainIntent = getIntent();    //intent 수신
        washerId = Objects.requireNonNull(mainIntent.getExtras()).getInt("washerId");
        dormId = Objects.requireNonNull(mainIntent.getExtras()).getInt("dormId");
        washerIdTextView.setText(Integer.toString(dormId) + "기숙사 세탁기 " + Integer.toString(washerId));

        hour_edit_text = (EditText)findViewById(R.id.hour_text);
        minute_edit_text = (EditText)findViewById(R.id.minute_text);

        seekBar = (SeekBar) findViewById(R.id.time_seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // onProgressChange - Seekbar 값 변경될때마다 호출
                Log.d(TAG, String.format("onProgressChanged 값 변경 중 : progress [%d] fromUser [%b]", progress, fromUser));
                int total_minute = seekBar.getProgress() * 5;//0~24를 0~120으로 변환
                hour_edit_text.setText(String.valueOf(total_minute/60));
                minute_edit_text.setText(String.valueOf(total_minute - total_minute/60*60));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // onStartTeackingTouch - SeekBar 값 변경위해 첫 눌림에 호출
                Log.d(TAG, String.format("onStartTrackingTouch 값 변경 시작 : progress [%d]", seekBar.getProgress()));
                int total_minute = seekBar.getProgress() * 5;//0~24를 0~120으로 변환
                hour_edit_text.setText(String.valueOf(total_minute/60));
                minute_edit_text.setText(String.valueOf(total_minute - total_minute/60*60));
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // onStopTrackingTouch - SeekBar 값 변경 끝나고 드래그 떼면 호출
                Log.d(TAG, String.format("onStopTrackingTouch 값 변경 종료: progress [%d]", seekBar.getProgress()));
                total_minute = seekBar.getProgress() * 5;//0~24를 0~120으로 변환
                hour_edit_text.setText(String.valueOf(total_minute/60));
                minute_edit_text.setText(String.valueOf(total_minute - total_minute/60*60));
            }
        });

        okBtn = (Button) findViewById(R.id.OK_bt);
        okBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hour_str = (hour_edit_text).getText().toString();
                minute_str = (minute_edit_text).getText().toString();
                Log.d(TAG,"minute_str : " + minute_str);
                if (!TextUtils.isEmpty(hour_str)){
                    left_hour = Integer.parseInt(hour_str);
                }
                else{
                    left_hour = 0;
                }
                if (!TextUtils.isEmpty(minute_str)){
                    left_minute = Integer.parseInt(minute_str);
                }
                else{
                    left_minute = 0;
                }
                if (left_minute == 0 && left_hour == 0){
                    Toast.makeText(getApplicationContext(), "입력된 값이 없어 자동으로 취소합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    Intent main_act = new Intent( InputTime.this, MainActivity.class);
                    main_act.putExtra("hour",left_hour); /*송신*/
                    main_act.putExtra("minute",left_minute); /*송신*/
                    main_act.putExtra("washerId", washerId); /*송신*/
                    main_act.putExtra("dormId", dormId); /*송신*/

                    startActivity(main_act);
                }

                /*Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            //boolean success = jsonObject.getBoolean("success");
                        } catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                }*/

            }
        });



//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        startActivity(intent);
    }
}