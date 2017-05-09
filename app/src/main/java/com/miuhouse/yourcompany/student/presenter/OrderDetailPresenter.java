package com.miuhouse.yourcompany.student.presenter;


import com.miuhouse.yourcompany.student.interactor.OrderInteractor;
import com.miuhouse.yourcompany.student.interactor.interf.IOrderInteractor;
import com.miuhouse.yourcompany.student.listener.OnLoadCallBack;
import com.miuhouse.yourcompany.student.presenter.interf.IOrderDetailPresenter;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IOrderDetailActivity;
import com.miuhouse.yourcompany.student.view.widget.ViewOverrideManager;

/**
 * Created by khb on 2016/7/21.
 */
public class OrderDetailPresenter implements IOrderDetailPresenter {


    private IOrderDetailActivity activity;
    private IOrderInteractor interactor;

    public OrderDetailPresenter(IOrderDetailActivity activity) {
        this.activity = activity;
        interactor = new OrderInteractor();
    }

    @Override
    public void getOrderDetail(String teacherId, String orderInfoId) {
        activity.showLoading(null);
        interactor.getOrderInfo(orderInfoId, new OnLoadCallBack() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onLoadSuccess(Object data) {
                activity.hideLoading();
                OrderInteractor.OrderListBean bean
                        = (OrderInteractor.OrderListBean) data;
                if (bean.getCode() == 1) {
                    activity.onTokenExpired();
                    return;
                }
                if (null != bean) {
                    if (bean.getCode() == 0) {
                        if (null != bean.getOrderList()) {
                            activity.fillView(bean.getOrderList());
                        } else {
//                        activity.showError("无数据");
                        }
                    } else {
//                    activity.showError("请求失败"+bean.getMsg());
                    }
                }
            }

            @Override
            public void onLoadFailed(String msg) {
//        activity.showError(msg);
                activity.showError(ViewOverrideManager.NO_NETWORK);
            }
        });
    }

//    @Override
//    public void beginClass(String teacherId, String orderInfoId) {
//        activity.beforeBeginClass();
//        interactor.beginClass(teacherId, orderInfoId, new OnLoadCallBack() {
//            @Override
//            public void onPreLoad() {
//                activity.showLoading(null);
//            }
//
//            @Override
//            public void onLoadSuccess(Object data) {
//                    String resp = (String) data;
//                    try {
//                        JSONObject jObject = new JSONObject(resp);
//                        if (jObject.getInt("code") == 0) {
////                    activity.showLoading();
//                            activity.afterBeginClass();
//                        } else {
////                    activity.showError(jObject.getString("msg"));
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//            }
//
//            @Override
//            public void onLoadFailed(String msg) {
//
//            }
//        });
//    }

}
