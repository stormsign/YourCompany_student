package com.miuhouse.yourcompany.student.interactor.interf;

import com.miuhouse.yourcompany.student.listener.OnLoadCallBack;

/**
 * Created by khb on 2016/8/30.
 */
public interface IStudentInteractor {
    void getStudentInfo(String parentId, OnLoadCallBack onLoadBack);
    void getDefaultAddress(String parentId, OnLoadCallBack onLoadCallBack);

}
