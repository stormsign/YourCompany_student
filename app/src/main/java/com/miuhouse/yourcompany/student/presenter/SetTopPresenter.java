package com.miuhouse.yourcompany.student.presenter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.yourcompany.student.interactor.SetTopInteractor;
import com.miuhouse.yourcompany.student.interactor.interf.ISetTop;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.presenter.interf.ISetTopPresenter;
import com.miuhouse.yourcompany.student.view.ui.base.BaseView;

/**
 * Created by kings on 1/11/2017.
 */
public class SetTopPresenter implements ISetTopPresenter {

    private ISetTop setTopInteractor;
    private BaseView baseView;

    public SetTopPresenter(BaseView baseView) {
        setTopInteractor = new SetTopInteractor();
        this.baseView = baseView;
    }

    @Override public void setTop(String schoolTeacherId, String schoolNewsId, int isTop) {
        setTopInteractor.setTop(new Response.Listener<BaseBean>() {
            @Override public void onResponse(BaseBean response) {
                if (response.getCode() == 0) {
                    baseView.showLoading(response.getMsg());
                }
            }
        }, new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {

            }
        }, schoolTeacherId, schoolNewsId, isTop);
    }
}
