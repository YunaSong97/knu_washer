package com.example.washer;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class getLogRequest extends StringRequest {
    // 서버 URL 설정(PHP 파일 연동)
    final static private String URL = "http://moapp.dothome.co.kr/getLog.php";
    private Map<String, String> map;

    public getLogRequest(String usr_num, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("usr_num",usr_num);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
