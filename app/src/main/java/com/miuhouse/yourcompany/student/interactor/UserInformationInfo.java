package com.miuhouse.yourcompany.student.interactor;

import com.android.volley.Response;
import com.miuhouse.yourcompany.student.application.App;
import com.miuhouse.yourcompany.student.http.VolleyManager;
import com.miuhouse.yourcompany.student.interactor.interf.IUserInformation;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.model.User;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kings on 7/13/2016.
 */
public class UserInformationInfo implements IUserInformation {

    @Override
    public void updateUserInformation( String pName, String cName,  String mobile,String headUrl, Response.Listener<BaseBean> listener, Response.ErrorListener errorListener) {
        String urlPath = Constants.URL_VALUE + "parentUpdate";
        Map<String, Object> map = new HashMap<>();
        map.put("parentId", App.getInstance().getParentId());
        map.put("pName", pName);

        map.put("cName", cName);

        map.put("mobile", mobile);
        map.put("headUrl", headUrl);

        VolleyManager.getInstance().sendGsonRequest(null, urlPath, map, SPUtils.getData(SPUtils.TOKEN, null), BaseBean.class, listener, errorListener);
    }

    @Override
    public void getUserInfo(String studentId, Response.Listener<User> listener, Response.ErrorListener errorListener) {
        String urlPath = Constants.URL_VALUE + "parentInfo";
        Map<String, Object> map = new HashMap<>();
        map.put("parentId", App.getInstance().getParentId());
        VolleyManager.getInstance().sendGsonRequest(null, urlPath, map, SPUtils.getData(SPUtils.TOKEN, null), User.class, listener, errorListener);

    }

    @Override
    public void updateUserPhone(String studentId, String mobile, Response.Listener<BaseBean> listener, Response.ErrorListener errorListener) {
        String urlPath = Constants.URL_VALUE + "updateMobile";
        Map<String, Object> map = new HashMap<>();
        map.put("studentId", App.getInstance().getParentId());
        map.put("mobile", mobile);
        VolleyManager.getInstance().sendGsonRequest(null, urlPath, map, SPUtils.getData(SPUtils.TOKEN, null), BaseBean.class, listener, errorListener);
    }
}
