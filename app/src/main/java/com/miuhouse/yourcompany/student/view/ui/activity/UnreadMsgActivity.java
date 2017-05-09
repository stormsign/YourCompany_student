package com.miuhouse.yourcompany.student.view.ui.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.db.AccountDBTask;
import com.miuhouse.yourcompany.student.listener.OnListItemClick;
import com.miuhouse.yourcompany.student.model.CommentsBean;
import com.miuhouse.yourcompany.student.presenter.UnreadMsgPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.IUnreadMsgPresenter;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IUnreadMsg;
import com.miuhouse.yourcompany.student.view.ui.adapter.UnreadMsgAdapter;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;
import com.miuhouse.yourcompany.student.view.widget.RecyclerViewItemDecoration;
import com.miuhouse.yourcompany.student.view.widget.ViewOverrideManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khb on 2017/1/17.
 */
public class UnreadMsgActivity extends BaseActivity implements IUnreadMsg, OnListItemClick, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout refresh;
    private RecyclerView list;

    private List<CommentsBean> msgList = new ArrayList<>();
    private UnreadMsgAdapter adapter;

    private final int REQUSET_DETAIL = 1;

    public static final int RESULT_ERROR = -1;
    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_EMPTY = 2;

    private IUnreadMsgPresenter presenter;
    private int page;
    private String id;

    private static final int CANREFRESH = 1;
    private static final int CANLOADMORE = 2;
    private static final int LOADFAILED = -1;
    private static final int LOADING = 0;
    private int refresh_status = CANREFRESH;

    @Override
    protected String setTitle() {
        return "消息";
    }

    @Override
    protected String setRight() {
        return null;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_unreadmsg;
    }

    @Override
    protected View getOverrideParentView() {
        return refresh;
    }

    @Override
    protected void initViewAndEvents() {
        presenter = new UnreadMsgPresenter(this);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refresh.setOnRefreshListener(this);
        refresh.setColorSchemeResources(R.color.themeColor);
        list = (RecyclerView) findViewById(R.id.msgList);
        adapter = new UnreadMsgAdapter(this, msgList, this);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.addItemDecoration(new RecyclerViewItemDecoration(this));
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
                        presenter.getUnreadMsg(id, page);
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
        page = 1;
        id = AccountDBTask.getAccount().getId();
        presenter.getUnreadMsg(id, page);
    }

    @Override
    public void showUnreaedList(List<CommentsBean> commentList) {
        refresh_status = CANLOADMORE;
        if (page == 1) {
            msgList.clear();
        }
        msgList.addAll(commentList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(Object data) {
        CommentsBean comment = (CommentsBean) data;
        startActivityForResult(new Intent(this, DetailTwitterActivity.class)
            .putExtra("schoolNewsId", comment.getNewsId()), REQUSET_DETAIL);
    }

    @Override
    public void onRefresh() {
        if (refresh_status != LOADING) {
            refresh_status = LOADING;
            page = 1;
            presenter.getUnreadMsg(id, page);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUSET_DETAIL){
            if (resultCode == RESULT_OK){
                page = 1;
                presenter.getUnreadMsg(id, page);
            }
        }
    }

    @Override
    public void showLoading(String msg) {
        if (!refresh.isRefreshing()){
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
        switch (type){
            case RESULT_ERROR:
                viewOverrideManager.showLoading(ViewOverrideManager.NO_NETWORK, new ViewOverrideManager.OnExceptionalClick() {
                    @Override
                    public void onExceptionalClick() {
                        if (refresh_status != LOADING) {
                            refresh_status = LOADING;
                            page = 1;
                            presenter.getUnreadMsg(id, page);
                        }
                    }
                });
                break;
            case RESULT_EMPTY:
                viewOverrideManager.showLoading(ViewOverrideManager.NO_MSG, null);
                break;
        }
    }

    @Override
    public void hideLoading() {
        if (refresh.isRefreshing()){
            refresh.setRefreshing(false);
        }
//        super.hideLoading();
    }
}
