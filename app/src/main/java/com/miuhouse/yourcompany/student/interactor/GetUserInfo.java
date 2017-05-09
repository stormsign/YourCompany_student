package com.miuhouse.yourcompany.student.interactor;

import com.android.volley.Response;
import com.miuhouse.yourcompany.student.http.VolleyManager;
import com.miuhouse.yourcompany.student.interactor.interf.IGetUser;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.model.User;
import com.miuhouse.yourcompany.student.utils.AesUtil;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.SPUtils;
import com.miuhouse.yourcompany.student.view.ui.activity.LoginRegistActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * 耗时操作
 * Created by kings on 7/1/2016.
 */
public class GetUserInfo implements IGetUser {
    @Override
    public void getUserInfo(String name, String password, Response.Listener<User> listener, Response.ErrorListener errorListener) {

        String urlPath = Constants.URL_VALUE + "login";
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", name);
        try {
            map.put("password", AesUtil.Encrypt(password, AesUtil.cKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("deviceType", 3);
        map.put("deviceCode", SPUtils.getData(SPUtils.DEVICE_CODE, null));
        VolleyManager.getInstance().sendGsonRequest("login", urlPath, map, User.class, listener, errorListener);
    }

    @Override
    public void getRegistInfo(int typeMark, String name, String password, Response.Listener<BaseBean> listener, Response.ErrorListener errorListener) {

        String urlPath = null;
        if (typeMark == LoginRegistActivity.TYPE_MARK_REGIST) {
            urlPath = Constants.URL_VALUE + "regist";
        } else {
            urlPath = Constants.URL_VALUE + "reset";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", name);
        try {
            map.put("password", AesUtil.Encrypt(password, AesUtil.cKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("deviceType", 3);
        map.put("deviceCode", SPUtils.getData(SPUtils.DEVICE_CODE, null));
        VolleyManager.getInstance().sendGsonRequest("register", urlPath, map, BaseBean.class, listener, errorListener);
    }
}
