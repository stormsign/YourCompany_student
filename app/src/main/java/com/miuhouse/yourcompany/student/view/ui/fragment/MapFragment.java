package com.miuhouse.yourcompany.student.view.ui.fragment;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.db.AccountDBTask;
import com.miuhouse.yourcompany.student.db.DictDBTask;
import com.miuhouse.yourcompany.student.model.ParentInfo;
import com.miuhouse.yourcompany.student.model.TeacherInfoBean;
import com.miuhouse.yourcompany.student.presenter.MapPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.IMapPresenter;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.view.ui.activity.LongTermSelect;
import com.miuhouse.yourcompany.student.view.ui.activity.OrderConfirmActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.UserInformationActivity;
import com.miuhouse.yourcompany.student.view.ui.base.BaseFragment;
import com.miuhouse.yourcompany.student.view.ui.fragment.interf.IHomeFragment;
import com.miuhouse.yourcompany.student.view.widget.LovelyCustomDialog;
import com.miuhouse.yourcompany.student.view.widget.MarkView;
import com.miuhouse.yourcompany.student.view.widget.RangedTimePickerDialog;

import java.util.Calendar;
import java.util.List;

//import com.miuhouse.yourcompany.student.view.ui.fragment.interf.IOrdersFragment;
//import com.miuhouse.yourcompany.student.view.widget.CantScrollViewPager;

/**
 * Created by khb on 2016/7/13.
 */
//implements IOrdersFragment, View.OnClickListener
public class MapFragment extends BaseFragment implements IHomeFragment, View.OnClickListener, ValueAnimator.AnimatorUpdateListener {
    private static final long ANIMATE_DURATION = 200;
    public static final int ORDER_TYPE_SHORT = 1;
    public static final int ORDER_TYPE_LONG = 2;

//    private CantScrollViewPager contentViewPager;

    private MapView map;
    private AMap aMap;

    private IMapPresenter mapPresenter;
    private Bundle bundle;
    private TextView tvPeiban;
    private TextView tvZuoye;
    private TextView tvYishu;
    private LinearLayout bottomType;
    private LinearLayout lloptions;
    private ViewWrapper options;
    private TextView findButton;
    private MyLayoutChangeListener myLayoutChangeListener;
    private ViewWrapper button;
    private LinearLayout showTime;
    private TextView cutClass;
    private TextView addClass;
    private TextView classTime;
    private TextView time;
    private TextView singlePrice;
    private EditText addPrice;
    private TextView address;

