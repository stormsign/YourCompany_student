package com.miuhouse.yourcompany.student.view.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.db.DictDBTask;
import com.miuhouse.yourcompany.student.listener.OnListItemClick;
import com.miuhouse.yourcompany.student.model.EvaluateBean;
import com.miuhouse.yourcompany.student.model.TeacherDetailBean;
import com.miuhouse.yourcompany.student.model.TeacherInfoBean;
import com.miuhouse.yourcompany.student.presenter.GetTeacherInfoPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.IGetTeacherinfoPresenter;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.utils.Values;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IGetTeachinfoView;
import com.miuhouse.yourcompany.student.view.ui.adapter.CommentAdapter;
import com.miuhouse.yourcompany.student.view.ui.adapter.PhotoAdapter;
import com.miuhouse.yourcompany.student.view.widget.CircularImageViewHome;
import com.miuhouse.yourcompany.student.view.widget.RecyclerRefreshLayout;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * 老师详情
 * Created by kings on 8/22/2016.
 */
public class DetailpagesActivity extends AppCompatActivity implements IGetTeachinfoView, View.OnClickListener, RecyclerRefreshLayout.SuperRefreshLayoutListener {

    private static final String TAG = "DetailpagesActivity";
    protected RecyclerRefreshLayout mRefreshLayout;
    private CircularImageViewHome imgAvatar;
    private TextView tvName;
    private TextView tvIntroduce;
    private TextView tvCollege;
    private TextView tvEducation;
    private TextView tvGrade;
    private TextView tvCardCheck;
    private TextView tvEducationCheck;
    private TextView tvProfessionCheck;
    private ImageView imgGender;
    private TextView tvCommentCount;
    private TextView tvPhotoCount;
    private TextView mLogoNick;
    private TextView tvTitle;
    private CircularImageViewHome mLogoPortrait;
    /**
     * empty 暂无评论
     */
    private LinearLayout linearNotComment;

    private PhotoAdapter photoAlbumAdapter;

    /**
     * 评论
     */
    private CommentAdapter commentAdapter;

