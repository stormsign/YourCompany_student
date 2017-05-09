package com.miuhouse.yourcompany.student.view.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.interactor.SysMsgInteractor;
import com.miuhouse.yourcompany.student.listener.OnListItemClick;
import com.miuhouse.yourcompany.student.model.SysMsgEntity;
import com.miuhouse.yourcompany.student.presenter.SysMsgPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.ISysMsgPresenter;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.ISysMsgActivity;
import com.miuhouse.yourcompany.student.view.ui.adapter.SysMsgAdapter;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;
import com.miuhouse.yourcompany.student.view.widget.ViewOverrideManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khb on 2016/7/14.
 */
public class SysMsgActivity extends BaseActivity implements ISysMsgActivity, OnListItemClick, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout refresh;
    private RecyclerView sysMsgList;

    private int page = 1;

    private List<SysMsgEntity> list;
    private SysMsgAdapter adapter;
    private LinearLayout content;
    private ISysMsgPresenter presenter;

    private int refresh_status = CANREFRESH;
    private static final int CANREFRESH = 1;
    private static final int CANLOADMORE = 2;
    private static final int LOADFAILED = -1;
    private static final int LOADING = 0;

    @Override
    protected String setTitle() {
        return "系统通知";
    }

    @Override
    protected String setRight() {
        return null;
    }

    @Override
    protected void initViewAndEvents() {
        list = new ArrayList<>();
        presenter = new SysMsgPresenter(this);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        sysMsgList = (RecyclerView) findViewById(R.id.sysMsgList);
        content = (LinearLayout) findViewById(R.id.content);
        refresh.setColorSchemeResources(R.color.themeColor);
        refresh.setOnRefreshListener(this);
        adapter = new SysMsgAdapter(context, list, this);
        sysMsgList.setAdapter(adapter);
        sysMsgList.setLayoutManager(new LinearLayoutManager(this));
        sysMsgList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        presenter.getMsgs(page);
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
        if (refresh_status != LOADING) {
            page = 1;
            presenter.getMsgs(page);
            refresh_status = LOADING;
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_sysmsg;
    }

    @Override
    protected View getOverrideParentView() {
        return content;
    }

    @Override
    public void refresh(SysMsgInteractor.SysMsgBean bean) {
        refresh_status = CANLOADMORE;
        if (page == 1) {
            list.clear();
        }
        list.addAll(bean.getNoticeList());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(Object data) {

    }

    @Override
    public void showLoading(String msg) {
//        super.showLoading(msg);
        if (!refresh.isRefreshing()){
            refresh.post(new Runnable() {
                @Override
                public void run() {
                    refresh.setRefreshing(true);
                }
            });
        }
    }

    @Override
    public void hideLoading() {
//        super.hideLoading();
        if (refresh.isRefreshing()){
            refresh.setRefreshing(false);
        }
    }

    @Override
    public void showError(int type) {
        refresh_status = LOADFAILED;
        if (type == ViewOverrideManager.NO_MSG) {
            viewOverrideManager.showLoading(type, null);
        }else if (type == ViewOverrideManager.NO_NETWORK){
            viewOverrideManager.showLoading(type, new ViewOverrideManager.OnExceptionalClick() {
                @Override
                public void onExceptionalClick() {
                    if (refresh_status != LOADING) {
                        refresh_status = LOADING;
                        page = 1;
                        presenter.getMsgs(page);
                    }
                }
            });
        }
    }

    @Override
    public void onRefresh() {
//        if (refresh.isRefreshing()) {
//            refresh.setRefreshing(false);
//        }
        if (refresh_status != LOADING){
            refresh_status = LOADING;
            page = 1;
            presenter.getMsgs(page);
        }
    }
}
