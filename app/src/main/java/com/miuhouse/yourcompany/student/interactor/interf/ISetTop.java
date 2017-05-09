package com.miuhouse.yourcompany.student.interactor.interf;

import com.android.volley.Response;
import com.miuhouse.yourcompany.student.model.BaseBean;

/**
 * Created by kings on 1/11/2017.
 */
public interface ISetTop {
  void setTop(Response.Listener<BaseBean> listener, Response.ErrorListener errorListener, String schoolTeacherId, String schoolNewsId, int isTop);
}
