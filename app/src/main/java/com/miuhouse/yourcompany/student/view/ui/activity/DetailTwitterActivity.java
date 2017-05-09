package com.miuhouse.yourcompany.student.view.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;
import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.application.App;
import com.miuhouse.yourcompany.student.listener.OnListItemClick;
import com.miuhouse.yourcompany.student.listener.OnListItemLongClick;
import com.miuhouse.yourcompany.student.model.CommentsBean;
import com.miuhouse.yourcompany.student.model.SchoolNewsBean;
import com.miuhouse.yourcompany.student.presenter.DeleteCommentPresenter;
import com.miuhouse.yourcompany.student.presenter.LikeOrCommentPresenter;
import com.miuhouse.yourcompany.student.presenter.SchoolNewsInfoPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.IDeleteCommentPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.ILikeOrCommentPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.ISchoolNewsInfoPresenter;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.DialogHelp;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.ICommentingView;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IDeleteCommentView;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IDeleteTwitterView;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.ISchoolNewsInfoView;
import com.miuhouse.yourcompany.student.view.ui.adapter.TwitterCommentAdapter;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kings on 12/21/2016.
 */
public class DetailTwitterActivity extends BaseActivity
    implements OnListItemClick, OnListItemLongClick, ICommentingView, ISchoolNewsInfoView,
    IDeleteCommentView {

    public static final int RESULT_SUCESS = 0;
    public static final int RESULT_ERROR = 1;
    public static final int RESULT_NO_PERFECT = 2;

    private RecyclerView myRecyclerView;
    private List<CommentsBean> datas = new ArrayList<>();

    private IDeleteCommentPresenter deleteCommentPresenter;
    private ILikeOrCommentPresenter likeOrCommentPresenter;
    private SchoolNewsBean schoolNewsBean;
    private EditText input;
    private String commentContent;
    private TwitterCommentAdapter mTwitterCommentAdapter;

    /**
     * when activity is finish, 假如isResult为ture说明动态列表页要更新
     */
    private boolean isResult;
    /**
     * 判断是评论还是回复某人
     */
    private boolean isReplyToSb;

    /**
     * 当前点击评论列表的位置
     */
    private int position;

    private boolean isSoftKeyboard;

    @Override protected String setTitle() {
        return "详细";
    }

    @Override protected String setRight() {
        return null;
    }

    @Override protected void initViewAndEvents() {

        schoolNewsBean = (SchoolNewsBean) getIntent().getSerializableExtra("schoolNewsBean");
        isSoftKeyboard = getIntent().getBooleanExtra("softKeyboard", false);

        String schoolNewsId = getIntent().getStringExtra("schoolNewsId");
        myRecyclerView = (RecyclerView) findViewById(R.id.recycler_detail_twitter);
        Button send = (Button) findViewById(R.id.send);
        input = (EditText) findViewById(R.id.input);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        likeOrCommentPresenter = new LikeOrCommentPresenter(this);

        send.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                Util.hideSoftKeyboard(input, DetailTwitterActivity.this);

                commentContent = input.getText().toString();
                if (isReplyToSb) {
                    CommentsBean commentsBean = datas.get(position);
                    likeOrCommentPresenter.getLikeOrComment(commentsBean.getId(),
                        App.getInstance().getParentId(),
                        schoolNewsBean.getId(),
                        Constants.COMMENT_TYPE_COMMENT,
                        App.getInstance().getParentId(),
                        Constants.LINK_TYPE_PARENTS,
                        App.getInstance().getParentInfo().getcName() + " " + App.getInstance()
                            .getParentInfo()
                            .getRoleName(),
                        commentsBean.getLinkId(),
                        commentsBean.getLinkType(),
                        commentsBean.getLinkName(), commentContent, commentsBean);
                } else {
                    likeOrCommentPresenter.getLikeOrComment(null, App.getInstance().getParentId(),
                        schoolNewsBean.getId(),
                        Constants.COMMENT_TYPE_COMMENT,
                        App.getInstance().getParentId(),
                        Constants.LINK_TYPE_PARENTS,
                        App.getInstance().getParentInfo().getcName() + " " + App.getInstance()
                            .getParentInfo()
                            .getRoleName(), null, null, null,
                        commentContent, null);
                }
            }
        });
        if (schoolNewsId != null) {
            ISchoolNewsInfoPresenter schoolNewsInfoPresenter = new SchoolNewsInfoPresenter(this);
            schoolNewsInfoPresenter.getSchoolNewsInfo(App.getInstance().getParentId(),
                schoolNewsId);
        } else if (schoolNewsBean != null) {
            getSchoolNewsInfo(schoolNewsBean);
        }

        if (isSoftKeyboard) {
            //当前界面必须已经加载完成，不能直接在Activity的onCreate()，onResume()，onAttachedToWindow()中使用，
            // 可以在这些方法中通过postDelayed的方式来延迟执行showSoftInput()。
            (new Handler()).postDelayed(new Runnable() {
                public void run() {
                    Util.showSoftKeyboard(input, context);
                }
            }, 200);
        }
    }

    @Override protected int getContentLayoutId() {
        return R.layout.activity_detail_twitter;
    }

    @Override protected View getOverrideParentView() {
        return null;
    }

    @Override public void onItemClick(Object data) {
        View itemView = (View) data;
        if (itemView.getTag() == null) {
            return;
        }

        Util.showSoftKeyboard(input, DetailTwitterActivity.this);

        position = (Integer) itemView.getTag();
        position -= 1;
        if (datas.size() >= position && datas.get(position) != null) {
            isReplyToSb = true;
            CommentsBean commentsBean = datas.get(position);
            input.setHint("回复:" + commentsBean.getLinkName());
        }
    }

    private View getHeaderView() {
        return LayoutInflater.from(this)
            .inflate(R.layout.list_item_head_detail_twitter, myRecyclerView, false);
    }

    private View getHeaderNotificationView() {
        return LayoutInflater.from(this)
            .inflate(R.layout.list_head_notification, myRecyclerView, false);
    }

    @Override public void showError(int type) {
        if (type == RESULT_SUCESS) {
            isResult = true;
            Util.hideSoftKeyboard(input, this);
            clean();
            CommentsBean commentsBean = new CommentsBean();
            commentsBean.setComment(commentContent);
            commentsBean.setCommentTime(System.currentTimeMillis());
            commentsBean.setCommentType(Constants.COMMENT_TYPE_COMMENT);
            commentsBean.setId(schoolNewsBean.getId());
            commentsBean.setLinkId(App.getInstance().getParentId());
            commentsBean.setLinkName(
                App.getInstance().getParentInfo().getcName() + " " + App.getInstance()
                    .getParentInfo()
                    .getRoleName());
            commentsBean.setLinkHeadUrl(App.getInstance().getParentInfo().getHeadUrl());
            //commentsBean.setNewsId();
            datas.add(0, commentsBean);
            mTwitterCommentAdapter.notifyDataSetChanged();
            input.setHint("点击评论");
        } else if (type == RESULT_ERROR) {
            Toast.makeText(this, "提交失败", Toast.LENGTH_SHORT).show();
        } else if (type == RESULT_NO_PERFECT) {
            Toast.makeText(this, "未填写内容", Toast.LENGTH_SHORT).show();
        }
    }

    @Override public void showError(int type, CommentsBean mCommentsBean) {
        if (type == RESULT_SUCESS) {

            isResult = true;
            clean();

            CommentsBean commentsBean = new CommentsBean();
            commentsBean.setComment(commentContent);
            commentsBean.setCommentTime(System.currentTimeMillis());
            commentsBean.setCommentType(Constants.COMMENT_TYPE_COMMENT);
            commentsBean.setId(schoolNewsBean.getId());
            commentsBean.setLinkId(App.getInstance().getParentId());
            commentsBean.setLinkName(
                App.getInstance().getParentInfo().getcName() + " " + App.getInstance()
                    .getParentInfo()
                    .getRoleName());
            commentsBean.setLinkHeadUrl(App.getInstance().getParentInfo().getHeadUrl());

            if (mCommentsBean != null) {
                commentsBean.setLinkedId(mCommentsBean.getLinkId());
                commentsBean.setLinkedName(mCommentsBean.getLinkName());
                commentsBean.setLinkedType(mCommentsBean.getLinkType());
            }

            datas.add(0, commentsBean);

            int newCommentNum = schoolNewsBean.getCommentNum() + 1;
            schoolNewsBean.setCommentNum(newCommentNum);

            mTwitterCommentAdapter.notifyDataSetChanged();
            input.setHint("点击评论");
        } else if (type == RESULT_ERROR) {
            Toast.makeText(this, "提交失败", Toast.LENGTH_SHORT).show();
        } else if (type == RESULT_NO_PERFECT) {
            Toast.makeText(this, "未填写内容", Toast.LENGTH_SHORT).show();
        }
    }

    @Override public void getSchoolNewsInfo(SchoolNewsBean schoolNewsBean) {
        this.schoolNewsBean = schoolNewsBean;
        datas.addAll(schoolNewsBean.getComments());
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTwitterCommentAdapter = new TwitterCommentAdapter(this, datas, this, this, schoolNewsBean);
        myRecyclerView.setAdapter(mTwitterCommentAdapter);
        mTwitterCommentAdapter.setmHeaderView(getHeaderView());
        mTwitterCommentAdapter.setType(TwitterCommentAdapter.TYPE_HEADER);
    }

    public boolean isResult() {
        return isResult;
    }

    public void clean() {
        input.setText(null);
        input.setTag(null);
        isReplyToSb = false;
    }

    @Override public void onBackClick() {
        if (isResult || mTwitterCommentAdapter.isResult()) {
            this.setResult(RESULT_OK);
            finish();
        } else {
            super.onBackClick();
        }
    }

    @Override public void onItemLongClick(Object data) {
        View itemView = (View) data;
        if (itemView.getTag() == null) {
            return;
        }

        //Util.showSoftKeyboard(this, input);

        position = (Integer) itemView.getTag();
        position -= 1;
        DialogHelp.getConfirmDialog(this, "删除这条评论吗？",
            new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {
                    if (deleteCommentPresenter == null) {
                        deleteCommentPresenter =
                            new DeleteCommentPresenter(DetailTwitterActivity.this);
                    }
                    deleteCommentPresenter.deleteComment(App.getInstance().getParentId(),
                        mTwitterCommentAdapter.getItem(position).getId(), position);
                }
            }).show();
    }

    @Override public void deleteResult(int type, String msg, int position) {
        if (type == this.RESULT_SUCESS) {
            datas.remove(position);
            mTwitterCommentAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
