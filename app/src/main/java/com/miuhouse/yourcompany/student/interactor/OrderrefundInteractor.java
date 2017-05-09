package com.miuhouse.yourcompany.student.interactor;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.yourcompany.student.db.AccountDBTask;
import com.miuhouse.yourcompany.student.http.VolleyManager;
import com.miuhouse.yourcompany.student.interactor.interf.IOrderRefundInteractor;
import com.miuhouse.yourcompany.student.listener.OnLoadCallBack;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by khb on 2016/9/18.
 */
public class OrderrefundInteractor implements IOrderRefundInteractor {
    @Override
    public void submitRefundRequest(String orderId, String reason, String remark, final OnLoadCallBack onLoadCallBack) {
        onLoadCallBack.onPreLoad();

        String url = Constants.URL_VALUE + "orderRefund";
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", AccountDBTask.getAccount().getId());
        params.put("orderInfoId", orderId);
        params.put("refundReason", reason);
        params.put("refundRemark", remark);
        VolleyManager.getInstance().sendStringRequest(null, url, params, SPUtils.getData(SPUtils.TOKEN, null),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.getInt("code");
                            String msg = jsonObject.getString("msg");
                            if (code == 0){
                                onLoadCallBack.onLoadSuccess(response);
                            }else {
                                onLoadCallBack.onLoadFailed(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onLoadCallBack.onLoadFailed(null);
                    }
                });
    }
}
