package com.miuhouse.yourcompany.student.interactor.interf;

import com.android.volley.Response;
import com.miuhouse.yourcompany.student.model.EvaluateListBean;

/**
 * Created by kings on 8/25/2016.
 */
public interface IGetAllCommentInteractor {
    void getAllComment(String teacherId, int page,Response.Listener<EvaluateListBean> listener,Response.ErrorListener errorListener);
}
