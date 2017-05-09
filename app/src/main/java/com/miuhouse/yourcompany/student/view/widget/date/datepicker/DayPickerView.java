/*
 * Copyright (C) 2014 The Android Open Source Project
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

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.view.widget.date.hepers.DateTimePatternHelper;
import com.miuhouse.yourcompany.student.view.widget.date.utilities.Config;
import com.miuhouse.yourcompany.student.view.widget.date.utilities.SUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

/**
 * This displays a list of months in a calendar format with selectable days.
 */
class DayPickerView extends ViewGroup {
    private static final String TAG = DayPickerView.class.getSimpleName();

    private static final int[] ATTRS_TEXT_COLOR = new int[]{android.R.attr.textColor};

    private SelectedDate mSelectedDay = null;
    private final Calendar mMinDate = Calendar.getInstance();
    private final Calendar mMaxDate = Calendar.getInstance();
    private final Calendar mCalendar = Calendar.getInstance();
    private final Calendar mNextCalendar = Calendar.getInstance();

    private final AccessibilityManager mAccessibilityManager;

    private final DayPickerViewPager mViewPager;
    private final TextView mPrevButton;
    private final TextView mNextButton;
    private final TextView tvReset;

    private final DayPickerPagerAdapter mAdapter;

    /**
     * Temporary calendar used for date calculations.
     */
    private Calendar mTempCalendar;

    private ProxyDaySelectionEventListener mProxyDaySelectionEventListener;

    private OnPageClickListener mOnPageClickListener;

    private OnResetClickListener mOnResetClickListener;
    private SimpleDateFormat mTitleFormatter;

    public DayPickerView(Context context) {
        this(context, null);
    }

