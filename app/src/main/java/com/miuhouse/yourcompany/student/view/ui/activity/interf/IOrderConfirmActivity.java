package com.miuhouse.yourcompany.student.view.ui.activity.interf;

import com.miuhouse.yourcompany.student.model.AddressBean;
import com.miuhouse.yourcompany.student.model.ParentInfo;
import com.miuhouse.yourcompany.student.view.ui.base.BaseView;

/**
 * Created by khb on 2016/8/29.
 */
public interface IOrderConfirmActivity extends BaseView {
    void showInfo(ParentInfo parentInfo);
    void showPriceDetail();
//    void showAddress(String address, String addressDetail);
    void showAddress(AddressBean address);
    void notifyUserToCompleteSelection();
    void goToPay(String orderId);
}
