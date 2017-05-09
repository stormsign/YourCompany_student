package com.miuhouse.yourcompany.student.view.ui.activity.interf;


import com.miuhouse.yourcompany.student.model.OrderMsgBean;
import com.miuhouse.yourcompany.student.view.ui.base.BaseView;

import java.util.List;

/**
 * Created by khb on 2016/7/26.
 */
public interface IOrderMsgActivity extends BaseView {
    void refresh(List<OrderMsgBean> dataList);
}
