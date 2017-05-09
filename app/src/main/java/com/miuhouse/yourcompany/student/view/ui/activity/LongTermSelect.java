package com.miuhouse.yourcompany.student.view.ui.activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.db.DictDBTask;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.view.ui.adapter.ChoiceAdapter;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;
import com.miuhouse.yourcompany.student.view.widget.LovelyChoiceDialog;
import com.miuhouse.yourcompany.student.view.widget.RangedTimePickerDialog;
import com.miuhouse.yourcompany.student.view.widget.date.SublimePicker;
import com.miuhouse.yourcompany.student.view.widget.date.datepicker.SimpleMonthView;
import com.miuhouse.yourcompany.student.view.widget.date.hepers.DateTimePatternHelper;
import com.miuhouse.yourcompany.student.view.widget.date.utilities.SUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by kings on 9/30/2016.
 */
public class LongTermSelect extends BaseActivity implements View.OnClickListener, SublimePicker.OnDateChangedListener {

    /**
     * 根据需求大目录，确定每个课时的价格
     */
    private double hourPrice;

    private TextView classTime;
    private TextView tvGotoClassData;
    private LinearLayout linearDate;
    /**
     * 每周上课时间
     */
    private LinearLayout linearWeek;
    private SublimePicker mSublimePicker;
    private TextView tvGoToClassDateData;
    private TextView tvHourPrice;
    private TextView tvOrderType;
    private TextView tvGoToClassDateWeek;
    private ImageView cutClass;
    private ImageView addClass;

    private Button payNow;

    /**
     * 每日课时和服务端lessonDay
     */
    private int classCount = 1;

    /**
     * 用hashMap保存选择的日期，key为月，value是日
     */
    private HashMap<Integer, List<Integer>> countDate = new HashMap<Integer, List<Integer>>();

    /**
     * 一周中的某几天集合<String>
     */
    private ArrayList<String> weeks = new ArrayList<>();

    /**
     * 一周中的某几天集合<Integer>
     */
    private ArrayList<Integer> dayOfWeeks = new ArrayList<>();

    private ArrayList<Integer> endDay = new ArrayList<>();

    private String[] orderTypes = {"包周", "包月"};

    /**
     * 上课天数
     */
    private int count = 0;

    /**
     * 获取订单类型，0周单，1月单,对应2周单，3月单\*
     */
    private int index;

    /**
     * 需求大类目 1陪伴,2作业辅导,3兴趣培养
     */
    private int majorDemand;

    private int selectedHour = -1;
    private int selectedMinute = -1;

    /**
     * 上课开始的年份
     */
    private int selectedYear;

    /**
     * 课程结束的年份
     */
    private int endSelectedYear;

    /**
     * 课程开始月份
     */
    private int month;

    /**
     * 课程结束月份
     */
    private int endMonth;

    /**
     * 课程开始日期
     */
    private int selectday = 0;

    /**
     * 课程结束日期
     */
    private int endSelectday = 0;

    //总价
    private BigDecimal amount;
    private StringBuilder weekSf;

    /**
     * 一个月最多四周二十八天，30或者31减28剩下两到三天，判断这两三天是不是上课时间，是的话
     * calssDay加一
     */
    private int classDay;
    private String province;
    private String city;
    private String district;
    private String street;
    private double lat;
    private double lon;
    private SimpleMonthView mSimpleMonthView;

    private static final String TAG = "LongTermSelect";

    @Override
    protected String setTitle() {
        return getResources().getString(R.string.reservation_teacher);
    }

    @Override
    protected String setRight() {
        return "";
    }

    @Override
    public void onBackClick() {
        super.onBackClick();
    }

    /**
     * 重置
     */
    public void reset() {
        linearWeek.setVisibility(View.GONE);
        mSublimePicker.reset();
        count = 0;
        countDate.clear();
        weeks.clear();
        dayOfWeeks.clear();
        endDay.clear();
        classCount = 1;
        classTime.setText(String.valueOf(classCount));
        fillTvDate();
        selectday = 0;
        selectedYear = 0;
        month = 0;
        tvGoToClassDateData.setText("");
        payNow.setEnabled(false);
    }

