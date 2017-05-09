package com.miuhouse.yourcompany.student.view.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.application.App;
import com.miuhouse.yourcompany.student.model.OrderEntity;
import com.miuhouse.yourcompany.student.presenter.OrderPayPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.IOrderPayPresenter;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IOrderPayActivity;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;
import com.miuhouse.yourcompany.student.view.widget.ViewOverrideManager;
import com.pingplusplus.android.Pingpp;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kings on 9/2/2016.
 */
public class OrderPayActivity extends BaseActivity implements IOrderPayActivity, View.OnClickListener {

    private ImageView header;
    private TextView tName;
    private TextView tvClassTime;
    private TextView tvTel;
    private TextView totalPay;
    private RelativeLayout rlAlipay;
    private RelativeLayout rlWeixin;
    private RadioButton rdAlipay;
    private RadioButton rdWeixin;
    private TextView memo;
    private TextView tvActualPay;
    private int totalPrice;
    private IOrderPayPresenter presenter;

    private String payChannel;
    private String orderId;
    private long beginTime;
    private String tel;
    private int orderType;
    private TextView tvOrderTerm;
    private String studentName;
    private RelativeLayout content;
    private Button payNow;

    public static final String PAYERROR_CHARGEERROR = "charge_error";
    public static final String PAYERROR_NOCHARGE = "no_charge";
    public static final String PAYERROR_ORDERTIMEOUT = "order_timeout";
    public static final String PAYERROR_NONETWORK = "no_network";

    @Override
    protected String setTitle() {
        return "订单支付";
    }

    @Override
    protected String setRight() {
        return null;
    }

    @Override
    protected void initViewAndEvents() {
        orderId = getIntent().getStringExtra("orderId");
//        totalPrice = getIntent().getIntExtra("totalPrice", 0);
//        beginTime = getIntent().getLongExtra("beginTime", 0);
//        tel = getIntent().getStringExtra("tel");
        orderType = getIntent().getIntExtra("orderType", 1);
//        studentName = getIntent().getStringExtra("studentName");

        presenter = new OrderPayPresenter(this);
        content = (RelativeLayout) findViewById(R.id.content);
        header = (ImageView) findViewById(R.id.header);
        tName = (TextView) findViewById(R.id.teacherName);
        tvClassTime = (TextView) findViewById(R.id.classTime);
        tvTel = (TextView) findViewById(R.id.tel);
        totalPay = (TextView) findViewById(R.id.totalPay);
        rlAlipay = (RelativeLayout) findViewById(R.id.rlAlipay);
        rlWeixin = (RelativeLayout) findViewById(R.id.rlWeixin);
        rlAlipay.setOnClickListener(this);
        rlWeixin.setOnClickListener(this);
        rdAlipay = (RadioButton) findViewById(R.id.radio_alipay);
        rdWeixin = (RadioButton) findViewById(R.id.radio_weixin);
        memo = (TextView) findViewById(R.id.memo);
        tvActualPay = (TextView) findViewById(R.id.actualPay);
        tvOrderTerm = (TextView) findViewById(R.id.orderTerm);
        payNow = (Button) findViewById(R.id.payNow);
        payNow.setOnClickListener(this);
        payChannel = Constants.ALIPAY;
        rdAlipay.setChecked(true);
        rdWeixin.setChecked(false);
        presenter.getOrderInfo(orderId);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_order_pay;
    }

