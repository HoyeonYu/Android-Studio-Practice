package com.jungmin.registerloginexample;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ChangePassRequest extends StringRequest {

    final static private  String URL = "http://pjm9673.dothome.co.kr/UpdatePass.php";
    private Map<String, String> map;

    public ChangePassRequest(String userID, String userPassword, Response.Listener<String> listener){
        super(Request.Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID", userID);
        map.put("userPassword", userPassword);

    }

    protected Map<String, String> getParams() throws AuthFailureError{
        return map;
    }
}
