package com.miuhouse.yourcompany.student.view.ui.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.db.AccountDBTask;
import com.miuhouse.yourcompany.student.db.DictDBTask;
import com.miuhouse.yourcompany.student.model.AddressBean;
import com.miuhouse.yourcompany.student.model.ParentInfo;
import com.miuhouse.yourcompany.student.presenter.LongOrderConfirmPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.ILongOrderConfirmPresenter;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.utils.Values;
import com.miuhouse.yourcompany.student.utils.ViewUtils;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IOrderConfirmActivity;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;
import com.miuhouse.yourcompany.student.view.widget.LovelyChoiceDialog;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 包月，包周
 * Created by khb on 2016/8/29.
 */
public class LongTermOrderConfirmActivity extends BaseActivity implements IOrderConfirmActivity, View.OnClickListener {

    private ILongOrderConfirmPresenter presenter;
    private LinearLayout priceDetail;
    private int priceDetailHeight;
    private RelativeLayout.LayoutParams params;
    private EditText addressDetail;

    /**
     * 老师需求
     */
    private EditText demands;
    private boolean isShowPriceDetail;
    private ViewWrapper priceDetailWrapper;
    private View bg;


    private RelativeLayout bottom;

    private double totalPrice;
    private TextView tvTotalPrice;
    private double single;
    private TextView address;
    private TextView tvStudentName;
    private TextView tvParentName;
    private TextView tvTel;

    private ImageView pointer;
    /**
     * 科目
     */
    private TextView tvSubject;

    private TextView tvStudentGrade;
    private TextView tvTeacherGender;

    /**
     * 兴趣
     */
    private TextView tvInterest;
    private TextView tvTotalClassTime;
    private RelativeLayout content;
    private double lat;
    private double lon;

    /**
     * 根据需求大目录，确定每个课时的价格
     */
    private double hourPrice;
    /**
     * 订单类型（包月，包年）；
     */
    private int orderTypeIndex;

    /**
     * 需求大类目 1陪伴,2作业辅导,3兴趣培养
     */
    private int majorDemand;

    /**
     * 开始时间
     */
    private long classBeginTimePromise;

    /**
     * 结束时间
     */
    private long classEndTimePromise;

    /**
     * 总的金额
     */
    private BigDecimal amount;

    /**
     * 总课时
     */
    private int lesson;

    /**
     * 每日课时
     */
    private int lessonDay;

    /**
     * 一周中的某几天集合<Integer>
     */
    private ArrayList<Integer> dayOfWeeks;

    private String province;
    private String city;
    private String district;
    private String street;
    private boolean haveSubjectOrInterest = true;
    private ScrollView scrollView;


    @Override
    protected String setTitle() {
        return "确认订单";
    }

    @Override
    protected String setRight() {
        return null;
    }

