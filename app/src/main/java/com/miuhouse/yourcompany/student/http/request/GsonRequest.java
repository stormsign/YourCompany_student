package com.miuhouse.yourcompany.student.http.request;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.utils.Util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by khb on 2016/5/13.
 */
public class GsonRequest<T> extends Request<T> {

    private final Response.Listener<T> mListener;
    private TypeToken<T> typeToken;
    private Map<String, Object> params;
    private Class<T> clazz;
    private Gson mGson;

    private String token;

    //get
    public GsonRequest(int method, String url, Class<T> clazz,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mGson = new Gson();
        this.clazz = clazz;
        this.mListener = listener;
        setRetryPolicy(new DefaultRetryPolicy(Constants.TIMEOUT * 1000, Constants.MAX_RETRIES, 1.0f));
    }

    //post
    public GsonRequest(int method, String url, Map<String, Object> params, Class<T> clazz,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mGson = new Gson();
        this.params = params;
        this.clazz = clazz;
        this.mListener = listener;
        setRetryPolicy(new DefaultRetryPolicy(Constants.TIMEOUT * 1000, Constants.MAX_RETRIES, 1.0f));
    }

    //post
    public GsonRequest(int method, String url, Map<String, Object> params, TypeToken<T> typeToken,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mGson = new Gson();
        this.params = params;
        this.typeToken = typeToken;
        this.mListener = listener;
        setRetryPolicy(new DefaultRetryPolicy(Constants.TIMEOUT * 1000, Constants.MAX_RETRIES, 1.0f));
    }

    //post 需要token验证 的请求
    public GsonRequest(int method, String url, Map<String, Object> params, String token, Class<T> clazz,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mGson = new Gson();
        this.params = params;
        this.clazz = clazz;
        this.mListener = listener;
        this.token = token;
        setRetryPolicy(new DefaultRetryPolicy(Constants.TIMEOUT * 1000, Constants.MAX_RETRIES, 1.0f));
    }

    //post 需要token验证的请求
    public GsonRequest(int method, String url, Map<String, Object> params, String token, TypeToken<T> typeToken,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mGson = new Gson();
        this.params = params;
        this.typeToken = typeToken;
        this.mListener = listener;
        this.token = token;
        setRetryPolicy(new DefaultRetryPolicy(Constants.TIMEOUT * 1000, Constants.MAX_RETRIES, 1.0f));
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> map = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(Constants.DEVICETYPE, Constants.DEVICETYPE_VALUE);
            jsonObject.put(Constants.VERSIONCODE, Constants.VERSIONCODE_VALUE);
            jsonObject.put(Constants.IMEI, Constants.IMEI_VALUE);

            if (params != null) {
                Set<Map.Entry<String, Object>> set = params.entrySet();
                for (Map.Entry<String, Object> entry : set)
                    jsonObject.put(entry.getKey(), entry.getValue());

            }
            String json = jsonObject.toJSONString();
            L.i("请求：" + json);
            String md5 = null;
            if (!Util.isEmpty(token)) {
                md5 = Util.md5String(Constants.DEVICETYPE_VALUE
                        + Constants.IMEI_VALUE
                        + Constants.VERSIONCODE_VALUE
                        + Constants.APPKEY
                        + token);
                L.i("TAG","GsonRequest-token="+token);

            } else {

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
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String charset = HttpHeaderParser.parseCharset(response.headers);
            Cache.Entry entry = HttpHeaderParser.parseCacheHeaders(response);
            String jsonString = new String(response.data, charset);
            L.i("jsonString=" + jsonString);
            if (typeToken == null) {
                T fromJson = mGson.fromJson(jsonString, clazz);
                return Response.success(fromJson, entry);
            } else {
                Object fromJson = mGson.fromJson(jsonString, typeToken.getType());
                return (Response<T>) Response.success(mGson.fromJson(jsonString, typeToken.getType())
                        , entry);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
          mListener.onResponse(response);
    }
}
