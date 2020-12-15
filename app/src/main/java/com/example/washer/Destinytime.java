package com.example.washer;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Destinytime extends StringRequest {
    // 서버 URL 설정(PHP 파일 연동)
    final static private String URL = "http://moapp.dothome.co.kr/GetTime.php";
    private Map<String, String> map;

    public Destinytime(String usr_id, String usr_password, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("usr_id", usr_id);
        map.put("usr_password",usr_password);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
