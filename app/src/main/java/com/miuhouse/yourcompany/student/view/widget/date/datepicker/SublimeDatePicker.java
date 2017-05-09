/*
 * Copyright (C) 2014 The Android Open Source Project
 * Copyright 2015 Vikram Kakkar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.miuhouse.yourcompany.student.view.widget.date.datepicker;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.ViewAnimator;


import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.view.widget.date.hepers.SublimeOptions;
import com.miuhouse.yourcompany.student.view.widget.date.utilities.Config;
import com.miuhouse.yourcompany.student.view.widget.date.utilities.SUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

public class SublimeDatePicker extends FrameLayout implements DayPickerView.OnResetClickListener {
    private static final String TAG = SublimeDatePicker.class.getSimpleName();

    private static final int DEFAULT_START_YEAR = 1900;
    private static final int DEFAULT_END_YEAR = 2100;

    private Context mContext;
    private ViewGroup mContainer;

    private DayPickerView mDayPickerView;

    private SublimeDatePicker.OnDateChangedListener mDateChangedListener;

    private OnResetClickListener mOnResetClickListener;

    private SelectedDate mCurrentDate;
    private Calendar mTempDate;
    private Calendar mMinDate;
    private Calendar mMaxDate;

    /**
     * 当isClick等于true，点击无效
     */
    private boolean isClick=false;

    private Locale mCurrentLocale;

    public SublimeDatePicker(Context context) {
        this(context, null);
    }

    public SublimeDatePicker(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.spDatePickerStyle);
    }

    public SublimeDatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeLayout(attrs, defStyleAttr, R.style.SublimeDatePickerStyle);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SublimeDatePicker(Context context, AttributeSet attrs,
                             int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeLayout(attrs, defStyleAttr, defStyleRes);
    }

    private void initializeLayout(AttributeSet attrs,
                                  int defStyleAttr, int defStyleRes) {
        mContext = getContext();

        setCurrentLocale(Locale.getDefault());
        mCurrentDate = new SelectedDate(Calendar.getInstance(mCurrentLocale));
        mTempDate = Calendar.getInstance(mCurrentLocale);
        mMinDate = Calendar.getInstance(mCurrentLocale);
        mMaxDate = Calendar.getInstance(mCurrentLocale);

        final TypedArray a = mContext.obtainStyledAttributes(attrs,
                R.styleable.SublimeDatePicker, defStyleAttr, defStyleRes);
        final LayoutInflater inflater
                = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final int layoutResourceId = R.layout.date_picker_layout;

        try {
            // Set up and attach container.
            mContainer = (ViewGroup) inflater.inflate(layoutResourceId, this, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        addView(mContainer);
        int firstDayOfWeek = a.getInt(R.styleable.SublimeDatePicker_spFirstDayOfWeek,
                mCurrentDate.getFirstDate().getFirstDayOfWeek());

        // Set up min and max dates.
        final Calendar tempDate = Calendar.getInstance(mCurrentLocale);

        tempDate.set(tempDate.get(Calendar.YEAR), tempDate.get(Calendar.MONTH), 1);

        final long minDateMillis = tempDate.getTimeInMillis();

        int endYear = tempDate.get(Calendar.YEAR);
        int endMonth = tempDate.get(Calendar.MONTH) + 1;
        if (tempDate.get(Calendar.MONTH) == 11) {
            endYear = endYear + 1;
            endMonth = 0;
        }
        tempDate.set(endYear, endMonth, 31);
        final long maxDateMillis = tempDate.getTimeInMillis();

        if (maxDateMillis < minDateMillis) {
            throw new IllegalArgumentException("maxDate must be >= minDate");
        }

        final long setDateMillis = SUtils.constrain(
                System.currentTimeMillis(), minDateMillis, maxDateMillis);

        Log.i("TAG", "minDateMillis=" + minDateMillis + "maxDateMills=" + maxDateMillis + "setDateMillis=" + setDateMillis);
        mMinDate.setTimeInMillis(minDateMillis);
        mMaxDate.setTimeInMillis(maxDateMillis);
        mCurrentDate.setTimeInMillis(setDateMillis);

        a.recycle();

        // Set up picker container.
        ViewAnimator mAnimator = (ViewAnimator) mContainer.findViewById(R.id.animator);

        // Set up day picker view.
        mDayPickerView = (DayPickerView) mAnimator.findViewById(R.id.date_picker_day_picker);
        setFirstDayOfWeek(firstDayOfWeek);
        //设置总的范围
        mDayPickerView.setMinDate(mMinDate.getTimeInMillis());
        mDayPickerView.setMaxDate(mMaxDate.getTimeInMillis());
        mDayPickerView.setProxyDaySelectionEventListener(mProxyDaySelectionEventListener);
        mDayPickerView.setOnResetClickListener(this);

    }

    public void setIsClick(boolean isClick) {
        this.isClick = isClick;
    }

    /**
     * 重置
     * onDateChanged最后一个参数表示是否重置
     */
    public void reset() {
        isSelected = false;
        mCurrentDate = new SelectedDate(new SublimeOptions().getDateParams());
        onDateChanged(false, false, false, true);
    }

    /**
     * 隐藏重置按钮
     */
    public void goneResetTv() {
        mDayPickerView.goneResetTv();
    }

    public void setmDateChangedListener(OnDateChangedListener listener) {
        mDateChangedListener = listener;
    }

    public void setmOnResetClickListener(OnResetClickListener listener) {
        mOnResetClickListener = listener;
    }

    /**
     * Listener called when the user selects a day in the day picker view.
     * <p/>
     * 点击日期后的七天背景变红
     * 返回true 超出了选择范围
     */
    private final DayPickerView.ProxyDaySelectionEventListener mProxyDaySelectionEventListener
            = new DayPickerView.ProxyDaySelectionEventListener() {
        @Override
        public boolean onDaySelected(SimpleMonthView view, Calendar day) {

            if (isClick) {
                return true;
            }
            Calendar startCal = SUtils.getCalendarForLocale(null, Locale.getDefault());

            if (day.get(Calendar.YEAR) < startCal.get(Calendar.YEAR)) {
                Toast.makeText(mContext, "超出了选择选择范围", Toast.LENGTH_LONG).show();
                return true;

            } else if (day.get(Calendar.YEAR) == startCal.get(Calendar.YEAR)) {
                if (day.get(Calendar.MONTH) < startCal.get(Calendar.MONTH)) {
                    Toast.makeText(mContext, "超出了选择范围", Toast.LENGTH_LONG).show();
                    return true;
                } else if (day.get(Calendar.MONTH) == startCal.get(Calendar.MONTH)) {
                    if (day.get(Calendar.DAY_OF_MONTH) > startCal.get(Calendar.DAY_OF_MONTH)) {
                        return getRange(view, day);
                    } else {
                        Toast.makeText(mContext, "超出了选择范围", Toast.LENGTH_LONG).show();
                        return true;
                    }
                } else {
                    return getRange(view, day);
                }
            } else {
                return getRange(view, day);
            }
        }

        ;

        @Override
        public void onDateRangeSelectionStarted(@NonNull SelectedDate selectedDate) {

//            onDateChanged(false, false, false);
        }

        @Override
        public void onDateRangeSelectionEnded(@Nullable SelectedDate selectedDate) {
            Log.i("TAG", "onDateRangeSelectionEnded");
//
        }

        @Override
        public void onDateRangeSelectionUpdated(@NonNull SelectedDate selectedDate) {
        }
    };
    //点击一次后，在七天范围外点击无效，
    public boolean isSelected = false;

    public boolean getRange(SimpleMonthView view, Calendar day) {
        //true,点击范围在七天之内
        if (SUtils.compareCalendar(mCurrentDate, day)) {
            mCurrentDate = new SelectedDate(mCurrentDate, day);
            onDateChanged(false, false, false);
            mDateChangedListener.onDateChanged(view, day);

        } else if (!isSelected) {
            //确定七天范围
            isSelected = true;
            mCurrentDate = new SelectedDate(day, getWeekCalendar(day));
            onDateChanged(false, false, false);
        } else {
            return true;
        }
        return false;
    }


    private Calendar getWeekCalendar(Calendar day) {
        Calendar startCal = SUtils.getCalendarForLocale(null, Locale.getDefault());
        int isClickDay = day.get(Calendar.DAY_OF_MONTH);
        int targetDay = 0;
        int targetMonth = day.get(Calendar.MONTH);
        int daysInMonth = SUtils.getDaysInMonth(day.get(Calendar.MONTH), day.get(Calendar.YEAR));
        if (daysInMonth - isClickDay >= 6) {
            targetDay = isClickDay + 6;
        } else {
            targetMonth = targetMonth + 1;
            targetDay = 6 - (daysInMonth - isClickDay);
        }
        Log.i("TAG", "targetDay=" + targetDay);
        startCal.set(day.get(Calendar.YEAR), targetMonth, targetDay);
        return startCal;
    }

    ;

    /**
     * Initialize the state. If the provided values designate an inconsistent
     * date the values are normalized before updating the spinners.
     *
     * @param selectedDate The initial date or date range.
     * @param canPickRange Enable/disable date range selection
     * @param callback     How user is notified date is changed by
     *                     user, can be null.
     */
    //public void init(int year, int monthOfYear, int dayOfMonth, boolean canPickRange,
    public void init(SelectedDate selectedDate, boolean canPickRange,
                     SublimeDatePicker.OnDateChangedListener callback) {


        mDayPickerView.setCanPickRange(canPickRange);

    }

    private void onDateChanged(boolean fromUser, boolean callbackToClient, boolean goToPosition, boolean isReset) {
        mDayPickerView.setDate(new SelectedDate(mCurrentDate), false, goToPosition, isReset);

        if (fromUser) {
            SUtils.vibrateForDatePicker(SublimeDatePicker.this);
        }
    }

    public void setCalendar(LinkedHashMap<Integer, List<Integer>> countDate,boolean isfill){
        mDayPickerView.setCalendar(countDate,isfill);
    }

    private void onDateChanged(boolean fromUser, boolean callbackToClient, boolean goToPosition) {

        onDateChanged(fromUser, callbackToClient, goToPosition, false);
    }
//


    public SelectedDate getSelectedDate() {
        return new SelectedDate(mCurrentDate);
    }


    //    /**
//     * Sets the minimal date supported by this {@link SublimeDatePicker} in
//     * milliseconds since January 1, 1970 00:00:00 in
//     * {@link java.util.TimeZone#getDefault()} time zone.
//     *
//     * @param minDate The minimal supported date.
//     */
    public void setMinDate(long minDate) {
        Log.i("TAG", "setMinDate");
        mTempDate.setTimeInMillis(minDate);
        if (mTempDate.get(Calendar.YEAR) == mMinDate.get(Calendar.YEAR)
                && mTempDate.get(Calendar.DAY_OF_YEAR) != mMinDate.get(Calendar.DAY_OF_YEAR)) {
            return;
        }
        if (mCurrentDate.getStartDate().before(mTempDate)) {
            mCurrentDate.getStartDate().setTimeInMillis(minDate);
            onDateChanged(false, true, true);
        }
        mMinDate.setTimeInMillis(minDate);
        mDayPickerView.setMinDate(minDate);
    }

    /**
     * Gets the minimal date supported by this {@link SublimeDatePicker} in
     * milliseconds since January 1, 1970 00:00:00 in
     * {@link java.util.TimeZone#getDefault()} time zone.
     * Note: The default minimal date is 01/01/1900.
     *
     * @return The minimal supported date.
     */
    @SuppressWarnings("unused")
    public Calendar getMinDate() {
        return mMinDate;
    }

    //    /**
//     * Sets the maximal date supported by this {@link SublimeDatePicker} in
//     * milliseconds since January 1, 1970 00:00:00 in
//     * {@link java.util.TimeZone#getDefault()} time zone.
//     *
//     * @param maxDate The maximal supported date.
//     */
    public void setMaxDate(long maxDate) {
        mTempDate.setTimeInMillis(maxDate);
        if (mTempDate.get(Calendar.YEAR) == mMaxDate.get(Calendar.YEAR)
                && mTempDate.get(Calendar.DAY_OF_YEAR) != mMaxDate.get(Calendar.DAY_OF_YEAR)) {
            return;
        }
        if (mCurrentDate.getEndDate().after(mTempDate)) {
            mCurrentDate.getEndDate().setTimeInMillis(maxDate);
            onDateChanged(false, true, true);
        }
        mMaxDate.setTimeInMillis(maxDate);
        mDayPickerView.setMaxDate(maxDate);
    }

    /**
     * Gets the maximal date supported by this {@link SublimeDatePicker} in
     * milliseconds since January 1, 1970 00:00:00 in
     * {@link java.util.TimeZone#getDefault()} time zone.
     * Note: The default maximal date is 12/31/2100.
     *
     * @return The maximal supported date.
     */
    @SuppressWarnings("unused")
    public Calendar getMaxDate() {
        return mMaxDate;
    }

    public void setFirstDayOfWeek(int firstDayOfWeek) {
        if (firstDayOfWeek < Calendar.SUNDAY || firstDayOfWeek > Calendar.SATURDAY) {
            if (Config.DEBUG) {
                Log.e(TAG, "Provided `firstDayOfWeek` is invalid - it must be between 1 and 7. " +
                        "Given value: " + firstDayOfWeek + " Picker will use the default value for the given locale.");
            }
            firstDayOfWeek = mCurrentDate.getFirstDate().getFirstDayOfWeek();
        }
        Log.i("TAG", "firstDayOfWeek=" + firstDayOfWeek);

        mDayPickerView.setFirstDayOfWeek(firstDayOfWeek);
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (isEnabled() == enabled) {
            return;
        }
        mContainer.setEnabled(enabled);
        mDayPickerView.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return mContainer.isEnabled();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        setCurrentLocale(newConfig.locale);
    }

    private void setCurrentLocale(Locale locale) {
        if (!locale.equals(mCurrentLocale)) {
            mCurrentLocale = locale;
        }
    }


    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        onPopulateAccessibilityEvent(event);
        return true;
    }

    @Override
    public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
        super.onPopulateAccessibilityEvent(event);
        event.getText().add(mCurrentDate.getStartDate().getTime().toString());
    }

    public CharSequence getAccessibilityClassName() {
        return SublimeDatePicker.class.getName();
    }

    @Override
    public void onResetClick() {
        if (mOnResetClickListener != null)
            mOnResetClickListener.onResetClick();
    }


    /**
     * The callback used to indicate the user changed the date.
     */
    public interface OnDateChangedListener {

        /**
         * Called upon a date change.
         *
         * @param view The view associated with this listener.
         */
        void onDateChanged(SimpleMonthView view, Calendar date);
    }

    public interface OnResetClickListener {
        void onResetClick();
    }
}
