package com.miuhouse.yourcompany.student.view.ui.activity.interf;

import com.miuhouse.yourcompany.student.model.OrderEntity;
import com.miuhouse.yourcompany.student.view.ui.base.BaseView;

/**
 * Created by khb on 2016/7/20.
 */
public interface IOrderLongTermDetailActivity extends BaseView {

    void showOrderStatus(OrderEntity order);
    void showCountdown(OrderEntity order);
    void call(String number);
    void fillView(OrderEntity order);
    void beforeBeginClass();
    void afterBeginClass();
}
