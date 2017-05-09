package com.miuhouse.yourcompany.student.interactor;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.yourcompany.student.db.AccountDBTask;
import com.miuhouse.yourcompany.student.http.VolleyManager;
import com.miuhouse.yourcompany.student.interactor.interf.IOrderInteractor;
import com.miuhouse.yourcompany.student.listener.OnLoadCallBack;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.model.OrderEntity;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by khb on 2016/8/31.
 */
public class OrderInteractor implements IOrderInteractor {

    @Override
    public void getOrderInfo(String orderInfoId, final OnLoadCallBack onLoadCallBack) {
        onLoadCallBack.onPreLoad();
        String url = Constants.URL_VALUE + "orderInfo";
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", AccountDBTask.getAccount().getId());
        params.put("orderInfoId", orderInfoId);
        VolleyManager.getInstance().sendGsonRequest(null, url, params,
                SPUtils.getData(SPUtils.TOKEN, null),
                OrderListBean.class, new Response.Listener<OrderListBean>() {
                    @Override
                    public void onResponse(OrderListBean response) {
                        if (null != response.getOrderList()) {
                            onLoadCallBack.onLoadSuccess(response);
                        } else {
                            onLoadCallBack.onLoadFailed(null);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onLoadCallBack.onLoadFailed(error.toString());
                    }
                });
    }

    @Override
    public void confirmOrder(String parentId, String description, String majorDemand,
                             String minorDemand, String starLevel, long classBeginTimePromise,
                             String lesson, String mobile,
                             double singlePrice, double extraPrice, double amount,
                             String sex, String province, String city, String district, String street,
                             String houseNumber, double lon, double lat,
                             final OnLoadCallBack onLoadCallBack) {
        onLoadCallBack.onPreLoad();
        String url = Constants.URL_VALUE + "addOrderInfo";
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", parentId);
        params.put("description", description);
        params.put("majorDemand", majorDemand);
        params.put("minorDemand", minorDemand);
        params.put("gradeLevel", starLevel);
        params.put("classBeginTimePromise", classBeginTimePromise);
        params.put("lesson", lesson);
        params.put("mobile", mobile);
        params.put("price", singlePrice);
        params.put("pricePlus", extraPrice);
        params.put("amount", amount);
        params.put("sex", sex);
        params.put("province", province);
        params.put("city", city);
        params.put("district", district);
        params.put("street", street);
        params.put("houseNumber", houseNumber);
        params.put("lon", lon);
        params.put("lat", lat);
        params.put("orderType", "1");
        VolleyManager.getInstance().sendStringRequest(null, url, params, SPUtils.getData(SPUtils.TOKEN, ""),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onLoadCallBack.onLoadSuccess(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onLoadCallBack.onLoadFailed(error.toString());
                    }
                });
    }

    @Override
    public void confirmOrder(String parentId, String description, String majorDemand,
                             String minorDemand, String starLevel, long classBeginTimePromise,
                             String lesson, String mobile, double price, double amount, String sex,
                             String province, String city, String district, String street,
                             String houseNumber, double lon, double lat,
                             String orderType,
                             String lessonDay, ArrayList<Integer> weekLoop, long classEndTimePromise,
                             final OnLoadCallBack onLoadCallBack) {
        onLoadCallBack.onPreLoad();
        String url = Constants.URL_VALUE + "addOrderInfo";
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", parentId);
        params.put("description", description);
        params.put("majorDemand", majorDemand);
        params.put("minorDemand", minorDemand);
        params.put("starLevel", starLevel);
        params.put("classBeginTimePromise", classBeginTimePromise);
        params.put("lesson", lesson);
        params.put("mobile", mobile);
        params.put("amount", amount);
        params.put("price", price);
        params.put("sex", sex);
        params.put("province", province);
        params.put("city", city);
        params.put("district", district);
        params.put("street", street);
        params.put("houseNumber", houseNumber);
        params.put("lon", lon);
        params.put("lat", lat);
        params.put("orderType", orderType);
        params.put("lessonDay", lessonDay);
        params.put("weekLoop", weekLoop);
        params.put("classEndTimePromise", classEndTimePromise);
//        onLoadCallBack.onLoadSuccess("00000000000");
        VolleyManager.getInstance().sendStringRequest(null, url, params, SPUtils.getData(SPUtils.TOKEN, ""),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("TAG","response="+response);

                                onLoadCallBack.onLoadSuccess(response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onLoadCallBack.onLoadFailed(error.toString());
                    }
                });
    }

    public class OrderListBean extends BaseBean {
        OrderEntity orderList;

        public OrderEntity getOrderList() {
            return orderList;
        }

        public void setOrderList(OrderEntity orderList) {
            this.orderList = orderList;
        }
    }

}