    @Override
    protected void initViewAndEvents() {
        majorDemand = getIntent().getIntExtra("orderType", 0);
        province = getIntent().getStringExtra("province");
        city = getIntent().getStringExtra("city");
        district = getIntent().getStringExtra("district");
        street = getIntent().getStringExtra("street");
        lat = getIntent().getDoubleExtra("lat", 0);
        lon = getIntent().getDoubleExtra("lon", 0);
        classTime = (TextView) findViewById(R.id.classTime);
        tvGotoClassData = (TextView) findViewById(R.id.tv_go_to_class_time_data);
        linearDate = (LinearLayout) findViewById(R.id.linear_date);
        linearWeek = (LinearLayout) findViewById(R.id.linear_week);
        mSublimePicker = (SublimePicker) findViewById(R.id.sublime_picker);
        tvGoToClassDateData = (TextView) findViewById(R.id.tv_go_to_class_date_data);
        tvHourPrice = (TextView) findViewById(R.id.tv_hour_price);
        tvOrderType = (TextView) findViewById(R.id.tv_order_type);
        tvGoToClassDateWeek = (TextView) findViewById(R.id.tv_go_to_class_date_week);
        payNow = (Button) findViewById(R.id.payNow);
        payNow.setOnClickListener(this);
        findViewById(R.id.linear_order_type).setOnClickListener(this);
        findViewById(R.id.linear_go_to_class_time).setOnClickListener(this);
        hourPrice = DictDBTask.getOrderTypePrice(majorDemand);
        tvHourPrice.setText(hourPrice + "");
        mSublimePicker.setOnDatePickerEnabled(this);
        addClass = (ImageView) findViewById(R.id.addClass);
        addClass.setOnClickListener(this);
        cutClass = (ImageView) findViewById(R.id.cutClass);
        cutClass.setOnClickListener(this);
        mSublimePicker.setmOnResetClickListener(new SublimePicker.OnResetClickListener() {
            @Override
            public void onResetClick() {
                //重置
                reset();
            }
        });
    }


    @Override
    protected void onRestart() {
        Log.i("TAG", "onRestart");
        if (mSimpleMonthView != null) {
            mSimpleMonthView.setRangeSelect(true);
        }
        super.onRestart();
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_reservation;
    }

    @Override
    protected View getOverrideParentView() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cutClass:
                if (classCount > 1) {
                    classCount -= 1;
                    classTime.setText(String.valueOf(classCount));
                    setTime(selectedHour, selectedMinute);
                    fillAmountPrice(count);
                    addClass.setImageResource(R.mipmap.ico_jia);
                    if (classCount == 1) {
                        cutClass.setImageResource(R.mipmap.home_but_jiann);
                    } else {
                        cutClass.setImageResource(R.mipmap.ico_jian);
                    }
                } else {
                    cutClass.setImageResource(R.mipmap.home_but_jiann);
                }
                break;
            case R.id.addClass:
                if (classCount < 24) {
                    classCount += 1;
                    classTime.setText(String.valueOf(classCount));
                    setTime(selectedHour, selectedMinute);
                    fillAmountPrice(count);
                    cutClass.setImageResource(R.mipmap.ico_jian);
                    if (classCount == 24) {
                        addClass.setImageResource(R.mipmap.home_but_jia);
                    } else {
                        addClass.setImageResource(R.mipmap.ico_jia);
                    }
                } else {
                    addClass.setImageResource(R.mipmap.home_but_jia);

                }

                break;
            case R.id.payNow:

                if (prepareForPay()) {
                    return;
                }
                Calendar startCalendar = Calendar.getInstance();
                Calendar endCalendar = Calendar.getInstance();
                startCalendar.set(selectedYear, month - 1, selectday, selectedHour, selectedMinute);

