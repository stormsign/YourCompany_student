package com.miuhouse.yourcompany.student.view.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.application.App;
import com.miuhouse.yourcompany.student.db.DictDBTask;
import com.miuhouse.yourcompany.student.model.OrderEntity;
import com.miuhouse.yourcompany.student.presenter.OrderDetailPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.IOrderDetailPresenter;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.utils.Values;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IOrderDetailActivity;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;
import com.miuhouse.yourcompany.student.view.widget.CircularImageViewHome;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by khb on 2016/7/20.
 */
public class OrderDetailActivity extends BaseActivity implements IOrderDetailActivity {

    private CircularImageViewHome teacherHead;
    private TextView gradeAndSubject;
    private ImageView ivOrderStatus;
    private TextView tvOrderStatus;
    private TextView teacherName;
    private TextView student;
    private TextView tel;
    private TextView address;
    private TextView demend;
    private LinearLayout call;
    private RelativeLayout content;
    private IOrderDetailPresenter presenter;
    private TextView classCount;
    private TextView totalPrice;
    private TextView time;

    private final int REQUESTCODE_REFUND = -1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            OrderEntity order = (OrderEntity) msg.obj;

            long beginTime = order.getClassBeginTimeActual();
            long currentTime = System.currentTimeMillis();
            long diffTime = currentTime - beginTime;
            long hour = diffTime / (60 * 60 * 1000);
            long minute = diffTime % (60 * 60 * 1000) / (60 * 1000);

            String strHour = hour < 10 ? "0" + hour : "" + hour;
            String strMinute = minute < 10 ? "0" + minute : "" + minute;
            if (diffTime < Integer.parseInt(order.getLesson()) * 60 * 60 * 1000) {
                time.setText(strHour + ":" + strMinute);
                Message msg2 = Message.obtain();
                msg2.obj = order;
                handler.sendMessageDelayed(msg2, 1000 * 10);
            } else {
                time.setText("已到点");
            }


        }
    };
    private TextView actual;
    private RelativeLayout bottom;
    private TextView button;
    private String teacherId;
    private String orderInfoId;

    private OrderEntity order;
    private TextView classDate;
    private TextView classTime;
    private LinearLayout actualLayout;
    private TextView singlePrice;
    private LinearLayout studentInfo;
    private TextView orderType;
    private RelativeLayout orderTypeLayout;

    @Override
    protected String setTitle() {
        return "订单详情";
    }

    @Override
    protected String setRight() {
        return null;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_orderdetail;
    }

    @Override
    protected View getOverrideParentView() {
        return content;
    }

    @Override
    protected void initViewAndEvents() {

        presenter = new OrderDetailPresenter(this);

        ivOrderStatus = (ImageView) findViewById(R.id.ivOrderStatus);
        tvOrderStatus = (TextView) findViewById(R.id.tvOrderStatus);
        time = (TextView) findViewById(R.id.time);
        orderTypeLayout = (RelativeLayout) findViewById(R.id.orderTypeLayout);
        orderType = (TextView) findViewById(R.id.orderType);
        gradeAndSubject = (TextView) findViewById(R.id.gradeAndSubject);
        teacherHead = (CircularImageViewHome) findViewById(R.id.teacherHead);
        teacherName = (TextView) findViewById(R.id.teacherName);
        singlePrice = (TextView) findViewById(R.id.singlePrice);
        classCount = (TextView) findViewById(R.id.classCount);
        totalPrice = (TextView) findViewById(R.id.totalPrice);
        student = (TextView) findViewById(R.id.teacher);
        tel = (TextView) findViewById(R.id.tel);
        address = (TextView) findViewById(R.id.classAddress);
        classDate = (TextView) findViewById(R.id.classDate);
        classTime = (TextView) findViewById(R.id.classTime);
        demend = (TextView) findViewById(R.id.demand);
        call = (LinearLayout) findViewById(R.id.call);
        content = (RelativeLayout) findViewById(R.id.content);
        actualLayout = (LinearLayout) findViewById(R.id.actualLayout);
        actual = (TextView) findViewById(R.id.actual);
        bottom = (RelativeLayout) findViewById(R.id.bottom);
        button = (TextView) findViewById(R.id.button);
        studentInfo = (LinearLayout) findViewById(R.id.studentInfo);

        orderInfoId = getIntent().getStringExtra("orderId");
        teacherId = App.getInstance().getParentId();
    }

    @Override
    public void fillView(final OrderEntity order) {
        this.order = order;
        if (Integer.parseInt(order.getOrderStatus()) < 3) {
            studentInfo.setVisibility(View.VISIBLE);
            teacherHead.setImageResource(Util.getResourceId(this, String.format("ico_ordertype_%s", order.getMajorDemand()), "mipmap"));
            teacherName.setText(Util.getResourceId(this, String.format("order_type_%s", order.getMajorDemand()), "string"));
            orderTypeLayout.setVisibility(View.GONE);
        } else {
            teacherName.setText(order.getTname());
            if (!Util.isEmpty(order.getUserHeader())) {
                Glide.with(activity).load(order.getUserHeader())
                        .placeholder(R.mipmap.ico_head_default)
                        .error(R.mipmap.ico_head_default)
                        .into(teacherHead);
            }
            teacherHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(activity, DetailpagesActivity.class)
                    .putExtra("teacherId", order.getTeacherId()));
                }
            });
            orderTypeLayout.setVisibility(View.VISIBLE);
            int strId = Util.getResourceId(this,
                    String.format("order_type_%s", order.getMajorDemand()), "string");
            int bgId = Util.getResourceId(this,
                    String.format("home_list_title_%s", order.getMajorDemand()), "mipmap");
            orderType.setText(strId);
            orderType.setBackgroundResource(bgId);
        }
        student.setText(order.getPname());
        tel.setText(order.getMobile());
        classCount.setText("x" + order.getLesson());
