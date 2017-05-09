package com.miuhouse.yourcompany.student.view.ui.activity.interf;

import com.miuhouse.yourcompany.student.view.ui.base.BaseView;

/**
 * Created by khb on 2016/9/14.
 */
public interface IOrderRefundActivity extends BaseView{
    void refundSuccess();
    void refundFailed(String msg);
}
