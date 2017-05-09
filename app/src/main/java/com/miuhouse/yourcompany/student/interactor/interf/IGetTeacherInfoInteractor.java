package com.miuhouse.yourcompany.student.interactor.interf;

import com.android.volley.Response;
import com.miuhouse.yourcompany.student.model.TeacherDetailBean;

/**
 * Created by kings on 8/24/2016.
 */
public interface IGetTeacherInfoInteractor {
    void getTeacherInfo(String id,Response.Listener<TeacherDetailBean> listener,Response.ErrorListener errorListener);
}
