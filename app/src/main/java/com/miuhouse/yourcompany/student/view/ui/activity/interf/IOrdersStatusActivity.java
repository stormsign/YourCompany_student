package com.miuhouse.yourcompany.student.view.ui.activity.interf;

import com.miuhouse.yourcompany.student.interactor.OrderManageInteractor;
import com.miuhouse.yourcompany.student.model.TeacherInfoBean;
import com.miuhouse.yourcompany.student.view.ui.base.BaseView;

import java.util.List;

/**
 * Created by khb on 2016/9/18.
 */
public interface IOrdersStatusActivity extends BaseView{
    /**
     * 显示抢单老师列表
     * @param teacherList
     */
    void showTeachers(List<TeacherInfoBean> teacherList);

    /**
     * 显示未选定老师的订单列表
     * @param list
     */
    void showNoTeacherOrders(List<OrderManageInteractor.WaitingOrder> list);

    /**
     * 显示未选定老师的订单列表的界面
     * @param goingDown
     */
    void showNoTeacherOrderList(boolean goingDown);

    /**
     * 显示顶部已选订单的信息
     * @param order
     */
    void showSelectedOrder(OrderManageInteractor.WaitingOrder order);

    /**
     * 前往成功页面
     * @param orderId
     */
    void goToSuccessPage(String orderId,String orderType);

    /**
     * 显示老师列表加载
     */
    void showListLoading();

    /**
     * 隐藏老师列表加载
     */
    void hideListLoading();
}
