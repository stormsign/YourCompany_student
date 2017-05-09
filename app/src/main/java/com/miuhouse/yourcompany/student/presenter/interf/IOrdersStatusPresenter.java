package com.miuhouse.yourcompany.student.presenter.interf;

/**
 * Created by khb on 2016/9/18.
 */
public interface IOrdersStatusPresenter {
    void getInterestedTeachers(String parentId, String orderId);
    void getNoTeacherOrders(String parentId, String orderId);
    void selectTeacher(String orderId, String orderType,String teacherId, String parentId);
}
