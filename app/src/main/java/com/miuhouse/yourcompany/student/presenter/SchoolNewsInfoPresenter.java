package com.miuhouse.yourcompany.student.presenter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.yourcompany.student.interactor.SchoolNewsInfoInteractor;
import com.miuhouse.yourcompany.student.interactor.interf.ISchoolNewsInfo;
import com.miuhouse.yourcompany.student.model.SchoolNewsBean;
import com.miuhouse.yourcompany.student.model.SchoolNewsInfo;
import com.miuhouse.yourcompany.student.presenter.interf.ISchoolNewsInfoPresenter;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.ISchoolNewsInfoView;

/**
 * Created by kings on 1/17/2017.
 */
public class SchoolNewsInfoPresenter implements ISchoolNewsInfoPresenter {

    private ISchoolNewsInfo schoolNewsInfo;

    private ISchoolNewsInfoView schoolNewsInfoView;

    public SchoolNewsInfoPresenter(ISchoolNewsInfoView schoolNewsInfoView) {
        schoolNewsInfo = new SchoolNewsInfoInteractor();
        this.schoolNewsInfoView = schoolNewsInfoView;
    }

    @Override public void getSchoolNewsInfo(String schoolTeacherId, final String schoolNewsId) {
        //schoolNewsInfoView.showLoading(null);
        schoolNewsInfo.getSchoolNewsInfo(
            new Response.Listener<SchoolNewsInfo>() {
                @Override public void onResponse(SchoolNewsInfo response) {
                    schoolNewsInfoView.hideLoading();
                    schoolNewsInfoView.getSchoolNewsInfo(response.getSchoolNewsInfo());
                }
            }, new Response.ErrorListener() {
                @Override public void onErrorResponse(VolleyError error) {
                    schoolNewsInfoView.hideLoading();
                    schoolNewsInfoView.showError(0);
                }
            }, schoolTeacherId, schoolNewsId);
    }
}