    @Override
    protected void initViewAndEvents() {

        majorDemand = getIntent().getIntExtra("majorDemand", 0);
        classBeginTimePromise = getIntent().getLongExtra("classBeginTimePromise", 0);
        amount = (BigDecimal) getIntent().getSerializableExtra("amount");
        orderTypeIndex = getIntent().getIntExtra("orderType", 0);
        lessonDay = getIntent().getIntExtra("lessonDay", 0);
        dayOfWeeks = getIntent().getIntegerArrayListExtra("weekLoop");
        classEndTimePromise = getIntent().getLongExtra("classEndTimePromise", 0);
        String strClassTime = getIntent().getStringExtra("classTime");
        String startClassTime = getIntent().getStringExtra("time");
        String weekStr = getIntent().getStringExtra("weekStr");
        province = getIntent().getStringExtra("province");
        city = getIntent().getStringExtra("city");
        district = getIntent().getStringExtra("district");
        street = getIntent().getStringExtra("street");
        lat = getIntent().getDoubleExtra("lat", 0);
        lon = getIntent().getDoubleExtra("lon", 0);
        lesson = getIntent().getIntExtra("lesson", 0);

        hourPrice = getIntent().getDoubleExtra("price", 0);

        presenter = new LongOrderConfirmPresenter(this);

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        content = (RelativeLayout) findViewById(R.id.content);
        priceDetail = (LinearLayout) findViewById(R.id.priceDetail);
        params = (RelativeLayout.LayoutParams) priceDetail.getLayoutParams();
        priceDetail.setOnClickListener(this);
        ViewTreeObserver viewTreeObserver = priceDetail.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                priceDetailHeight = priceDetail.getHeight();
                priceDetail.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                params.setMargins(0, 0, 0, -priceDetailHeight);
                priceDetail.setLayoutParams(params);
            }
        });
        bg = findViewById(R.id.transparentBg);
        bg.setOnClickListener(this);
        bg.setVisibility(View.GONE);

        tvStudentName = (TextView) findViewById(R.id.studentName);
        tvParentName = (TextView) findViewById(R.id.parentName);
        tvTel = (TextView) findViewById(R.id.tel);
        RelativeLayout rlTel = (RelativeLayout) findViewById(R.id.list_tel);
        rlTel.setOnClickListener(this);
        RelativeLayout rlGrade = (RelativeLayout) findViewById(R.id.list_grade);
        rlGrade.setOnClickListener(this);
        tvStudentGrade = (TextView) findViewById(R.id.studentGrade);
        RelativeLayout subjectContainer = (RelativeLayout) findViewById(R.id.list_subject);
        subjectContainer.setOnClickListener(this);
        tvSubject = (TextView) findViewById(R.id.subject);
        RelativeLayout interestContainer = (RelativeLayout) findViewById(R.id.list_interest);
        interestContainer.setOnClickListener(this);
        tvInterest = (TextView) findViewById(R.id.interest);

        pointer = (ImageView) findViewById(R.id.pointer);

        RelativeLayout rlTeacherGender = (RelativeLayout) findViewById(R.id.list_teacherGender);
        tvTeacherGender = (TextView) findViewById(R.id.teacherGender);
        tvTeacherGender.setText("不限");

        TextView tvDate = (TextView) findViewById(R.id.tv_date);
        TextView tvTime = (TextView) findViewById(R.id.tv_time);
        TextView tvWeek = (TextView) findViewById(R.id.tv_week);

