package com.miuhouse.yourcompany.student.interactor;

import com.android.volley.Response;
import com.miuhouse.yourcompany.student.application.App;
import com.miuhouse.yourcompany.student.http.VolleyManager;
import com.miuhouse.yourcompany.student.interactor.interf.IOrderEvaluateInteractor;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.SPUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kings on 8/31/2016.
 */
public class OrderEvaluateInteractor implements IOrderEvaluateInteractor {
    @Override
    public void commitOrderEvaluate(String orderInfoId, String evaluateRank, String evaluateContent, Response.Listener listener, Response.ErrorListener errorListener) {
        String urlPath = Constants.URL_VALUE + "orderEvaluate";
        Map<String, Object> map = new HashMap<>();
        map.put("parentId", App.getInstance().getParentId());
        map.put("orderInfoId", orderInfoId);
        map.put("evaluateRank", evaluateRank);
        map.put("evaluateContent", evaluateContent);
        VolleyManager.getInstance().sendGsonRequest(null, urlPath, map, SPUtils.getData(SPUtils.TOKEN, null), BaseBean.class, listener, errorListener);
    }
}
