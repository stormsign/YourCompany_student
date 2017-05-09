package com.miuhouse.yourcompany.student.interactor.interf;

import com.android.volley.Response;
import com.miuhouse.yourcompany.student.model.MyOrderInfoMessageBean;

/**
 * Created by kings on 10/11/2016.
 */
public interface IMyOrderInfoMessageInteractor {
    void myOrderInfoMessage(Response.Listener<MyOrderInfoMessageBean> listener, Response.ErrorListener errorListener);
}
