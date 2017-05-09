package com.miuhouse.yourcompany.student.view.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

/**
 * 控制状态栏的颜色
 * Created by khb on 2015/11/13.
 */
public class StatusCompat {
    private static final  int INVALID_VAL = -1;
    private static final int COLOR_DEFAULT = Color.parseColor("#20000000");

    public static void compat(Activity activity, int statusColor){
//        如果android版本是5.X的，可以直接设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            if (statusColor != INVALID_VAL){
                activity.getWindow().setStatusBarColor(statusColor);
            }
            return ;
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            int color = COLOR_DEFAULT;
//            利用系统获取根布局
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            if (statusColor != INVALID_VAL){
                color = statusColor;
            }
            View statusCompatView = new View(activity);
            ViewGroup.LayoutParams params =
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            getStatusBarHeight(activity));
            statusCompatView.setLayoutParams(params);
            statusCompatView.setBackgroundColor(color);
            contentView.addView(statusCompatView, 0);
        }
    }

    private static int getStatusBarHeight(Context context) {
        int result = 0;
//        应该是从配置文件中获取状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0){
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}














