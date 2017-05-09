package com.miuhouse.yourcompany.student.interactor.interf;

import com.android.volley.Response;
import com.miuhouse.yourcompany.student.model.SchoolNewsListBean;

/**
 * Created by kings on 12/28/2016.
 */
public interface ITwitterInteractor {
    void getSchoolNewsList(Response.Listener<SchoolNewsListBean> listener,
                           Response.ErrorListener errorListener, String schoolTeacherId, String schoolId, String contentType,
                           long classes, int page, int pageSize);

    void getSchoolNewsList(Response.Listener<SchoolNewsListBean> listener,
                           Response.ErrorListener errorListener, String schoolTeacherId, String schoolId, String contentType,
                           String linkId, int page, int pageSize);

}
