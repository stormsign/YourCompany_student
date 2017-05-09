package com.miuhouse.yourcompany.student.view.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.application.ActivityManager;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.ISelectSuccessActivity;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;

/**
 * Created by khb on 2016/9/22.
 */
public class SelectSuccessActivity extends BaseActivity implements ISelectSuccessActivity, View.OnClickListener {

    /**
     * 短单
     */
    private static final String TYPE_ONE = "1";

    /**
     * 周单
     */
    private static final String TYPE_WEEKS = "2";
    /**
     * 月单
     */
    private static final String TYPE_MONTH = "3";

    private TextView goBack;
    private TextView check;
    private String orderId;
    private String orderType;

    @Override
    protected String setTitle() {
        return "订单选择";
    }

    @Override
    protected String setRight() {
        return null;
    }

    @Override
    public void onBackClick() {
        super.onBackClick();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_selectsuccess;
    }

    @Override
    protected View getOverrideParentView() {
        return null;
    }

    @Override
    protected void initViewAndEvents() {
        orderId = getIntent().getStringExtra("orderId");
        orderType = getIntent().getStringExtra("orderType");
        goBack = (TextView) findViewById(R.id.goBack);
        check = (TextView) findViewById(R.id.check);
        goBack.setOnClickListener(this);
        check.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goBack:
                goBack();
                break;
            case R.id.check:
                checkOrder(orderId, orderType);
                break;
        }
    }

    @Override
    public void goBack() {
        finish();
        while (!(ActivityManager.getInstance().getTopActivity()
                instanceof MainActivity)){
            ActivityManager.getInstance()
                    .popActivity(ActivityManager.getInstance().getTopActivity());
        }
    }

    @Override
    public void checkOrder(String orderId, String orderType) {
        finish();
        while (!(ActivityManager.getInstance().getTopActivity()
                instanceof MainActivity)){
            ActivityManager.getInstance()
                    .popActivity(ActivityManager.getInstance().getTopActivity());
        }
        if (orderType.equals(TYPE_ONE)) {
            startActivity(new Intent(activity, OrderDetailActivity.class)
                    .putExtra("orderId", orderId));
        } else {
            startActivity(new Intent(activity, OrderLongTermDetailActivity.class)
                    .putExtra("orderId", orderId)
                    .putExtra("orderType", orderType));
        }
    }
}
