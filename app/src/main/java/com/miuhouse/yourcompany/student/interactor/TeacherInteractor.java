package com.miuhouse.yourcompany.student.interactor;

import com.amap.api.maps.model.LatLng;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.yourcompany.student.http.VolleyManager;
import com.miuhouse.yourcompany.student.interactor.interf.ITeacherInteractor;
import com.miuhouse.yourcompany.student.listener.OnLoadCallBack;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.model.TeacherInfoBean;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.SPUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khb on 2016/8/31.
 */
public class TeacherInteractor implements ITeacherInteractor {

    public TeacherInteractor(){}

    @Override
    public void getNearbyTeachers(String parentId, LatLng latlng, String pbxType, final OnLoadCallBack onLoadCallBack) {
        onLoadCallBack.onPreLoad();
        String url = Constants.URL_VALUE+"teacherNearList";
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", parentId);
        params.put("lat", latlng.latitude);
        params.put("lon", latlng.longitude);
        params.put("pbxType", pbxType);
        VolleyManager.getInstance().sendGsonRequest(null, url, params, SPUtils.getData(SPUtils.TOKEN, ""),
                TeacherInfoListBean.class,
                new Response.Listener<TeacherInfoListBean>() {
                    @Override
                    public void onResponse(TeacherInfoListBean response) {
                        onLoadCallBack.onLoadSuccess(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onLoadCallBack.onLoadFailed(error.toString());
                    }
                });

    }

    @Override
    public void getInterestedTeachers(String parentId, String orderId, final OnLoadCallBack onLoadCallBack) {
        onLoadCallBack.onPreLoad();
        String url = Constants.URL_VALUE + "teacherList";
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", parentId);
        params.put("orderId", orderId);
        VolleyManager.getInstance().sendGsonRequest(null, url, params, SPUtils.getData(SPUtils.TOKEN, null),
                TeacherInfoListBean.class,
                new Response.Listener<TeacherInfoListBean>() {
                    @Override
                    public void onResponse(TeacherInfoListBean response) {
                        onLoadCallBack.onLoadSuccess(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onLoadCallBack.onLoadFailed(error.toString());
                    }
                });
    }

    @Override
    public void selectTeacher(String orderId, String teacherId, String parentId, final OnLoadCallBack onLoadCallBack) {
        onLoadCallBack.onPreLoad();
        String url = Constants.URL_VALUE + "orderCheck";
        Map<String, Object> params = new HashMap<>();
        params.put("orderInfoId", orderId);
        params.put("teacherId", teacherId);
        params.put("parentId", parentId);
        VolleyManager.getInstance().sendStringRequest(null, url, params, SPUtils.getData(SPUtils.TOKEN, null),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onLoadCallBack.onLoadSuccess(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        onLoadCallBack.onLoadFailed(error.toString());
                        onLoadCallBack.onLoadFailed(error.toString());
                    }
                });
    }

    public class TeacherInfoListBean extends BaseBean{
        List<TeacherInfoBean> teacherList;

        public List<TeacherInfoBean> getTeacherList() {
            return teacherList;
        }

        public void setTeacherList(List<TeacherInfoBean> teacherList) {
            this.teacherList = teacherList;
        }
    }
}