    public DayPickerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.spDayPickerStyle);
    }

    public DayPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(SUtils.createThemeWrapper(context, R.attr.sublimePickerStyle,
                R.style.SublimePickerStyleLight, defStyleAttr,
                R.style.DayPickerViewStyle), attrs);

        context = getContext();

        mAccessibilityManager = (AccessibilityManager) context.getSystemService(
                Context.ACCESSIBILITY_SERVICE);

        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.DayPickerView, defStyleAttr, R.style.DayPickerViewStyle);

        final int monthTextAppearanceResId = a.getResourceId(
                R.styleable.DayPickerView_spMonthTextAppearance,
                R.style.SPMonthLabelTextAppearance);
        // verified
        final int dayOfWeekTextAppearanceResId = a.getResourceId(
                R.styleable.DayPickerView_spWeekDayTextAppearance,
                R.style.SPWeekDayLabelTextAppearance);
        // verified
        final int dayTextAppearanceResId = a.getResourceId(
                R.styleable.DayPickerView_spDateTextAppearance,
                R.style.SPDayTextAppearance);

        final ColorStateList daySelectorColor = a.getColorStateList(
                R.styleable.DayPickerView_spDaySelectorColor);

        a.recycle();

        if (Config.DEBUG) {
            Log.i(TAG, "MDayPickerView_spmMonthTextAppearance: " + monthTextAppearanceResId);
            Log.i(TAG, "MDayPickerView_spmWeekDayTextAppearance: " + dayOfWeekTextAppearanceResId);
            Log.i(TAG, "MDayPickerView_spmDateTextAppearance: " + dayTextAppearanceResId);
        }

        String titleFormat;
        final Resources res = getContext().getResources();

        final Locale locale = res.getConfiguration().locale;
        if (SUtils.isApi_18_OrHigher()) {
            titleFormat = DateFormat.getBestDateTimePattern(locale, "MM");
        } else {
            titleFormat = DateTimePatternHelper.getBestDateTimePattern(locale,
                    DateTimePatternHelper.PATTERN_MMMMy);
        }

        mTitleFormatter = new SimpleDateFormat(titleFormat, locale);

        // Set up adapter.
        mAdapter = new DayPickerPagerAdapter(context,
                R.layout.date_picker_month_item, R.id.month_view, this);
        mAdapter.setMonthTextAppearance(monthTextAppearanceResId);
        mAdapter.setDayOfWeekTextAppearance(dayOfWeekTextAppearanceResId);
        mAdapter.setDayTextAppearance(dayTextAppearanceResId);
        mAdapter.setDaySelectorColor(daySelectorColor);

        final LayoutInflater inflater = LayoutInflater.from(context);

        int layoutIdToUse, viewPagerIdToUse;

        if (getTag() != null && getTag() instanceof String
                && getResources().getString(R.string.recurrence_end_date_picker_tag).equals(getTag())) {
            layoutIdToUse = R.layout.day_picker_content_redp;
            viewPagerIdToUse = R.id.redp_view_pager;
        } else {
            layoutIdToUse = R.layout.day_picker_content_sdp;
            viewPagerIdToUse = R.id.sdp_view_pager;
        }

        inflater.inflate(layoutIdToUse, this, true);

        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                int direction = -1;
                if (v == mPrevButton) {
                    direction = -1;
                } else if (v == mNextButton) {
                    direction = 1;
                } else if (v == tvReset) {
                    if (mOnResetClickListener != null)
                        mOnResetClickListener.onResetClick();
//                    return;
                }

                // Animation is expensive for accessibility services since it sends
                // lots of scroll and content change events.
                final boolean animate = !mAccessibilityManager.isEnabled();

                // ViewPager clamps input values, so we don't need to worry
                // about passing invalid indices.
                final int nextItem = mViewPager.getCurrentItem() + direction;
                mViewPager.setCurrentItem(nextItem, animate);
            }
        };

        mPrevButton = (TextView) findViewById(R.id.prev);
        mPrevButton.setOnClickListener(onClickListener);

        mNextButton = (TextView) findViewById(R.id.next);
        mNextButton.setOnClickListener(onClickListener);
        mPrevButton.setText(getTitle());
        mNextButton.setText(getNextTitle());
        tvReset = (TextView) findViewById(R.id.tv_reset);
        tvReset.setOnClickListener(onClickListener);
        ViewPager.OnPageChangeListener onPageChangedListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                final float alpha = Math.abs(0.5f - positionOffset) * 2.0f;
