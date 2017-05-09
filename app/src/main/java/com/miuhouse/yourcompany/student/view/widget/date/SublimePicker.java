/*
 * Copyright 2016 Vikram Kakkar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.miuhouse.yourcompany.student.view.widget.date;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcelable;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.view.widget.date.datepicker.SimpleMonthView;
import com.miuhouse.yourcompany.student.view.widget.date.datepicker.SublimeDatePicker;
import com.miuhouse.yourcompany.student.view.widget.date.hepers.SublimeOptions;
import com.miuhouse.yourcompany.student.view.widget.date.utilities.SUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * A customizable view that provisions picking of a date,
 * time and recurrence option, all from a single user-interface.
 * You can also view 'SublimePicker' as a collection of
 * material-styled (API 23) DatePicker, TimePicker
 * and RecurrencePicker, backported to API 14.
 * You can opt for any combination of these three Pickers.
 */
public class SublimePicker extends FrameLayout implements SublimeDatePicker.OnDateChangedListener, SublimeDatePicker.OnResetClickListener {
    private static final String TAG = SublimePicker.class.getSimpleName();

    // Used for formatting date range
    private static final long MONTH_IN_MILLIS = DateUtils.YEAR_IN_MILLIS / 12;

    // Container for 'SublimeDatePicker' & 'SublimeTimePicker'

    private SublimePicker.OnDateChangedListener mDateChangedListener;
    private SublimePicker.OnResetClickListener mOnResetClickListener;
    private HashMap<Integer, List<Integer>> countDate ;

    // Keeps track which picker is showing

    private SublimeDatePicker mDatePicker;

    // Client-set options
    private SublimeOptions mOptions;

    // Ok, cancel & switch button handler
//    private ButtonHandler mButtonLayout;


    public SublimePicker(Context context) {
        this(context, null);
    }

    public SublimePicker(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.sublimePickerStyle);
    }

    public SublimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(createThemeWrapper(context), attrs, defStyleAttr);
        initializeLayout();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SublimePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(createThemeWrapper(context), attrs, defStyleAttr, defStyleRes);
        initializeLayout();
    }

    private static ContextThemeWrapper createThemeWrapper(Context context) {
        final TypedArray forParent = context.obtainStyledAttributes(
                new int[]{R.attr.sublimePickerStyle});
        int parentStyle = forParent.getResourceId(0, R.style.SublimePickerStyleLight);
        forParent.recycle();

        return new ContextThemeWrapper(context, parentStyle);
    }

    public void setOnDatePickerEnabled(OnDateChangedListener listener) {
        mDateChangedListener = listener;
    }

    public void setmOnResetClickListener(OnResetClickListener listener) {
        mOnResetClickListener = listener;
    }

    private void initializeLayout() {
        Context context = getContext();
        SUtils.initializeResources(context);

        LayoutInflater.from(context).inflate(R.layout.sublime_picker_view_layout,
                this, true);
        mDatePicker = (SublimeDatePicker) findViewById(R.id.datePicker);
        mDatePicker.setmDateChangedListener(this);
        mDatePicker.setmOnResetClickListener(this);

    }

    public void setCalendar(LinkedHashMap<Integer, List<Integer>> countDate,boolean isfill){
        mDatePicker.setCalendar(countDate,isfill);

    }

    public void setIsSublimeClick(boolean isClick) {
        mDatePicker.setIsClick(isClick);
    }

    /**
     * 重置日历
     */
    public void reset() {
        mDatePicker.reset();
    }

    /**
     * 隐藏重置按钮
     */
    public void setGoneResetTv() {
        mDatePicker.goneResetTv();
    }


    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        super.dispatchRestoreInstanceState(container);
    }


    @Override
    public void onDateChanged(SimpleMonthView view, Calendar date) {
//        mDatePicker.init(selectedDate, mOptions.canPickDateRange(), this);
        if (mDateChangedListener != null) {
            mDateChangedListener.onDateChanged(view, date);
        }
    }

    @Override
    public void onResetClick() {
        if (mOnResetClickListener != null) {
            mOnResetClickListener.onResetClick();
        }
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
