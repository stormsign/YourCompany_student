package com.miuhouse.yourcompany.student.view.ui.activity.interf;

import com.miuhouse.yourcompany.student.model.OrderEntity;
import com.miuhouse.yourcompany.student.view.ui.base.BaseView;

/**
 * Created by khb on 2016/9/6.
 */
public interface IOrderPayActivity extends BaseView{
    void payInAlipay(String orderId);
    void payInWeixin(String orderid);
    void paySuccess();
    void payFailed();
    void showOrderInfo(OrderEntity order);
    void payOnGoing();
    void payOver(String tag);

}