//                mPrevButton.setAlpha(alpha);
//                mNextButton.setAlpha(alpha);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.i("TAG", "position=" + position);
                mOnPageClickListener.onPageSelected(position);
                updateButtonVisibility(position);
            }
        };

        mViewPager = (DayPickerViewPager) findViewById(viewPagerIdToUse);
        mViewPager.setAdapter(mAdapter);

        mViewPager.addOnPageChangeListener(onPageChangedListener);

        // Proxy the month text color into the previous and next buttons.
        if (monthTextAppearanceResId != 0) {
            final TypedArray ta = context.obtainStyledAttributes(null,
                    ATTRS_TEXT_COLOR, 0, monthTextAppearanceResId);
            final ColorStateList monthColor = ta.getColorStateList(0);

            ta.recycle();
        }

        // Proxy selection callbacks to our own listener.
        mAdapter.setDaySelectionEventListener(new DayPickerPagerAdapter.DaySelectionEventListener() {
            @Override
            public boolean onDaySelected(SimpleMonthView view, Calendar day) {
                if (mProxyDaySelectionEventListener != null) {
                    return mProxyDaySelectionEventListener.onDaySelected(view, day);
                }
                return true;
            }

            @Override
            public void onDateRangeSelectionStarted(@NonNull SelectedDate selectedDate) {
                if (mProxyDaySelectionEventListener != null) {
                    mProxyDaySelectionEventListener.onDateRangeSelectionStarted(selectedDate);
                }
            }

            @Override
            public void onDateRangeSelectionEnded(@Nullable SelectedDate selectedDate) {
                if (mProxyDaySelectionEventListener != null) {
                    mProxyDaySelectionEventListener.onDateRangeSelectionEnded(selectedDate);
                }
            }

            @Override
            public void onDateRangeSelectionUpdated(@NonNull SelectedDate selectedDate) {
                if (mProxyDaySelectionEventListener != null) {
                    mProxyDaySelectionEventListener.onDateRangeSelectionUpdated(selectedDate);
                }
            }
        });
    }

    public void goneResetTv() {
        tvReset.setVisibility(View.GONE);
    }

    public CharSequence getTitle() {
        return mTitleFormatter.format(mCalendar.getTime());
    }

    public CharSequence getNextTitle() {

        int month = mNextCalendar.get(Calendar.MONTH);
        int day = mNextCalendar.get(Calendar.DAY_OF_MONTH);
        int year = mNextCalendar.get(Calendar.YEAR);
        if (month == 11) {
            month = 1;
            mNextCalendar.set(Calendar.MONTH, month);
            mNextCalendar.set(Calendar.YEAR, year + 1);
        } else {
            month = month + 1;
            mNextCalendar.set(Calendar.MONTH, month);
            mNextCalendar.set(Calendar.YEAR, year);
        }
        mNextCalendar.set(Calendar.DAY_OF_MONTH, day);

        return mTitleFormatter.format(mNextCalendar.getTime());

    }

    public void setCanPickRange(boolean canPickRange) {
        mViewPager.setCanPickRange(canPickRange);
    }

    private void updateButtonVisibility(int position) {
        final boolean hasPrev = position > 0;
        final boolean hasNext = position < (mAdapter.getCount() - 1);
        mPrevButton.setTextColor(hasPrev ? getContext().getResources().getColor(R.color.textDarkthree) : getContext().getResources().getColor(R.color.themeColor));
        mNextButton.setTextColor(hasNext ? getContext().getResources().getColor(R.color.textDarkthree) : getContext().getResources().getColor(R.color.themeColor));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final ViewPager viewPager = mViewPager;
        measureChild(viewPager, widthMeasureSpec, heightMeasureSpec);

        final int measuredWidthAndState = viewPager.getMeasuredWidthAndState();
        final int measuredHeightAndState = viewPager.getMeasuredHeightAndState();
        setMeasuredDimension(measuredWidthAndState, measuredHeightAndState);

        final int pagerWidth = viewPager.getMeasuredWidth();
        final int pagerHeight = viewPager.getMeasuredHeight();
        final int buttonWidthSpec = MeasureSpec.makeMeasureSpec(pagerWidth, MeasureSpec.AT_MOST);
        final int buttonHeightSpec = MeasureSpec.makeMeasureSpec(pagerHeight, MeasureSpec.AT_MOST);
//        final int buttonWidthSpec = MeasureSpec.makeMeasureSpec(pagerWidth, MeasureSpec.AT_MOST);
//        final int buttonHeightSpec = MeasureSpec.makeMeasureSpec(pagerHeight, MeasureSpec.AT_MOST);
        mPrevButton.measure(buttonWidthSpec, buttonHeightSpec);
        mNextButton.measure(buttonWidthSpec, buttonHeightSpec);
        tvReset.measure(buttonWidthSpec, buttonHeightSpec);
    }

    @Override
    public void onRtlPropertiesChanged(/*@ResolvedLayoutDir*/ int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);

        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final TextView leftButton;
        final TextView rightButton;
        if (SUtils.isLayoutRtlCompat(this)) {
            leftButton = mNextButton;
            rightButton = mPrevButton;
        } else {
            leftButton = mPrevButton;
            rightButton = mNextButton;
        }

        final int width = right - left;
        final int height = bottom - top;
        mViewPager.layout(0, 0, width, height);

        final SimpleMonthView monthView = (SimpleMonthView) mViewPager.getChildAt(0)
                .findViewById(R.id.month_view);
        final int monthHeight = monthView.getMonthHeight();
