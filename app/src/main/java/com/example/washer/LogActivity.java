package com.example.washer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class LogActivity extends AppCompatActivity {
    private ListView listview;
    private MyListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        adapter = new MyListAdapter();

        listview=(ListView)findViewById(R.id.log_view);
        listview.setAdapter(adapter);

        adapter.addItem("2020-12-17 12:12:12", "누리관1층 1번");
        adapter.addItem("2020-12-17 12:12:12", "누리관1층 1번");
        adapter.addItem("2020-12-17 12:12:12", "누리관1층 3번");
        adapter.addItem("2020-12-17 12:12:12", "누리관1층 4번");

        adapter.notifyDataSetChanged();
    }
}