package com.miuhouse.yourcompany.student.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.miuhouse.yourcompany.student.R;


/**
 * Created by kings on 8/26/2015.
 */
public class MyCount extends CountDownTimer {
    private TextView btnCode;
    private Context context;

    public MyCount(long millisInFuture, long countDownInterval, TextView btnCode, Context context) {
        super(millisInFuture, countDownInterval);
        this.btnCode = btnCode;
        this.context = context;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        btnCode.setEnabled(false);
//        btnCode.setTextColor(context.getResources().getColor(R.color.themeColor));
        btnCode.setText(millisUntilFinished/1000+"s");
    }

    @Override
    public void onFinish() {
        btnCode.setEnabled(true);
        btnCode.setText("获取验证码");
//        btnCode.setTextColor(context.getResources().getColor(R.color.themeColor));
    }
}
