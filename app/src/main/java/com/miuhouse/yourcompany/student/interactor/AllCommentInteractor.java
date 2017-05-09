package com.miuhouse.yourcompany.student.interactor;

import com.android.volley.Response;
import com.miuhouse.yourcompany.student.application.App;
import com.miuhouse.yourcompany.student.http.VolleyManager;
import com.miuhouse.yourcompany.student.interactor.interf.IGetAllCommentInteractor;
import com.miuhouse.yourcompany.student.model.EvaluateListBean;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.SPUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kings on 8/25/2016.
 */
public class AllCommentInteractor implements IGetAllCommentInteractor {

    @Override
    public void getAllComment(String teacherId, int page, Response.Listener<EvaluateListBean> listener, Response.ErrorListener errorListener) {
        String urlPath = Constants.URL_VALUE + "teaEvaluate";
        Map<String, Object> map = new HashMap<>();
        map.put("parentId", App.getInstance().getParentId());
        map.put("teacherId", teacherId);
        map.put("page", page);
        map.put("pageSize", Constants.PAGE_SIZE);
        VolleyManager.getInstance().sendGsonRequest(null, urlPath, map, SPUtils.getData(SPUtils.TOKEN, null), EvaluateListBean.class, listener, errorListener);
    }


}
