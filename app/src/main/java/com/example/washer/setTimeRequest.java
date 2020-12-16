package com.example.washer;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class setTimeRequest extends StringRequest{
    final static private String URL = "http://moapp.dothome.co.kr/setWasher.php";
    private Map<String, String> map;
    public setTimeRequest(String washer_id, String destinyTime, String washer_state, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);

        map = new HashMap<>();

        map.put("washer_id", washer_id);
        map.put("destinyTime", destinyTime);
        map.put("washer_state", washer_state);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}