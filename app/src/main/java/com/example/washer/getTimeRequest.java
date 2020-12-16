package com.example.washer;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

public class getTimeRequest extends StringRequest{
    final static private String URL = "http://moapp.dothome.co.kr/getWasher.php";

    public getTimeRequest(Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
    }
}
