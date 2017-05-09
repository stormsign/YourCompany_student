package com.miuhouse.yourcompany.student.view.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.application.App;
import com.miuhouse.yourcompany.student.listener.OnListItemClick;
import com.miuhouse.yourcompany.student.listener.OnListItemLongClick;
import com.miuhouse.yourcompany.student.model.ClassEntity;
import com.miuhouse.yourcompany.student.model.ParentInfo;
import com.miuhouse.yourcompany.student.model.SchoolNewsBean;
import com.miuhouse.yourcompany.student.model.SchoolNewsListBean;
import com.miuhouse.yourcompany.student.model.User;
import com.miuhouse.yourcompany.student.model.VideoController;
import com.miuhouse.yourcompany.student.presenter.DeleteTwitterPresenter;
import com.miuhouse.yourcompany.student.presenter.SchoolTeacherInfoPresenter;
import com.miuhouse.yourcompany.student.presenter.TwitterPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.IDeleteTwitterPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.ISchoolTeacherInfoPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.ITwitterPresenter;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.DialogHelp;
import com.miuhouse.yourcompany.student.utils.SPUtils;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.utils.ViewUtils;
import com.miuhouse.yourcompany.student.view.ui.activity.AccountActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.DetailTwitterActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.PublishActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IDeleteTwitterView;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.ISchoolTeacherInfoView;
import com.miuhouse.yourcompany.student.view.ui.adapter.TwitterAdapter;
import com.miuhouse.yourcompany.student.view.ui.base.BaseFragment;
import com.miuhouse.yourcompany.student.view.ui.fragment.interf.ITwitterFragment;
import com.miuhouse.yourcompany.student.view.widget.LovelyChoiceDialog;
import com.miuhouse.yourcompany.student.view.widget.ViewOverrideManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by khb on 2016/12/29.
 */
