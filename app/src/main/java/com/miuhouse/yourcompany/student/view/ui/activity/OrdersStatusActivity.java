package com.miuhouse.yourcompany.student.view.ui.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.application.ActivityManager;
import com.miuhouse.yourcompany.student.db.AccountDBTask;
import com.miuhouse.yourcompany.student.interactor.OrderManageInteractor;
import com.miuhouse.yourcompany.student.model.OrderEntity;
import com.miuhouse.yourcompany.student.model.TeacherInfoBean;
import com.miuhouse.yourcompany.student.presenter.OrdersStatusPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.IOrdersStatusPresenter;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.utils.Values;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IOrdersStatusActivity;
import com.miuhouse.yourcompany.student.view.ui.adapter.NoTeacherOrderAdapter;
import com.miuhouse.yourcompany.student.view.ui.adapter.TeachersAdapter;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;
import com.miuhouse.yourcompany.student.view.widget.RecyclerViewItemDecoration;
import com.miuhouse.yourcompany.student.view.widget.ViewOverrideManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by khb on 2016/9/18.
 */
public class OrdersStatusActivity extends BaseActivity implements IOrdersStatusActivity, SwipeRefreshLayout.OnRefreshListener, TeachersAdapter.TeachersItemOnclickListener, NoTeacherOrderAdapter.NoTOnclickListener {

    private SwipeRefreshLayout refresh;
    private RecyclerView teachersList;
    private int refresh_status = CANREFRESH;
    private static final int CANREFRESH = 1;
    private static final int CANLOADMORE = 2;
    private static final int LOADFAILED = -1;
    private static final int LOADING = 0;

    private int page = 1;
    private IOrdersStatusPresenter presenter;
    private TeachersAdapter adapter;
    private RelativeLayout rlOrderSelect;
    private RecyclerView rVOrderList;
    private List<TeacherInfoBean> list;
    private List<OrderManageInteractor.WaitingOrder> orderList;
    private NoTeacherOrderAdapter noTeacherAdapter;
    private int orderListHeight;
    private int orderSelectHeight;
    private int hideHeight;
    private int showHeight;
    private ImageView arrow;
    private boolean isOrderListShowing = false;
    private View transparentBg;
    private ImageView ivOrderHead;
    private TextView tvOrderType;
    private TextView tvBeginTime;
    private TextView tvTCount;

    private boolean isOrderSelectShowing = true;
    private RelativeLayout orderContent;
    private int ordercontentHeight;
    private RelativeLayout.LayoutParams refreshLp;
    //默认显示选中的订单id
    private String defaultOrderId;

    public static final String EXTRA_ORDERID = "defaultOrderId";

    @Override
    protected String setTitle() {
        return "最新订单状态";
    }

    @Override
    protected String setRight() {
        return null;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_ordersstatus;
    }

    @Override
    protected View getOverrideParentView() {
        return findViewById(R.id.content);
    }

