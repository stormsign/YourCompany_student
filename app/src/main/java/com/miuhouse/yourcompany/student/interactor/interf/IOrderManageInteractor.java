package com.miuhouse.yourcompany.student.interactor.interf;

import com.miuhouse.yourcompany.student.listener.OnLoadCallBack;

/**
 * Created by khb on 2016/7/19.
 */
public interface IOrderManageInteractor {

    void getAOrders(String parentId, int page);
    void getBOrders(String parentId, int page);
    void getCOrders(String parentId, int page);
    void getDOrders(String parentId, int page);
//    void beginClass(String parentId, String orderInfoId);
    void getNoTeacherOrders(String parentId, String orderInfoId, int page, OnLoadCallBack onLoadCallBack);
}
