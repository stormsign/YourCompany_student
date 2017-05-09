package com.miuhouse.yourcompany.student.presenter.interf;

/**
 * Created by khb on 2016/7/19.
 */
public interface IOrderManagePresenter {

    void getAOrders(String teacherId, int page);
    void getBOrders(String teacherId, int page);
    void getCOrders(String teacherId, int page);
    void getDOrders(String teacherId, int page);
//    void goToPay(String teacherId, String orderId);
//    void goToComment(String teacherId, String orderId);
//    void goToRefund(String teacherId, String orderId);
}
