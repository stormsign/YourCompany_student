package com.miuhouse.yourcompany.student.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.miuhouse.yourcompany.student.R;


/** 进化版listDivider类
 * Created by khb on 2016/9/21.
 */
public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {
    private Context context;
    private int left;
    private int top;
    private int right;
    private int bottom;
    private Drawable mdividerDrawable;

    public RecyclerViewItemDecoration(Context context){
        this.context = context;
        mdividerDrawable = ContextCompat.getDrawable(context, R.drawable.list_divider);
    }

    public RecyclerViewItemDecoration(Context context, int drawable){
        this.context = context;
        mdividerDrawable = ContextCompat.getDrawable(context, drawable);
    }

    public RecyclerViewItemDecoration(Context context, int left, int top, int right, int bottom){
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.context = context;
        mdividerDrawable = ContextCompat.getDrawable(context, R.drawable.list_divider);
    }

    public RecyclerViewItemDecoration(Context context, int drawable, int left, int top, int right, int bottom){
        this.context = context;
        mdividerDrawable = ContextCompat.getDrawable(context, drawable);
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.context = context;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(left, top, right, bottom);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft() + this.left;
        int right = parent.getWidth() - parent.getPaddingRight() - this.right;
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view  = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
            int top = view.getBottom() + params.bottomMargin + this.top;
            int bottom = top + mdividerDrawable.getIntrinsicHeight() + this.bottom;
            mdividerDrawable.setBounds(left, top, right, bottom);
            mdividerDrawable.draw(c);
        }
    }
}