    private ArrayList<String> lists = new ArrayList<>();
    private ArrayList<String> imageUrls = new ArrayList<>();
    private ArrayList<EvaluateBean> evaluatelists = new ArrayList<>();
    private String headUrl;
    private FrameLayout commentFrameLayout;
    private TeacherInfoBean teacherInfoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailpages);
        initView();
        initData();
    }

    protected void initWindow() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }


    private void initData() {
        String teacherId = getIntent().getStringExtra("teacherId");
        IGetTeacherinfoPresenter getTeacherinfoPresenter = new GetTeacherInfoPresenter(this);
        getTeacherinfoPresenter.getTeacherInfo(teacherId);
    }

    private void initView() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mLogoNick = (TextView) findViewById(R.id.tv_logo_nick);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        mLogoPortrait = (CircularImageViewHome) findViewById(R.id.iv_logo_portrait);
        mToolbar.setTitle("");
        mToolbar.setSubtitle("");
        mLogoNick.setText("老师详情");
        setSupportActionBar(mToolbar);
        initWindow();

        AppBarLayout mLayoutAppBar = (AppBarLayout) findViewById(R.id.layout_appbar);
        mLayoutAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int mScrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (mScrollRange == -1) {
                    mScrollRange = appBarLayout.getTotalScrollRange();
                }
                if (mScrollRange + i == 0) {
                    tvTitle.setVisibility(View.GONE);
                    mLogoNick.setVisibility(View.VISIBLE);
                    mLogoPortrait.setVisibility(View.VISIBLE);
                    isShow = true;
                } else if (isShow) {
                    mLogoNick.setVisibility(View.GONE);
                    mLogoPortrait.setVisibility(View.GONE);
                    tvTitle.setVisibility(View.VISIBLE);
                    isShow = false;
                }
            }
        });
        mRefreshLayout = (RecyclerRefreshLayout) findViewById(R.id.refreshLayout);
        mRefreshLayout.setSuperRefreshLayoutListener(this);
        mRefreshLayout.setRefreshing(true);
        mRefreshLayout.setColorSchemeResources(R.color.themeColor);

        RecyclerView recyclerPhotoAlbum = (RecyclerView) findViewById(R.id.recycler_photo_album);
        commentFrameLayout = (FrameLayout) findViewById(R.id.framelayout_comment);
        FrameLayout framelayoutPhotoAlbum = (FrameLayout) findViewById(R.id.framelayout_photo_album);
        imgAvatar = (CircularImageViewHome) findViewById(R.id.img_avatar);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvIntroduce = (TextView) findViewById(R.id.tv_introduce);
        tvEducation = (TextView) findViewById(R.id.tv_education);
        tvGrade = (TextView) findViewById(R.id.tv_grade);
        tvCollege = (TextView) findViewById(R.id.tv_college);
        tvEducationCheck = (TextView) findViewById(R.id.tv_education_check);
        tvCardCheck = (TextView) findViewById(R.id.tv_check_card);
        tvProfessionCheck = (TextView) findViewById(R.id.tv_profession_check);
        RecyclerView commentRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        imgGender = (ImageView) findViewById(R.id.img_gender);
        tvCommentCount = (TextView) findViewById(R.id.tv_comment_count);
        tvPhotoCount = (TextView) findViewById(R.id.tv_photo_count);
        linearNotComment = (LinearLayout) findViewById(R.id.linear_not_comment);

        commentFrameLayout.setOnClickListener(this);
        framelayoutPhotoAlbum.setOnClickListener(this);
        tvIntroduce.setOnClickListener(this);
        imgAvatar.setOnClickListener(this);

        commentRecyclerView.setLayoutManager(getLayoutManager());
        commentAdapter = new CommentAdapter(this, evaluatelists);
        commentRecyclerView.setAdapter(commentAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerPhotoAlbum.setLayoutManager(linearLayoutManager);
        photoAlbumAdapter = new PhotoAdapter(this, lists, new OnListItemClick() {
            @Override
            public void onItemClick(Object data) {
                Intent i = new Intent(DetailpagesActivity.this, GalleryActivity.class);
                i.putExtra("position", (int) data);
                i.putStringArrayListExtra("urls", imageUrls);
                startActivity(i);
            }
        });
        recyclerPhotoAlbum.setAdapter(photoAlbumAdapter);
    }


    @Override
    public void result(TeacherDetailBean teacherDetailBean) {
        teacherInfoBean = teacherDetailBean.getTeacherInfo();
        headUrl = teacherInfoBean.getHeadUrl();
        if (teacherInfoBean.getImages() != null) {
            imageUrls.addAll(teacherInfoBean.getImages());
        }
        if (!Util.isEmpty(teacherInfoBean.gettName())) {
            mLogoNick.setText(teacherInfoBean.gettName());
        }
        if (Util.isEmpty(headUrl)) {
            Glide.with(this).load(R.mipmap.ico_head_default).override(Util.dip2px(this, 34), Util.dip2px(this, 34)).centerCrop().into(mLogoPortrait);
        } else {
            Glide.with(this).load(headUrl).override(Util.dip2px(this, 34), Util.dip2px(this, 34)).centerCrop().into(mLogoPortrait);

        }
        if (!Util.isEmpty(headUrl)) {
            Glide.with(this).load(headUrl).override(Util.dip2px(this, 70), Util.dip2px(this, 70)).centerCrop().into(imgAvatar);
        } else {
            Glide.with(this).load(R.mipmap.ico_head_default).override(Util.dip2px(this, 70), Util.dip2px(this, 70)).centerCrop().into(imgAvatar);
        }
        if (!Util.isEmpty(teacherInfoBean.getGrade())) {
            tvGrade.setText(Values.getValue(Values.teacherGrades, Integer.parseInt(teacherInfoBean.getGrade())));
        }
        if (!Util.isEmpty(teacherInfoBean.getEducation()))
            tvEducation.setText(DictDBTask.getDcName("college_grade", Integer.parseInt(teacherInfoBean.getEducation())));

        tvName.setText(teacherInfoBean.gettName());
        tvIntroduce.setText(teacherInfoBean.getIntroduction());
        tvCollege.setText(teacherInfoBean.getCollege() + teacherInfoBean.getProfession());
        if (teacherDetailBean.getEvaluateSize() > 0) {
            tvCommentCount.setText("评价(" + teacherDetailBean.getEvaluateSize() + ")");
        } else {
            commentFrameLayout.setVisibility(View.GONE);
            linearNotComment.setVisibility(View.VISIBLE);
        }
        tvPhotoCount.setText("相册(" + imageUrls.size() + ")");

        if (teacherInfoBean.getIsCardCheck().equals("1")) {
            tvCardCheck.setVisibility(View.VISIBLE);
        } else {
            tvCardCheck.setVisibility(View.GONE);
        }
        if (teacherInfoBean.getIsEducationCheck().equals("1")) {
            tvEducationCheck.setVisibility(View.VISIBLE);
        } else {
            tvEducationCheck.setVisibility(View.GONE);
        }
        if (teacherInfoBean.getProfessionCheck().equals("1")) {
            tvProfessionCheck.setVisibility(View.VISIBLE);
        } else {
            tvProfessionCheck.setVisibility(View.GONE);
        }
        if (teacherInfoBean.getSex().equals("0")) {
            Glide.with(this).load(R.mipmap.gender_woman).override(Util.dip2px(this, 16), Util.dip2px(this, 16)).into(imgGender);
        } else {
            Glide.with(this).load(R.mipmap.gender).override(Util.dip2px(this, 16), Util.dip2px(this, 16)).into(imgGender);
        }
        photoAlbumAdapter.addAll(teacherInfoBean.getImages());
        commentAdapter.addAll(teacherDetailBean.getEvaluates());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.framelayout_comment:
                if (teacherInfoBean != null) {
                    startActivity(new Intent(this, AllCommentActivity.class).putExtra("teacherId", teacherInfoBean.getId()));
                }
                break;
            case R.id.framelayout_photo_album:
                Intent intent = new Intent(this, PhotoAlbumActivity.class);
                intent.putStringArrayListExtra("imageUrls", imageUrls);
                startActivity(intent);
                break;
            case R.id.tv_introduce:
                Intent intentIntroduce = new Intent(this, DescriptionIntroduceActivity.class);
                intentIntroduce.putExtra("context", tvIntroduce.getText().toString());
                startActivity(intentIntroduce);
                break;
            case R.id.img_avatar:
                startPictureActivity();
                break;
        }
    }

    private void startPictureActivity() {
        Intent i = new Intent(this, PicActivity.class);
        i.putExtra(PicActivity.EXTRA_IMAGE_URL, headUrl);
//
//        ActivityOptionsCompat optionsCompat =
//                ActivityOptionsCompat.makeSceneTransitionAnimation(this, imgAvatar,
//                        PicActivity.TRANSIT_PIC);
//        ActivityCompat.startActivity(this, i, optionsCompat.toBundle());
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void showError(int type) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void hideError() {

    }

    @Override
    public void netError() {

    }

    @Override
    public ProgressDialog showWaitDialog() {
        return null;
    }

    @Override
    public ProgressDialog showWaitDialog(int msg) {
        return null;
    }

    @Override
    public ProgressDialog showWaitDialog(String msg) {
        return null;
    }

    @Override
    public void hideWaitDialog() {

    }

    @Override
    public void onTokenExpired() {

    }

    @Override
    public void onRefreshing() {

    }

    @Override
    public void onLoadMore() {

    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
