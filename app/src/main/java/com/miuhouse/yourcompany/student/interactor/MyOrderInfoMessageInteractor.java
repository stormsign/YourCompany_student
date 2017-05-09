package com.miuhouse.yourcompany.student.interactor;

import com.android.volley.Response;
import com.miuhouse.yourcompany.student.application.App;
import com.miuhouse.yourcompany.student.http.VolleyManager;
import com.miuhouse.yourcompany.student.interactor.interf.IMyOrderInfoMessageInteractor;
import com.miuhouse.yourcompany.student.model.MyOrderInfoMessageBean;
import com.miuhouse.yourcompany.student.model.User;
import com.miuhouse.yourcompany.student.utils.AesUtil;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.utils.SPUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kings on 10/11/2016.
 */
public class MyOrderInfoMessageInteractor implements IMyOrderInfoMessageInteractor {
    @Override
    public void myOrderInfoMessage(Response.Listener<MyOrderInfoMessageBean> listener, Response.ErrorListener errorListener) {

        String urlPath = Constants.URL_VALUE + "my";
        Map<String, Object> map = new HashMap<>();
        map.put("parentId", App.getInstance().getParentId());
        VolleyManager.getInstance().sendGsonRequest("my", urlPath, map, SPUtils.getData(SPUtils.TOKEN, null), MyOrderInfoMessageBean.class, listener, errorListener);
    }
}