                if (index == 0) {
                    endCalendar.set(endSelectedYear, endMonth - 1, getEndday(), selectedHour + classCount, selectedMinute);
                } else {
                    if (endMonth == month) {
                        endCalendar.set(endSelectedYear, endMonth, getEndday(), selectedHour + classCount, selectedMinute);
                    } else {
                        endCalendar.set(endSelectedYear, endMonth - 1, getEndday(), selectedHour + classCount, selectedMinute);
                    }
                }
                startActivity(new Intent(this, LongTermOrderConfirmActivity.class)
                        .putExtra("majorDemand", majorDemand)
                        .putExtra("classBeginTimePromise", startCalendar.getTimeInMillis())
                        .putExtra("amount", amount)
                        .putExtra("orderType", getOrderType())
                        .putExtra("lessonDay", classCount)
                        .putIntegerArrayListExtra("weekLoop", dayOfWeeks)
                        .putExtra("classEndTimePromise", endCalendar.getTimeInMillis())
                        .putExtra("weekStr", weekSf.toString())
                        .putExtra("time", tvGotoClassData.getText().toString())
                        .putExtra("province", province)
                        .putExtra("city", city)
                        .putExtra("district", district)
                        .putExtra("street", street)
                        .putExtra("lat", lat)
                        .putExtra("price", hourPrice)
                        .putExtra("lon", lon)
                        .putExtra("lesson", lesson()));
                break;
            case R.id.linear_order_type:
                showSingleChoiceDialog(orderTypes[index], Arrays.asList(orderTypes), "长期套餐");
                break;
            case R.id.linear_go_to_class_time:
                showTimePicker();
                break;
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);
    }

    /**
     * 总课时
     *
     * @return
     */
    public int lesson() {
        if (index == 1) {
            return (classDay + (dayOfWeeks.size() * 4)) * classCount;
        } else {
            return dayOfWeeks.size() * classCount;
        }
    }

    private boolean prepareForPay() {
        if (!Util.hasInternet()) {
            Toast.makeText(this, getResources().getString(R.string.tip_no_internet), Toast.LENGTH_SHORT).show();
            return true;
        }
        if (selectedHour == -1 && selectedMinute == -1) {
            Toast.makeText(this, R.string.selection_class_time, Toast.LENGTH_SHORT).show();
            return true;
        }
        if (count == 0) {
            Toast.makeText(this, R.string.selection_class_date, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    /**
     * 获取订单类型，0周单，1月单,对应2周单，3月单
     *
     * @return
     */
    private int getOrderType() {
        int orderType = 0;

        switch (index) {
            case 0:
                orderType = 2;
                break;
            case 1:
                orderType = 3;
                break;
        }
        return orderType;
    }

    private void showSingleChoiceDialog(String selectedStr, List<String> arrays, String title) {
        ArrayAdapter<String> adapter = new ChoiceAdapter(this, arrays, selectedStr);
        final LovelyChoiceDialog lovelyChoiceDialog = new LovelyChoiceDialog(this);
        lovelyChoiceDialog.setTopColorRes(R.color.backgroundcolor_common).goneIconView().setTitle(title).setItems(adapter, new LovelyChoiceDialog.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(int position, String item) {
                tvOrderType.setText(orderTypes[position]);
                if (index != position) {
                    reset();
                    index = position;
                }
            }
        }).show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() + 60 * 60 * 1000);
        RangedTimePickerDialog timeDialog = new RangedTimePickerDialog(this,

                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        selectedHour = hourOfDay;
                        selectedMinute = minute;
                        setTime(hourOfDay, minute);
                        //判断是否可以下单，可以下单的条件是选择了日期和时间
                        payNowEnable();
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
        timeDialog.setCanceledOnTouchOutside(true);
        timeDialog.show();
    }


    private void setTime(int hourOfDay, int minute) {
        if (hourOfDay != -1 && minute != -1) {
            String minuteFormat = String.valueOf(minute);
            if (minute < 10) {
                minuteFormat = "0" + minute;
            }
            tvGotoClassData.setText(hourOfDay + ":" + minuteFormat + " ~ " + ((hourOfDay + classCount) < 24 ? (hourOfDay + classCount) + ":" + minuteFormat : ""));
        }


    }

    /**
     * @param view The view associated with this listener.
     * @param date
     */
    @Override
    public void onDateChanged(SimpleMonthView view, Calendar date) {
        this.mSimpleMonthView = view;
        linearDate.setVisibility(View.VISIBLE);
        if (selectedYear == 0 || selectedYear > date.get(Calendar.YEAR)) {
            selectedYear = date.get(Calendar.YEAR);
        } else {
            endSelectedYear = date.get(Calendar.YEAR);
        }
        Log.i("TAG", "month==" + date.get(Calendar.MONTH));
        countDate(date);
        week(date);
        fillTvDate();

        if (index == 1) {
            fillWeek();
        } else {
            linearWeek.setVisibility(View.GONE);
        }

    }

    /**
     * 统计天数,保存到hashmap中
     *
     * @param date
     */
    public void countDate(Calendar date) {

        int month = date.get(Calendar.MONTH) + 1;
        int day = date.get(Calendar.DAY_OF_MONTH);
        if (countDate.size() > 0) {
            if (countDate.keySet().contains(month)) {
                if (countDate.get(month).contains(Integer.valueOf(day))) {
                    countDate.get(month).remove(Integer.valueOf(day));
                } else {
                    countDate.get(month).add(day);
                    Collections.sort(countDate.get(month));
                }
            } else {
                ArrayList<Integer> dayList = new ArrayList<>();
                dayList.add(day);
                countDate.put(month, dayList);
            }
        } else {
            ArrayList<Integer> dayList = new ArrayList<>();
            dayList.add(day);
            countDate.put(month, dayList);
        }
    }

    public void fillTvDate() {

        Iterator iterator = countDate.keySet().iterator();
        StringBuilder sf = new StringBuilder();
        int countMonth = 0;
        count = 0;
        while (iterator.hasNext()) {

            int selectMonth = (Integer) iterator.next();
            if (countDate.get(selectMonth).size() > 0) {
                if (month == 0 || month >= selectMonth) {
                    month = selectMonth;
                } else {
                    endMonth = selectMonth;
                }
                if (endMonth == 0) {
                    endMonth = month;
                }
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

                if (month == selectMonth) {

                    if (selectday == 0 || selectday > day) {
                        selectday = day;
                    } else {
                        endSelectday = day;
                    }
                } else {
                    endDay.add(day);
                }
                count += 1;
            }
        }
        //根据天数，课时和价格得出总价格
        fillAmountPrice(count);
        //判断是否可以下单，可以下单的条件是选择了日期和时间
        payNowEnable();

        if (index == 0) {
            tvGoToClassDateData.setText(sf.toString());
        } else if (index == 1) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(selectedYear, month - 1, selectday);
            Calendar calendarSecond = Calendar.getInstance();
            calendarSecond.set(selectedYear, month, selectday);
            StringBuffer sb = new StringBuffer();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            sb.append(simpleDateFormat.format(calendar.getTime()));
            sb.append("~");
            sb.append(simpleDateFormat.format(calendarSecond.getTime()));
            tvGoToClassDateData.setText(sb.toString());
        }
    }

    /**
     * 获取结束的day
     *
     * @return
     */
    private int getEndday() {
        int result = 0;
        if (endDay.size() > 0) {
            for (Integer day : endDay) {
                if (day > result) {
                    result = day;
                }
            }
        } else {
            result = endSelectday;
        }
        Log.i("TAG", "result=" + result);
        return result;
    }


    /**
     * 判断payNow是不是激活状态
     */
    private void payNowEnable() {
        if (count > 0 && selectedHour != -1) {
            payNow.setEnabled(true);
        } else {
            payNow.setEnabled(false);
        }
    }

    public void fillAmountPrice(int count) {
        
        NumberFormat currency = NumberFormat.getCurrencyInstance(); //建立货币格式化引用

        if (count == 0) {
            tvHourPrice.setText(hourPrice + "");
        } else {
            if (index == 0) {
                amount = new BigDecimal(classCount * count * hourPrice);
            } else {
                amount = new BigDecimal(getMonthPrice());
            }
            L.i("TAG" + "amount=" + amount);
            tvHourPrice.setText(currency.format(amount));
        }
    }

    public double getMonthPrice() {
        classDay = 0;
        double result;
        int dayCount = getDaysByYearMonth(selectedYear, month);

        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedYear, month - 1, selectday);
        Log.i("TAG", "selectedYear=" + selectedYear + "month=" + month + "selectday=" + selectday);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int residue = dayCount - 28;
        Integer[] residueWeek = new Integer[residue];
        for (int i = 0; i < residue; i++) {
            residueWeek[i] = dayOfWeek + i;
            if (residueWeek[i] - 6 > 0) {
                residueWeek[i] = residueWeek[i] - 7;
            }
            for (Integer week : dayOfWeeks) {
                if (week == residueWeek[i]) {
                    classDay += 1;
                }
            }
        }
        result = (classCount * count * hourPrice * 4)
                + (classDay * classCount * hourPrice);
        return result;
    }

    public void week(Calendar date) {
        String datePattern;
        if (SUtils.isApi_18_OrHigher()) {
            datePattern = android.text.format.DateFormat.getBestDateTimePattern(Locale.getDefault(), "eee");
        } else {
            datePattern = DateTimePatternHelper.getBestDateTimePattern(Locale.getDefault(), DateTimePatternHelper.PATTERN_EMMMd);
        }
        SimpleDateFormat mMonthDayFormat = new SimpleDateFormat(datePattern, Locale.getDefault());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = simpleDateFormat.format(date.getTime());
        Log.i("TAG", "simpleDateFormat=" + strDate);

        int dayOfWeek = date.get(Calendar.DAY_OF_WEEK) - 1;
        Log.i("TAG", "dayOfWeek--" + date.get(Calendar.DAY_OF_WEEK));
        if (dayOfWeeks.contains(Integer.valueOf(dayOfWeek))) {
            weeks.remove(strDate);
            dayOfWeeks.remove(Integer.valueOf(dayOfWeek));
        } else {
            weeks.add(strDate);
            dayOfWeeks.add(Integer.valueOf(dayOfWeek));
        }

        sort();
        weekSf = new StringBuilder();
        Log.i("TAG", "weeks.size=" + weeks.size());
        for (int i = 0; i < weeks.size(); i++) {
//            Date dates = new Date()
            String[] split = weeks.get(i).split("-");
            Log.i("TAG", "split=" + Integer.valueOf(split[0]) + " " + Integer.valueOf(split[1]) + "ss" + Integer.valueOf(split[2]));
            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.valueOf(split[0]), Integer.valueOf(split[1]) - 1, Integer.valueOf(split[2]));
            Log.i("TAG", "week=" + calendar.get(Calendar.DAY_OF_WEEK));
            weekSf.append(mMonthDayFormat.format(calendar.getTime()));
            if (i != weeks.size() - 1) {
                weekSf.append("、");
            }
        }
    }

    public void fillWeek() {
        linearWeek.setVisibility(View.VISIBLE);
        tvGoToClassDateWeek.setText(weekSf.toString());
    }

    /**
     * 日历排序呢
     */
    public void sort() {
        String tempCalendar;
        for (int i = weeks.size() - 1; i > 0; --i) {
            for (int j = 0; j < i; ++j) {
                if (compareDate(weeks.get(j), weeks.get(j + 1)) == 1) {
                    Log.i("TAG", "compareDate");
                    tempCalendar = weeks.get(j);
                    weeks.set(j, weeks.get(j + 1));
                    weeks.set(j + 1, tempCalendar);
                }

            }
        }
    }

    /**
     * 比较两个时间大小
     *
     * @param first
     * @param second
     * @return
     */
    public int compareDate(String first, String second) {
        String[] split = first.split("-");
        String[] secondSplit = second.split("-");
        for (int i = 0; i < split.length; i++) {
            if (Integer.valueOf(split[0]) > Integer.valueOf(secondSplit[0])) {
                return 1;
            } else if (Integer.valueOf(split[1]) > Integer.valueOf(secondSplit[1])) {
                return 1;
            } else if (Integer.valueOf(split[2]) > Integer.valueOf(secondSplit[2])) {
                return 1;
            } else {
                return -1;
            }
        }
        return -1;
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
}

