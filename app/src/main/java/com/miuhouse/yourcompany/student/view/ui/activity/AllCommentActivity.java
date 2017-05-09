package com.miuhouse.yourcompany.student.view.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.model.EvaluateBean;
import com.miuhouse.yourcompany.student.model.EvaluateListBean;
import com.miuhouse.yourcompany.student.presenter.GetAllCommentPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.IGetAllCommentPresenter;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IGetAllCommentView;
import com.miuhouse.yourcompany.student.view.ui.adapter.CommentAdapter;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;
import com.miuhouse.yourcompany.student.view.widget.ViewOverrideManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kings on 8/25/2016.
 */
public class AllCommentActivity extends BaseActivity implements IGetAllCommentView, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView teacherAllCommentRecycler;
    private CommentAdapter allCommentAdapter;
    private IGetAllCommentPresenter getAllCommentPresenter;
    private SwipeRefreshLayout refresh;
    private RelativeLayout relativeContent;
    private int page = 1;
    private ArrayList<EvaluateBean> list = new ArrayList<EvaluateBean>();
    private String teacherId;

    @Override
    protected String setTitle() {
        return "全部评价";
    }

    @Override
    protected String setRight() {
        return null;
    }

    @Override
    protected void initViewAndEvents() {
        teacherId = getIntent().getStringExtra("teacherId");
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        teacherAllCommentRecycler = (RecyclerView) findViewById(R.id.recycler_my_comment);
        relativeContent = (RelativeLayout) findViewById(R.id.relative_content);
        teacherAllCommentRecycler.setLayoutManager(new LinearLayoutManager(context));

        refresh.setColorSchemeResources(android.R.color.holo_orange_light);
        refresh.setOnRefreshListener(this);

        allCommentAdapter = new CommentAdapter(this, list);
        teacherAllCommentRecycler.setAdapter(allCommentAdapter);

        getAllCommentPresenter = new GetAllCommentPresenter(this);
        getAllCommentPresenter.getAllComment(teacherId, page);


        teacherAllCommentRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisibleItem;
            private int firstVisibleItem;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();


            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == allCommentAdapter.getItemCount()
                        && firstVisibleItem != 0 && !refresh.isRefreshing()) {
                    page += 1;
                    getAllCommentPresenter.getAllComment(teacherId, page);

                }
            }
        });
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_all_comment;
    }

    @Override
    protected View getOverrideParentView() {
        return relativeContent;
    }


    @Override
    public void result(EvaluateListBean evaluateListBean) {
        if (page == 1) {
            if (list.size() > 0) {
                list.clear();
            }
        }
        refresh.setRefreshing(false);
        refresh.setEnabled(true);
//        list.addAll(myEvaluate.getEvaluates());
//        adapter.notifyDataSetChanged();
        allCommentAdapter.addAll(evaluateListBean.getEvaluates());
    }


    private boolean mIsFirstTimeTouchBottom = true;


    @Override
    public void onRefresh() {
        refresh.setEnabled(false);
        page = 1;
        getAllCommentPresenter.getAllComment(teacherId, page);

    }


    @Override
    public void showError(int type) {
        viewOverrideManager.showLoading(type, new ViewOverrideManager.OnExceptionalClick() {
            @Override
            public void onExceptionalClick() {

            }
        });
    }
}
