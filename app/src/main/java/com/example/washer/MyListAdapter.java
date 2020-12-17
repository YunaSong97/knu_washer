package com.example.washer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyListAdapter extends BaseAdapter {
    private TextView usedwasher;
    private TextView usedtime;

    private ArrayList<list_item> listViewItemList = new ArrayList<list_item>();

    public MyListAdapter(){

    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.listview_custom, parent, false);
        }

        usedwasher=(TextView) convertView.findViewById(R.id.used_washer);
        usedtime=(TextView) convertView.findViewById(R.id.used_time);

        list_item listitem = listViewItemList.get(position);

        usedwasher.setText(listitem.getWasher_id());
        usedtime.setText(listitem.getUsed_time());

        return convertView;
    }
    public void addItem(String usedwasher, String usedtime){
        list_item item = new list_item();

        item.setWasher_id(usedwasher);
        item.setUsed_time(usedtime);

        listViewItemList.add(item);
    }
}
