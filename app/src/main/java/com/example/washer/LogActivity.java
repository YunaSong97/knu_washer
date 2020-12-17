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
    private MyListAdapter adapter;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        listview = findViewById(R.id.log_view);

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
                        count++;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        getLogRequest GetLogRequest = new getLogRequest(GlobalObject.usr_id, responseListener);
        RequestQueue requestQueue = Volley.newRequestQueue(LogActivity.this);
        requestQueue.add(GetLogRequest);


        for(int i=0;i<count;i++){
            adapter.addItem(washer_id[i], start_time[i]);
        }

        /*adapter.addItem("누리관1층 1번", "2020-12-17-12:12:12");
        adapter.addItem("누리관1층 1번", "2020-12-17-12:12:12");
        adapter.addItem("누리관1층 1번", "2020-12-17-12:12:12");
        adapter.addItem("누리관1층 1번", "2020-12-17-12:12:12");*/

        adapter.notifyDataSetChanged();
    }
}