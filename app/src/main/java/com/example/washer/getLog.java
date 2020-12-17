package com.example.washer;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class getLog extends AppCompatActivity{
    private ListView log_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        log_view = findViewById(R.id.log_view);

        final String[] washer_id = new String[100];
        final String[] usr_num = new String[100];
        final String[] start_time = new String[100];

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        washer_id[i] = jsonObject.getString("washer_id");
                        usr_num[i] = jsonObject.getString("usr_num");
                        start_time[i] = jsonObject.getString("start_time");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

    }
}