    private int orderTypeIndex;
    private TextView teacherCount;
    private LinearLayout longTermSelect;
    private int longTermSelectHeight;
    private ViewWrapper longTerm;
    private LinearLayout setTime;
    private ImageView optionBack;
    private Calendar selectedCalendar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = savedInstanceState;
    }

    @Override
    public int getFragmentResourceId() {
        return R.layout.fragment_map;
    }

    @Override
    public void getViews(View view) {
        map = (MapView) view.findViewById(R.id.map);
        map.onCreate(bundle);
        aMap = this.map.getMap();
        tvPeiban = (TextView) view.findViewById(R.id.peiban);
        tvZuoye = (TextView) view.findViewById(R.id.zuoyefudao);
        tvYishu = (TextView) view.findViewById(R.id.yishupeixun);
        bottomType = (LinearLayout) view.findViewById(R.id.bottomType);
        bottomType.setClickable(true);
        lloptions = (LinearLayout) view.findViewById(R.id.options);
        findButton = (TextView) view.findViewById(R.id.find);
        options = new ViewWrapper(lloptions);
        button = new ViewWrapper(findButton);
        showTime = (LinearLayout) view.findViewById(R.id.showTime);
        setTime = (LinearLayout) view.findViewById(R.id.setTime);
        TextView setTimeButton = (TextView) view.findViewById(R.id.setTimeButton);
        setTimeButton.setOnClickListener(this);
        time = (TextView) view.findViewById(R.id.time);
        view.findViewById(R.id.cutClass).setOnClickListener(this);
        view.findViewById(R.id.addClass).setOnClickListener(this);
        classTime = (TextView) view.findViewById(R.id.classTime);
        singlePrice = (TextView) view.findViewById(R.id.singlePrice);
        addPrice = (EditText) view.findViewById(R.id.addPrice);
        address = (TextView) view.findViewById(R.id.pinedLocation);
        teacherCount = (TextView) view.findViewById(R.id.teacherCount);
        longTermSelect = (LinearLayout) view.findViewById(R.id.longTermSelect);
        longTerm = new ViewWrapper(longTermSelect);
        optionBack = (ImageView) view.findViewById(R.id.optionBack);
        optionBack.setVisibility(View.GONE);
        optionBack.setOnClickListener(this);

        addPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = addPrice.getText().toString().trim();
                if (text.indexOf("0") == 0) {
                    text = text.substring(1, text.length());
                    addPrice.setText(text);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void setupView() {
        mapPresenter = new MapPresenter(this);
        tvPeiban.setOnClickListener(this);
        tvZuoye.setOnClickListener(this);
        tvYishu.setOnClickListener(this);
        findButton.setOnClickListener(this);
        showTime.setOnClickListener(this);
//        cutClass.setOnClickListener(this);
//        addClass.setOnClickListener(this);
        longTermSelect.setOnClickListener(this);
        changeType(0);
        lloptions.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        myLayoutChangeListener = new MyLayoutChangeListener();
        findButton.addOnLayoutChangeListener(myLayoutChangeListener);
        addPrice.setCursorVisible(false);
        addPrice.setOnClickListener(this);
//        addPrice.setFilters(new InputFilter[]{new MoneyInputFilter()});

        longTermSelect.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        longTermSelectHeight = longTermSelect.getMeasuredHeight();

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            L.i("ACCESS_FINE_LOCATION DENIED");
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            L.i("ACCESS_COARSE_LOCATION DENIED");
        }

        if ((ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
                ||
                (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
//                Toast.makeText(this, "大哥，跪求通过权限", Toast.LENGTH_LONG).show();
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            } else {
//                Toast.makeText(context, "应用的读取位置权限已被禁止，要想重新开启，请在手机的权限管理中找到" + getResources().getString(R.string.app_name) + "应用，找到获取位置权限并选择允许", Toast.LENGTH_LONG).show();
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        } else {
            mapPresenter.getCurrentLocation();
        }

        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setTrafficEnabled(false);
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                searchAddress();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                mapPresenter.getCurrentLocation();
            } else {
                Toast.makeText(getActivity(), "读取位置权限被拒绝", Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void showPosition(AMapLocation location) {
        if (location != null) {
            if (location.getErrorCode() == 0) {
                LatLng latLng = new LatLng(location.getLatitude(),
                        location.getLongitude());
                CameraPosition currentCameraPosition = aMap.getCameraPosition();
                float zoom = currentCameraPosition.zoom;
                zoom = 20;
                CameraPosition cameraPosition = new CameraPosition(latLng, zoom, 0, 0);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                aMap.animateCamera(cameraUpdate, 500, new AMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
//                        searchAddress();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        }
    }

    private String province;
    private String city;
    private String district;
    private String street;
    private double lat;
    private double lon;

    private void searchAddress() {
        aMap.clear();
        LatLng currPosition = aMap.getCameraPosition().target;
        GeocodeSearch geocoderSearch = new GeocodeSearch(context);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
//                L.i("ADDRESS:" + "0 " + regeocodeAddress.getFormatAddress() + "\n1 "
//                        + regeocodeAddress.getCity()
//                        + "\n2 " + (regeocodeAddress.getRoads().size() > 0 ? regeocodeAddress.getRoads().get(0).getName() : "null")
//                        + "\n3 " + (regeocodeAddress.getCrossroads().size() > 0 ? regeocodeAddress.getCrossroads().get(0).getName() : "null")
//                        + "\n4 " + (regeocodeAddress.getAois().size() > 0 ? regeocodeAddress.getAois().get(0).getAoiName() : "null")
//                        + "\n5 " + regeocodeAddress.getPois().get(0).getTitle() + "\n6 " + regeocodeAddress.getPois().get(0).getSnippet()
//                        + "\n7 " + regeocodeAddress.getDistrict()
//                        + "\n8 " + regeocodeAddress.getNeighborhood()
//                        + "\n9 " + regeocodeAddress.getBuilding()
//                        + "\n10 " + regeocodeAddress.getTownship()
//                        + "\n11 " + regeocodeAddress.getStreetNumber().getStreet());
                province = regeocodeAddress.getProvince();
                city = regeocodeAddress.getCity();
                district = regeocodeAddress.getDistrict();
                String strAddress = "";
//                if (!TextUtils.isEmpty(regeocodeAddress.getDistrict())){
//                    strAddress+=regeocodeAddress.getDistrict();
//                }
                if (!TextUtils.isEmpty(regeocodeAddress.getTownship())) {
                    strAddress += regeocodeAddress.getTownship();
                }
                if (regeocodeAddress.getRoads().size() > 0) {
                    strAddress += regeocodeAddress.getRoads().get(0).getName();
                    street = regeocodeAddress.getRoads().get(0).getName();
                }
                if (!TextUtils.isEmpty(regeocodeAddress.getNeighborhood())
                        || !TextUtils.isEmpty(regeocodeAddress.getBuilding())) {
                    if (!TextUtils.isEmpty(regeocodeAddress.getNeighborhood())) {
                        strAddress += regeocodeAddress.getNeighborhood();
                    }
                    if (!TextUtils.isEmpty(regeocodeAddress.getBuilding())) {
                        strAddress += regeocodeAddress.getBuilding();
                    }
                } else if (regeocodeAddress.getAois().size() > 0) {
                    strAddress += regeocodeAddress.getAois().get(0).getAoiName();
                }
                if (TextUtils.isEmpty(strAddress)) {
                    strAddress = "......";
                }
                address.setText(strAddress);
                LatLonPoint point = regeocodeResult.getRegeocodeQuery().getPoint();
                lat = point.getLatitude();
                lon = point.getLongitude();
                mapPresenter.getTeachers(AccountDBTask.getAccount().getId(),
                        new LatLng(point.getLatitude(), point.getLongitude()),
                        orderTypeIndex + 1 + "");
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
//      latLonPoint参数表示一个Latlng，第二参数表示范围多少米，GeocodeSearch.AMAP表示是国测局坐标系还是GPS原生坐标系
        LatLonPoint latLonPoint = new LatLonPoint(currPosition.latitude, currPosition.longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }

    @Override
    public View getOverrideParentView() {
        return null;
    }

    @Override
    public void showTeachers(List<TeacherInfoBean> list) {
        if (null != list && list.size() > 0) {
            L.i(list.size() + "==========");
            teacherCount.setText(list.size() + "");
            for (TeacherInfoBean teacherInfoBean : list) {
                MarkView markView = new MarkView(context);
                markView.setIcon(teacherInfoBean.getHeadUrl());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(teacherInfoBean.getLat(), teacherInfoBean.getLon()))
                        .icon(BitmapDescriptorFactory.fromView(markView));
                aMap.addMarker(markerOptions);
            }
        } else {
            teacherCount.setText("0");
        }
    }

    private int selectedYear;
    private int selectedMonth;
    private int selectedDay;
    private int selectedHour;
    private int selectedMinute;
    private int classCount = 1;

    @Override
    public void showSchedule() {
//        时间选择定在当前时间的一个小时后
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() + 60 * 60 * 1000);
        final DatePickerDialog dateDialog = new DatePickerDialog(context, null, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dateDialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatePicker datePicker = dateDialog.getDatePicker();
                selectedYear = datePicker.getYear();
                selectedMonth = datePicker.getMonth() + 1;
                selectedDay = datePicker.getDayOfMonth();
                showTimePicker();
            }
        });
        dateDialog.setCanceledOnTouchOutside(true);
        DatePicker datePicker = dateDialog.getDatePicker();
//        datePicker.setMinDate(System.currentTimeMillis()- 60 * 60 * 1000);
        dateDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() + 60 * 60 * 1000);
        RangedTimePickerDialog timeDialog = new RangedTimePickerDialog(context,

                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        selectedHour = hourOfDay;
                        selectedMinute = minute;
                        showMore();
                        setTime();
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
        timeDialog.setCanceledOnTouchOutside(true);
//        TimePicker timePicker = timeDialog.getTimePicker();
//        timeDialog.setMinTime(System.currentTimeMillis() + 60 * 60 * 1000);
        timeDialog.show();
    }

    private void setTime() {
        selectedCalendar = Calendar.getInstance();
        int month = selectedMonth - 1;
        selectedCalendar.set(selectedYear, month, selectedDay, selectedHour, selectedMinute);
        if (selectedCalendar.getTimeInMillis() < System.currentTimeMillis() + (60 * 60 * 1000)) {   //所选时间小于当前时间+1小时
            Toast.makeText(getActivity(), "请至少选择一个小时后的时间", Toast.LENGTH_LONG).show();
            hideMore();
            return;
        }

        if (selectedMinute >= 10) {
            time.setText(selectedMonth + "月" + selectedDay + "日" + " " + selectedHour + ":" + selectedMinute);
        } else {
            time.setText(selectedMonth + "月" + selectedDay + "日" + " " + selectedHour + ":" + "0" + selectedMinute);
        }
        showTime.setVisibility(View.VISIBLE);
        setTime.setVisibility(View.GONE);
    }

    private void resetTime() {
        time.setText("选择时间");
        selectedMonth = 0;
        selectedDay = 0;
        selectedHour = 0;
        selectedMinute = 0;
        selectedYear = 0;
        addPrice.setText(0 + "");
        classTime.setText("1");
        classCount = 1;
        setTime.setVisibility(View.VISIBLE);
        showTime.setVisibility(View.GONE);
    }

    @Override
    public void showMore() {
        if (!options.isExpanded) {
            options.setIsExpanded(true);
            expandOption();
            showTeacherButton();
            hideLongTermSelect();
            showBackButton();
        } else {
            return;
        }
    }

    @Override
    public void hideMore() {
        if (options.isExpanded) {
            options.setIsExpanded(false);
            shrinkOption();
            hideTeacherButton();
            showLongTermSelect();
            hideBackButton();
            resetTime();
        } else {
            return;
        }
    }

    @Override
    public void changeType(int index) {
        orderTypeIndex = index;
        singlePrice.setText("￥" + DictDBTask.getOrderTypePrice(index));
        switch (index) {
            case 0:
                setTextSelect(tvPeiban, true);
                setTextSelect(tvYishu, false);
                setTextSelect(tvZuoye, false);
                break;
            case 1:
                setTextSelect(tvPeiban, false);
                setTextSelect(tvYishu, false);
                setTextSelect(tvZuoye, true);
                break;
            case 2:
                setTextSelect(tvPeiban, false);
                setTextSelect(tvYishu, true);
                setTextSelect(tvZuoye, false);
                break;
        }
    }

    private void setTextSelect(TextView tv, boolean isSelected) {
        if (isSelected) {
            tv.setTextColor(getResources().getColor(R.color.textOrange));
            tv.setSelected(true);
        } else {
            tv.setTextColor(getResources().getColor(R.color.textDarkthree));
            tv.setSelected(false);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.peiban:
                changeType(0);
                aMap.clear();
                mapPresenter.getTeachers(AccountDBTask.getAccount().getId(),
                        new LatLng(lat, lon),
                        orderTypeIndex + 1 + "");
                break;
            case R.id.zuoyefudao:
                changeType(1);
                aMap.clear();
                mapPresenter.getTeachers(AccountDBTask.getAccount().getId(),
                        new LatLng(lat, lon),
                        orderTypeIndex + 1 + "");
                break;
            case R.id.yishupeixun:
                changeType(2);
                aMap.clear();
                mapPresenter.getTeachers(AccountDBTask.getAccount().getId(),
                        new LatLng(lat, lon),
                        orderTypeIndex + 1 + "");
                break;
            case R.id.find:
                mapPresenter.getStudentInfo(AccountDBTask.getAccount().getId(), ORDER_TYPE_SHORT);
                break;
            case R.id.longTermSelect:
                mapPresenter.getStudentInfo(AccountDBTask.getAccount().getId(), ORDER_TYPE_LONG);

                break;
            case R.id.showTime:
//                showSchedule();
                break;
            case R.id.setTimeButton:
                showSchedule();
                break;
            case R.id.cutClass:
                if (classCount > 1) {
                    classCount -= 1;
                }
                classTime.setText(classCount + "");
                break;
            case R.id.addClass:
                if (classCount < 24) {
                    classCount += 1;
                }
                classTime.setText(classCount + "");
                break;
            case R.id.addPrice:
                if (!TextUtils.isEmpty(addPrice.getText().toString())) {
                    addPrice.setSelection(addPrice.getText().length());
                }
                break;
            case R.id.optionBack:
                hideMore();
                break;
        }
    }

    private class MyLayoutChangeListener implements View.OnLayoutChangeListener {
        public int bottom;

        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            this.bottom = bottom;
        }
    }

    private void expandOption() {
        ValueAnimator animator = ObjectAnimator.ofFloat(options, "height",
                getResources().getDimensionPixelSize(R.dimen.height_home_options_top),
                getResources().getDimensionPixelSize(R.dimen.height_home_options_bottom)
                        + getResources().getDimensionPixelSize(R.dimen.height_home_options_top));
        animator.setDuration(ANIMATE_DURATION);
        animator.addUpdateListener(this);
        animator.start();
    }

    private void shrinkOption() {
        ValueAnimator animator = ObjectAnimator.ofFloat(options, "height",
                getResources().getDimensionPixelSize(R.dimen.height_home_options_bottom)
                        + getResources().getDimensionPixelSize(R.dimen.height_home_options_top),
                getResources().getDimensionPixelSize(R.dimen.height_home_options_top));
        animator.setDuration(ANIMATE_DURATION);
        animator.addUpdateListener(this);
        animator.start();
    }

    private void showTeacherButton() {
        ValueAnimator animator = ObjectAnimator.ofFloat(button, "bottom",
                0,
                getResources().getDimensionPixelSize(R.dimen.homeBottomHeight)
                        + Util.dip2px(context, 10));
        animator.setDuration(ANIMATE_DURATION);
        animator.addUpdateListener(this);
        animator.start();
    }

    private void hideTeacherButton() {
        ValueAnimator animator = ObjectAnimator.ofFloat(button, "bottom",
                getResources().getDimensionPixelSize(R.dimen.homeBottomHeight)
                        + Util.dip2px(context, 10),
                0);
        animator.setDuration(ANIMATE_DURATION);
        animator.addUpdateListener(this);
        animator.start();
    }

    private void hideLongTermSelect() {
        ValueAnimator animator = ObjectAnimator.ofFloat(longTerm, "top",
                Util.dip2px(context, -1), -longTermSelectHeight);
        animator.setDuration(ANIMATE_DURATION);
        animator.addUpdateListener(this);
        animator.start();
    }

    private void showLongTermSelect() {
        ValueAnimator animator = ObjectAnimator.ofFloat(longTerm, "top",
                -longTermSelectHeight, Util.dip2px(context, -1));
        animator.setDuration(ANIMATE_DURATION);
        animator.addUpdateListener(this);
        animator.start();
    }

    private void showBackButton() {
        optionBack.setVisibility(View.VISIBLE);
        ScaleAnimation sa = new ScaleAnimation(0, 1f, 0, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(ANIMATE_DURATION);
        sa.setFillAfter(true);
        optionBack.startAnimation(sa);
    }

    private void hideBackButton() {
        ScaleAnimation sa = new ScaleAnimation(1f, 0, 1f, 0,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(ANIMATE_DURATION);
        sa.setFillAfter(true);
        optionBack.startAnimation(sa);
    }

    @Override
    public void showProfileNotComplete(final ParentInfo parentInfo) {
//        Toast.makeText(context, "资料未完善", Toast.LENGTH_LONG).show();
        final LovelyCustomDialog dialog = new LovelyCustomDialog(context, R.style.TintTheme);
        dialog.setView(R.layout.dialog_profile_imcomplete).goneView();
        View view = dialog.getAddedView();
        TextView exitBtn = (TextView) view.findViewById(R.id.exit);
        TextView completeBtn = (TextView) view.findViewById(R.id.complete);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                goToProfile(parentInfo);
            }
        });
        dialog.show();
    }

    @Override
    public void goToOrderConfirm(ParentInfo parentInfo) {
        startActivity(new Intent(context, OrderConfirmActivity.class)
                .putExtra("mapAddress", address.getText().toString().trim())
                .putExtra("orderType", orderTypeIndex)
                .putExtra("extraPrice", addPrice.getText().toString())
                .putExtra("classTime", classTime.getText().toString())
                .putExtra("beginTime", selectedCalendar.getTimeInMillis())
                .putExtra("province", province)
                .putExtra("city", city)
                .putExtra("district", district)
                .putExtra("street", street)
                .putExtra("lat", lat)
                .putExtra("lon", lon)
                .putExtra("parentInfo", parentInfo));
        hideMore();
    }

    @Override
    public void goToLongOrderConfirm(ParentInfo parentInfo) {

        getActivity().startActivity(new Intent(getActivity(), LongTermSelect.class)
                        .putExtra("orderType", orderTypeIndex)
                        .putExtra("province", province)
                        .putExtra("city", city)
                        .putExtra("district", district)
                        .putExtra("street", street)
                        .putExtra("lat", lat)
                        .putExtra("lon", lon)
        );
    }

    @Override
    public void goToProfile(ParentInfo parentInfo) {
        startActivity(new Intent(context, UserInformationActivity.class)
                .putExtra("parentInfo", parentInfo));
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        lloptions.invalidate();
        findButton.invalidate();
        longTermSelect.invalidate();
    }

    public boolean isOptionExpanded() {
        return options.isExpanded();
    }

    class ViewWrapper {
        private View view;
        private float height;
        private float bottom;
        private float top;
        private boolean isExpanded;

        public ViewWrapper(View view) {
            this.view = view;
        }

        public float getHeight() {
            return height;
        }

        public void setHeight(float height) {
            view.getLayoutParams().height = (int) height;
            view.requestLayout();
            this.height = height;
        }

        public float getBottom() {
            return bottom;
        }

        public void setBottom(float bottom) {
            this.bottom = bottom;
            ((RelativeLayout.LayoutParams) view.getLayoutParams())
                    .setMargins(Util.dip2px(context, 10), 0, Util.dip2px(context, 10), (int) bottom);
        }

        public float getTop() {
            return top;
        }

        public void setTop(float top) {
            this.top = top;
            ((RelativeLayout.LayoutParams) view.getLayoutParams())
                    .setMargins(Util.dip2px(context, 10), (int) top, Util.dip2px(context, 10), Util.dip2px(context, 10));
        }

        public boolean isExpanded() {
            return isExpanded;
        }

        public void setIsExpanded(boolean isExpanded) {
            this.isExpanded = isExpanded;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }
}
