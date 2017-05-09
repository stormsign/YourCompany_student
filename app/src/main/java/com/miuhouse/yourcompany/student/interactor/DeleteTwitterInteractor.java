package com.miuhouse.yourcompany.student.interactor;

import com.android.volley.Response;
import com.miuhouse.yourcompany.student.http.VolleyManager;
import com.miuhouse.yourcompany.student.interactor.interf.IDeleteTwitterInteractor;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.SPUtils;
import java.util.HashMap;

/**
 * Created by kings on 2/20/2017.
 */
public class DeleteTwitterInteractor implements IDeleteTwitterInteractor {
    @Override
    public void deleteTwitter(Response.Listener<BaseBean> listener,
        Response.ErrorListener errorListener,
        String parentId, String id) {
        String urlPath = Constants.URL_VALUE + "schoolNewsDelete";
        HashMap<String, Object> parameter = new HashMap<>();
        parameter.put("parentId", parentId);
        parameter.put("id", id);
        VolleyManager.getInstance()
            .sendGsonRequest("delete", urlPath, parameter, SPUtils.getData(SPUtils.TOKEN, null),
                BaseBean.class, listener, errorListener);
    }
}
