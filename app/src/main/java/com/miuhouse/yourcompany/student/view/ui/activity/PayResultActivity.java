package com.miuhouse.yourcompany.student.view.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.application.ActivityManager;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IPaySuccessActivity;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;

import java.util.List;

/**
 * Created by khb on 2016/9/19.
 */
public class PayResultActivity extends BaseActivity implements IPaySuccessActivity, View.OnClickListener {

    private TextView back;
    private TextView check;
    private String orderId;
    private int orderType;
    public static final String RESULT = "result";
    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_FAIL = 1;
    public static final int RESULT_DUPLICATE = 2;
    public static final int RESULT_INVALID = 3;
    private int result;

    @Override
    protected String setTitle() {
        return "支付成功";
    }

    @Override
    protected String setRight() {
        return null;
    }

    @Override
    public void onBackClick() {
        goBack();
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    @Override
    protected void initViewAndEvents() {
        orderId = getIntent().getStringExtra("orderId");
        orderType = getIntent().getIntExtra("orderType", 1);
        result = getIntent().getIntExtra(RESULT, RESULT_SUCCESS);
        back = (TextView) findViewById(R.id.goBack);
        check = (TextView) findViewById(R.id.check);
        back.setOnClickListener(this);
        check.setOnClickListener(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_payresult;
    }

    @Override
    protected View getOverrideParentView() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.goBack:
                goBack();
                break;
            case R.id.check:
                checkOrder(orderId);
                break;
        }
    }

    @Override
    public void goBack() {
        finish();
        if (!(ActivityManager.getInstance().getTopActivity()
                instanceof OrdersManageActivity)){
            while (!(ActivityManager.getInstance().getTopActivity()
                    instanceof MainActivity)){
                ActivityManager.getInstance()
                        .popActivity(ActivityManager.getInstance().getTopActivity());
            }
        }
    }

    @Override
    public void checkOrder(String orderId) {

        List<Activity> list = ActivityManager.getInstance().getStackActivities();
        for (Activity activity : list){
            if (activity instanceof OrderPayActivity
                    || activity instanceof OrderConfirmActivity){
                ActivityManager.getInstance().popActivity(activity);
            }
        }
        finish();
        if (orderType==1){
            startActivity(new Intent(activity, OrderDetailActivity.class)
                    .putExtra("orderId", orderId));
        }else{
            startActivity(new Intent(activity, OrderLongTermDetailActivity.class)
                    .putExtra("orderId", orderId));
        }
    }


}
