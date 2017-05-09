package com.miuhouse.yourcompany.student.view.widget.date.datepicker;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * Created by Admin on 25/02/2016.
 */
public class SelectedDate {

    //TOGETHER:在某个日期范围内(range),可以同时存在单个日期(single),所以取名叫together,
    public enum Type {
        SINGLE, RANGE, TOGETHER
    }

    private Calendar mFirstDate, mSecondDate;
    private Calendar selecteCalendar;

    public SelectedDate(Calendar startDate, Calendar endDate) {
        mFirstDate = startDate;
        mSecondDate = endDate;
        selecteCalendar=null;
    }

    public SelectedDate(Calendar date) {
        //selecteCalendar 初始化为空
        selecteCalendar = null;
        mFirstDate = mSecondDate = date;
    }

    public SelectedDate(SelectedDate mCurrentDate, Calendar date) {
        selecteCalendar = date;
        mFirstDate = mCurrentDate.getFirstDate();
        mSecondDate = mCurrentDate.getSecondDate();
    }

    public SelectedDate(SelectedDate date) {
        mFirstDate = Calendar.getInstance();
        mSecondDate = Calendar.getInstance();
        if (date != null) {
            mFirstDate.setTimeInMillis(date.getStartDate().getTimeInMillis());
            mSecondDate.setTimeInMillis(date.getEndDate().getTimeInMillis());
            if (date.selecteCalendar != null) {
                selecteCalendar = Calendar.getInstance();
                selecteCalendar.setTimeInMillis(date.selecteCalendar.getTimeInMillis());
            }
        }
    }

    public Calendar getFirstDate() {
        return mFirstDate;
    }

    public Calendar getSelecteCalendar() {
        return selecteCalendar;
    }

    public void setFirstDate(Calendar firstDate) {
        mFirstDate = firstDate;
    }

    public Calendar getSecondDate() {
        return mSecondDate;
    }

    public void setSecondDate(Calendar secondDate) {
        mSecondDate = secondDate;
    }

    public void setDate(Calendar date) {
        mFirstDate = date;
        mSecondDate = date;
    }

    public Calendar getStartDate() {
        return compareDates(mFirstDate, mSecondDate) == -1 ? mFirstDate : mSecondDate;
    }

    public Calendar getEndDate() {
        return compareDates(mFirstDate, mSecondDate) == 1 ? mFirstDate : mSecondDate;
    }

    public Type getType() {
        return compareDates(mFirstDate, mSecondDate) == 0 ? Type.SINGLE : selecteCalendar == null ? Type.RANGE : Type.TOGETHER;
    }

    // a & b should never be null, so don't perform a null check here.
    // Let the source of error identify itself.
    public static int compareDates(Calendar a, Calendar b) {
        int aYear = a.get(Calendar.YEAR);
        int bYear = b.get(Calendar.YEAR);

        int aMonth = a.get(Calendar.MONTH);
        int bMonth = b.get(Calendar.MONTH);

        int aDayOfMonth = a.get(Calendar.DAY_OF_MONTH);
        int bDayOfMonth = b.get(Calendar.DAY_OF_MONTH);

        if (aYear < bYear) {
            return -1;
        } else if (aYear > bYear) {
            return 1;
        } else {
            if (aMonth < bMonth) {
                return -1;
            } else if (aMonth > bMonth) {
                return 1;
            } else {
                if (aDayOfMonth < bDayOfMonth) {
                    return -1;
                } else if (aDayOfMonth > bDayOfMonth) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
    }

    public void setTimeInMillis(long timeInMillis) {
        mFirstDate.setTimeInMillis(timeInMillis);
        mSecondDate.setTimeInMillis(timeInMillis);
    }

    public void set(int field, int value) {
        mFirstDate.set(field, value);
        mSecondDate.set(field, value);
    }

    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder();

        if (mFirstDate != null) {
            toReturn.append(DateFormat.getDateInstance().format(mFirstDate.getTime()));
            toReturn.append("\n");
        }

        if (mSecondDate != null) {
            toReturn.append(DateFormat.getDateInstance().format(mSecondDate.getTime()));
        }

        return toReturn.toString();
    }
}