    @Override
    protected View getOverrideParentView() {
        return content;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlAlipay:
                payChannel = Constants.ALIPAY;
                rdAlipay.setChecked(true);
                rdWeixin.setChecked(false);
                break;
            case R.id.rlWeixin:
                payChannel = Constants.WEIXIN;
                rdWeixin.setChecked(true);
                rdAlipay.setChecked(false);
                break;
            case R.id.payNow:
                if (payChannel == Constants.ALIPAY) {
                    payInAlipay(orderId);
                } else {
                    payInWeixin(orderId);
                }
                break;
        }
    }

    @Override
    public void payInAlipay(String orderId) {
//        paySuccess();
        presenter.payInAlipay(this, orderId);
    }

    @Override
    public void payInWeixin(String orderid) {
        presenter.payInWeixin(this, orderid);
    }

    @Override
    public void payOnGoing() {
        payNow.setEnabled(false);
    }

    @Override
    public void payOver(String tag) {
        payNow.setEnabled(true);
        if (TextUtils.isEmpty(tag)){
            return ;
        }
        if (tag.equals(PAYERROR_CHARGEERROR)){
            L.i("PAY", "请检查URL" + "URL无法获取charge");
            Toast.makeText(App.getContext(), "服务器错误，支付失败", Toast.LENGTH_LONG).show();
        }else if (tag.equals(PAYERROR_NOCHARGE)){
            L.i("PAY", "没有返回charge字段");
            Toast.makeText(App.getContext(), "服务器错误，支付失败", Toast.LENGTH_LONG).show();
        }else if (tag.equals(PAYERROR_ORDERTIMEOUT)){
            Toast.makeText(App.getContext(), "课程超过上课时间", Toast.LENGTH_LONG).show();
        }else if (tag.equals(PAYERROR_NONETWORK)) {
            Toast.makeText(App.getContext(), "服务器错误，支付失败", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void paySuccess() {
        finish();
        startActivity(new Intent(activity, PayResultActivity.class)
                .putExtra("orderId", orderId));
    }

    @Override
    public void payFailed() {
        Toast.makeText(this, "支付未完成", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void showOrderInfo(OrderEntity order) {
        tvClassTime.setText("开始时间  "
                + new SimpleDateFormat("MM/dd HH:mm")
                .format(new Date(order.getClassBeginTimePromise())));
        tvTel.setText(order.getMobile() + " （手机以便联系）");
        int colorId = Util.getResourceId(this,
                String.format("background_order_%s", order.getMajorDemand()), "color");
        int nameId = Util.getResourceId(this,
                String.format("order_type_%s", order.getMajorDemand()), "string");
        tName.setText(getResources().getString(nameId));
        if (TextUtils.isEmpty(order.getOrderType())) {
            order.setOrderType("1");
        }
        if (order.getOrderType().equals("1")) {
            tvOrderTerm.setVisibility(View.INVISIBLE);
        } else {
            int termId = Util.getResourceId(this,
                    String.format("order_term_%s", order.getOrderType()), "string");
            tvOrderTerm.setText(getResources().getString(termId));
        }
        int headId = Util.getResourceId(this,
                String.format("ico_ordertype_%s", order.getMajorDemand()), "mipmap");
        header.setImageResource(headId);
        tvActualPay.setText(order.getAmount() + "");
    }

    @Override
    public void showLoading(String msg) {
        super.showLoading(msg);
    }

    @Override
    public void showError(int type) {
        viewOverrideManager.showLoading(type, new ViewOverrideManager.OnExceptionalClick() {
            @Override
            public void onExceptionalClick() {
                presenter.getOrderInfo(orderId);
            }
        });
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            payOver(null);
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");

                // 处理返回值
                // "success" - 支付成功
                // "fail"    - 支付失败
                // "cancel"  - 取消支付
                // "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）

                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                L.i(result + " -- " + errorMsg + " -- " + extraMsg);
                if (result.equals("success")) {
                    startActivity(new Intent(context, PayResultActivity.class)
                            .putExtra("orderId", orderId)
                            .putExtra("orderType", orderType)
                            .putExtra(PayResultActivity.RESULT, PayResultActivity.RESULT_SUCCESS));
                } else if (result.equals("fail")) {
                    Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();
                } else if (result.equals("cancel")) {
                    Toast.makeText(context, "支付取消", Toast.LENGTH_SHORT).show();
                } else if (result.equals("invalid")) {
                    Toast.makeText(context, "没有安装客户端", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
