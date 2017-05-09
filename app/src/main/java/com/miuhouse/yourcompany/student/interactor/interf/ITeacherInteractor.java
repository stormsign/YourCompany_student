package com.miuhouse.yourcompany.student.interactor.interf;


import com.amap.api.maps.model.LatLng;
import com.miuhouse.yourcompany.student.listener.OnLoadCallBack;

/**
 * Created by khb on 2016/8/31.
 */
public interface ITeacherInteractor {
    void getNearbyTeachers(String parentId, LatLng latlng, String pbxType, OnLoadCallBack onLoadCallBack);
    void getInterestedTeachers(String parentId, String orderId, OnLoadCallBack onLoadCallBack);
    void selectTeacher(String orderId, String teacherId, String parentId, OnLoadCallBack onLoadCallBack);
}
