package com.miuhouse.yourcompany.student.view.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.application.App;
import com.miuhouse.yourcompany.student.db.AccountDBTask;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;
import com.miuhouse.yourcompany.student.view.widget.CircularImageViewHome;

/**
 * Created by kings on 1/9/2017.
 */
public class AccountActivity extends BaseActivity implements View.OnClickListener {

    private CircularImageViewHome imgAvatar;
    //private RelativeLayout relativePresenterTwitter;

    @Override protected String setTitle() {
        return null;
    }

    @Override protected String setRight() {
        return null;
    }

    @Override protected void initViewAndEvents() {
        imgAvatar = (CircularImageViewHome) findViewById(R.id.img_avatar);
        //relativePresenterTwitter = (RelativeLayout) findViewById(R.id.relative_presenter_twitter);
        imgAvatar.setOnClickListener(this);
        //relativePresenterTwitter.setOnClickListener(this);
        String strHeadUrl = App.getInstance().getParentInfo().getHeadUrl();

        if (strHeadUrl != null) {
            Glide.with(this)
                .load(strHeadUrl)
                .centerCrop()
                .override(Util.dip2px(this, 50), Util.dip2px(this, 50))
                .into(imgAvatar);
        } else {
            Glide.with(this)
                .load(R.mipmap.ico_head_default)
                .centerCrop()
                .override(Util.dip2px(this, 50), Util.dip2px(this, 50))
                .into(imgAvatar);
        }
    }

    @Override protected int getContentLayoutId() {
        return R.layout.activity_account;
    }

    @Override protected View getOverrideParentView() {
        return null;
    }

    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_avatar:
                startActivity(new Intent(this, UserInformationActivity.class));
                break;
            //case R.id.relative_presenter_twitter:
            //    startActivity(new Intent(this,PersonalTwitterActivity.class));
            //    break;
        }
    }
}
