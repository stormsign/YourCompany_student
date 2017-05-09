package com.miuhouse.yourcompany.student.interactor.interf;

import com.android.volley.Response;

/**
 * Created by khb on 2017/1/17.
 */
public interface IMsgInteractor {
    void getUnreadMsgs(String schoolTeacherId, int page,
                       Response.Listener listener, Response.ErrorListener errorListener);
}
