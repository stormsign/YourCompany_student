package com.miuhouse.yourcompany.student.view.ui.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
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
import com.miuhouse.yourcompany.student.presenter.OrderConfirmPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.IOrderConfirmPresenter;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.utils.Values;
import com.miuhouse.yourcompany.student.utils.ViewUtils;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IOrderConfirmActivity;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;
import com.miuhouse.yourcompany.student.view.widget.LovelyChoiceDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by khb on 2016/8/29.
 */
public class OrderConfirmActivity extends BaseActivity
    implements IOrderConfirmActivity, View.OnClickListener {

  private IOrderConfirmPresenter presenter;
  private LinearLayout priceDetail;
  private int priceDetailHeight;
  private View bg;
  private RelativeLayout.LayoutParams params;
  private EditText addressDetail;
  private EditText demands;
  private RelativeLayout bottom;
  private boolean isShowPriceDetail;
  private ViewWrapper priceDetailWrapper;
  private TextView confirm;
  private EditText edExtraPrice;
  private TextView cutClass;
  private TextView addClass;
  private TextView classTime;
  private int classCount = 1;
  private double totalPrice;
  private TextView tvTotalPrice;
  private double single;
  private TextView address;
  private TextView tvStudentName;
  private TextView tvParentName;
  private TextView tvTel;
  private TextView tvSubject;
  private RelativeLayout rlTeacherGender;
  private TextView tvStudentGrade;
  private String mapAddress;
  private long beginTime;
  private int orderTypeIndex;
  private TextView tvTeacherGender;
  private RelativeLayout subjectContainer;
  private RelativeLayout interestContainer;
  private TextView tvInterest;
  private RelativeLayout rlTel;
  private RelativeLayout rlGrade;
  private TextView tvTotalClassTime;
  private RelativeLayout content;
  private ParentInfo parentInfo;
  private double lat;
  private double lon;
  private ScrollView scrollView;
  private String province;
  private String city;
  private String district;
  private String street;
  private double extraPrice;
  private boolean haveSubjectOrInterest = true;

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
    mapAddress = getIntent().getStringExtra("mapAddress");
    orderTypeIndex = getIntent().getIntExtra("orderType", 0);
    String strExtraPrice = getIntent().getStringExtra("extraPrice");
    String strClassTime = getIntent().getStringExtra("classTime");
    beginTime = getIntent().getLongExtra("beginTime", 0);
    parentInfo = (ParentInfo) getIntent().getSerializableExtra("parentInfo");
    province = getIntent().getStringExtra("province");
    city = getIntent().getStringExtra("city");
    district = getIntent().getStringExtra("district");
    street = getIntent().getStringExtra("street");
    lat = getIntent().getDoubleExtra("lat", 0);
    lon = getIntent().getDoubleExtra("lon", 0);

    presenter = new OrderConfirmPresenter(this);
    scrollView = (ScrollView) findViewById(R.id.scrollview);
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
    rlTel = (RelativeLayout) findViewById(R.id.list_tel);
    rlTel.setOnClickListener(this);
    rlGrade = (RelativeLayout) findViewById(R.id.list_grade);
    rlGrade.setOnClickListener(this);
    tvStudentGrade = (TextView) findViewById(R.id.studentGrade);
    subjectContainer = (RelativeLayout) findViewById(R.id.list_subject);
    subjectContainer.setOnClickListener(this);
    tvSubject = (TextView) findViewById(R.id.subject);
    interestContainer = (RelativeLayout) findViewById(R.id.list_interest);
    interestContainer.setOnClickListener(this);
    tvInterest = (TextView) findViewById(R.id.interest);
    rlTeacherGender = (RelativeLayout) findViewById(R.id.list_teacherGender);
    tvTeacherGender = (TextView) findViewById(R.id.teacherGender);
    tvTeacherGender.setText("不限");
    sex = "-1";
    rlTeacherGender.setOnClickListener(this);
    if (orderTypeIndex == 2) {
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
    confirm = (TextView) findViewById(R.id.confirm);
    confirm.setOnClickListener(this);
    TextView singlePrice = (TextView) findViewById(R.id.singlePrice);
    edExtraPrice = (EditText) findViewById(R.id.extraPrice);
    edExtraPrice.setCursorVisible(false);
    edExtraPrice.setOnClickListener(this);
    //        extraPrice.setFilters(new InputFilter[]{new MoneyInputFilter()});
    cutClass = (TextView) findViewById(R.id.cutClass);
    addClass = (TextView) findViewById(R.id.addClass);
    tvTotalPrice = (TextView) findViewById(R.id.totalPrice);
    cutClass.setOnClickListener(this);
    addClass.setOnClickListener(this);
    classTime = (TextView) findViewById(R.id.classTime);
    single = DictDBTask.getOrderTypePrice(orderTypeIndex);
    singlePrice.setText("￥" + single);
    if (Util.isNumeric(strClassTime)) {
      classCount = Integer.parseInt(strClassTime);
      classTime.setText(strClassTime);
      tvTotalClassTime.setText("共" + strClassTime + "个课时");
      totalPrice += classCount * single;
      //            double d = Double.parseDouble(strExtraPrice);
      if (TextUtils.isEmpty(strExtraPrice)) {
        strExtraPrice = "0.00";
      }
      edExtraPrice.setText(strExtraPrice);
      extraPrice = Double.parseDouble(strExtraPrice);
      totalPrice += (extraPrice * classCount);
    }
    getTotalPrice();
    edExtraPrice.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        String text = edExtraPrice.getText().toString().trim();
        if (text.indexOf("0") == 0) {
          text = text.substring(1, text.length());
          edExtraPrice.setText(text);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {
        getTotalPrice();
      }
    });
    showInfo(parentInfo);
    //        presenter.getOrderInfo(AccountDBTask.getAccount().getId());
    presenter.getDefaultAddress(AccountDBTask.getAccount().getId());
  }

  private void getTotalPrice() {
    totalPrice = 0;
    String strExtra = edExtraPrice.getText().toString().trim();
    //        if (Util.isNumeric(strExtra)) {
    if (TextUtils.isEmpty(strExtra)) {
      strExtra = "0.00";
    }
    extraPrice = Double.parseDouble(strExtra);
    totalPrice += (extraPrice * classCount);
    //        }
    totalPrice += classCount * single;
    DecimalFormat df = new DecimalFormat("#0.00");
    //        totalPrice = df.format(totalPrice);
    tvTotalPrice.setText(df.format(totalPrice) + "");
  }

  @Override
  protected int getContentLayoutId() {
    return R.layout.activity_orderconfirm;
  }

  @Override
  protected View getOverrideParentView() {
    return content;
  }

  @Override
  protected void onResume() {
    presenter.getDefaultAddress(AccountDBTask.getAccount().getId());
    super.onResume();
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
          Toast.makeText(OrderConfirmActivity.this, "请填写详细地址", Toast.LENGTH_SHORT).show();
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
            orderTypeIndex + 1 + "",
            tvSubject,
            tvInterest,
            tvStudentGrade,
            beginTime,
            classCount + "",
            tvTel,
            single,
            extraPrice,
            totalPrice,
            sex,
            province,
            city,
            district,
            street,
            addressDetail.getText().toString().trim(),
            lon,
            lat
        );
        break;
      case R.id.extraPrice:
        edExtraPrice.setCursorVisible(true);
        break;
      case R.id.cutClass:
        if (classCount > 1) {
          classCount -= 1;
        }
        classTime.setText(classCount + "");
        tvTotalClassTime.setText("共" + classCount + "个课时");
        getTotalPrice();
        break;
      case R.id.addClass:
        if (classCount < 24) {
          classCount += 1;
        }
        classTime.setText(classCount + "");
        tvTotalClassTime.setText("共" + classCount + "个课时");
        getTotalPrice();
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
        intentMobile.putExtra("value",
            TextUtils.isEmpty(tvTel.getText()) ? "" : tvTel.getText().toString());
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
    final List<String> list = DictDBTask.getDcNameList("interest_type");
    ViewUtils.showRadioBoxDialog(this, list, "兴趣", defaultInterest,
        new LovelyChoiceDialog.OnItemSelectedListener<String>() {
          @Override
          public void onItemSelected(int position, String item) {
            tvInterest.setText(list.get(position));
            defaultInterest = list.get(position);
          }
        });
  }

  private String defaultSubject = DictDBTask.getDcName("subject_type", 4);

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
      String subject =
          DictDBTask.getDcName("subject_type", Integer.parseInt(parentInfo.getSubject()));
      tvSubject.setText(subject);
      defaultSubject = subject;
    } else {
      tvSubject.setText("未设置");
      if (orderTypeIndex == 2) {
        haveSubjectOrInterest = false;
      }
    }

    if (!TextUtils.isEmpty(parentInfo.getGrade())
        && Util.isNumeric(parentInfo.getGrade())) {
      tvStudentGrade.setText(
          Values.getValue(Values.studentGrades, Integer.parseInt(parentInfo.getGrade())));
    }
    if (!TextUtils.isEmpty(parentInfo.getInterest())
        && Util.isNumeric(parentInfo.getInterest())) {
      String interest =
          DictDBTask.getDcName("interest_type", Integer.parseInt(parentInfo.getInterest()));
      tvInterest.setText(interest);
      defaultInterest = interest;
    } else {
      tvInterest.setText("未设置");
      if (orderTypeIndex < 2) {
        haveSubjectOrInterest = false;
      }
    }
    //        DictDBTask.
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
    } else {
      isShowPriceDetail = false;
      bg.setVisibility(View.GONE);
      animator
          = ObjectAnimator.ofInt(priceDetailWrapper, "bottom", 0, -priceDetailHeight);
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
      this.address.setText(mapAddress);
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
    } else {
      this.address.setText(mapAddress);
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
        .putExtra("beginTime", beginTime)
        .putExtra("orderType", orderTypeIndex + 1)
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
