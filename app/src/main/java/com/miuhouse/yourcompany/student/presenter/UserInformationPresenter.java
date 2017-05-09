package com.miuhouse.yourcompany.student.presenter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.yourcompany.student.interactor.UserInformationInfo;
import com.miuhouse.yourcompany.student.interactor.interf.IUserInformation;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.model.User;
import com.miuhouse.yourcompany.student.presenter.interf.IUserInformationPresenter;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IUserInformationView;
import com.miuhouse.yourcompany.student.view.widget.ViewOverrideManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kings on 7/13/2016.
 */
public class UserInformationPresenter implements IUserInformationPresenter {
    private IUserInformationView userInformationView;
    private IUserInformation userInformation;

    public UserInformationPresenter(IUserInformationView userInformationView) {
        this.userInformationView = userInformationView;
        userInformation = new UserInformationInfo();
    }

    @Override
    public void doChangeUserInformation(String pName, String cName, String mobile, String headUrl) {
        userInformation.updateUserInformation(pName, cName, mobile, headUrl,
            new Response.Listener<BaseBean>() {
                @Override
                public void onResponse(BaseBean response) {
                    userInformationView.UpdateSuccess(response);
                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    @Override
    public void getUserInfo(String studentId) {
        userInformationView.showLoading("正在加载中...");
        userInformation.getUserInfo(studentId, new Response.Listener<User>() {
            @Override
            public void onResponse(User response) {
                if (response.getCode() == 1) {
                    userInformationView.onTokenExpired();
                } else {
                    userInformationView.getUserInfo(response);
                    userInformationView.hideLoading();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                userInformationView.showError(ViewOverrideManager.NO_NETWORK);
            }
        });
    }

    @Override
    public void updatePhone(String studentId, String mobile) {
        userInformation.updateUserPhone(studentId, mobile, new Response.Listener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                userInformationView.UpdateSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                userInformationView.showError(ViewOverrideManager.NO_NETWORK);
            }
        });
    }
}