public class DongTaiFragment extends BaseFragment
    implements SwipeRefreshLayout.OnRefreshListener, OnListItemClick, OnListItemLongClick,
    ITwitterFragment, IDeleteTwitterView, ISchoolTeacherInfoView, View.OnClickListener {

    private static final long ANIMATE_DURATION = 200;
    private static final int FIRST_REQUEST_CODE = 1;

    public static final int RESULT_SUCESS = 0;
    public static final int RESULT_ERROR = 1;

    private SwipeRefreshLayout mySwipeRefreshLayout;
    private RelativeLayout contentLinear;
    private TwitterAdapter twitterAdapter;

    private ITwitterPresenter twitterPresenter;
    private IDeleteTwitterPresenter deleteTwitterPresenter;

    private List<SchoolNewsBean> list = new ArrayList<>();
    private CardView manageLayout;
    private CollapsingToolbarLayout.LayoutParams params;
    private int manageLayoutHeight;

    private int page = 1;
    private ImageView manage;

    /**
     * 动态类型 0：午托班 -1：老师圈 其它是班级id
     */
    private int twitterType = Constants.SCHOOLNEWSLIST_ONE;

    public static final int ERROR = -1;

    /**
     * 置顶数量
     */
    private int size = 0;
    private TextView range;

    @Override public int getFragmentResourceId() {

        return R.layout.fragment_dongtai;
    }

    @Override public void getViews(View view) {

        Constants.IMEI_VALUE = Util.getIMEI(getActivity());

        final RecyclerView twitterRecycler =
            (RecyclerView) view.findViewById(R.id.recycler_my_twitter);
        mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        RelativeLayout contentLinear = (RelativeLayout) view.findViewById(R.id.content);

        mySwipeRefreshLayout.setColorSchemeResources(R.color.themeColor);
        mySwipeRefreshLayout.setOnRefreshListener(this);
        twitterRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        twitterAdapter = new TwitterAdapter(getActivity(), list, this, this);
        twitterRecycler.setAdapter(twitterAdapter);

        range = (TextView) view.findViewById(R.id.range);

        view.findViewById(R.id.manage).setOnClickListener(this);

        twitterPresenter = new TwitterPresenter(this);

        twitterRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private int lastVisibleItem;
            private int firstVisibleItem;

            @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == twitterAdapter.getItemCount()
                    && firstVisibleItem != 0
                    && !mySwipeRefreshLayout.isRefreshing()) {
                    page += 1;
                    twitterPresenter.getSchoolTwitterList(App.getInstance().getParentId(),
                        String.valueOf(App.getInstance().getParentInfo().getSchoolId()), null,
                        App.getInstance().getParentInfo().getClassId(), page, Constants.PAGE_SIZE);
                }
            }

            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem =
                    ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                firstVisibleItem =
                    ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            }
        });

        ISchoolTeacherInfoPresenter schoolTeacherInfoPresenter =
            new SchoolTeacherInfoPresenter(this);
        schoolTeacherInfoPresenter.getSchoolTeacherInfo(App.getInstance().getParentId());
    }

    @Override public void onAttach(Context context) {

        super.onAttach(context);
        if (context != null) {
            getActivity().runOnUiThread(new Runnable() {

                @Override public void run() {

                    showWaitDialog("加载个人信息");
                }
            });
        }
    }

    @Override public void setupView() {

    }

    @Override public View getOverrideParentView() {

        return mySwipeRefreshLayout;
    }

    @Override public void onItemClick(Object data) {

        Integer position = (Integer) data;
        startActivityForResult(
            new Intent(getActivity(), DetailTwitterActivity.class).putExtra("schoolNewsBean",
                list.get(position)), FIRST_REQUEST_CODE);
    }

    @Override public void onRefresh() {

        page = 1;
        twitterPresenter.getSchoolTwitterList(App.getInstance().getParentId(),
            String.valueOf(App.getInstance().getParentInfo().getSchoolId()), null,
            App.getInstance().getParentInfo().getClassId(), page, Constants.PAGE_SIZE);
    }

    @Override public void getSchoolNewListBean(SchoolNewsListBean schoolNewsListBean) {

        if (page == 1 && list.size() > 0) {
            list.clear();
            size = 0;
        }

        for (SchoolNewsBean schoolNewsBean : schoolNewsListBean.getSchoolNewsInfos()) {

            if (schoolNewsBean.getIsTop() != null && schoolNewsBean.getIsTop().equals("1")) {
                size++;
            }
        }

        twitterAdapter.setSize(size);

        list.addAll(schoolNewsListBean.getSchoolNewsInfos());
        twitterAdapter.notifyDataSetChanged();

        if (mySwipeRefreshLayout.isRefreshing()) {
            mySwipeRefreshLayout.setRefreshing(false);
        }
        unreadDisplayControll(SPUtils.getData(Constants.UNREAD_DONGTAI, 0) > 0);
    }

    @Override public void onClick(View v) {

        switch (v.getId()) {
            case R.id.account:
                startActivity(new Intent(getActivity(), AccountActivity.class));
                break;

            case R.id.manage:
                showManage();
                break;
        }
    }

    private void showManage() {

        List<String> list = new ArrayList<String>();
        list.add("发文字");
        list.add("发图片");
        list.add("发视频");
        ViewUtils.showSimpleChoiceDialog(getActivity(), list,
            new LovelyChoiceDialog.OnItemSelectedListener<String>() {

                @Override public void onItemSelected(int position, String item) {

                    switch (position) {
                        case 0:
                            startActivityForResult(
                                new Intent(context, PublishActivity.class).putExtra("isNotVideo",
                                    true), FIRST_REQUEST_CODE);

                            break;
                        case 1:
                            startActivityForResult(
                                new Intent(context, PublishActivity.class).putExtra("isNotVideo",
                                    true), FIRST_REQUEST_CODE);

                            break;
                        case 2:
                            VideoController.startRecordingVideo(getActivity());
                            break;
                    }
                }
            });
    }

    @Override public void unreadDisplayControll(boolean displayFlag) {

        if (twitterAdapter != null) {
            if (displayFlag) {
                twitterAdapter.setUnreadCount(SPUtils.getData(Constants.UNREAD_DONGTAI, 0));
            }
            twitterAdapter.setDisplayUnread(displayFlag);
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FIRST_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mySwipeRefreshLayout.setRefreshing(true);
            page = 1;
            twitterPresenter.getSchoolTwitterList(App.getInstance().getParentId(),
                String.valueOf(App.getInstance().getParentInfo().getSchoolId()), null,
                App.getInstance().getParentInfo().getClassId(), page, Constants.PAGE_SIZE);
        }
    }

    @Override public void showLoading(String msg) {

        if (!mySwipeRefreshLayout.isRefreshing()) {
            mySwipeRefreshLayout.post(new Runnable() {

                @Override public void run() {

                    mySwipeRefreshLayout.setRefreshing(true);
                }
            });
        }
    }

    @Override public void hideLoading() {

        if (mySwipeRefreshLayout.isRefreshing()) {
            mySwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override public void showError(int type) {

        hideWaitDialog();
        if (null == viewOverrideManager) {
            viewOverrideManager = new ViewOverrideManager(getOverrideParentView());
        }
        if (type == ViewOverrideManager.NO_CONTENT) {
            viewOverrideManager.showLoading(ViewOverrideManager.NO_CONTENT,
                new ViewOverrideManager.OnExceptionalClick() {

                    @Override public void onExceptionalClick() {

                        showManage();
                    }
                });
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

    @Override public void getSchoolTeacherInfo(User user) {

        range.setText(user.getParentInfo().getSchoolName());

        hideWaitDialog();
        twitterPresenter.getSchoolTwitterList(user.getParentInfo().getParentId(),
            String.valueOf(user.getParentInfo().getSchoolId()), null,
            user.getParentInfo().getClassId(), page, Constants.PAGE_SIZE);
    }

    @Override public void onItemLongClick(final Object data) {

        DialogHelp.getConfirmDialog(getActivity(), "删除这条动态吗？",
            new DialogInterface.OnClickListener() {

                @Override public void onClick(DialogInterface dialog, int which) {

                    if (deleteTwitterPresenter == null) {
                        deleteTwitterPresenter = new DeleteTwitterPresenter(DongTaiFragment.this);
                    }
                    deleteTwitterPresenter.deleteTwitter(App.getInstance().getParentId(),
                        twitterAdapter.getItem((Integer) data).getId(), (Integer) data);
                }
            }).show();
    }

    @Override public void showDelete(int type, int index, String msg) {

        if (type == DongTaiFragment.RESULT_SUCESS) {
            list.remove(index);
            twitterAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override public void showDeleteDialog() {

        super.showLoading(null);
    }

    @Override public void hideDeletDialog() {

        super.hideLoading();
    }

    class ViewWrapper {

        View v;
        float marginTop;

        public ViewWrapper(View v) {

            this.v = v;
        }

        public float getMarginTop() {

            LinearLayout.LayoutParams layoutParams =
                (LinearLayout.LayoutParams) v.getLayoutParams();
            return layoutParams.topMargin;
        }

        public void setMarginTop(float marginTop) {

            this.marginTop = marginTop;
            LinearLayout.LayoutParams layoutParams =
                (LinearLayout.LayoutParams) v.getLayoutParams();
            layoutParams.setMargins(0, (int) marginTop, 0, 0);
            v.setLayoutParams(layoutParams);
        }
    }

    @Override public void onDestroy() {

        super.onDestroy();
        list.clear();
        page = 1;
        size = 0;
    }
}







