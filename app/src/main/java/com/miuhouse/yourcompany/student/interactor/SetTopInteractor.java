package com.miuhouse.yourcompany.student.interactor;

import com.android.volley.Response;
import com.miuhouse.yourcompany.student.http.VolleyManager;
import com.miuhouse.yourcompany.student.interactor.interf.ISetTop;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.SPUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kings on 1/11/2017.
 */
public class SetTopInteractor implements ISetTop {
  @Override public void setTop(Response.Listener<BaseBean> listener, Response.ErrorListener errorListener,
      String schoolTeacherId, String schoolNewsId, int isTop) {
    String urlPath = Constants.URL_VALUE + "isTop";
    Map<String, Object> map = new HashMap<>();
    map.put("parentId", schoolTeacherId);
    map.put("schoolNewsId", schoolNewsId);
    map.put("isTop", isTop);
    VolleyManager.getInstance()
        .sendGsonRequest("isTop", urlPath, map, SPUtils.getData(SPUtils.TOKEN, null),
            BaseBean.class, listener, errorListener);
  }
}
