package com.miuhouse.yourcompany.student.presenter.interf;

import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by khb on 2016/8/29.
 */
public interface IOrderConfirmPresenter {
    void getInfo(String parentId);
    void getDefaultAddress(String parentId);
    void getPriceDetail();
    void confirmOrder(String parentId, EditText etDescription, String majorDemand,
                      TextView tvMinorDemand, TextView tvInterest,  TextView tvStarLavel, long classBeginTimePromise,
                      String lesson, TextView tvMobile, double singlePrice, double extraPrice, double amount, String sex,
                      String province, String city, String district, String street,
                      String houseNumber, double lon, double lat);
}
