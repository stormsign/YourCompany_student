package com.miuhouse.yourcompany.student.presenter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.yourcompany.student.interactor.GetTeacherInfoInteractor;
import com.miuhouse.yourcompany.student.interactor.interf.IGetTeacherInfoInteractor;
import com.miuhouse.yourcompany.student.model.TeacherDetailBean;
import com.miuhouse.yourcompany.student.presenter.interf.IGetTeacherinfoPresenter;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IGetTeachinfoView;

/**
 * Created by kings on 8/24/2016.
 */
public class GetTeacherInfoPresenter implements IGetTeacherinfoPresenter {

    private IGetTeacherInfoInteractor getTeacherInfoInteractor;
    private IGetTeachinfoView getTeachinfoView;

    public GetTeacherInfoPresenter(IGetTeachinfoView getTeachinfoView) {
        this.getTeachinfoView = getTeachinfoView;
        getTeacherInfoInteractor = new GetTeacherInfoInteractor();
    }


    @Override
    public void getTeacherInfo(String teacherId) {
        getTeacherInfoInteractor.getTeacherInfo(teacherId, new Response.Listener<TeacherDetailBean>() {
            @Override
            public void onResponse(TeacherDetailBean response) {
                if (response.getCode() == 1) {
                    getTeachinfoView.onTokenExpired();
                } else {
                    getTeachinfoView.result(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }
}
