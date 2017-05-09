package com.miuhouse.yourcompany.student.interactor.interf;

import com.miuhouse.yourcompany.student.listener.OnLoadCallBack;

import java.util.ArrayList;

/**
 * Created by khb on 2016/8/31.
 */
public interface IOrderInteractor {
    void getOrderInfo(String orderInfoId, OnLoadCallBack onLoadCallBack);
    void confirmOrder(String parentId, String description, String majorDemand,
                      String minorDemand, String starLavel, long classBeginTimePromise,
                      String lesson, String mobile, double price,double amount, String sex,
                      String province, String city, String district, String street,
                      String houseNumber, double lon, double lat,
                      String orderType,
                      String lessonDay, ArrayList<Integer> weekLoop,long classEndTimePromise,
                      final OnLoadCallBack onLoadCallBack);
    void confirmOrder(String parentId, String description, String majorDemand,
                      String minorDemand, String starLavel, long classBeginTimePromise,
                      String lesson, String mobile,
                      double singlePrice, double extraPrice, double amount, String sex,
                      String province, String city, String district, String street,
                      String houseNumber, double lon, double lat,
                      final OnLoadCallBack onLoadCallBack);

}
