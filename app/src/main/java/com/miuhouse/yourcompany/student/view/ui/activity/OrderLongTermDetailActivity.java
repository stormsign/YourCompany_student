package com.miuhouse.yourcompany.student.view.ui.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.miuhouse.yourcompany.student.presenter.OrderLongTermDetailPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.IOrderDetailPresenter;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.utils.Values;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IOrderLongTermDetailActivity;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;
import com.miuhouse.yourcompany.student.view.widget.CircularImageViewHome;
import com.miuhouse.yourcompany.student.view.widget.date.SublimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import cn.smssdk.gui.IdentifyNumPage;

/**
 * Created by khb on 2016/7/20.
 * 长单详情
 */
public class OrderLongTermDetailActivity extends BaseActivity implements IOrderLongTermDetailActivity {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private TextView orderType;
    private ImageView ivOrderStatus;
    private TextView tvOrderStatus;
    private TextView teacherName;
    private TextView student;
    private TextView tel;
    private TextView address;
    //    private TextView schedule;
    private TextView tvDemend;
    private ImageView orderTag;
    private TextView classTime;

    /**
     * 单价
     */
    private TextView singlePrice;

    /**
     * 年级和科目
     */
    private TextView gradeAndSubject;
    private LinearLayout call;
    private FrameLayout content;
    private TextView classCount;
    private TextView totalPrice;
    private TextView time;
    private TextView classDate;
    private TextView tvWeekly;
    private TimeInterpolator ANIMATION_INTERPOLATOR = new DecelerateInterpolator();
    private int ANIMATION_DURATION = 500;
    private SublimePicker sublimePicker;
    private LinearLayout linearAddress;
    private LinearLayout studentInfo;
    private CircularImageViewHome teacherHead;
    private RelativeLayout orderTypeLayout;
    private LinearLayout linearCalendar;

    private final Rect mTmpRect = new Rect();

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

    private RelativeLayout bottom;
    private TextView button;
    private ImageView btnCalendarIcon;


    private OrderEntity order;

