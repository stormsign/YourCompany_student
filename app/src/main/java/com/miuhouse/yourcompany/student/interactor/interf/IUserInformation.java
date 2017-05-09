package com.miuhouse.yourcompany.student.interactor.interf;

import com.android.volley.Response;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kings on 7/13/2016.
 */
public interface IUserInformation {
    void updateUserInformation(String pName, String cName, String mobile, String headUrl, Response.Listener<BaseBean> listener, Response.ErrorListener errorListener);

    void getUserInfo(String teacherId, Response.Listener<User> listener, Response.ErrorListener errorListener);

    void updateUserPhone(String teacherId, String mobile, Response.Listener<BaseBean> listener, Response.ErrorListener errorListener);


}