//        tvDate =
        tvTime.setText(startClassTime);

        tvWeek.setText(weekStr);
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(classBeginTimePromise);
        Log.i("TAG", "Month=" + mCalendar.get(Calendar.MONTH));
        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
        sp.format(new Date(classBeginTimePromise));
        tvDate.setText(sp.format(new Date(classBeginTimePromise)));
        sex = "-1";
        rlTeacherGender.setOnClickListener(this);
        if (majorDemand == 2) {
            subjectContainer.setVisibility(View.GONE);
            interestContainer.setVisibility(View.VISIBLE);
        } else {
            subjectContainer.setVisibility(View.VISIBLE);
            interestContainer.setVisibility(View.GONE);
        }

        addressDetail = (EditText) findViewById(R.id.addressDetail);
        address = (TextView) findViewById(R.id.address);
        address.setOnClickListener(this);
        demands = (EditText) findViewById(R.id.demands);
        addressDetail.setCursorVisible(false);
        demands.setCursorVisible(false);
        addressDetail.setOnClickListener(this);
        demands.setOnClickListener(this);
        bottom = (RelativeLayout) findViewById(R.id.bottom);
        bottom.setOnClickListener(this);
        tvTotalClassTime = (TextView) findViewById(R.id.totalClassTime);
        priceDetailWrapper = new ViewWrapper(priceDetail);
        TextView confirm = (TextView) findViewById(R.id.confirm);
        confirm.setOnClickListener(this);

        tvTotalPrice = (TextView) findViewById(R.id.totalPrice);

        single = DictDBTask.getOrderTypePrice(majorDemand);

        fillTotalPrice();
        fillOrderTypes();

        presenter.getDefaultAddress(AccountDBTask.getAccount().getId());
        presenter.getInfo(AccountDBTask.getAccount().getId());
    }

    private void fillTotalPrice() {
        totalPrice = amount.doubleValue();
        DecimalFormat myformat = new DecimalFormat("0.00");
        String str = myformat.format(totalPrice);
        tvTotalPrice.setText(str);
    }

    private void fillOrderTypes() {
        tvTotalClassTime.setText(Util.getResourceId(this, String.format("order_term_%s", orderTypeIndex), "string"));

    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_lont_orderconfirm;
    }

    @Override
    protected View getOverrideParentView() {
        return content;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address:
                startActivity(new Intent(this, AddressActivity.class));
                break;
            case R.id.addressDetail:
                addressDetail.setCursorVisible(true);
                break;
            case R.id.demands:
                demands.setCursorVisible(true);
                break;
            case R.id.bottom:
                showPriceDetail();
                break;

            case R.id.confirm:
                if (TextUtils.isEmpty(addressDetail.getText())) {
                    Toast.makeText(LongTermOrderConfirmActivity.this, "请填写详细地址", Toast.LENGTH_SHORT).show();
                    scrollView.fullScroll(ScrollView.FOCUS_UP);
                    addressDetail.requestFocus();
                    return;
                }
                if (!haveSubjectOrInterest) {
                    notifyUserToCompleteSelection();
                    return;
                }
                presenter.confirmOrder(AccountDBTask.getAccount().getId(),
                        demands,
                        majorDemand + 1 + "",
                        tvSubject,
                        tvInterest,
                        tvStudentGrade,
                        classBeginTimePromise,
                        String.valueOf(lesson),
                        tvTel,
                        hourPrice,
                        amount.doubleValue(),
                        sex,
                        province,
                        city,
                        district,
                        street,
                        addressDetail.getText().toString().trim(),
                        lon,
                        lat,
                        String.valueOf(orderTypeIndex),
                        String.valueOf(lessonDay),
                        dayOfWeeks,
                        classEndTimePromise
                );
                break;
            case R.id.extraPrice:
                break;
            case R.id.list_teacherGender:
                chooseGender();
                break;
            case R.id.transparentBg:
                showPriceDetail();
                break;
            case R.id.list_tel:
                Intent intentMobile = new Intent(this, ChangeUserNameActivity.class);
                intentMobile.putExtra("title", "电话");
                intentMobile.putExtra("value", TextUtils.isEmpty(tvTel.getText()) ? "" : tvTel.getText().toString());
                intentMobile.putExtra("isShow", true);
                startActivityForResult(intentMobile, UserInformationActivity.USER_MOBILE);
                break;
            case R.id.list_grade:
                chooseGrade();
                break;
            case R.id.list_subject:
                chooseSubject();
                break;
            case R.id.list_interest:
                chooseInterest();
                break;
        }
    }

    private String defaultInterest = DictDBTask.getDcName("interest_type", 1);

    private void chooseInterest() {
        final List<String> list = DictDBTask.getDcNameList("subject_type");
        ViewUtils.showRadioBoxDialog(this, list, "兴趣", defaultInterest,
                new LovelyChoiceDialog.OnItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(int position, String item) {
                        tvInterest.setText(list.get(position));
                        defaultInterest = list.get(position);
                    }
                });
    }

    private String defaultSubject = DictDBTask.getDcName("subject_type", 1);

    private void chooseSubject() {
        final List<String> list = DictDBTask.getDcNameList("subject_type");
        ViewUtils.showRadioBoxDialog(this, list, "科目", defaultSubject,
                new LovelyChoiceDialog.OnItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(int position, String item) {
                        tvSubject.setText(list.get(position));
                        defaultSubject = list.get(position);
                    }
                });
    }

    private String defaultGrades = Values.getValue(Values.studentGrades, 0);

    private void chooseGrade() {
        final List<String> list = Values.getListValue(Values.studentGrades);
        ViewUtils.showRadioBoxDialog(this, list, "年级", defaultGrades,
                new LovelyChoiceDialog.OnItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(int position, String item) {
                        tvStudentGrade.setText(list.get(position));
                        defaultGrades = list.get(position);
                    }
                });
    }

    private String defaultGender = "不限";
    private String sex = "-1";

    private void chooseGender() {
        final List<String> arrays = new ArrayList<>();
        arrays.add("男");
        arrays.add("女");
        arrays.add("不限");
        ViewUtils.showRadioBoxDialog(this, arrays, "性别", defaultGender,
                new LovelyChoiceDialog.OnItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(int position, String item) {
                        tvTeacherGender.setText(arrays.get(position));
                        defaultGender = arrays.get(position);
                        if (defaultGender.equals("男")) {
                            sex = "1";
                        } else if (defaultGender.equals("女")) {
                            sex = "0";
                        } else { //不限
                            sex = "-1";
                        }
                    }
                });
    }

    @Override
    public void showInfo(ParentInfo parentInfo) {
        if (!TextUtils.isEmpty(parentInfo.getcName())) {
            tvStudentName.setText(parentInfo.getcName());
        }
        if (!TextUtils.isEmpty(parentInfo.getpName())) {
            tvParentName.setText(parentInfo.getpName());
        }
        if (!TextUtils.isEmpty(parentInfo.getMobile())) {
            tvTel.setText(parentInfo.getMobile());
        }
        if (!TextUtils.isEmpty(parentInfo.getSubject())
                && Util.isNumeric(parentInfo.getSubject())) {
            tvSubject.setText(DictDBTask.getDcName("subject_type", Integer.parseInt(parentInfo.getSubject())));
        } else {
            tvSubject.setText("未设置");
            if (orderTypeIndex == 2) {
                haveSubjectOrInterest = false;
            }
        }
        if (!TextUtils.isEmpty(parentInfo.getGrade())
                && Util.isNumeric(parentInfo.getGrade())) {
            tvStudentGrade.setText(Values.getValue(Values.studentGrades, Integer.parseInt(parentInfo.getGrade())));
        }
        if (!TextUtils.isEmpty(parentInfo.getInterest())
                && Util.isNumeric(parentInfo.getInterest())) {
            tvInterest.setText(DictDBTask.getDcName("interest_type", Integer.parseInt(parentInfo.getInterest())));
        } else {
            tvInterest.setText("未设置");
            if (orderTypeIndex < 2) {
                haveSubjectOrInterest = false;
            }
//        DictDBTask.
        }
    }

    @Override
    public void showPriceDetail() {
        ValueAnimator animator = null;
        priceDetail.setVisibility(View.VISIBLE);
        if (!isShowPriceDetail) {
            isShowPriceDetail = true;
            bg.setVisibility(View.VISIBLE);
            animator
                    = ObjectAnimator.ofInt(priceDetailWrapper, "bottom", -priceDetailHeight, 0);
            ObjectAnimator.ofFloat(pointer, "rotationX", 0.0F, 180F).setDuration(200).start();
        } else {
            isShowPriceDetail = false;
            bg.setVisibility(View.GONE);
            animator
                    = ObjectAnimator.ofInt(priceDetailWrapper, "bottom", 0, -priceDetailHeight);
            ObjectAnimator.ofFloat(pointer, "rotationX", 180F, 0.0F).setDuration(200).start();

        }
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                bottom.invalidate();
            }
        });
        animator.setDuration(200);
        animator.start();
    }

    @Override
    public void showAddress(AddressBean address) {
        if (address == null) {
            return;
        }
        if (!TextUtils.isEmpty(address.getCity() + address.getDistrict() + address.getStreet())
                && !TextUtils.isEmpty(address.getHouseNumber())) {
            this.address.setText(address.getCity() + address.getDistrict() + address.getStreet());
            this.addressDetail.setText(address.getHouseNumber());
            province = address.getProvince();
            city = address.getCity();
            district = address.getDistrict();
            street = address.getStreet();
            lat = address.getLat();
            lon = address.getLon();
        }
    }

    @Override
    public void notifyUserToCompleteSelection() {
        if (orderTypeIndex == 2) {
            Toast.makeText(this, "请先选择一个兴趣", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "请先选择一个科目", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void goToPay(String orderId) {
//        String tel = tel
        startActivity(new Intent(this, OrderPayActivity.class).putExtra("totalPrice", totalPrice)
                .putExtra("orderId", orderId)
                .putExtra("beginTime", classBeginTimePromise)
                .putExtra("orderType", orderTypeIndex)
                .putExtra("tel", tvTel.getText().toString()));
    }

    class ViewWrapper {
        private int bottom;
        private View view;

        public ViewWrapper(View view) {
            this.view = view;
        }

        public int getBottom() {
            return bottom;
        }

        public void setBottom(int bottom) {
            this.bottom = bottom;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
            params.setMargins(0, 0, 0, bottom);
            view.setLayoutParams(params);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UserInformationActivity.USER_MOBILE) {
            tvTel.setText(data.getStringExtra("value"));
        }
    }

    @Override
    public void showLoading(String msg) {
        super.showLoading(msg);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

    @Override
    public void showError(int type) {
        viewOverrideManager.showLoading(type, null);
    }
}
