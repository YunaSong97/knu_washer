package com.example.washer;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class inputLogRequest extends StringRequest {

    // 서버 URL 설정(PHP 파일 연동)
    final static private String URL = "http://moapp.dothome.co.kr/inputLog.php";
    private Map<String, String> map;

    public inputLogRequest(String usr_num, String washer_id, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("usr_num",usr_num);
        map.put("washer_id",washer_id);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
