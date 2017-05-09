package com.miuhouse.yourcompany.student.view.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.application.ActivityManager;
import com.miuhouse.yourcompany.student.db.AccountDBTask;
import com.miuhouse.yourcompany.student.interactor.OrderManageInteractor;
import com.miuhouse.yourcompany.student.model.OrderEntity;
import com.miuhouse.yourcompany.student.presenter.OrderManagePresenter;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.view.ui.activity.MainActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.OrderDetailActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.OrderLongTermDetailActivity;
import com.miuhouse.yourcompany.student.view.ui.adapter.OrderAdapter;
import com.miuhouse.yourcompany.student.view.ui.base.BaseFragment;
import com.miuhouse.yourcompany.student.view.ui.fragment.interf.IOrderManageFragment;
import com.miuhouse.yourcompany.student.view.widget.ViewOverrideManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单管理
 * 待评价订单
 * Created by khb on 2016/7/18.
 */
public class FragmentD extends BaseFragment implements IOrderManageFragment, SwipeRefreshLayout.OnRefreshListener, OrderAdapter.OnOrderClick {

    private RecyclerView blist;
    private SwipeRefreshLayout refresh;

    private List<OrderEntity> list;
    private OrderAdapter adapter;
    private OrderManagePresenter presenter;

    private int page = 1;
    private String teacherId;

    private int refresh_status = CANREFRESH;
    private static final int CANREFRESH = 1;
    private static final int CANLOADMORE = 2;
    private static final int LOADFAILED = -1;
    private static final int LOADING = 0;
    private LinearLayout content;

    @Override
    public int getFragmentResourceId() {
        return R.layout.fragment_a;
    }

    @Override
    public void getViews(View view) {
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.aRefresh);
        blist = (RecyclerView) view.findViewById(R.id.aList);
        content = (LinearLayout) view.findViewById(R.id.content);
    }

    @Override
    public void setupView() {
        list = new ArrayList<>();
        teacherId = AccountDBTask.getAccount().getId();
//        teacherId = "4028b88155c4dd070155c4dd8a340000";

        presenter = new OrderManagePresenter(this);
        refresh.setOnRefreshListener(this);
        refresh.setColorSchemeResources(R.color.themeColor);
        blist.setLayoutManager(new LinearLayoutManager(context));
        adapter = new OrderAdapter(context, list, 3);
        adapter.setOnOrderClick(this);
        blist.setAdapter(adapter);
//        presenter.getDOrders(teacherId, page);
        blist.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        presenter.getDOrders(teacherId, page);
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
    }

    @Override
    public View getOverrideParentView() {
        return content;
    }

    @Override
    public void refresh(OrderManageInteractor.OrderListBean bean) {
        refresh_status = CANLOADMORE;
        if (page == 1) {
            list.clear();
        }
        list.addAll(bean.getOrderList());
        adapter.notifyDataSetChanged();
        hideLoading();
    }

    @Override
    public void goToDetail(OrderEntity order, int reqCode) {

    }

    @Override
    public void onRefresh() {
        if (refresh_status != LOADING) {
            page = 1;
            presenter.getDOrders(teacherId, page);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (refresh_status != LOADING) {
            page = 1;
            presenter.getDOrders(teacherId, page);
        }
    }

    @Override
    public void showLoading(String msg) {

        if (!refresh.isRefreshing()) {
            refresh.post(new Runnable() {
                @Override
                public void run() {
                    refresh.setRefreshing(true);
                }
            });
        }
//        super.showLoading(msg);
    }

    @Override
    public void showError(int type) {
//        super.showError(msg);
        refresh_status = LOADFAILED;
        if (type == ViewOverrideManager.NO_COMMENT) {
            if (page == 1) {
                viewOverrideManager.showLoading(type, new ViewOverrideManager.OnExceptionalClick() {
                    @Override
                    public void onExceptionalClick() {
                        ActivityManager.getInstance().finishAll();
                        startActivity(new Intent(context, MainActivity.class));
                    }
                });
            }
        } else if (type == ViewOverrideManager.NO_NETWORK) {
            viewOverrideManager.showLoading(type, new ViewOverrideManager.OnExceptionalClick() {
                @Override
                public void onExceptionalClick() {
                    if (refresh_status != LOADING) {
                        refresh_status = LOADING;
                        page = 1;
                        presenter.getDOrders(teacherId, page);
                    }
                }
            });
        }
    }

    @Override
    public void hideLoading() {
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
        }
        super.hideLoading();
    }

    @Override
    public void hideError() {
        super.hideError();
    }

    @Override
    public void onOrderClick(OrderEntity order) {
        if (order.getOrderType().equals(Constants.TYPE_SMALL)) {
            startActivity(new Intent(context, OrderDetailActivity.class)
                    .putExtra("orderId", order.getId()));
        } else {
            startActivity(new Intent(context, OrderLongTermDetailActivity.class)
                            .putExtra("orderId", order.getId())
                            .putExtra("orderType", order.getOrderType())
            );
        }
    }

//    @Override
//    public void onButtonClick(OrderEntity order) {
//
//    }

//    @Override
//    public void beforeBeginClass() {
//
//    }
//
//    @Override
//    public void afterBeginClass() {
//
//    }
}
