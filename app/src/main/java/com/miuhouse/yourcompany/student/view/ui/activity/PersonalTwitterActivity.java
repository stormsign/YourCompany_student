package com.miuhouse.yourcompany.student.view.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.application.App;
import com.miuhouse.yourcompany.student.listener.OnListItemClick;
import com.miuhouse.yourcompany.student.model.SchoolNewsBean;
import com.miuhouse.yourcompany.student.model.SchoolNewsListBean;
import com.miuhouse.yourcompany.student.presenter.TwitterPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.ITwitterPresenter;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.view.ui.adapter.TwitterAdapter;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;
import com.miuhouse.yourcompany.student.view.ui.fragment.DongTaiFragment;
import com.miuhouse.yourcompany.student.view.ui.fragment.interf.ITwitterFragment;

import com.miuhouse.yourcompany.student.view.widget.ViewOverrideManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kings on 1/18/2017.
 */
public class PersonalTwitterActivity extends BaseActivity
    implements SwipeRefreshLayout.OnRefreshListener,
    OnListItemClick, ITwitterFragment {
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private TwitterAdapter twitterAdapter;
    private ITwitterPresenter twitterPresenter;
    private List<SchoolNewsBean> list = new ArrayList<>();

    private int page = 1;

    private String linkId;

    @Override protected String setTitle() {
        return "个人动态";
    }

    @Override protected String setRight() {
        return null;
    }

    @Override protected void initViewAndEvents() {

        RecyclerView twitterRecycler = (RecyclerView) findViewById(R.id.recycler_my_twitter);
        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mySwipeRefreshLayout.setColorSchemeResources(R.color.themeColor);
        mySwipeRefreshLayout.setOnRefreshListener(this);
        twitterRecycler.setLayoutManager(new LinearLayoutManager(this));

        twitterPresenter = new TwitterPresenter(this);

        twitterAdapter = new TwitterAdapter(this, list, this);
        twitterRecycler.setAdapter(twitterAdapter);
        linkId = App.getInstance().getParentId();
        twitterPresenter.getSchoolTwitterList(App.getInstance().getParentId(),
            String.valueOf(App.getInstance().getParentInfo().getSchoolId()), null,
            linkId, page,
            Constants.PAGE_SIZE);

        twitterRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private int lastVisibleItem;
            private int firstVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == twitterAdapter.getItemCount()
                    && firstVisibleItem != 0
                    && !mySwipeRefreshLayout.isRefreshing()) {
                    page += 1;
                    twitterPresenter.getSchoolTwitterList(App.getInstance().getParentId(), "1",
                        null,
                        App.getInstance().getParentId(), page,
                        Constants.PAGE_SIZE);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem =
                    ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                firstVisibleItem =
                    ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            }
        });
    }

    @Override protected int getContentLayoutId() {
        return R.layout.activity_personal_twitter;
    }

    @Override protected View getOverrideParentView() {
        return mySwipeRefreshLayout;
    }

    @Override public void getSchoolNewListBean(SchoolNewsListBean schoolNewsListBean) {
        if (page == 1) {
            list.clear();
        }
        list.addAll(schoolNewsListBean.getSchoolNewsInfos());
        twitterAdapter.notifyDataSetChanged();

        if (mySwipeRefreshLayout.isRefreshing()) {
            mySwipeRefreshLayout.setRefreshing(false);
        }
    }

    //@Override public void showClasses() {
    //
    //}
    //
    //@Override public void hideClasses() {
    //
    //}

    @Override public void unreadDisplayControll(boolean displayFlag) {

    }

    @Override public void onItemClick(Object data) {

    }

    @Override public void onRefresh() {
        page = 1;
        twitterPresenter.getSchoolTwitterList(App.getInstance().getParentId(), "1", null,
            linkId, page,
            Constants.PAGE_SIZE);
    }

    @Override public void showLoading(String msg) {
        if (!mySwipeRefreshLayout.isRefreshing()) {
            mySwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mySwipeRefreshLayout.setRefreshing(true);
                }
            });
        }
    }

    @Override
    public void showError(int type) {
        if (null == viewOverrideManager) {
            viewOverrideManager = new ViewOverrideManager(getOverrideParentView());
        }
        if (type == ViewOverrideManager.NO_CONTENT) {
            viewOverrideManager.showLoading(ViewOverrideManager.NO_PERSONAL_CONTENT, null);
        } else if (type == DongTaiFragment.ERROR) {
            viewOverrideManager.showLoading(ViewOverrideManager.NO_NETWORK,
                new ViewOverrideManager.OnExceptionalClick() {
                    @Override public void onExceptionalClick() {
                        mySwipeRefreshLayout.setRefreshing(true);
                        page = 1;
                        twitterPresenter.getSchoolTwitterList(App.getInstance().getParentId(),
                            String.valueOf(App.getInstance().getParentInfo().getSchoolId()), null,
                            App.getInstance().getParentInfo().getClassId(), page,
                            Constants.PAGE_SIZE);
                    }
                });
        }
    }
}
