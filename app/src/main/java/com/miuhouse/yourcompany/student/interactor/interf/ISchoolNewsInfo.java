package com.miuhouse.yourcompany.student.interactor.interf;

import com.android.volley.Response;
import com.miuhouse.yourcompany.student.model.SchoolNewsBean;
import com.miuhouse.yourcompany.student.model.SchoolNewsInfo;

/**
 * Created by kings on 1/17/2017.
 */
public interface ISchoolNewsInfo {
    void getSchoolNewsInfo(Response.Listener<SchoolNewsInfo> listener,
                           Response.ErrorListener errorListener, String schoolTeacherId, String schoolNewsId);
}
