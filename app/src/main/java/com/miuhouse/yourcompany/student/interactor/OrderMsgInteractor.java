package com.miuhouse.yourcompany.student.interactor;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.yourcompany.student.http.VolleyManager;
import com.miuhouse.yourcompany.student.interactor.interf.IOrderMsgInteractor;
import com.miuhouse.yourcompany.student.listener.OnLoadCallBack;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.model.OrderMsgBean;
import com.miuhouse.yourcompany.student.presenter.interf.IOrderMsgPresenter;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.utils.SPUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khb on 2016/7/26.
 */
public class OrderMsgInteractor implements IOrderMsgInteractor {
    private IOrderMsgPresenter presenter;
//    private OnLoadCallBack onLoadCallBack;
    public OrderMsgInteractor(IOrderMsgPresenter pursePresenter) {
        this.presenter = pursePresenter;
    }

    @Override
    public void getPurseMsgs(String teacherId, int page, final OnLoadCallBack onLoadCallBack) {
        onLoadCallBack.onPreLoad();
        String url = Constants.URL_VALUE + "orderMsgList";
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", teacherId);
        params.put("page", page);
        params.put("pageSize", 10);
        String token = SPUtils.getData(SPUtils.TOKEN, null);
        L.i("TOKEN : "+ token);
        VolleyManager.getInstance().sendGsonRequest(null, url, params,
                token,
                OrderMsgListBean.class,
                new Response.Listener<OrderMsgListBean>() {
                    @Override
                    public void onResponse(OrderMsgListBean orderMsgListBean) {
                        onLoadCallBack.onLoadSuccess(orderMsgListBean);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        onLoadCallBack.onLoadFailed(volleyError.toString());
                    }
                });

    }

    public class OrderMsgListBean extends BaseBean{
        List<OrderMsgBean> orderMsgs;

        public List<OrderMsgBean> getOrderMsgs() {
            return orderMsgs;
        }

        public void setOrderMsgs(List<OrderMsgBean> orderMsgs) {
            this.orderMsgs = orderMsgs;
        }
    }

}