    /**
     * true;收起SublimePicker,false 显示SublimePicker
     */
    private boolean isMark;

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
        return R.layout.activity_orderlongtermdetail;
    }

    @Override
    protected View getOverrideParentView() {
        return content;
    }

    @Override
    protected void initViewAndEvents() {

        IOrderDetailPresenter presenter = new OrderLongTermDetailPresenter(this);

        ivOrderStatus = (ImageView) findViewById(R.id.ivOrderStatus);
        tvOrderStatus = (TextView) findViewById(R.id.tvOrderStatus);
        time = (TextView) findViewById(R.id.time);
        orderType = (TextView) findViewById(R.id.orderType);
        teacherHead = (CircularImageViewHome) findViewById(R.id.teacherHead);
        teacherName = (TextView) findViewById(R.id.teacherName);
        orderTypeLayout = (RelativeLayout) findViewById(R.id.orderTypeLayout);
        classCount = (TextView) findViewById(R.id.classCount);
        totalPrice = (TextView) findViewById(R.id.totalPrice);
        student = (TextView) findViewById(R.id.teacher);
        tel = (TextView) findViewById(R.id.tel);
        address = (TextView) findViewById(R.id.classAddress);
        tvDemend = (TextView) findViewById(R.id.demand);
        singlePrice = (TextView) findViewById(R.id.singlePrice);
        gradeAndSubject = (TextView) findViewById(R.id.gradeAndSubject);
        orderTag = (ImageView) findViewById(R.id.orderTag);
        classTime = (TextView) findViewById(R.id.classTime);
        call = (LinearLayout) findViewById(R.id.call);
        content = (FrameLayout) findViewById(R.id.content);
        classDate = (TextView) findViewById(R.id.classDate);
        bottom = (RelativeLayout) findViewById(R.id.bottom);
        button = (TextView) findViewById(R.id.button);
        tvWeekly = (TextView) findViewById(R.id.tv_weekly);
        sublimePicker = (SublimePicker) findViewById(R.id.sublime_picker);
        linearAddress = (LinearLayout) findViewById(R.id.linear_address);
        studentInfo = (LinearLayout) findViewById(R.id.studentInfo);
        btnCalendarIcon = (ImageView) findViewById(R.id.img_calendar);
        linearCalendar = (LinearLayout) findViewById(R.id.linear_calendar);
        btnCalendarIcon.setOnClickListener(getCalendarOnClickListener());
        linearCalendar.setOnClickListener(getCalendarOnClickListener());

        /**隐藏重置按钮*/
        sublimePicker.setGoneResetTv();
        /**设置点击事件无效*/
        sublimePicker.setIsSublimeClick(true);

        /*
      订单id
     */
        String orderInfoId = getIntent().getStringExtra("orderId");
        presenter.getOrderDetail(App.getInstance().getParentId(), orderInfoId);
    }

    private void focusOn(View v) {

        v.getDrawingRect(mTmpRect);
        content.offsetDescendantRectToMyCoords(v, mTmpRect);

    }


    private void fadeOutToBottom(View v, boolean animated, SublimePicker sublimePicker) {
        v.animate().
                translationYBy(sublimePicker.getHeight()).
                setDuration(animated ? ANIMATION_DURATION : 0).
                setInterpolator(ANIMATION_INTERPOLATOR).
                setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        Log.i("TAG", "start");
                        btnCalendarIcon.setEnabled(false);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        btnCalendarIcon.setEnabled(true);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                }).
                start();
        sublimePicker.animate().
                alpha(1).
                setDuration(animated ? ANIMATION_DURATION : 0).
                setInterpolator(ANIMATION_INTERPOLATOR).
                start();
    }

    private void fadeInToTop(View v, boolean animated, SublimePicker sublimePicker) {
        v.animate().
                translationYBy(-sublimePicker.getHeight()).
                setDuration(animated ? ANIMATION_DURATION : 0).
                setInterpolator(ANIMATION_INTERPOLATOR).
                start();
        sublimePicker.animate().
                alpha(0).
                setDuration(animated ? ANIMATION_DURATION : 0).
                setInterpolator(ANIMATION_INTERPOLATOR).
                start();
    }

    /**
     * 返回的订单详情数据
     *
     * @param order
     */
    @Override
    public void fillView(final OrderEntity order) {
        this.order = order;

        if (Integer.parseInt(order.getOrderStatus()) < 3) {
            studentInfo.setVisibility(View.VISIBLE);
            teacherHead.setImageResource(Util.getResourceId(this, String.format("ico_ordertype_%s", order.getMajorDemand()), "mipmap"));
            teacherName.setText(Util.getResourceId(this, String.format("order_type_%s", order.getMajorDemand()), "string"));
            orderTypeLayout.setVisibility(View.GONE);
            call.setVisibility(View.GONE);
        } else {
            teacherName.setText(order.getTname());
            if (!Util.isEmpty(order.getUserHeader())) {
                Glide.with(activity).load(order.getUserHeader())
                        .override(Util.dip2px(this, 50), Util.dip2px(this, 50))
                        .placeholder(R.mipmap.ico_head_default)
                        .error(R.mipmap.ico_head_default).centerCrop()
                        .into(teacherHead);
            } else {
                Glide.with(activity)
                        .load(R.mipmap.ico_head_default)
                        .override(Util.dip2px(this, 50), Util.dip2px(this, 50))
                        .centerCrop()
                        .into(teacherHead);
            }
            orderTypeLayout.setVisibility(View.VISIBLE);
            int strId = Util.getResourceId(this,
                    String.format("order_type_%s", order.getMajorDemand()), "string");
            int bgId = Util.getResourceId(this,
                    String.format("home_list_title_%s", order.getMajorDemand()), "mipmap");
            orderType.setText(strId);
            orderType.setBackgroundResource(bgId);
            call.setVisibility(View.GONE);
            teacherHead.setOnClickListener(getHeadViewOnClickListener(order.getTeacherId()));
        }
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(order.getClassBeginTimePromise());
        endCalendar.setTimeInMillis(order.getClassEndTimePromise());

        //给日历填充数据
        sublimePicker.setCalendar(getCountDate(startCalendar, endCalendar), true);


        String startTime = new SimpleDateFormat("HH:mm")
                .format(startCalendar.getTime());
        String endTime = new SimpleDateFormat("HH:mm")
                .format(endCalendar.getTime());
        classTime.setText(startTime + " ~ " + endTime);

        if (order.getOrderType().equals(Constants.TYPE_MONTH)) {
            classCount.setText("包月");
            orderTag.setImageResource(R.mipmap.ico_yue);
            classDate.setText(simpleDateFormat.format(startCalendar.getTime()) + "~" + simpleDateFormat.format(endCalendar.getTime()));

        } else if (order.getOrderType().equals(Constants.TYPE_WEEK)) {
            classCount.setText("包周");
            orderTag.setImageResource(R.mipmap.ico_zhou);
            classDate.setText(getOrderWeekDate(getCountDate(startCalendar, endCalendar)));
        } else {
            classCount.setText("共购买" + order.getLesson() + "课时");
        }
        double amount = order.getAmount();
        totalPrice.setText("总计" + amount + "元");
        singlePrice.setText(amount / Integer.parseInt(order.getLesson()) + "元");

        student.setText(order.getPname());
        tel.setText(order.getMobile());
        address.setText("上课地址：" + order.getAddress());

        tvDemend.setText(order.getDescription());
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(order.getMobile());
            }
        });

