package com.miuhouse.yourcompany.student.presenter.interf;

import android.app.Activity;

/**
 * Created by khb on 2016/9/7.
 */
public interface IOrderPayPresenter {
    void payInAlipay(Activity activity, String orderId);
    void payInWeixin(Activity activity, String orderId);
    void getOrderInfo(String orderId);
}