    @Override
    protected void initViewAndEvents() {
        defaultOrderId = getIntent().getStringExtra("defaultOrderId");
        presenter = new OrdersStatusPresenter(this);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        teachersList = (RecyclerView) findViewById(R.id.teachersList);
        list = new ArrayList<>();
        adapter = new TeachersAdapter(this, list, this);
        teachersList.setAdapter(adapter);
        teachersList.addItemDecoration(
                new RecyclerViewItemDecoration(this,
                        Util.dip2px(this, 10),
                        Util.dip2px(this, 10),
                        Util.dip2px(this, 10),
                        Util.dip2px(this, 10)));
        refreshLp = (RelativeLayout.LayoutParams) refresh.getLayoutParams();
        refresh.setColorSchemeResources(R.color.themeColor);
        refresh.setOnRefreshListener(this);
        teachersList.setLayoutManager(new LinearLayoutManager(this));

        orderContent = (RelativeLayout) findViewById(R.id.orderContent);
        orderContent.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        ordercontentHeight = orderContent.getMeasuredHeight();
        rlOrderSelect = (RelativeLayout) findViewById(R.id.orderSelect);
        tvOrderType = (TextView) findViewById(R.id.orderType);
        ivOrderHead = (ImageView) findViewById(R.id.orderHead);
        tvBeginTime = (TextView) findViewById(R.id.beginTime);
        tvTCount = (TextView) findViewById(R.id.teacherCount);
        rVOrderList = (RecyclerView) findViewById(R.id.orderList);
        arrow = (ImageView) findViewById(R.id.arrow);
        transparentBg = findViewById(R.id.transparentBg);
        transparentBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNoTeacherOrderList(false);
            }
        });
        showArrow();
        orderSelectHeight = getResources().getDimensionPixelSize(R.dimen.teacherSelectHeadHeight);
        rlOrderSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNoTeacherOrderList(isOrderListShowing);
            }
        });

        orderList = new ArrayList<>();
        rVOrderList.setLayoutManager(new LinearLayoutManager(this));
        noTeacherAdapter = new NoTeacherOrderAdapter(this, orderList, this);
        rVOrderList.setAdapter(noTeacherAdapter);
        presenter.getNoTeacherOrders(AccountDBTask.getAccount().getId(), defaultOrderId);

        teachersList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisibleItem;
            private int firstVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == noTeacherAdapter.getItemCount()
                        && firstVisibleItem != 0
                        && ! recyclerView.canScrollVertically(1)) {
                    if (refresh_status != LOADING) {
                        if (refresh_status == CANLOADMORE) {
                            page += 1;
                        } else if (refresh_status == LOADFAILED) {
                            page = page;
                        }
                        refresh_status = LOADING;
                        presenter.getInterestedTeachers(AccountDBTask.getAccount().getId(),
                                orderList.get(noTeacherAdapter.getSelectedIndex()).getId());
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

    private void showOrderSelect(boolean showOrderSelect) {
        ValueAnimator animator = null;
        ViewWrapper vw = new ViewWrapper(orderContent);
        if (!showOrderSelect) {
            animator = ObjectAnimator.ofInt(vw, "top", 0, -ordercontentHeight - 100);
            animator.setDuration(200);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    orderContent.invalidate();
                }
            });
            animator.start();

        } else {
            animator = ObjectAnimator.ofInt(vw, "top", -ordercontentHeight - 100, 0);
            animator.setDuration(200);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    orderContent.invalidate();
                }
            });
            animator.start();
        }
    }


    private void showArrow() {
        if (isOrderListShowing) {
            arrow.setImageResource(R.mipmap.img_up);
        } else {
            arrow.setImageResource(R.mipmap.img_down);
        }
    }

    @Override
    public void showNoTeacherOrderList(boolean goingDown) {
        ViewWrapper vp = new ViewWrapper(rVOrderList);
        ValueAnimator animator = null;
        if (!isOrderListShowing) {
            transparentBg.setVisibility(View.VISIBLE);
            animator = ObjectAnimator.ofInt(vp, "top",
                    hideHeight, showHeight);
        } else {
            transparentBg.setVisibility(View.GONE);
            animator = ObjectAnimator.ofInt(vp, "top",
                    showHeight, hideHeight);
        }
        animator.setDuration(100);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rVOrderList.invalidate();
                arrow.invalidate();
            }
        });
        isOrderListShowing = !isOrderListShowing;
        showArrow();
        animator.start();
    }

    private class ViewWrapper {
        private int top;
        private View view;

        public ViewWrapper(View view) {
            this.view = view;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();
            lp.setMargins(0, top, 0, 0);
            view.setLayoutParams(lp);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRefresh() {
        if (refresh_status != LOADING) {
            refresh_status = LOADING;
            page = 1;
            presenter.getInterestedTeachers(AccountDBTask.getAccount().getId(),
                    orderList.get(noTeacherAdapter.getSelectedIndex()).getId());
        }
    }

    @Override
    public void showTeachers(List<TeacherInfoBean> teacherList) {
        refresh_status = CANLOADMORE;
        if (page == 1) {
            list.clear();
        }
//        TeacherInfoBean teacher = new TeacherInfoBean();
//        teacher.setCollege("野鸡大学");
//        teacher.setEducation("吊的飞起");
//        teacher.setGrade("人家还小");
//        teacher.setHeadUrl("http://img1.imgtn.bdimg.com/it/u=1711878437,2645714142&fm=21&gp=0.jpg");
//        teacher.settName("痴汉家教");
//        teacher.setSex("男");
////        TeacherDetailBean td = new TeacherDetailBean();
////        td.setTeacherInfo(teacher);
//        list.add(teacher);
//        list.add(teacher);
//        list.add(teacher);
//        list.add(teacher);
        showSelectedOrder(orderList.get(noTeacherAdapter.getSelectedIndex()));
        list.addAll(teacherList);
        adapter.notifyDataSetChanged();
        hideListLoading();
    }

    @Override
    public void showNoTeacherOrders(List<OrderManageInteractor.WaitingOrder> list) {
//        OrderEntity order = new OrderEntity();
//        order.setMajorDemand("1");
//        for (int i = 0; i<5; i++){
//            order.setClassBeginTimePromise(System.currentTimeMillis() + new Random().nextLong());
//            orderList.add((OrderManageInteractor.WaitingOrder) order);
//        }
        orderList.clear();
        orderList.addAll(list);
        noTeacherAdapter.notifyDataSetChanged();

//        noTeacherAdapter.setSelectedIndex(1);
        RelativeLayout.LayoutParams params
                = (RelativeLayout.LayoutParams) rVOrderList.getLayoutParams();

        if (orderList.size() > 4) {    //大于4行时固定4行高度
            orderListHeight = getResources().getDimensionPixelSize(R.dimen.item_noteacherorder_height) * 4;
            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    orderListHeight);
            rVOrderList.setLayoutParams(params);
        } else {
            rVOrderList.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            orderListHeight = rVOrderList.getMeasuredHeight();
        }

        hideHeight = orderSelectHeight - orderListHeight;
        showHeight = orderSelectHeight;
        params.setMargins(0, hideHeight, 0, 0);
        rVOrderList.setLayoutParams(params);
        L.i("ORDER LIST global layout: " + orderListHeight + " " + orderSelectHeight);

    }

    @Override
    public void showSelectedOrder(OrderManageInteractor.WaitingOrder order) {
        tvOrderType.setText(Values.getValue(Values.majorDemand,
                Integer.parseInt(order.getMajorDemand())));
        tvBeginTime.setText(new SimpleDateFormat("上课时间 MM/dd HH:mm")
                .format(new Date(
                        order.getClassBeginTimePromise())));
        tvTCount.setText(order.getNum() + "");
        Glide.with(this).load(Util.getResourceId(this, String.format("ico_ordertype_%s", order.getMajorDemand()), "mipmap"))
                .placeholder(R.mipmap.ico_head_default)
                .error(R.mipmap.ico_head_default)
                .into(ivOrderHead);
    }

    @Override
    public void onOrderClick(OrderEntity order, int position) {
        showNoTeacherOrderList(false);
        presenter.getInterestedTeachers(AccountDBTask.getAccount().getId(),
                order.getId());
    }

    @Override
    public void onItemClick(Object data, int position) {
        TeacherInfoBean teacher = (TeacherInfoBean) data;
        startActivity(new Intent(activity, DetailpagesActivity.class).putExtra("teacherId", teacher.getId()));
    }

    @Override
    public void onButtonClick(Object data, int position) {
        TeacherInfoBean teacher = (TeacherInfoBean) data;
        String orderId = orderList.get(noTeacherAdapter.getSelectedIndex()).getId();
        String orderType = orderList.get(noTeacherAdapter.getSelectedIndex()).getOrderType();
        presenter.selectTeacher(orderId, orderType, teacher.getId(), AccountDBTask.getAccount().getId());
    }

    @Override
    public void goToSuccessPage(String orderId, String orderType) {
        startActivity(new Intent(activity, SelectSuccessActivity.class)
                        .putExtra("orderId", orderId)
                        .putExtra("orderType", orderType)
        );
    }

    @Override
    public void showLoading(String msg) {
        super.showLoading(msg);
    }

    @Override
    public void hideLoading() {
        L.i("hideloading");
        super.hideLoading();
    }

    @Override
    public void showError(int type) {
        if (type == ViewOverrideManager.NO_ORDER) {
            viewOverrideManager.showLoading(type, new ViewOverrideManager.OnExceptionalClick() {
                @Override
                public void onExceptionalClick() {
                    finish();
                    ActivityManager.getInstance().finishAll();
                    startActivity(new Intent(context, MainActivity.class));
                }
            });
        }else if (type == ViewOverrideManager.NO_NETWORK){
            viewOverrideManager.showLoading(type, null);
        }
    }

    @Override
    public void showListLoading() {
        if (!refresh.isRefreshing()) {
            refresh.post(new Runnable() {
                @Override
                public void run() {
                    refresh.setRefreshing(true);
                }
            });
        }
    }

    @Override
    public void hideListLoading() {
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
        }
    }
}
