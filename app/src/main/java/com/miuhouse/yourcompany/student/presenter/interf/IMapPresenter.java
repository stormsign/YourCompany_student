package com.miuhouse.yourcompany.student.presenter.interf;

import com.amap.api.maps.model.LatLng;

/**
 * Created by khb on 2016/8/22.
 */
public interface IMapPresenter {
    void getCurrentLocation();
    void getTeachers(String parentId, LatLng latlng, String pbxType);
    void getStudentInfo(String parentId,int orderType);
}
