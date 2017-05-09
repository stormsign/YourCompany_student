package com.miuhouse.yourcompany.student.presenter;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.yourcompany.student.application.App;
import com.miuhouse.yourcompany.student.db.AccountDBTask;
import com.miuhouse.yourcompany.student.interactor.GetUserInfo;
import com.miuhouse.yourcompany.student.interactor.interf.IGetUser;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.model.User;
import com.miuhouse.yourcompany.student.presenter.interf.ILoginPresenter;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.utils.SPUtils;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.ILoginView;

/**
 * 程序逻辑在Presenter里实现 Created by kings on 7/1/2016.
 */
public class LoginPresenter implements ILoginPresenter {
    private ILoginView iLoginView;
    private IGetUser getUser;
    private Context context;

    public LoginPresenter(ILoginView iLoginView, Context context) {
        this.context = context;
        this.iLoginView = iLoginView;
        getUser = new GetUserInfo();
    }

    @Override
    public void clear() {

    }

    //登录
    @Override
    public void doLogin(String name, String password) {
        getUser.getUserInfo(name, password, new Response.Listener<User>() {
            @Override
            public void onResponse(User response) {
                if (response != null && response.getParentInfo() != null) {
                    if (response.getParentInfo().getToken() != null) {
                        SPUtils.saveData(SPUtils.TOKEN, response.getParentInfo().getToken());
                    }
                    AccountDBTask.saveUserBean(response.getParentInfo());
                    App.getInstance().setLogin(true);
                }

                iLoginView.showLoginSuccess(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                L.i("TAG", "error=" + error.toString());
                iLoginView.netError();
            }
        });
    }

    @Override
    public void doRegist(int typeMark, String name, String password) {
        getUser.getRegistInfo(typeMark, name, password, new Response.Listener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                iLoginView.showRegistSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                iLoginView.netError();
            }
        });
    }
}
