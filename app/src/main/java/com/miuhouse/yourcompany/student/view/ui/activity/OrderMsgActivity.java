package com.miuhouse.yourcompany.student.view.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.application.ActivityManager;
import com.miuhouse.yourcompany.student.db.AccountDBTask;
import com.miuhouse.yourcompany.student.listener.OnListItemClick;
import com.miuhouse.yourcompany.student.model.OrderMsgBean;
import com.miuhouse.yourcompany.student.presenter.OrderMsgPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.IOrderMsgPresenter;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.utils.SPUtils;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IOrderMsgActivity;
import com.miuhouse.yourcompany.student.view.ui.adapter.OrderMsgAdapter;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;
import com.miuhouse.yourcompany.student.view.widget.RecyclerViewItemDecoration;
import com.miuhouse.yourcompany.student.view.widget.ViewOverrideManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khb on 2016/7/26.
 */
public class OrderMsgActivity extends BaseActivity implements IOrderMsgActivity, SwipeRefreshLayout.OnRefreshListener, OnListItemClick {

    private LinearLayout content;
    private SwipeRefreshLayout refresh;
    private RecyclerView list;
//    private List<OrderMsgBean> msgList;
//    private OrderMsgAdapter adapter;

    private IOrderMsgPresenter presenter;
    private String parentId;
    private int page = 1;

    private int refresh_status = CANREFRESH;
    private static final int CANREFRESH = 1;
    private static final int CANLOADMORE = 2;
    private static final int LOADFAILED = -1;
    private static final int LOADING = 0;
    private List<OrderMsgBean> msgList;
    private OrderMsgAdapter adapter;

    @Override
    protected String setTitle() {
        return "订单消息";
    }

    @Override
    protected String setRight() {
        return null;
    }

    @Override
    protected void initViewAndEvents() {
        msgList = new ArrayList<>();
        presenter = new OrderMsgPresenter(this);
        content = (LinearLayout) findViewById(R.id.content);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        list = (RecyclerView) findViewById(R.id.purseMsgList);
        refresh.setColorSchemeResources(R.color.themeColor);
        refresh.setOnRefreshListener(this);
        list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderMsgAdapter(this, msgList, this);
        list.setAdapter(adapter);
        parentId = AccountDBTask.getAccount().getId();
//        parentId = "4028b88155c4dd070155c4dd8a340000";
        list.addItemDecoration(new RecyclerViewItemDecoration(this,
                Util.dip2px(this, 10),
                Util.dip2px(this, 10),
                Util.dip2px(this, 10),
                0
        ));
        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisibleItem;
            private int firstVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == adapter.getItemCount()
                        && firstVisibleItem != 0) {
                    if (refresh_status != LOADING) {
                        if (refresh_status == CANLOADMORE) {
                            page += 1;
                        } else if (refresh_status == LOADFAILED) {
                            page = page;
                        }
                        refresh_status = LOADING;
                        presenter.getOrderMsgs(parentId, page);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            L.i("permission");
        } else {
            Constants.IMEI_VALUE = Util.getIMEI(this);
        }

        if (refresh_status != LOADING) {
            page = 1;
            presenter.getOrderMsgs(parentId, page);
            refresh_status = LOADING;
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_pursemsg;
    }

    @Override
    protected View getOverrideParentView() {
        return content;
    }

    @Override
    public void onRefresh() {
        if (refresh_status != LOADING) {
            page = 1;
            presenter.getOrderMsgs(parentId, page);
            refresh_status = LOADING;
        }
    }

    @Override
    public void refresh(List<OrderMsgBean> data) {
        refresh_status = CANLOADMORE;
        //        重置新通知数
        SPUtils.saveData(Constants.UNREAD_ORDERMSG_COUNT, 0);
        List<OrderMsgBean> list = data;
        msgList.clear();
        msgList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(Object data) {
        OrderMsgBean orderMsg = (OrderMsgBean) data;
        switch (Integer.parseInt(orderMsg.getOrderMsgType())){
            case 1:     //有老师抢单
                startActivity(new Intent(context, OrdersStatusActivity.class)
                        .putExtra(OrdersStatusActivity.EXTRA_ORDERID, orderMsg.getOrderId()));
                break;
            case 2:     //开始上课
                startActivity(new Intent(context, OrderDetailActivity.class)
                        .putExtra("orderId", orderMsg.getOrderId()));
                break;
            case 3:     //上课结束（老师已上完课）
                startActivity(new Intent(context, OrderDetailActivity.class)
                        .putExtra("orderId", orderMsg.getOrderId()));
                break;
            case 4:     //退款成功
                startActivity(new Intent(context, OrderDetailActivity.class)
                        .putExtra("orderId", orderMsg.getOrderId()));
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                L.i("permission granted");
                Constants.IMEI_VALUE = Util.getIMEI(this);
                if (refresh_status != LOADING) {
                    page = 1;
                    presenter.getOrderMsgs(parentId, page);
                    refresh_status = LOADING;
                }
            } else {
                L.i("permission denied");
                Toast.makeText(this, "权限被拒绝", Toast.LENGTH_LONG).show();
                finish();
                ActivityManager.getInstance().finishAll();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void showLoading(String msg) {
//        super.showLoading(msg);
        refresh.post(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(true);
            }
        });

    }

    @Override
    public void hideLoading() {
        if (refresh.isRefreshing()) {
            refresh.post(new Runnable() {
                @Override
                public void run() {
                    refresh.setRefreshing(false);
                }
            });
        }
    }

    @Override
    public void showError(int type) {
//        super.showError(type);
        refresh_status = LOADFAILED;
        if (type == ViewOverrideManager.NO_MSG) {
            viewOverrideManager.showLoading(type, null);
        }else if (type == ViewOverrideManager.NO_NETWORK){
            viewOverrideManager.showLoading(type, new ViewOverrideManager.OnExceptionalClick() {
                @Override
                public void onExceptionalClick() {
                    page = 1;
                    presenter.getOrderMsgs(parentId, page);
                }
            });
        }
    }
}
