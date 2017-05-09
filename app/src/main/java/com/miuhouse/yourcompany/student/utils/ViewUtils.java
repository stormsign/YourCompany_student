package com.miuhouse.yourcompany.student.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.ArrayAdapter;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.view.ui.adapter.ChoiceAdapter;
import com.miuhouse.yourcompany.student.view.widget.LovelyChoiceDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 显示控件的工具类
 * Created by khb on 2016/9/2.
 */
public class ViewUtils {

    /***
     * 加载对话框
     *
     * @param context
     * @param message
     * @return
     */
    public static ProgressDialog getWaitDialog(Context context, String message) {
        ProgressDialog waitDialog = new ProgressDialog(context);
        if (!TextUtils.isEmpty(message)) {
            waitDialog.setMessage(message);
        }
        return waitDialog;
    }


    /**
     * 显示单选对话框
     *
     * @param radioList    单选项
     * @param title        对话框标题
     * @param defaultRadio 显示对话框时默认选中的文本
     * @param listener     点击监听
     */
    public static void showRadioBoxDialog(Activity activity, List<String> radioList, String title, String defaultRadio,
                                          LovelyChoiceDialog.OnItemSelectedListener<String> listener) {
        final ArrayAdapter<String> adapter = new ChoiceAdapter(activity, radioList, defaultRadio);
        final LovelyChoiceDialog lovelyChoiceDialog = new LovelyChoiceDialog(activity);
        lovelyChoiceDialog.setTopColorRes(R.color.backgroundcolor_common)
                .goneIconView()
                .setTitle(title)
                .setItems(adapter, listener).show();
    }

    /**
     * 简单单选对话框
     * @param activity
     * @param list 单选选项
     * @param listener 点击监听
     */
    public static void showSimpleChoiceDialog(Activity activity, List<String> list,
                                              LovelyChoiceDialog.OnItemSelectedListener<String> listener){
        ArrayList<String> items = new ArrayList<>();
        items.addAll(list);
        ArrayAdapter adapter = new ArrayAdapter(activity, R.layout.item_simple_text_center, items);
        LovelyChoiceDialog lovelyChoiceDialog =
                new LovelyChoiceDialog(activity, R.style.CheckBoxTintTheme);
        lovelyChoiceDialog.goneIconView().setItems(adapter, listener).show();
    }

    /**
     * 弹出通知
     *
     * @param context
     * @param contentTitle 通知栏标题
     * @param contentText  通知栏内容
     * @param pi           点击后跳转
     */
    public static void showNotification(Context context, String contentTitle, String contentText, PendingIntent pi) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(contentTitle);
        builder.setContentText(contentText);
        android.support.v4.app.NotificationCompat.BigTextStyle bigTextStyle = new android.support.v4.app.NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(contentTitle);
        bigTextStyle.bigText(contentText);
        builder.setStyle(bigTextStyle);
//        builder.setSubText("subtext");
//        Notification notification = builder.build();
        builder.setAutoCancel(true);
        builder.setTicker(contentTitle);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        builder.setContentIntent(pi);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        Notification notification = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.getNotification();
        } else {
            notification = builder.build();
        }
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.defaults = Notification.DEFAULT_SOUND
                | Notification.DEFAULT_LIGHTS
                | Notification.DEFAULT_VIBRATE;

        nm.notify((int) System.currentTimeMillis(), notification);

    }


}
