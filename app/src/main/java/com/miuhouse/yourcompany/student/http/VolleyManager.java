package com.miuhouse.yourcompany.student.http;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.miuhouse.yourcompany.student.application.App;
import com.miuhouse.yourcompany.student.http.request.GsonRequest;
import com.miuhouse.yourcompany.student.http.request.StringRequest;
import com.miuhouse.yourcompany.student.utils.L;

import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * Volley+OkHttp请求简单封装
 * Created by khb on 2016/5/13.
 */
public class VolleyManager {


    private RequestQueue mRequestQueue;


    //静态内部类单例模式
    private static class Holder {
        private final static VolleyManager INSTANCE = new VolleyManager(App.getContext());
    }

    public static VolleyManager getInstance() {
        return Holder.INSTANCE;
    }

    private VolleyManager(Context context) {
        mRequestQueue = Volley.newRequestQueue(context, new OkHttpStack(new OkHttpClient()));
    }

    /**
     * 添加请求到队列
     *
     * @param request
     * @param <T>
     * @return
     */
    public <T> Request<T> add(Request<T> request) {
        return mRequestQueue.add(request);
    }

    /**
     * 发送返回Gson包装的对象的post请求
     *
     * @param tag
     * @param url
     * @param params
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>
     * @return
     */
    public <T> GsonRequest<T> sendGsonRequest(Object tag, String url, Map<String, Object> params, Class<T> clazz,
                                              Response.Listener<T> listener, Response.ErrorListener errorListener) {
        GsonRequest<T> gsonRequest = new GsonRequest<T>(Request.Method.POST, url, params,
                clazz, listener, errorListener);

        gsonRequest.setTag(tag);
        add(gsonRequest);
        return gsonRequest;
    }

    /**
     * 发送带token验证的返回Gson包装的对象的post请求
     *
     * @param tag
     * @param url
     * @param params
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>
     * @return
     */
    public <T> GsonRequest<T> sendGsonRequest(Object tag, String url, Map<String, Object> params, String token, Class<T> clazz,
                                              Response.Listener<T> listener, Response.ErrorListener errorListener) {
        L.i("TAG", "url=" + url);
        L.i("TAG", "tokenss=" + token);

        GsonRequest<T> gsonRequest = new GsonRequest<T>(Request.Method.POST, url, params,
                token, clazz, listener, errorListener);
        gsonRequest.setTag(tag);
        add(gsonRequest);
        return gsonRequest;
    }

    /**
     * 发送返回jsonString的post请求
     *
     * @param tag
     * @param url
     * @param params
     * @param listener
     * @param errorListener
     * @return
     */
    public StringRequest sendStringRequest(Object tag, String url, Map<String, Object> params,
                                           Response.Listener<String> listener, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, params,
                listener, errorListener);
        stringRequest.setTag(tag);
        add(stringRequest);
        return stringRequest;
    }

    /**
     * 发送带token验证的返回jsonString的post请求
     *
     * @param tag
     * @param url
     * @param params
     * @param listener
     * @param errorListener
     * @return
     */
    public StringRequest sendStringRequest(Object tag, String url, Map<String, Object> params, String token,
                                           Response.Listener<String> listener, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, params, token,
                listener, errorListener);
        stringRequest.setTag(tag);
        add(stringRequest);
        return stringRequest;
    }

    /**
     * 取消带指定tag的请求
     *
     * @param tag
     */
    public void cancel(Object tag) {
        mRequestQueue.cancelAll(tag);
    }


}
