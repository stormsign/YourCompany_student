package com.miuhouse.yourcompany.student.interactor.interf;

import com.miuhouse.yourcompany.student.listener.OnLoadCallBack;

/**
 * Created by khb on 2016/7/26.
 */
public interface IOrderMsgInteractor {
    void getPurseMsgs(String teacherId, int page, OnLoadCallBack onLoadCallBack);
}
