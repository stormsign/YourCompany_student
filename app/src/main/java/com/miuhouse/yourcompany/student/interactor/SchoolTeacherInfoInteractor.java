package com.miuhouse.yourcompany.student.interactor;

import com.android.volley.Response;
import com.miuhouse.yourcompany.student.http.VolleyManager;
import com.miuhouse.yourcompany.student.interactor.interf.ISchoolTeacherInfoInteractor;
import com.miuhouse.yourcompany.student.model.User;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.SPUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kings on 1/6/2017.
 */
public class SchoolTeacherInfoInteractor implements ISchoolTeacherInfoInteractor {
  @Override public void getSchoolTeacherInfo(Response.Listener<User> listener,
      Response.ErrorListener errorListener, String schoolTeacherId) {
    String urlPath = Constants.URL_VALUE + "parentInfo";
    Map<String, Object> map = new HashMap<>();
    map.put("parentId", schoolTeacherId);
    VolleyManager.getInstance()
        .sendGsonRequest("schoolTeacherInfo", urlPath, map, SPUtils.getData(SPUtils.TOKEN, null),
            User.class, listener, errorListener);
  }
}
