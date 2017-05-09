package com.miuhouse.yourcompany.student.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.miuhouse.yourcompany.student.listener.OnReceiveListener;

/**
 * Created by khb on 2016/7/15.
 */
public class MyPushReceiver extends BroadcastReceiver {

    public static final int TYPE_STUDENT_DONGTAI = 9;
    public static final int TYPE_DAYCARE_DONGTAI = 10;

    private OnReceiveListener listener;

    public MyPushReceiver(OnReceiveListener listener){
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        listener.onReceive(context, intent);
    }
}
