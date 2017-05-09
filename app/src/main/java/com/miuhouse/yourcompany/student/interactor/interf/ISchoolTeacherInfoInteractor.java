package com.miuhouse.yourcompany.student.interactor.interf;

import com.android.volley.Response;
import com.miuhouse.yourcompany.student.model.User;

/**
 * Created by kings on 1/6/2017.
 */
public interface ISchoolTeacherInfoInteractor {
  void getSchoolTeacherInfo(Response.Listener<User> listener,
                            Response.ErrorListener errorListener, String schoolTeacherId);
}
