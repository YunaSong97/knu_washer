package com.example.washer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class getTime extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success){ //가져오기에 성공한 경우
                        String[] washer_id = new String[1000];
                        String[] washer_state = new String[1000];
                        String[] destinyTime = new String[1000];

                        JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                        for(int i=0; i<jsonArray.length(); i++){
                            JSONObject one_jsonObject = jsonArray.getJSONObject(i);
                            washer_id[i] = one_jsonObject.getString("washer_id")+i;
                            washer_state[i] = one_jsonObject.getString("washer_state")+i;
                            destinyTime[i] = one_jsonObject.getString("destinyTime")+i;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        getTimeRequest GetTimeRequest = new getTimeRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(getTime.this);
        queue.add(GetTimeRequest);
    }
}
