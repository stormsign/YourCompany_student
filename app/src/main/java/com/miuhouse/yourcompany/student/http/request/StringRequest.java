package com.miuhouse.yourcompany.student.http.request;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.utils.Util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by khb on 2016/5/13.
 */
public class StringRequest extends Request<String> {

    private Map<String, Object> params;
    private Response.Listener<String> listener;
    private String token ;

    //get
    public StringRequest(int method, String url,
                         Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = listener;
        setRetryPolicy(new DefaultRetryPolicy(Constants.TIMEOUT * 1000, Constants.MAX_RETRIES, 1.0f));
    }

    //post
    public StringRequest(int method, String url, Map<String, Object> params,
                         Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.params = params;
        this.listener = listener;
        setRetryPolicy(new DefaultRetryPolicy(Constants.TIMEOUT * 1000, Constants.MAX_RETRIES, 1.0f));
    }

    //get token验证的请求
    public StringRequest(int method, String url, String token,
                         Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = listener;
        this.token = token;
        setRetryPolicy(new DefaultRetryPolicy(Constants.TIMEOUT * 1000, Constants.MAX_RETRIES, 1.0f));
    }

    //post token验证的请求
    public StringRequest(int method, String url, Map<String, Object> params, String token,
                         Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.params = params;
        this.listener = listener;
        this.token = token;
        setRetryPolicy(new DefaultRetryPolicy(Constants.TIMEOUT * 1000, Constants.MAX_RETRIES, 1.0f));
    }

    @Override
    public Map<String,String> getParams() {
        Map<String, String> map = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.DEVICETYPE, Constants.DEVICETYPE_VALUE);
            jsonObject.put(Constants.VERSIONCODE, Constants.VERSIONCODE_VALUE);
            jsonObject .put(Constants.IMEI, Constants.IMEI_VALUE);
            if (params != null){
                for (String key : params.keySet()){
                    jsonObject.put(key, params.get(key));
                }
            }
            String json = jsonObject.toString();
            L.i("请求："+json);
            String md5 = null;
            if (!Util.isEmpty(token)){
                md5 = Util.md5String(Constants.DEVICETYPE_VALUE
                        + Constants.IMEI_VALUE
                        + Constants.VERSIONCODE_VALUE
                        + Constants.APPKEY
                        + token);
            }else{
                md5 = Util.md5String(Constants.DEVICETYPE_VALUE
                        + Constants.IMEI_VALUE
                        + Constants.VERSIONCODE_VALUE
                        + Constants.APPKEY);
            }
            map.put("md5", md5);
            map.put("transData", json);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return map;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(String response) {
        listener.onResponse(response);
    }
}
