package com.miuhouse.yourcompany.student.interactor.interf;

import com.miuhouse.yourcompany.student.listener.OnLoadCallBack;

/**
 * Created by khb on 2016/7/14.
 */
public interface ISysMsgInteractor {

    void getMsgs(int page, OnLoadCallBack onLoadCallBack);
}
