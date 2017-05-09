package com.miuhouse.yourcompany.student.interactor;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.yourcompany.student.application.App;
import com.miuhouse.yourcompany.student.http.VolleyManager;
import com.miuhouse.yourcompany.student.interactor.interf.IOrderManageInteractor;
import com.miuhouse.yourcompany.student.listener.OnLoadCallBack;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.model.OrderEntity;
import com.miuhouse.yourcompany.student.model.TeacherInfoBean;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khb on 2016/7/19.
 */
public class OrderManageInteractor implements IOrderManageInteractor, Response.Listener<OrderManageInteractor.OrderListBean>, Response.ErrorListener {

    private OnLoadCallBack onLoadCallBack;

    public OrderManageInteractor(){

    }

    public OrderManageInteractor(OnLoadCallBack onLoadCallBack) {
        this.onLoadCallBack = onLoadCallBack;
    }

    /**
     * 获取全部订单
     * @param parentId
     * @param page
     */
    @Override
    public void getAOrders(String parentId, int page) {
        onLoadCallBack.onPreLoad();
        List<String> statusList = new ArrayList<>();
        statusList.add("1");    //待付款
        statusList.add("3");    //待上课
//        statusList.add("4");    //
        statusList.add("5");    //待评价
        statusList.add("6");    //完成
        statusList.add("7");    //投诉
        statusList.add("8");    //待退款
        statusList.add("0");    //取消
        String url = Constants.URL_VALUE + "orderList";
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", App.getInstance().getParentId());
//        params.put("orderStatus", statusList);
        params.put("page", page);
        params.put("pageSize", 15);
        VolleyManager.getInstance().sendGsonRequest(null, url, params,
                SPUtils.getData(SPUtils.TOKEN, null), OrderListBean.class,
                this, this);
    }

    /**
     * 获取待付款的订单
     * @param parentId
     * @param page
     */
    @Override
    public void getBOrders(String parentId, int page) {
        onLoadCallBack.onPreLoad();
        List<String> statusList = new ArrayList<>();
        statusList.add("1");    //待付款
        String url = Constants.URL_VALUE + "orderList";
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", App.getInstance().getParentId());
        params.put("orderStatus", "1");
        params.put("page", page);
        params.put("pageSize", 15);
        VolleyManager.getInstance().sendGsonRequest(null, url, params,
                SPUtils.getData(SPUtils.TOKEN, null), OrderListBean.class,
                this, this);
    }

    /**
     * 获取待上课的订单
     * @param parentId
     * @param page
     */
    @Override
    public void getCOrders(String parentId, int page) {
        onLoadCallBack.onPreLoad();
        List<String> statusList = new ArrayList<>();
        statusList.add("3");    //待上课
        String url = Constants.URL_VALUE + "orderList";
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", App.getInstance().getParentId());
        params.put("orderStatus", "3");
        params.put("page", page);
        params.put("pageSize", 15);
        VolleyManager.getInstance().sendGsonRequest(null, url, params,
                SPUtils.getData(SPUtils.TOKEN, null), OrderListBean.class,
                this, this);
    }

    /**
     * 获取待评价的订单
     * @param parentId
     * @param page
     */
    @Override
    public void getDOrders(String parentId, int page) {
        onLoadCallBack.onPreLoad();
        List<String> statusList = new ArrayList<>();
        statusList.add("5");    //待评价
        String url = Constants.URL_VALUE + "orderList";
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", App.getInstance().getParentId());
        params.put("orderStatus", "5");
        params.put("page", page);
        params.put("pageSize", 15);
        VolleyManager.getInstance().sendGsonRequest(null, url, params,
                SPUtils.getData(SPUtils.TOKEN, null), OrderListBean.class,
                this, this);
    }

    /**
     * 获取等待老师抢单的订单
     * @param parentId
     * @param page
     * @param onLoadCallBack
     */
    @Override
    public void getNoTeacherOrders(String parentId, String orderId, int page, final OnLoadCallBack onLoadCallBack) {
        onLoadCallBack.onPreLoad();
//        List<String> statusList
        String url = Constants.URL_VALUE + "orderGrabList";
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", parentId);
        params.put("orderInfoId", "2");
//        params.put("page", page);
//        params.put("pageSize", 50);
        VolleyManager.getInstance().sendGsonRequest(null, url, params,
                SPUtils.getData(SPUtils.TOKEN, null), OrderGrabBean.class,
                new Response.Listener<OrderGrabBean>() {
                    @Override
                    public void onResponse(OrderGrabBean response) {
                        onLoadCallBack.onLoadSuccess(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onLoadCallBack.onLoadFailed(error.toString());
                    }
                });
    }

    @Override
    public void onResponse(OrderListBean response) {
        onLoadCallBack.onLoadSuccess(response);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        onLoadCallBack.onLoadFailed(error.toString());
    }

//    @Override
//    public void beginClass(String teacherId, final String orderInfoId) {
//        onLoadCallBack.onPreLoad();
//
//    }

    public class OrderListBean extends BaseBean {
        List<OrderEntity> orderList;

        public List<OrderEntity> getOrderList() {
            return orderList;
        }

        public void setOrderList(List<OrderEntity> orderList) {
            this.orderList = orderList;
        }
    }

    public class OrderGrabBean extends BaseBean {
        List<TeacherInfoBean> teacherList;
        int sum;
        List<WaitingOrder> orderList;

        public List<WaitingOrder> getOrderList() {
            return orderList;
        }

        public void setOrderList(List<WaitingOrder> orderList) {
            this.orderList = orderList;
        }

        public int getSum() {
            return sum;
        }

        public void setSum(int sum) {
            this.sum = sum;
        }

        public List<TeacherInfoBean> getTeacherList() {
            return teacherList;
        }

        public void setTeacherList(List<TeacherInfoBean> teacherList) {
            this.teacherList = teacherList;
        }
    }

    public static class WaitingOrder extends OrderEntity{
        int num;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }

}
