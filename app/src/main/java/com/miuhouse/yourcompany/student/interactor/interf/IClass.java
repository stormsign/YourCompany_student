package com.miuhouse.yourcompany.student.interactor.interf;

import com.android.volley.Response;

/**
 * Created by khb on 2017/1/10.
 */
public interface IClass {
    void getAllClasses(String schoolTeacherId, String schoolId,
                       Response.Listener listener, Response.ErrorListener errorListener);
}
