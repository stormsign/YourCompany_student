package com.miuhouse.yourcompany.student.presenter.interf;

import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by khb on 2016/8/29.
 */
public interface ILongOrderConfirmPresenter {
    void getInfo(String parentId);
    void getDefaultAddress(String parentId);
    void getPriceDetail();

    /**
     *
     * @param parentId
     * @param etDescription 描述
     * @param majorDemand  需求大类目
     * @param tvMinorDemand
     * @param tvInterest
     * @param tvStarLavel
     * @param classBeginTimePromise 约定上课开始时间
     * @param lesson
     * @param tvMobile
     * @param amount
     * @param sex
     * @param province
     * @param city
     * @param district
     * @param street
     * @param houseNumber
     * @param lon
     * @param lat
     * @param orderType 包周，包月

     * @param lessonDay 每日课时
     * @param classEndTimePromise
     */
    void confirmOrder(String parentId, EditText etDescription, String majorDemand,
                      TextView tvMinorDemand, TextView tvInterest, TextView tvStarLavel, long classBeginTimePromise,
                      String lesson, TextView tvMobile,double price, double amount, String sex,
                      String province, String city, String district, String street,
                      String houseNumber, double lon, double lat, String orderType,
                      String lessonDay,ArrayList<Integer> weekLoop, long classEndTimePromise);
}
