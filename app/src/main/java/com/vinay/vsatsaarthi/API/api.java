package com.vinay.vsatsaarthi.API;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import  com.vinay.vsatsaarthi.utils.res;
import org.json.JSONObject;
public class api {
    public static final String url="https://satellite-detail.onrender.com/satellite";
    public static void postsatellite(String satname,String satlatitude,String satlongitude,String sataltitude,Context context)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonObject =new JSONObject();
        try {
            jsonObject.put("satname",satname);
            jsonObject.put("satlatitude",satlatitude);
            jsonObject.put("satlongitude",satlongitude);
            jsonObject.put("sataltitude",sataltitude);
        }
        catch (Exception e) {
            res.res(context,e.getMessage());
        }
        JsonObjectRequest jsonObjectRequest =new JsonObjectRequest(Request.Method.POST, url + "/postsatellite", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                res.res(context,"Data sent");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                res.res(context,error.getMessage());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public static void updatesatellite(int id,String satname,String satlatitude,String satlongitude,String sataltitude,Context context)
    {
        RequestQueue requestQueue =Volley.newRequestQueue(context);
        JSONObject jsonObject =new JSONObject();
        try {
            jsonObject.put("satname",satname);
            jsonObject.put("satlatitude",satlatitude);
            jsonObject.put("satlongitude",satlongitude);
            jsonObject.put("sataltitude",sataltitude);
        }
        catch (Exception e){
            res.res(context,e.getMessage());
        }
        JsonObjectRequest jsonObjectRequest =new JsonObjectRequest(Request.Method.PUT,url+"/updatesatellite/"+id, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                res.res(context,"Data updated");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                res.res(context,error.getMessage());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    public static void deletesatellite(int id,Context context)
    {
        RequestQueue requestQueue =Volley.newRequestQueue(context);
        StringRequest stringRequest =new StringRequest(Request.Method.DELETE,url + "/deletesatellite/" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                res.res(context,"Data deleted");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                res.res(context,error.getMessage());
            }
        });
        requestQueue.add(stringRequest);
    }
}