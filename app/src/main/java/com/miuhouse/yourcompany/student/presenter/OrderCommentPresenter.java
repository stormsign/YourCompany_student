package com.miuhouse.yourcompany.student.presenter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.yourcompany.student.interactor.OrderEvaluateInteractor;
import com.miuhouse.yourcompany.student.interactor.interf.IOrderEvaluateInteractor;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.presenter.interf.IOrderCommentPresenter;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IOrderEvaluateView;
import com.miuhouse.yourcompany.student.view.widget.ViewOverrideManager;

/**
 * Created by kings on 8/31/2016.
 */
public class OrderCommentPresenter implements IOrderCommentPresenter {

    private IOrderEvaluateView orderEvaluateView;
    private IOrderEvaluateInteractor orderEvaluateInteractor;

    public OrderCommentPresenter(IOrderEvaluateView orderEvaluateView) {
        this.orderEvaluateView = orderEvaluateView;
        orderEvaluateInteractor = new OrderEvaluateInteractor();
    }

    @Override
    public void commitOrderComment(String orderInfold, String evaluateRank, String evaluateContent) {
        orderEvaluateView.showWaitDialog("请稍后...");
        orderEvaluateInteractor.commitOrderEvaluate(orderInfold, evaluateRank, evaluateContent, new Response.Listener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                orderEvaluateView.hideWaitDialog();
                if (response.getCode() == 1) {
                    orderEvaluateView.onTokenExpired();
                } else {
                    orderEvaluateView.showLoading(response.getMsg());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                orderEvaluateView.netError();
            }
        });
    }
}
