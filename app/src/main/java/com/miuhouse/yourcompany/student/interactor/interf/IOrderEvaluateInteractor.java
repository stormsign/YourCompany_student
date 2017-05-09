package com.miuhouse.yourcompany.student.interactor.interf;

import com.android.volley.Response;

/**
 * Created by kings on 8/31/2016.
 */
public interface IOrderEvaluateInteractor {
    void commitOrderEvaluate(String orderInfoId, String evaluateRank, String evaluateContent, Response.Listener listener, Response.ErrorListener errorListener);
}
