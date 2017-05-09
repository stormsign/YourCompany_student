package com.miuhouse.yourcompany.student.view.ui.activity;

import android.view.View;
import android.widget.TextView;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.application.ActivityManager;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;

/**
 * Created by khb on 2016/10/25.
 */
public class PayHoldActivity extends BaseActivity {

    @Override
    protected String setTitle() {
        return "支付";
    }

    @Override
    protected String setRight() {
        return null;
    }

    @Override
    protected void initViewAndEvents() {
        TextView success = (TextView) findViewById(R.id.paysuccess);
        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                ActivityManager.getInstance().popActivity(ActivityManager.getInstance().getTopActivity());
            }
        });
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_payhold;
    }

    @Override
    protected View getOverrideParentView() {
        return null;
    }
}
