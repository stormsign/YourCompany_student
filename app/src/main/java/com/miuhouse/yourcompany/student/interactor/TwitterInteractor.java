package com.miuhouse.yourcompany.student.interactor;

import com.android.volley.Response;
import com.miuhouse.yourcompany.student.http.VolleyManager;
import com.miuhouse.yourcompany.student.interactor.interf.ITwitterInteractor;
import com.miuhouse.yourcompany.student.model.SchoolNewsListBean;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.SPUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kings on 12/28/2016.
 */
public class TwitterInteractor implements ITwitterInteractor {
    @Override public void getSchoolNewsList(Response.Listener<SchoolNewsListBean> listener,
        Response.ErrorListener errorListener, String schoolTeacherId, String schoolId,
        String contentType,
        long classes, int page, int pageSize) {
        String urlPath = Constants.URL_VALUE + "schoolNewsList";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("parentId", schoolTeacherId);
        paramMap.put("schoolId", schoolId);
        paramMap.put("contentType", contentType);
        paramMap.put("classes", classes);
        paramMap.put("page", page);
        paramMap.put("pageSize", pageSize);
        VolleyManager.getInstance()
            .sendGsonRequest(null, urlPath, paramMap, SPUtils.getData(SPUtils.TOKEN, null),
                SchoolNewsListBean.class, listener,
                errorListener);
    }

    @Override public void getSchoolNewsList(Response.Listener<SchoolNewsListBean> listener,
        Response.ErrorListener errorListener, String schoolTeacherId, String schoolId,
        String contentType,
        String linkId, int page, int pageSize) {
        String urlPath = Constants.URL_VALUE + "schoolNewsList";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("parentId", schoolTeacherId);
        paramMap.put("schoolId", schoolId);
        paramMap.put("contentType", contentType);
        paramMap.put("linkId", linkId);
        paramMap.put("page", page);
        paramMap.put("pageSize", pageSize);
        VolleyManager.getInstance()
            .sendGsonRequest(null, urlPath, paramMap, SPUtils.getData(SPUtils.TOKEN, null),
                SchoolNewsListBean.class, listener,
                errorListener);
    }
}
