package com.miuhouse.yourcompany.student.interactor;

import android.app.Activity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.yourcompany.student.application.App;
import com.miuhouse.yourcompany.student.http.VolleyManager;
import com.miuhouse.yourcompany.student.interactor.interf.IPayInteractor;
import com.miuhouse.yourcompany.student.listener.OnLoadCallBack;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.utils.SPUtils;
import com.miuhouse.yourcompany.student.view.ui.activity.OrderPayActivity;
import com.pingplusplus.android.Pingpp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by khb on 2016/9/7.
 */
public class PayInteractor implements IPayInteractor {
    @Override
    public void payInAlipay(final Activity activity, String orderId, final OnLoadCallBack onLoadCallBack) {
        onLoadCallBack.onPreLoad();
        String urlPath = Constants.URL_VALUE + "getCharge";
        Map<String, Object> map = new HashMap<>();
        map.put("orderInfoId", orderId);
        map.put("parentId", App.getInstance().getParentId());
        map.put("channel", Constants.ALIPAY);
        VolleyManager.getInstance().sendStringRequest(null, urlPath, map, SPUtils.getData(SPUtils.TOKEN, null),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            int code = jsonObj.getInt("code");
                            String msg = jsonObj.getString("msg");
                            if (code == 0) {
                                if (jsonObj.has("charge")) {
                                    String charge = jsonObj.getJSONObject("charge").toString();
                                    if (null == charge) {
                                        onLoadCallBack.onLoadFailed(OrderPayActivity.PAYERROR_CHARGEERROR);
                                        return;
                                    }
                                    L.i("PAY", "charge=" + charge);
                                    Pingpp.createPayment(activity, charge);
                                }else {
                                    onLoadCallBack.onLoadFailed(OrderPayActivity.PAYERROR_NOCHARGE);
                                }
                            }else if (code == 2){       //订单超时
                                onLoadCallBack.onLoadFailed(OrderPayActivity.PAYERROR_ORDERTIMEOUT);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        L.w(error);
                        onLoadCallBack.onLoadFailed(OrderPayActivity.PAYERROR_NONETWORK);
                    }
        });
    }

    @Override
    public void payInWeixin(final Activity activity, String orderId, final OnLoadCallBack onLoadCallBack) {
        onLoadCallBack.onPreLoad();
        String urlPath = Constants.URL_VALUE + "getCharge";
        Map<String, Object> map = new HashMap<>();
        map.put("orderInfoId", orderId);
        map.put("parentId", App.getInstance().getParentId());
        map.put("channel", Constants.WEIXIN);
        VolleyManager.getInstance().sendStringRequest(null, urlPath, map, SPUtils.getData(SPUtils.TOKEN, null), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    int code = jsonObj.getInt("code");
                    String msg = jsonObj.getString("msg");
                    if (code == 0) {
                        if (jsonObj.has("charge")) {
                            String charge = jsonObj.getString("charge");
                            if (null == charge) {
                                onLoadCallBack.onLoadFailed(OrderPayActivity.PAYERROR_CHARGEERROR);
                                return;
                            }
                            L.i("PAY", "charge=" + charge);
                            Pingpp.createPayment(activity, charge);
                        }else {
                            onLoadCallBack.onLoadFailed(OrderPayActivity.PAYERROR_NOCHARGE);
                        }
                    }else if (code == 2){       //订单超时
                        onLoadCallBack.onLoadFailed(OrderPayActivity.PAYERROR_ORDERTIMEOUT);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                L.w(error);
                onLoadCallBack.onLoadFailed(OrderPayActivity.PAYERROR_NONETWORK);
            }
        });
    }

}