//        double amount = Math.round(order.getAmount()*100)/100;
        double amount = order.getAmount();
        totalPrice.setText(amount + "元");
        singlePrice.setText(amount/Integer.parseInt(order.getLesson()) + "元");
        address.setText(order.getAddress());
        String date = new SimpleDateFormat("yyyy-MM-dd")
                .format(new Date(order.getClassBeginTimePromise()));
        String startTime = new SimpleDateFormat("HH:mm")
                .format(new Date(order.getClassBeginTimePromise()));
        String endTime = new SimpleDateFormat("HH:mm")
                .format(new Date(order.getClassEndTimePromise()));
        classDate.setText(date);
        classTime.setText(startTime + " ~ " + endTime);
//        schedule.setText("课程安排：" + formatTime);
        demend.setText(order.getDescription());

        if (Integer.parseInt(order.getOrderStatus())<5
                ){    //课程还没有结束，不显示打电话，不显示实际上课时间
            if (Integer.parseInt(order.getOrderStatus())<3){
                call.setVisibility(View.GONE);
            }
            actualLayout.setVisibility(View.GONE);
        }else {     //课程已结束
            if (Integer.parseInt(order.getOrderStatus())==8){   //特殊情况，订单退款中
                call.setVisibility(View.GONE);
                actualLayout.setVisibility(View.GONE);
            }else {
                actualLayout.setVisibility(View.VISIBLE);
                String dateActual = new SimpleDateFormat("MM月dd日")
                        .format(new Date(order.getClassBeginTimeActual()));
                String startTimeActual = new SimpleDateFormat("HH:mm")
                        .format(new Date(order.getClassBeginTimeActual()));
                String endTimeActual = new SimpleDateFormat("HH:mm")
                        .format(new Date(order.getClassBeginTimeActual()
                                + Integer.parseInt(order.getLesson()) * 60 * 60 * 1000));
                String formatTime = dateActual + " " + startTimeActual + "-" + endTimeActual;
                actual.setText(formatTime);
            }
        }


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(order.getTmobile());
            }
        });

        String gradeSubject = "";
        if (!TextUtils.isEmpty(order.getGradeLevel())){
            gradeSubject += Values.getValue(Values.studentGrades,
                    Integer.parseInt(order.getGradeLevel()));
        }
        if (order.getMajorDemand().equals("2")) { //作业辅导
            if (!TextUtils.isEmpty(order.getMinorDemand())) {
                gradeSubject += DictDBTask.getDcName("subject_type",
                        Integer.parseInt(order.getMinorDemand()));
            }
        }else if (order.getMajorDemand().equals("3")){ //兴趣培养
            if (!TextUtils.isEmpty(order.getMinorDemand())) {
                gradeSubject += DictDBTask.getDcName("interest_type",
                        Integer.parseInt(order.getMinorDemand()));
            }
        }
        if (!TextUtils.isEmpty(gradeSubject)){
            gradeAndSubject.setText(gradeSubject);
        }else {
            gradeAndSubject.setVisibility(View.GONE);
        }
        showOrderType(order);
        showOrderStatus(order);
        showCountdown(order);
    }

    @Override
    protected void onResume() {
        presenter.getOrderDetail(teacherId, orderInfoId);
        super.onResume();
    }

    private void showOrderType(OrderEntity order) {
        if (Integer.parseInt(order.getOrderStatus()) < 3) {
            orderTypeLayout.setVisibility(View.GONE);
        } else {
            orderTypeLayout.setVisibility(View.VISIBLE);
            int strId = Util.getResourceId(this,
                    String.format("order_type_%s", order.getMajorDemand()), "string");
            int bgId = Util.getResourceId(this,
                    String.format("home_list_title_%s", order.getMajorDemand()), "mipmap");
            orderType.setText(strId);
            orderType.setBackgroundResource(bgId);
        }
    }

    @Override
    public void showCountdown(OrderEntity order) {
        if (Integer.parseInt(order.getOrderStatus())
                == 4) {

            Message msg = Message.obtain();
            msg.obj = order;
            handler.sendMessage(msg);

        } else {
            time.setVisibility(View.GONE);
        }
    }

    @Override
    public void showOrderStatus(final OrderEntity order) {

        int imgId = Util.getResourceId(this,
                String.format("ico_orderdetail_%s", order.getOrderStatus()),
                "mipmap");
        int strId = Util.getResourceId(this,
                String.format("order_status_%s", order.getOrderStatus()),
                "string");
        ivOrderStatus.setImageResource(imgId);
        tvOrderStatus.setText(getResources().getString(strId));
        bottom.setVisibility(View.GONE);
        if (Values.orderStatuses.get(order.getOrderStatus()).equals("待支付")) {
            bottom.setVisibility(View.VISIBLE);
            button.setText("去支付");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context, OrderPayActivity.class)
                            .putExtra("orderId", order.getId()));
                }
            });
        } else if (Values.orderStatuses.get(order.getOrderStatus()).equals("待评价")) {
            bottom.setVisibility(View.VISIBLE);
            button.setText("去评价");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context, OrderCommentActivity.class)
                                    .putExtra("tname", order.getTname())
                                    .putExtra("tHeader", order.getUserHeader())
                                    .putExtra("classBeginTimePromise", order.getClassBeginTimePromise())
                                    .putExtra("majorDemand", order.getMajorDemand())
                                    .putExtra("orderInfoId",order.getId())
                    );
                }
            });
        } else if (Values.orderStatuses.get(order.getOrderStatus()).equals("已完成")) {

        } else if (Values.orderStatuses.get(order.getOrderStatus()).equals("待上课")) {
            bottom.setVisibility(View.VISIBLE);
            button.setText("退款");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(context, OrderRefundActivity.class)
                            .putExtra("order", order), REQUESTCODE_REFUND);
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE_REFUND){
            if (resultCode == RESULT_OK){
                presenter.getOrderDetail(teacherId, orderInfoId);
            }
        }
    }

    @Override
    public void call(String number) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            L.i("read phone state GRANTED");
        } else {
            L.i("read phone state DENIED");

        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
//                Toast.makeText(this, "大哥，跪求通过权限", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            } else {
                Toast.makeText(this, "权限已被禁止，要想重新开启，请在手机的权限管理中找到" + getResources().getString(R.string.app_name) + "应用，找到拨打电话权限并选择允许", Toast.LENGTH_LONG).show();
            }
        } else {
            //传入服务， parse（）解析号码
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
            //通知activtity处理传入的call服务
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //传入服务， parse（）解析号码
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + order.getMobile()));
                //通知activtity处理传入的call服务
                startActivity(intent);
            } else {
                Toast.makeText(this, "权限被拒绝", Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void showLoading(String msg) {
        super.showLoading("LOADING...");
    }

    @Override
    public void showError(int type) {
        viewOverrideManager.showLoading(type, null);
    }

    @Override
    public void onBackClick() {
        if (null != order) {
            if (order.getOrderStatus().equals("4")) {        //当前为进行中状态
                setResult(1);
            }
        }
        super.onBackClick();
    }

    @Override
    public void onBackPressed() {
        if (null != order) {
            if (order.getOrderStatus().equals("4")) {        //当前为进行中状态
                setResult(1);
            }
        }
        super.onBackPressed();
    }

    @Override
    public void beforeBeginClass() {
        showLoading(null);
    }

    @Override
    public void afterBeginClass() {
//        if (null != order){
//            if (order.getOrderStatus().equals("3")){        //当前为待上课状态
        setResult(1);
        finish();
//            }
//        }
    }
}
