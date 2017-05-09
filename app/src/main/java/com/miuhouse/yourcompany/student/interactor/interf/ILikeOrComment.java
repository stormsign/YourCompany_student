package com.miuhouse.yourcompany.student.interactor.interf;

import com.android.volley.Response;
import com.miuhouse.yourcompany.student.model.BaseBean;

/**
 * Created by kings on 12/30/2016.
 */
public interface ILikeOrComment {
  void getLikeOrComment(Response.Listener<BaseBean> listener, Response.ErrorListener errorListener,
                        String fId, String schoolTeacherId, String newsId, String commentType, String linkId,
                        String linkType, String linkName, String linkedId, String linkedType, String linkedName,
                        String comment);

  void getLikeOrComment(Response.Listener listener, Response.ErrorListener errorListener,
                        String schoolTeacherId, String newsId, String commentType, String linkId,
                        String linkType, String linkName);

  void cancelLike(Response.Listener<BaseBean> listener, Response.ErrorListener errorListener,
                  String schoolTeacherId, String schoolNewsId);
}
