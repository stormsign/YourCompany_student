package com.miuhouse.yourcompany.student.presenter;

import com.miuhouse.yourcompany.student.interactor.OrderrefundInteractor;
import com.miuhouse.yourcompany.student.interactor.interf.IOrderRefundInteractor;
import com.miuhouse.yourcompany.student.listener.OnLoadCallBack;
import com.miuhouse.yourcompany.student.presenter.interf.IOrderRefundPresenter;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IOrderRefundActivity;

/**
 * Created by khb on 2016/9/18.
 */
public class OrderRefundPresenter implements IOrderRefundPresenter {
    private IOrderRefundActivity activity;
    private IOrderRefundInteractor refundInteractor;

    public OrderRefundPresenter(IOrderRefundActivity activity) {
        this.activity = activity;
        this.refundInteractor = new OrderrefundInteractor();
    }

    @Override
    public void submitRefund(String orderId, String reason, String remark) {
        refundInteractor.submitRefundRequest(orderId, reason, remark, new OnLoadCallBack() {
            @Override
            public void onPreLoad() {
                activity.showLoading(null);
            }

            @Override
            public void onLoadSuccess(Object data) {
                activity.hideLoading();
                activity.refundSuccess();
            }

            @Override
            public void onLoadFailed(String msg) {
                activity.hideLoading();
                activity.refundFailed(msg);
            }
        });
    }
}
