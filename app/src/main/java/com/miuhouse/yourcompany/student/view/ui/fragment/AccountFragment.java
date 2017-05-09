package com.miuhouse.yourcompany.student.view.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.application.App;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.model.MyOrderInfoMessageBean;
import com.miuhouse.yourcompany.student.model.ParentInfo;
import com.miuhouse.yourcompany.student.model.User;
import com.miuhouse.yourcompany.student.presenter.MyOrderInfoMessagePresenter;
import com.miuhouse.yourcompany.student.presenter.UserInformationPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.IUserInformationPresenter;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.utils.SPUtils;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.view.ui.activity.AddressActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.BrowserActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.OrdersManageActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.OrdersStatusActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.SettingActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.UserInformationActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IUserInformationView;
import com.miuhouse.yourcompany.student.view.ui.base.BaseFragment;
import com.miuhouse.yourcompany.student.view.ui.fragment.interf.IMyOrderInfoMessageView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import cn.bingoogolapple.badgeview.BGABadgeFrameLayout;

/**
 * Created by khb on 2016/7/6. implements IAccountFragment, IUserInformationView,
 * View.OnClickListener
 */
public class AccountFragment extends BaseFragment
    implements View.OnClickListener, IUserInformationView, IMyOrderInfoMessageView {

    private static final int REQUEST = 1;
    private TextView tvName;
    private TextView tvMobile;
    private ImageView imgAvatar;
    private LinearLayout contentLinear;
    private BGABadgeFrameLayout waitingPayNum;

    private BGABadgeFrameLayout waitingClassNum;
    private BGABadgeFrameLayout waitingCommentNum;
    private TextView grabNum;

    private String defaultOrderId;
    private TextView tvOrderStatus;

    @Override
    public int getFragmentResourceId() {
        return R.layout.fragment_account;
    }

    @Override
    public void getViews(View view) {

        view.findViewById(R.id.linear_orders_state).setOnClickListener(this);
        view.findViewById(R.id.linear_all_order).setOnClickListener(this);
        view.findViewById(R.id.relative_setting).setOnClickListener(this);
        view.findViewById(R.id.relative_guide).setOnClickListener(this);
        view.findViewById(R.id.relative_feedback).setOnClickListener(this);
        view.findViewById(R.id.relative_address).setOnClickListener(this);
        view.findViewById(R.id.linear_wait_payment).setOnClickListener(this);
        view.findViewById(R.id.linear_wait_go_class).setOnClickListener(this);
        view.findViewById(R.id.linear_wait_comment).setOnClickListener(this);
        view.findViewById(R.id.img_icon_invitation).setOnClickListener(this);
        view.findViewById(R.id.relative_guide).setOnClickListener(this);
        contentLinear = (LinearLayout) view.findViewById(R.id.content);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        imgAvatar = (ImageView) view.findViewById(R.id.img_avatar);
        tvMobile = (TextView) view.findViewById(R.id.tv_mobile);
        waitingPayNum = (BGABadgeFrameLayout) view.findViewById(R.id.btv_waitpay_order);
        waitingClassNum = (BGABadgeFrameLayout) view.findViewById(R.id.btv_waitclass_order);
        waitingCommentNum = (BGABadgeFrameLayout) view.findViewById(R.id.btv_waitcomment_order);
        grabNum = (TextView) view.findViewById(R.id.grabNum);
        tvOrderStatus = (TextView) view.findViewById(R.id.tvOrderStatus);

        view.findViewById(R.id.relative_information).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserInformationActivity.class);
                startActivityForResult(intent, REQUEST);
            }
        });
    }

    @Override
    public void setupView() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //IUserInformationPresenter userInformationPresenter = new UserInformationPresenter(this);
        //MyOrderInfoMessagePresenter myOrderInfoMessagePresenter = new MyOrderInfoMessagePresenter(this);
        //userInformationPresenter.getUserInfo(null);
        //myOrderInfoMessagePresenter.getMyOrderInfoMessage();
    }

    @Override
    public View getOverrideParentView() {
        return contentLinear;
    }

    @Override
    public void UpdateSuccess(BaseBean baseBean) {

    }

    @Override
    public void getUserInfo(User user) {
        SPUtils.saveData(SPUtils.CHECK_PERSONAL_DATA, user.getParentInfo().isCheckPerSonalData());
        ParentInfo parentInfo = user.getParentInfo();
        App.getInstance().setParentInfo(parentInfo);
        String pName = user.getParentInfo().getpName();

        if (!Util.isEmpty(pName)) {
            tvName.setText(pName);
        } else {
            tvName.setText("未填写姓名");
        }
        if (!Util.isEmpty(user.getParentInfo().getMobile())) {
            tvMobile.setText(user.getParentInfo().getMobile());
        }
        if (user.getParentInfo().getHeadUrl() != null) {
            Glide.with(this)
                .load(user.getParentInfo()
                    .getHeadUrl())
                .centerCrop()
                .override(Util.dip2px(getActivity(), 50), Util.dip2px(getActivity(), 50))
                .error(R.mipmap.ic_launcher)
                .into(imgAvatar);
        } else {
            Glide.with(this)
                .load(R.mipmap.ic_launcher)
                .centerCrop()
                .override(Util.dip2px(getActivity(), 50), Util.dip2px(getActivity(), 50))
                .into(imgAvatar);
        }
    }

    @Override
    public void getMyOrderInfoMessage(MyOrderInfoMessageBean myOrderInfoMessageBean) {
        L.i("TAG", "myOrderInfoMessage=" + myOrderInfoMessageBean.getOrderInfoId());
        if (myOrderInfoMessageBean.getWaitingPayNum() > 0) {
            waitingPayNum.showCirclePointBadge();
            waitingPayNum.showTextBadge(myOrderInfoMessageBean.getWaitingPayNum() + "");
        }
        if (myOrderInfoMessageBean.getWaitingClassNum() > 0) {
            waitingClassNum.showCirclePointBadge();
            waitingClassNum.showTextBadge(myOrderInfoMessageBean.getWaitingClassNum() + "");
        }
        if (myOrderInfoMessageBean.getWaitingEvaluateNum() > 0) {
            waitingCommentNum.showCirclePointBadge();
            waitingCommentNum.showTextBadge(myOrderInfoMessageBean.getWaitingEvaluateNum() + "");
        }
        if (myOrderInfoMessageBean.getOrderGrabCount() <= 0) {
            tvOrderStatus.setText("你还没有发布订单");
        } else {
            tvOrderStatus.setText("你有" + myOrderInfoMessageBean.getOrderGrabCount() + "个订单等待抢单");
        }
        grabNum.setText(myOrderInfoMessageBean.getOrderGrabNum() + "");
        defaultOrderId = myOrderInfoMessageBean.getOrderInfoId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //最新订单状态
            case R.id.linear_orders_state:
                getActivity().startActivity(new Intent(getActivity(), OrdersStatusActivity.class)
                    .putExtra(OrdersStatusActivity.EXTRA_ORDERID, defaultOrderId));
                break;
            //全部订单
            case R.id.linear_all_order:
                //                pay();
                context.startActivity(new Intent(context, OrdersManageActivity.class)
                    .putExtra("index", 0));
                break;
            case R.id.relative_setting:
                getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.relative_feedback:
                Intent intentFeedback = new Intent(getActivity(), BrowserActivity.class);
                intentFeedback.putExtra(BrowserActivity.BROWSER_KEY,
                    Constants.URL_HEAD + "/mobile/suggest");
                intentFeedback.putExtra("title", "意见反馈");
                startActivity(intentFeedback);
                break;
            case R.id.linear_wait_comment:
                context.startActivity(new Intent(context, OrdersManageActivity.class)
                    .putExtra("index", 3));
                break;

            case R.id.linear_wait_go_class:
                getActivity().startActivity(new Intent(getActivity(), OrdersManageActivity.class)
                    .putExtra("index", 2));
                break;
            case R.id.linear_wait_payment:
                context.startActivity(new Intent(context, OrdersManageActivity.class)
                    .putExtra("index", 1));
                break;
            //上课地址管理
            case R.id.relative_address:
                startActivity(new Intent(getActivity(), AddressActivity.class));
                break;
            case R.id.img_icon_invitation:
                Intent intentInvitation = new Intent(getActivity(), BrowserActivity.class);
                intentInvitation.putExtra(BrowserActivity.BROWSER_KEY,
                    Constants.URL_HEAD + "/mobile/inviteRegist/");
                intentInvitation.putExtra("title", "邀请注册");
                startActivity(intentInvitation);
                break;
            case R.id.relative_guide:
                Intent intentGuide = new Intent(getActivity(), BrowserActivity.class);
                intentGuide.putExtra(BrowserActivity.BROWSER_KEY,
                    Constants.URL_HEAD + "/mobile/guideListP");
                intentGuide.putExtra("title", "用户指南");
                startActivity(intentGuide);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == REQUEST) {
            String name = data.getStringExtra("name");
            String headUrl = data.getStringExtra("headUrl");

            if (!Util.isEmpty(name)) {
                tvName.setText(data.getStringExtra("name"));
            }
            if (!Util.isEmpty(headUrl)) {
                Glide.with(this)
                    .load(headUrl)
                    .centerCrop()
                    .override(Util.dip2px(getActivity(), 50), Util.dip2px(getActivity(), 50))
                    .into(imgAvatar);
            }
        }
    }
}
