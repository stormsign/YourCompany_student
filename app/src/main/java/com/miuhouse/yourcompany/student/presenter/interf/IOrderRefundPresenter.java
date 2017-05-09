package com.miuhouse.yourcompany.student.presenter.interf;

/**
 * Created by khb on 2016/9/18.
 */
public interface IOrderRefundPresenter {
    void submitRefund(String orderId, String reason, String remark);
}
