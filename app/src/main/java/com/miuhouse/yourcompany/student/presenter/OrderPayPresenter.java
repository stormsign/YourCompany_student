package com.miuhouse.yourcompany.student.presenter;

import android.app.Activity;

import com.miuhouse.yourcompany.student.interactor.OrderInteractor;
import com.miuhouse.yourcompany.student.interactor.PayInteractor;
import com.miuhouse.yourcompany.student.interactor.interf.IOrderInteractor;
import com.miuhouse.yourcompany.student.interactor.interf.IPayInteractor;
import com.miuhouse.yourcompany.student.listener.OnLoadCallBack;
import com.miuhouse.yourcompany.student.presenter.interf.IOrderPayPresenter;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IOrderPayActivity;
import com.miuhouse.yourcompany.student.view.widget.ViewOverrideManager;

/**
 * Created by khb on 2016/9/7.
 */
public class OrderPayPresenter implements IOrderPayPresenter {
    private IOrderPayActivity payActivity;
    private IPayInteractor payInteractor;
    private IOrderInteractor orderInteractor;

    public OrderPayPresenter(IOrderPayActivity activity) {
        this.payActivity = activity;
        payInteractor = new PayInteractor();
        orderInteractor = new OrderInteractor();
    }

    @Override
    public void payInAlipay(final Activity activity, String orderId) {
        payInteractor.payInAlipay(activity, orderId, new OnLoadCallBack() {
            @Override
            public void onPreLoad() {
                payActivity.payOnGoing();
            }

            @Override
            public void onLoadSuccess(Object data) {

            }

            @Override
            public void onLoadFailed(String msg) {
                payActivity.payOver(msg);
            }
        });
    }

    @Override
    public void payInWeixin(Activity activity, String orderId) {
        payInteractor.payInWeixin(activity, orderId, new OnLoadCallBack() {
            @Override
            public void onPreLoad() {
                payActivity.payOnGoing();
            }

            @Override
            public void onLoadSuccess(Object data) {

            }

            @Override
            public void onLoadFailed(String msg) {
                payActivity.payOver(msg);
            }
        });
    }

    @Override
    public void getOrderInfo(String orderId) {
        orderInteractor.getOrderInfo(orderId, new OnLoadCallBack() {
            @Override
            public void onPreLoad() {
                payActivity.showLoading(null);
            }

            @Override
            public void onLoadSuccess(Object data) {
                payActivity.hideLoading();
                OrderInteractor.OrderListBean bean
                        = (OrderInteractor.OrderListBean) data;
                if (bean.getCode() == 1) {
                    payActivity.onTokenExpired();
                    return;
                }
                if (null != bean) {
                    if (bean.getCode() == 0) {
                        if (null != bean.getOrderList()) {
                            payActivity.showOrderInfo(bean.getOrderList());
                        } else {
//                        payActivity.showError("无数据");
                        }
                    } else {
//                    payActivity.showError("请求失败"+bean.getMsg());
                    }
                }
            }

            @Override
            public void onLoadFailed(String msg) {
                payActivity.hideLoading();
                payActivity.showError(ViewOverrideManager.NO_NETWORK);
            }
        });
    }
}
