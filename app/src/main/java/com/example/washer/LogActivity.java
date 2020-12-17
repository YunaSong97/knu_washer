package com.example.washer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LogActivity extends AppCompatActivity {
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        listview = findViewById(R.id.log_view);

        final MyListAdapter adapter;
        adapter = new MyListAdapter();
        listview=(ListView)findViewById(R.id.log_view);
        listview.setAdapter(adapter);

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
                        adapter.addItem(washer_id[i], start_time[i]);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        };

        getLogRequest GetLogRequest = new getLogRequest(GlobalObject.usr_id, responseListener);
        RequestQueue requestQueue = Volley.newRequestQueue(LogActivity.this);
        requestQueue.add(GetLogRequest);


    }
}