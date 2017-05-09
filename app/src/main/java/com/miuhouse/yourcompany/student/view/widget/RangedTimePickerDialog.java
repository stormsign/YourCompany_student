package com.miuhouse.yourcompany.student.view.widget;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

import com.miuhouse.yourcompany.student.utils.L;

import java.lang.reflect.Field;
import java.util.Calendar;

/**
 * Created by khb on 2016/9/1.
 */
public class RangedTimePickerDialog extends TimePickerDialog {

    private TimePicker mTimePicker;
    private int currentHour;
    private int currentMinute;

    public RangedTimePickerDialog(Context context, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
        super(context, listener, hourOfDay, minute, is24HourView);
        currentHour = hourOfDay;
        currentMinute = minute;
        Class<?> superClass = getClass().getSuperclass();
        try {
            Field declaredField = superClass.getDeclaredField("mTimePicker");
            declaredField.setAccessible(true);
            mTimePicker = (TimePicker) declaredField.get(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RangedTimePickerDialog(Context context, int themeResId, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
        super(context, themeResId, listener, hourOfDay, minute, is24HourView);
    }

    public TimePicker getTimePicker() {
        return mTimePicker;
    }

    public void setTimePicker(TimePicker mTimePicker) {
        this.mTimePicker = mTimePicker;
    }

    private int minHour;
    private int minMinute;
    private int maxHour;
    private int maxMinute;
    private boolean isMinSet = false;
    private boolean isMaxSet = false;

    public void setMinTime(long time){
        L.i("time:"+time);
        isMinSet = true;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        minHour = calendar.get(Calendar.HOUR_OF_DAY);
        minMinute = calendar.get(Calendar.MINUTE);
    }

    public void setMaxTime(long time){
        isMaxSet = true;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        maxHour = calendar.get(Calendar.HOUR_OF_DAY);
        maxMinute = calendar.get(Calendar.MINUTE);
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        boolean validTime = true;
        L.i("timepicker:"+view.getDrawingTime());
        if (isMinSet) {
            if (hourOfDay < minHour || (hourOfDay == minHour && minute < minMinute)) {
                validTime = false;
            }
        }
        if (isMaxSet) {
            if (hourOfDay > maxHour || (hourOfDay == maxHour && minute > maxMinute)) {
                validTime = false;
            }
        }
        if (validTime){
            currentHour = hourOfDay;
            currentMinute = minute;
        }else {
            updateTime(currentHour, currentMinute);
        }
    }

    @Override
    protected void onStop() {
//        super.onStop();
    }
}














