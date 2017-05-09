package com.miuhouse.yourcompany.student.presenter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.yourcompany.student.interactor.MyOrderInfoMessageInteractor;
import com.miuhouse.yourcompany.student.interactor.interf.IMyOrderInfoMessageInteractor;
import com.miuhouse.yourcompany.student.model.MyOrderInfoMessageBean;
import com.miuhouse.yourcompany.student.presenter.interf.IMyOrderInfoMessagePresenter;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IUserInformationView;
import com.miuhouse.yourcompany.student.view.ui.fragment.interf.IMyOrderInfoMessageView;

/**
 * Created by kings on 10/11/2016.
 */
public class MyOrderInfoMessagePresenter implements IMyOrderInfoMessagePresenter {

    private IMyOrderInfoMessageView iMyOrderInfoMessageView;

    private IMyOrderInfoMessageInteractor iMyOrderInfoMessageInteractor;

    public MyOrderInfoMessagePresenter(IMyOrderInfoMessageView iMyOrderInfoMessageView) {
        this.iMyOrderInfoMessageView = iMyOrderInfoMessageView;
        iMyOrderInfoMessageInteractor = new MyOrderInfoMessageInteractor();
    }

    @Override
    public void getMyOrderInfoMessage() {
        iMyOrderInfoMessageInteractor.myOrderInfoMessage(new Response.Listener<MyOrderInfoMessageBean>() {
            @Override
            public void onResponse(MyOrderInfoMessageBean response) {
                if (response.getCode() == 1) {
                    iMyOrderInfoMessageView.onTokenExpired();
                } else {
                    L.i("TAG", "response=" + response.getMsg());
                    iMyOrderInfoMessageView.getMyOrderInfoMessage(response);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }
}
