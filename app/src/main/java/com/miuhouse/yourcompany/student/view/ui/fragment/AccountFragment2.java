package com.miuhouse.yourcompany.student.view.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.application.App;
import com.miuhouse.yourcompany.student.application.App;
import com.miuhouse.yourcompany.student.db.AccountDBTask;
import com.miuhouse.yourcompany.student.model.ParentInfo;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.view.ui.activity.BrowserActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.PersonalTwitterActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.SettingActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.UserInformationActivity;
import com.miuhouse.yourcompany.student.view.ui.base.BaseFragment;
import com.miuhouse.yourcompany.student.view.widget.CircularImageViewHome;
import com.miuhouse.yourcompany.student.view.widget.ShareDialog;
import com.umeng.socialize.UMShareAPI;
import mabeijianxi.camera.util.Log;

/**
 * Created by khb on 2017/1/19.
 */
public class AccountFragment2 extends BaseFragment implements View.OnClickListener {

    private static final int REQUEST_CODE = 1;
    private TextView tvName;
    private CircularImageViewHome imgAvatar;
    private ParentInfo account;

    @Override
    public int getFragmentResourceId() {
        return R.layout.activity_account;
    }

    @Override
    public void getViews(View view) {

        imgAvatar = (CircularImageViewHome) view.findViewById(R.id.img_avatar);

        imgAvatar.setBorderWidth(Util.dip2px(getActivity(), 2));
        imgAvatar.setBorderColor(Color.parseColor("#ffffff"));

        RelativeLayout relativePresenterTwitter =
            (RelativeLayout) view.findViewById(R.id.relative_presenter_twitter);
        view.findViewById(R.id.relative_setting).setOnClickListener(this);
        view.findViewById(R.id.relative_feedback).setOnClickListener(this);
        view.findViewById(R.id.relative_me_share).setOnClickListener(this);

        tvName = (TextView) view.findViewById(R.id.tv_user_name);
        imgAvatar.setOnClickListener(this);
        relativePresenterTwitter.setOnClickListener(this);

        String strHeadUrl = null;

        account = AccountDBTask.getAccount();
        if (account != null && account.getcName() != null) {
            if (account.getRoleName() != null) {
                tvName.setText(account.getcName() + " " + account.getRoleName());
            } else {
                tvName.setText(account.getcName());
            }
        }
        if (account != null) {
            strHeadUrl = account.getHeadUrl();
        }
        if (!Util.isEmpty(strHeadUrl)) {
            Glide.with(this)
                .load(strHeadUrl)
                .centerCrop()
                .override(Util.dip2px(getActivity(), 65), Util.dip2px(getActivity(), 65))
                .into(imgAvatar);
        } else {
            Glide.with(this)
                .load(R.mipmap.ico_head_default)
                .centerCrop()
                .override(Util.dip2px(getActivity(), 65), Util.dip2px(getActivity(), 65))
                .into(imgAvatar);
        }
    }

    @Override
    public void setupView() {

    }

    @Override
    public View getOverrideParentView() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_avatar:
                startActivityForResult(new Intent(getActivity(), UserInformationActivity.class),
                    REQUEST_CODE);
                break;
            case R.id.relative_presenter_twitter:
                startActivity(new Intent(getActivity(), PersonalTwitterActivity.class));
                break;
            case R.id.relative_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.relative_feedback:
                Intent intentFeedback = new Intent(getActivity(), BrowserActivity.class);
                intentFeedback.putExtra(BrowserActivity.BROWSER_KEY,
                    Constants.URL_HEAD + "/mobile/suggest");
                intentFeedback.putExtra("title", "意见反馈");
                startActivity(intentFeedback);
                break;
            case R.id.relative_me_share:
                handleShare(getActivity(),
                    "http://a.app.qq.com/o/simple.jsp?pkgname=com.miuhouse.yourcompany.student",
                    "e学陪",
                    null);
                break;
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (account.getRoleName() != null) {
                    tvName.setText(data.getStringExtra("name") + " " + account.getRoleName());
                } else {
                    tvName.setText(data.getStringExtra("name"));
                }
                Glide.with(this)
                    .load(data.getStringExtra("headUrl"))
                    .centerCrop()
                    .override(Util.dip2px(getActivity(), 65), Util.dip2px(getActivity(), 65))
                    .into(imgAvatar);
            }
        } else {
            UMShareAPI.get(getActivity()).onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 分享
     */
    public void handleShare(Activity context, String url, String title, String shareImageUrl) {
        final ShareDialog dialog = new ShareDialog(context, url, title, shareImageUrl);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        TextView textView = (TextView) dialog.findViewById(android.R.id.title);
        textView.setTextSize(15);
        dialog.setTitle("分享");
        dialog.show();
    }
}