//        final int monthHeight = 0;
        final int cellWidth = monthView.getCellWidth();

        // Vertically center the previous/next buttons within the month
        // header, horizontally center within the day cell.
        final int leftDW = leftButton.getMeasuredWidth();
        final int leftDH = leftButton.getMeasuredHeight();
        final int leftIconTop = monthView.getPaddingTop() + (monthHeight - leftDH) / 2;
        final int leftIconLeft = monthView.getPaddingLeft() + (cellWidth - leftDW) / 2;
        leftButton.layout(leftIconLeft, leftIconTop, leftIconLeft + leftDW, leftIconTop + leftDH);

        final int rightDW = rightButton.getMeasuredWidth();
        final int tvResetDW = tvReset.getMeasuredWidth();
        final int rightDH = rightButton.getMeasuredHeight();
        final int tvResetDH = tvReset.getMeasuredHeight();
        final int rightIconTop = monthView.getPaddingTop() + (monthHeight - rightDH) / 2;
        final int tvResetIconTop = monthView.getPaddingTop() + (monthHeight - tvResetDH) / 2;
        final int rightIconRight1 = width - monthView.getPaddingRight() - (cellWidth - rightDW) / 2 - 60;
        final int rightIconRight = leftIconLeft + 180;

        rightButton.layout(rightIconRight, rightIconTop,
                rightIconRight + rightDW, rightIconTop + rightDH);

        tvReset.layout(rightIconRight1 - tvResetDW, tvResetIconTop, rightIconRight1, tvResetIconTop + tvResetDH);
    }

    public void setDayOfWeekTextAppearance(int resId) {
        mAdapter.setDayOfWeekTextAppearance(resId);
    }

    public int getDayOfWeekTextAppearance() {
        return mAdapter.getDayOfWeekTextAppearance();
    }

    @SuppressWarnings("unused")
    public void setDayTextAppearance(int resId) {
        mAdapter.setDayTextAppearance(resId);
    }

    @SuppressWarnings("unused")
    public int getDayTextAppearance() {
        return mAdapter.getDayTextAppearance();
    }

    /**
     * Sets the currently selected date to the specified timestamp. Jumps
     * immediately to the new date. To animate to the new date, use
     * {@link #setDate(SelectedDate, boolean)}.
     * <p/>
     * //@param timeInMillis the target day in milliseconds
     */
    public void setDate(SelectedDate date) {
        setDate(date, false);
    }

    /**
     * Sets the currently selected date to the specified timestamp. Jumps
     * immediately to the new date, optionally animating the transition.
     * <p/>
     * //@param timeInMillis the target day in milliseconds
     *
     * @param animate whether to smooth scroll to the new position
     */
    public void setDate(SelectedDate date, boolean animate) {
        setDate(date, animate, true, true, false);
    }

    /**
     * Sets the currently selected date to the specified timestamp. Jumps
     * immediately to the new date, optionally animating the transition.
     * <p/>
     * //@param timeInMillis the target day in milliseconds
     *
     * @param animate whether to smooth scroll to the new position
     */
    public void setDate(SelectedDate date, boolean animate, boolean goToPosition, boolean isReset) {
        setDate(date, animate, true, goToPosition, isReset);
    }

    /**
     * Moves to the month containing the specified day, optionally setting the
     * day as selected.
     * <p/>
     * //@param timeInMillis the target day in milliseconds
     *
     * @param animate     whether to smooth scroll to the new position
     * @param setSelected whether to set the specified day as selected
     */
    public void setDate(SelectedDate date, boolean animate, boolean setSelected, boolean goToPosition, boolean isReset) {
        Log.i("TAG","setDate");
        if (setSelected) {
            mSelectedDay = date;
        }

        final int position = getPositionFromDay(
                mSelectedDay == null ? Calendar.getInstance().getTimeInMillis()
                        : mSelectedDay.getStartDate().getTimeInMillis());
//

        if (date != null)
            if (goToPosition && position != mViewPager.getCurrentItem()) {
                mViewPager.setCurrentItem(position, animate);
            }

        mAdapter.setSelectedDay(new SelectedDate(mSelectedDay), isReset);
    }

    public void setCalendar(LinkedHashMap<Integer, List<Integer>> countDate,boolean isfile){
        mAdapter.setCalendar(countDate,isfile);
    }
    public SelectedDate getDate() {
        return mSelectedDay;
    }

    public void setFirstDayOfWeek(int firstDayOfWeek) {
        mAdapter.setFirstDayOfWeek(firstDayOfWeek);
    }

    public int getFirstDayOfWeek() {
        return mAdapter.getFirstDayOfWeek();
    }

    public void setMinDate(long timeInMillis) {
        Log.i("TAG", "setMinDate");
        mMinDate.setTimeInMillis(timeInMillis);
        onRangeChanged();
    }

    @SuppressWarnings("unused")
    public long getMinDate() {
        return mMinDate.getTimeInMillis();
    }

    public void setMaxDate(long timeInMillis) {
        Log.i("TAG", "setMaxDate");
        mMaxDate.setTimeInMillis(timeInMillis);
        onRangeChanged();
    }

    @SuppressWarnings("unused")
    public long getMaxDate() {
        return mMaxDate.getTimeInMillis();
    }

    /**
     * Handles changes to date range.
     */
    private void onRangeChanged() {
        Log.i("TAG", "onRangChanged");
        mAdapter.setRange(mMinDate, mMaxDate);

        // Changing the min/max date changes the selection position since we
        // don't really have stable IDs. Jumps immediately to the new position.
        setDate(mSelectedDay, false, false, true, false);

        updateButtonVisibility(mViewPager.getCurrentItem());
    }

    /**
     * Sets the listener to call when the user selects a day.
     *
     * @param listener The listener to call.
     */
    public void setProxyDaySelectionEventListener(ProxyDaySelectionEventListener listener) {
        mProxyDaySelectionEventListener = listener;
    }

    public void setOnPageClickListener(OnPageClickListener listener) {
        mOnPageClickListener = listener;
    }

    public void setOnResetClickListener(OnResetClickListener listener) {
        mOnResetClickListener = listener;
    }

    private int getDiffMonths(Calendar start, Calendar end) {
        final int diffYears = end.get(Calendar.YEAR) - start.get(Calendar.YEAR);
        return end.get(Calendar.MONTH) - start.get(Calendar.MONTH) + 12 * diffYears;
    }

    private int getPositionFromDay(long timeInMillis) {
        final int diffMonthMax = getDiffMonths(mMinDate, mMaxDate);
        final int diffMonth = getDiffMonths(mMinDate, getTempCalendarForTime(timeInMillis));
        return SUtils.constrain(diffMonth, 0, diffMonthMax);
    }

    private Calendar getTempCalendarForTime(long timeInMillis) {
        if (mTempCalendar == null) {
            mTempCalendar = Calendar.getInstance();
        }
        mTempCalendar.setTimeInMillis(timeInMillis);
        return mTempCalendar;
    }

    /**
     * Gets the position of the view that is most prominently displayed within the list view.
     */
    public int getMostVisiblePosition() {
        return mViewPager.getCurrentItem();
    }

    public void setPosition(int position) {
        mViewPager.setCurrentItem(position, false);
    }


    public interface ProxyDaySelectionEventListener {
        boolean onDaySelected(SimpleMonthView view, Calendar day);

        void onDateRangeSelectionStarted(@NonNull SelectedDate selectedDate);

        void onDateRangeSelectionEnded(@Nullable SelectedDate selectedDate);

        void onDateRangeSelectionUpdated(@NonNull SelectedDate selectedDate);
    }

    public interface OnPageClickListener {
        void onPageSelected(int position);
    }

    public interface OnResetClickListener {
        void onResetClick();
    }
}
