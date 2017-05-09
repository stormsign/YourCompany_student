package com.miuhouse.yourcompany.student.interactor;

import com.android.volley.Response;
import com.miuhouse.yourcompany.student.http.VolleyManager;
import com.miuhouse.yourcompany.student.interactor.interf.IDeleteCommentInteractor;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.SPUtils;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kings on 2/23/2017.
 */
public class DeleteCommentInteractor implements IDeleteCommentInteractor {
    @Override public void deleteComment(Response.Listener<BaseBean> listener,
        Response.ErrorListener errorListener, String parentId, String id) {
        String urlPath = Constants.URL_VALUE + "schoolCommentDelete";
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", parentId);
        params.put("id", id);
        VolleyManager.getInstance()
            .sendGsonRequest("deleteComment", urlPath, params, SPUtils.getData(SPUtils.TOKEN, null),
                BaseBean.class, listener, errorListener);
    }
}
