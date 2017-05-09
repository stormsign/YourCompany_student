package com.miuhouse.yourcompany.student.presenter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.yourcompany.student.application.App;
import com.miuhouse.yourcompany.student.db.AccountDBTask;
import com.miuhouse.yourcompany.student.interactor.SchoolTeacherInfoInteractor;
import com.miuhouse.yourcompany.student.interactor.interf.ISchoolTeacherInfoInteractor;
import com.miuhouse.yourcompany.student.model.ParentInfo;
import com.miuhouse.yourcompany.student.model.User;
import com.miuhouse.yourcompany.student.presenter.interf.ISchoolTeacherInfoPresenter;
import com.miuhouse.yourcompany.student.utils.SPUtils;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.ISchoolTeacherInfoView;
import mabeijianxi.camera.util.Log;

/**
 * Created by kings on 1/6/2017. 获取老师信息
 */
public class SchoolTeacherInfoPresenter implements ISchoolTeacherInfoPresenter {

    private ISchoolTeacherInfoInteractor iSchoolTeacherInfoInteractor;
    private ISchoolTeacherInfoView schoolTeacherInfoView;

    public SchoolTeacherInfoPresenter(ISchoolTeacherInfoView schoolTeacherInfoView) {
        iSchoolTeacherInfoInteractor = new SchoolTeacherInfoInteractor();
        this.schoolTeacherInfoView = schoolTeacherInfoView;
    }

    @Override public void getSchoolTeacherInfo(String schoolTeacherId) {
        iSchoolTeacherInfoInteractor.getSchoolTeacherInfo(
            new Response.Listener<User>() {
                @Override public void onResponse(User response) {
                    if (response.getCode() == 1) {
                        schoolTeacherInfoView.onTokenExpired();
                    } else {
                        // TODO: 2017/1/19  
                        //App.getInstance().setSchoolTeacherInfo(response.());
                        App.getInstance().setParentInfo(response.getParentInfo());
                        ParentInfo parentInfo = response.getParentInfo();
                        parentInfo.setId(parentInfo.getParentId());
                        parentInfo.setToken(SPUtils.getData(SPUtils.TOKEN, null));
                        AccountDBTask.saveUserBean(parentInfo);
                        schoolTeacherInfoView.getSchoolTeacherInfo(response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override public void onErrorResponse(VolleyError error) {

                }
            }, schoolTeacherId);
    }
}
