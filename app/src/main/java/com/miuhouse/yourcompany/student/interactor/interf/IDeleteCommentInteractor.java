package com.miuhouse.yourcompany.student.interactor.interf;

import com.android.volley.Response;
import com.miuhouse.yourcompany.student.model.BaseBean;

/**
 * Created by kings on 2/20/2017.
 */
public interface IDeleteCommentInteractor {

    void deleteComment(Response.Listener<BaseBean> listener, Response.ErrorListener errorListener,
        String parentId, String id);
}
