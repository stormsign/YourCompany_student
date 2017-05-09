package com.miuhouse.yourcompany.student.interactor;

import com.android.volley.Response;
import com.miuhouse.yourcompany.student.http.VolleyManager;
import com.miuhouse.yourcompany.student.interactor.interf.ILikeOrComment;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.SPUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kings on 12/30/2016.
 */
public class LikeOrComment implements ILikeOrComment {
  @Override
  public void getLikeOrComment(Response.Listener<BaseBean> listener,
      Response.ErrorListener errorListener,
      String fId, String schoolTeacherId, String newsId, String commentType, String linkId,
      String linkType, String linkName, String linkedId, String linkedType, String linkedName,
      String comment) {
    String urlPath = Constants.URL_VALUE + "schoolComment";
    Map<String, Object> map = new HashMap<>();
    map.put("parentId", schoolTeacherId);
    map.put("newsId", newsId);
    map.put("commentType", commentType);
    map.put("linkId", linkId);
    map.put("linkType", linkType);
    map.put("linkName", linkName);
    if (commentType.equals("1")) {
      map.put("fId", fId);
      map.put("linkedId", linkedId);
      map.put("linkedType", linkedType);
      map.put("linkedName", linkedName);
      map.put("comment", comment);
    }
    VolleyManager.getInstance()
        .sendGsonRequest("likeOrComment", urlPath, map, SPUtils.getData(SPUtils.TOKEN, null),
            BaseBean.class, listener, errorListener);
  }

  @Override
  public void getLikeOrComment(Response.Listener listener, Response.ErrorListener errorListener
      , String schoolTeacherId, String newsId, String commentType, String linkId,
      String linkType, String linkName) {
    getLikeOrComment(listener, errorListener, null, schoolTeacherId, newsId, commentType, linkId,
        linkType, linkName, null, null, null, null);
  }

  @Override
  public void cancelLike(Response.Listener listener, Response.ErrorListener errorListener,
      String schoolTeacherId, String schoolNewsId) {
    String urlPath = Constants.URL_VALUE + "thumbupCancel";
    Map<String, Object> parameter = new HashMap<>();
    parameter.put("parentId", schoolTeacherId);
    parameter.put("schoolNewsId", schoolNewsId);
    VolleyManager.getInstance()
        .sendGsonRequest("thumbupCancel", urlPath, parameter, SPUtils.getData(SPUtils.TOKEN, null),
            BaseBean.class, listener, errorListener);
  }
}
