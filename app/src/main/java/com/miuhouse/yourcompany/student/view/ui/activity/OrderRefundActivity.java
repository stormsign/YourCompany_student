package com.miuhouse.yourcompany.student.view.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.model.OrderEntity;
import com.miuhouse.yourcompany.student.presenter.OrderRefundPresenter;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.utils.Values;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IOrderRefundActivity;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;
import com.miuhouse.yourcompany.student.view.widget.CircularImageViewHome;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by khb on 2016/9/14.
 */
public class OrderRefundActivity extends BaseActivity implements IOrderRefundActivity, View.OnClickListener {

    private OrderEntity order;
    private CircularImageViewHome ivHeader;
    private TextView tvTName;
    private TextView tvBeginTime;
    private EditText edReason;
    private EditText edAmount;
    private TextView submit;
    private EditText edDesc;
    private OrderRefundPresenter presenter;

    @Override
    protected String setTitle() {
        return "退款";
    }

    @Override
    protected String setRight() {
        return null;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_orderrefund;
    }

    @Override
    protected View getOverrideParentView() {
        return findViewById(R.id.content);
    }

    @Override
    protected void initViewAndEvents() {
        presenter = new OrderRefundPresenter(this);
        order = (OrderEntity) getIntent().getSerializableExtra("order");
        ivHeader = (CircularImageViewHome) findViewById(R.id.header);
        tvTName = (TextView) findViewById(R.id.tName);
        tvBeginTime = (TextView) findViewById(R.id.beginTime);
        edReason = (EditText) findViewById(R.id.refundReason);
        edAmount = (EditText) findViewById(R.id.refundAmount);
        edDesc = (EditText) findViewById(R.id.refundDesc);
        submit = (TextView) findViewById(R.id.submit);
        edAmount.setClickable(false);
        edAmount.setFocusable(false);
        edReason.setOnClickListener(this);
        edDesc.setOnClickListener(this);
        submit.setOnClickListener(this);
        tvTName.setText(Values.getValue(Values.majorDemand,
                Integer.parseInt(order.getMajorDemand())));
        tvBeginTime.setText("课程安排 "+
                new SimpleDateFormat("MM/dd HH:mm")
                    .format(new Date(order.getClassBeginTimePromise())));
        ivHeader.setImageResource(Util.getResourceId(this, String.format("ico_ordertype_%s", order.getMajorDemand()), "mipmap"));
        edAmount.setText("￥"+order.getAmount());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.refundReason:

                break;
            case R.id.refundDesc:

                break;
            case R.id.submit:
                presenter.submitRefund(order.getId(),
                        edReason.getText().toString(),
                        edDesc.getText().toString());
                break;
        }
    }

    @Override
    public void showLoading(String msg) {
        super.showLoading(msg);
    }

    @Override
    public void showError(int type) {
        super.showError(type);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

    @Override
    public void refundSuccess() {
        finish();
        setResult(RESULT_OK  );
    }

    @Override
    public void refundFailed(String msg) {
        if (msg != null){
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "网络错误，退款失败", Toast.LENGTH_SHORT).show();
        }
    }
}
