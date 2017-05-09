package com.miuhouse.yourcompany.student.interactor;

import com.android.volley.Response;
import com.miuhouse.yourcompany.student.application.App;
import com.miuhouse.yourcompany.student.http.VolleyManager;
import com.miuhouse.yourcompany.student.interactor.interf.IGetTeacherInfoInteractor;
import com.miuhouse.yourcompany.student.model.TeacherDetailBean;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.SPUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kings on 8/24/2016.
 */
public class GetTeacherInfoInteractor implements IGetTeacherInfoInteractor {

    @Override
    public void getTeacherInfo(String teacherId, Response.Listener<TeacherDetailBean> listener, Response.ErrorListener errorListener) {
        String urlPath = Constants.URL_VALUE + "teacherInfo";
        Map<String, Object> map = new HashMap<>();
        map.put("teacherId", teacherId);
        map.put("parentId", App.getInstance().getParentId());
        VolleyManager.getInstance().sendGsonRequest(null, urlPath, map, SPUtils.getData(SPUtils.TOKEN, null), TeacherDetailBean.class, listener, errorListener);
    }
}