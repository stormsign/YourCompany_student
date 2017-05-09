package com.miuhouse.yourcompany.student.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.miuhouse.yourcompany.student.R;

/**
 * Created by khb on 2016/12/2.
 */
public class MyProgressDialog extends Dialog {
    private Context context;

    public MyProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public MyProgressDialog(Context context) {
        this(context, R.style.myProgressDialog);
        this.context = context;
    }

    public MyProgressDialog init(Context context) {
        setContentView(R.layout.view_loading);
        setCanceledOnTouchOutside(false);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        ImageView loading = (ImageView) findViewById(R.id.loading);
        RotateAnimation rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setFillAfter(true);
        rotate.setDuration(1000);
        rotate.setRepeatMode(Animation.RESTART);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setInterpolator(new LinearInterpolator());
        loading.setAnimation(rotate);
        rotate.start();
        return this;
    }



}
