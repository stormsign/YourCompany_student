package com.miuhouse.yourcompany.student.view.ui.activity.interf;

import com.miuhouse.yourcompany.student.view.ui.base.BaseView;

/**
 * Created by khb on 2016/9/22.
 */
public interface ISelectSuccessActivity extends BaseView {
    void goBack();
    void checkOrder(String orderId,String orderType);
}
