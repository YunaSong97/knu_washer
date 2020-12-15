package com.example.washer;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class getTime(String dataObject) {
    private void receiveArray(String dataObject){
        String[] washer_id = new String[16];
        String[] destinyTime = new String[16];
        try {
            // String 으로 들어온 값 JSONObject 로 1차 파싱
            JSONObject wrapObject = new JSONObject(dataObject);

            // JSONObject 의 키 "list" 의 값들을 JSONArray 형태로 변환
            JSONArray jsonArray = new JSONArray(wrapObject.getString("data"));
            for(int i = 0; i < jsonArray.length(); i++){
                // Array 에서 하나의 JSONObject 를 추출
                JSONObject dataJsonObject = jsonArray.getJSONObject(i);
                // 추출한 Object 에서 필요한 데이터를 표시할 방법을 정해서 화면에 표시
                // 필자는 RecyclerView 로 데이터를 표시 함
                washer_id[i]= dataJsonObject.getString("washer_id")+i;
                destinyTime[i] = dataJsonObject.getString("destinyTime")+i;
            }
            // Recycler Adapter 에서 데이터 변경 사항을 체크하라는 함수 호출
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
