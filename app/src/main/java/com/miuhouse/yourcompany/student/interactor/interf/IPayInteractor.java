package com.miuhouse.yourcompany.student.interactor.interf;

import android.app.Activity;

import com.miuhouse.yourcompany.student.listener.OnLoadCallBack;

/**
 * Created by khb on 2016/9/7.
 */
public interface IPayInteractor {
    void payInAlipay(Activity activity, String orderId, OnLoadCallBack onLoadCallBack);
    void payInWeixin(Activity activity, String orderId, OnLoadCallBack onLoadCallBack);
}