//        showOrderType(order);
        showOrderStatus(order);
        showCountdown(order);
        tvWeekly.setText(fillWeeks(order.getWeekLoop()));

        classCount.setText("x" + order.getLesson());

        /**年级和科目*/
        String gradeSubject = "";
        if (!TextUtils.isEmpty(order.getGradeLevel())) {
            gradeSubject += Values.getValue(Values.studentGrades,
                    Integer.parseInt(order.getGradeLevel()));
        }
        if (order.getMajorDemand().equals("2")) { //作业辅导
            if (!TextUtils.isEmpty(order.getMinorDemand())) {
                gradeSubject += DictDBTask.getDcName("subject_type",
                        Integer.parseInt(order.getMinorDemand()));
            }
        } else if (order.getMajorDemand().equals("3")) { //兴趣培养
            if (!TextUtils.isEmpty(order.getMinorDemand())) {
                gradeSubject += DictDBTask.getDcName("interest_type",
                        Integer.parseInt(order.getMinorDemand()));
            }
        }
        if (!TextUtils.isEmpty(gradeSubject)) {
            gradeAndSubject.setText(gradeSubject);
        } else {
            gradeAndSubject.setVisibility(View.INVISIBLE);
        }

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(order.getTmobile());
            }
        });

    }

    private View.OnClickListener getHeadViewOnClickListener(final String teacherId) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, DetailpagesActivity.class)
                        .putExtra("teacherId", teacherId));
            }
        };
    }

    private View.OnClickListener getCalendarOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMark) {
                    focusOn(findViewById(R.id.img_calendar));
                    fadeOutToBottom(linearAddress, true, sublimePicker);
                    isMark = true;
                } else {
                    isMark = false;
                    fadeInToTop(linearAddress, true, sublimePicker);
                }
            }
        };
    }

    /**
     * 根据开始时间，结束时间和每周星期几上课得到日期表
     *
     * @param startCalendar
     * @param endCalendar
     * @return
     */
    public LinkedHashMap<Integer, List<Integer>> getCountDate(Calendar startCalendar, Calendar endCalendar) {
        /**
         * 用hashMap保存选择的日期，key为月，value是日
         */
        LinkedHashMap<Integer, List<Integer>> countDate = new LinkedHashMap<Integer, List<Integer>>();
        int startMonth = startCalendar.get(Calendar.MONTH) + 1;
        int endMonth = endCalendar.get(Calendar.MONTH) + 1;
        int startDay = startCalendar.get(Calendar.DAY_OF_MONTH);

        int currentMonthDay = getDaysByYearMonth(startCalendar.get(Calendar.YEAR), startMonth);
        int startWeek = startCalendar.get(Calendar.DAY_OF_WEEK) - 1;
//        if (order.getOrderType().equals(Constants.TYPE_WEEK)) {
        if (startMonth == endMonth) {
            ArrayList<Integer> dayList = new ArrayList<>();
            for (int i = 0; i < Calendar.DAY_OF_WEEK; i++) {
                for (String week : order.getWeekLoop()) {
                    if (week.equals(String.valueOf(startWeek))) {
                        dayList.add(startDay);
                    }
                }
                startDay++;
                startWeek = (startWeek++ == 6) ? 0 : startWeek;
            }
            countDate.put(startMonth, dayList);
        } else {
            ArrayList<Integer> dayListOne = new ArrayList<>();
            ArrayList<Integer> dayListTwo = new ArrayList<>();
            boolean isCurrentMonth = false;
            for (int i = 0; i < Calendar.DAY_OF_WEEK; i++) {
                for (String week : order.getWeekLoop()) {
                    if (week.equals(String.valueOf(startWeek))) {
                        if (startDay > currentMonthDay) {
                            startDay = 1;
                            isCurrentMonth = true;
                        }
                        if (isCurrentMonth) {
                            dayListTwo.add(startDay);
                        } else {
                            dayListOne.add(startDay);
                        }
                        break;
                    }
                }
                startDay++;
                startWeek = (startWeek++ == 6) ? 0 : startWeek;
            }
            countDate.put(startMonth, dayListOne);
            countDate.put(endMonth, dayListTwo);


        }
        return countDate;
    }

    public String getOrderWeekDate(LinkedHashMap<Integer, List<Integer>> countDate) {
        Iterator iterator = countDate.keySet().iterator();
        StringBuilder sf = new StringBuilder();
        int countMonth = 0;
        while (iterator.hasNext()) {
            int count = 0;
            int selectMonth = (Integer) iterator.next();
            if (countDate.get(selectMonth).size() > 0) {

                sf.append(selectMonth).append("月");
                countMonth += 1;
            }
            for (Integer day : countDate.get(selectMonth)) {
                sf.append(day).append("日");
                if (countDate.size() > 1) {
                    if (countMonth == 1) {
                        sf.append("、");
                    } else {
                        if (count != countDate.get(selectMonth).size() - 1) {
                            sf.append("、");
                        }
                    }
                } else {
                    if (count != countDate.get(selectMonth).size() - 1) {
                        sf.append("、");
                    }
                }
                count += 1;
            }
        }
        return sf.toString();
    }

    private String fillWeeks(List<String> weekLoop) {
        StringBuffer sb = new StringBuffer();
        sb.append("星期");
        int count = 0;
        for (String strWeek : weekLoop) {

            if (strWeek.trim().equals("0")) {
                sb.append("日");
            } else if (strWeek.trim().equals("1")) {
                sb.append("一");
            } else if (strWeek.trim().equals("2")) {
                sb.append("二");
            } else if (strWeek.trim().equals("3")) {
                sb.append("三");
            } else if (strWeek.trim().equals("4")) {
                sb.append("四");
            } else if (strWeek.trim().equals("5")) {
                sb.append("五");
            } else if (strWeek.trim().equals("6")) {
                sb.append("六");
            }
            count += 1;
            if (count != weekLoop.size()) {
                sb.append("、");
            }
        }
        return sb.toString();
    }

    private void showOrderType(OrderEntity order) {
        int strId = Util.getResourceId(this,
                String.format("order_type_%s", order.getMajorDemand()),
                "string");
        int colorId = Util.getResourceId(this,
                String.format("background_order_%s", order.getMajorDemand()),
                "color");
        orderType.setText(getResources().getString(strId));
        orderType.setBackgroundResource(colorId);
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

    /**
     * 根据年 月 获取对应的月份 天数
     */
    public static int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
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
//        actual.setVisibility(View.GONE);
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
            String date = new SimpleDateFormat("MM月dd日")
                    .format(new Date(order.getClassBeginTimeActual()));
            String startTime = new SimpleDateFormat("HH:mm")
                    .format(new Date(order.getClassBeginTimeActual()));
            String endTime = new SimpleDateFormat("HH:mm")
                    .format(new Date(order.getClassBeginTimeActual()
                            + Integer.parseInt(order.getLesson()) * 60 * 60 * 1000));
            String formatTime = date + " " + startTime + "-" + endTime;
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
                                    .putExtra("orderInfoId", order.getId())
                    );
                }
            });
        } else if (Values.orderStatuses.get(order.getOrderStatus()).equals("已完成")) {
//            actual.setVisibility(View.VISIBLE);
            String date = new SimpleDateFormat("MM月dd日")
                    .format(new Date(order.getClassBeginTimeActual()));
            String startTime = new SimpleDateFormat("HH:mm")
                    .format(new Date(order.getClassBeginTimeActual()));
            String endTime = new SimpleDateFormat("HH:mm")
                    .format(new Date(order.getClassBeginTimeActual()
                            + Integer.parseInt(order.getLesson()) * 60 * 60 * 1000));
            String formatTime = date + " " + startTime + "-" + endTime;
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
        setResult(1);
        finish();
    }
}
