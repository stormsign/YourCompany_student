package com.miuhouse.yourcompany.student.view.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/** 不能左右滑动，懒加载的ViewPager
 * Created by khb on 2016/7/13.
 */
public class CantScrollViewPager extends ViewPager {

    private boolean isPagingEnabled;

    public CantScrollViewPager(Context context) {
        super(context);
    }

    public CantScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private float startx;
    private float starty;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        startx = ev.getX();
        starty = ev.getY();
        return this.isPagingEnabled && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float x;
        float y;
        x = ev.getX();
        y = ev.getY();
//        L.i("x:"+Math.abs(x-startx)+" -- "+"y:"+Math.abs(y-starty));
            return false;
    }

    public void setPaginEnabled(boolean isEnabled){
        this.isPagingEnabled = isEnabled;
    }
}
