package com.miuhouse.yourcompany.student.interactor;

import com.android.volley.Response;
import com.miuhouse.yourcompany.student.http.VolleyManager;
import com.miuhouse.yourcompany.student.interactor.interf.ISchoolNewsInfo;
import com.miuhouse.yourcompany.student.model.SchoolNewsBean;
import com.miuhouse.yourcompany.student.model.SchoolNewsInfo;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.SPUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kings on 1/17/2017.
 */
public class SchoolNewsInfoInteractor implements ISchoolNewsInfo {
    @Override public void getSchoolNewsInfo(Response.Listener<SchoolNewsInfo> listener,
        Response.ErrorListener errorListener, String schoolTeacherId, String schoolNewsId) {
        String urlPath = Constants.URL_VALUE + "schoolNewsInfo";
        Map<String, Object> map = new HashMap<>();
        map.put("parentId", schoolTeacherId);
        map.put("schoolNewsId", schoolNewsId);
        VolleyManager.getInstance()
            .sendGsonRequest("schoolNewsInfo", urlPath, map, SPUtils.getData(SPUtils.TOKEN, null),
                SchoolNewsInfo.class, listener, errorListener);
    }
}
