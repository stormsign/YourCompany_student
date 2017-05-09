package com.miuhouse.yourcompany.student.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.huawei.android.pushagent.api.PushEventReceiver;
import com.miuhouse.yourcompany.student.factory.FragmentFactory;
import com.miuhouse.yourcompany.student.presenter.MyOrderInfoMessagePresenter;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.utils.SPUtils;
import com.miuhouse.yourcompany.student.utils.ViewUtils;
import com.miuhouse.yourcompany.student.view.ui.activity.MainActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.OrderDetailActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.OrderMsgActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.OrdersStatusActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.SysMsgActivity;
import com.miuhouse.yourcompany.student.view.ui.base.BaseFragment;
import com.miuhouse.yourcompany.student.view.ui.fragment.AccountFragment;
import com.miuhouse.yourcompany.student.view.ui.fragment.interf.IMyOrderInfoMessageView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by khb on 2016/10/8.
 */
public class HWReceiver extends PushEventReceiver {
    /*
     * 显示Push消息
     */
    public void showPushMessage(int type, String msg) {
//        PustDemoActivity mPustTestActivity = MyApplication.instance().getMainActivity();
//        if (mPustTestActivity != null) {
//            Handler handler = mPustTestActivity.getHandler();
//            if (handler != null) {
//                Message message = handler.obtainMessage();
//                message.what = type;
//                message.obj = msg;
//                handler.sendMessageDelayed(message, 1L);
//            }
//        }
    }

    @Override
    public void onToken(Context context, String token, Bundle extras){
        String belongId = extras.getString("belongId");
        String content = "获取token和belongId成功，token = " + token + ",belongId = " + belongId;
        SPUtils.saveData(SPUtils.DEVICE_CODE, token);
        L.d(content);
//        showPushMessage(PustDemoActivity.RECEIVE_TOKEN_MSG, content);
    }


    /**
     * 收到透传消息
     * @param context
     * @param msg
     * @param bundle
     * @return
     */
    @Override
    public boolean onPushMsg(Context context, byte[] msg, Bundle bundle) {
        try {
            String strMsg = new String(msg, "UTF-8");
            String content = "收到一条Push消息： " + strMsg;
            L.d(content + "   -- 附加消息：" + bundle.getString("type"));
            JSONObject jsonObject = new JSONObject(strMsg);
            String title = jsonObject.getString("title");
            String desc = jsonObject.getString("description");
            int type = jsonObject.getInt("type");
//            int msgType = jsonObject.getInt("msgType");
            if (type==1){      //订单通知
                int count = SPUtils.getData(Constants.UNREAD_ORDERMSG_COUNT, 0);
                count += 1;
                SPUtils.saveData(Constants.UNREAD_ORDERMSG_COUNT, count);
                SPUtils.saveData(Constants.LATEST_ORDERMSG, desc);
                Intent intent = new Intent(Constants.INTENT_ACTION_RECEIVE);
                intent.putExtra(Constants.MSG_TYPE, type);
                context.sendBroadcast(intent);
                refreshMyOrderCount();
                ViewUtils.showNotification(context, title, desc, PendingIntent.getActivity(context, (int) System.currentTimeMillis(),
                        new Intent(context, OrderMsgActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP),
                        PendingIntent.FLAG_CANCEL_CURRENT));
            }else if (type==4){     //系统通知
                int count = SPUtils.getData(Constants.UNREAD_SYSMSG_COUNT, 0);
                count += 1;
                SPUtils.saveData(Constants.UNREAD_SYSMSG_COUNT, count);
                SPUtils.saveData(Constants.LATEST_SYSMSG, desc);
                ViewUtils.showNotification(context, title, desc, PendingIntent.getActivity(context, (int) System.currentTimeMillis(),
                        new Intent(context, SysMsgActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TOP),
                        PendingIntent.FLAG_CANCEL_CURRENT));
            }else if (type == 10){  //动态被点赞或被评论的消息
//                String id = jObject.getString("schoolNewsId");
//                int count5 = SPUtils.getData(Constants.UNREAD_DONGTAI, 0);
//                count5+=1;
//                SPUtils.saveData(Constants.LATEST_DONGTAI, id);
//                SPUtils.saveData(Constants.UNREAD_DONGTAI, count5);
//                Intent intent5 = new Intent(Constants.INTENT_ACTION_RECEIVE);
//                intent5.putExtra("type", type);
//                context.sendBroadcast(intent5);
//                    Intent intent5 = new Intent();
//                    intent5.setClass(context.getApplicationContext(), DetailTwitterActivity.class);
//                    intent5.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent5.putExtra("schoolNewsId", SPUtils.getData(Constants.LATEST_DONGTAI, null));
//                    context.startActivity(intent5);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void onEvent(Context context, Event event, Bundle extras) {
        if (Event.NOTIFICATION_OPENED.equals(event) || Event.NOTIFICATION_CLICK_BTN.equals(event)) {
            int notifyId = extras.getInt(BOUND_KEY.pushNotifyId, 0);
            if (0 != notifyId) {
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(notifyId);
            }
            String content = "收到通知附加消息： " + extras.getString(BOUND_KEY.pushMsgKey);
            L.d(content);
//            showPushMessage(PustDemoActivity.RECEIVE_NOTIFY_CLICK_MSG, content);
            try {
                JSONObject jObject = new JSONObject(extras.getString(BOUND_KEY.pushMsgKey));
                int type = jObject.getInt("type");
                switch (type) {
                    case 1:     //订单通知
                        int orderMsgType = jObject.getInt("orderMsgType");
                        String orderId = jObject.getString("orderId");
                        Intent intent = new Intent();
                        intent.setClass(context.getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        switch (orderMsgType){
                            case 1: //发布成功
                                context.startActivity(new Intent(context.getApplicationContext(), OrdersStatusActivity.class)
                                        .putExtra(OrdersStatusActivity.EXTRA_ORDERID, orderId));
                                break;
                            case 2: //订单取消
                                context.startActivity(new Intent(context.getApplicationContext(), OrderDetailActivity.class)
                                        .putExtra("orderId", orderId));
                                break;
                            case 3: //订单完成
                                context.startActivity(new Intent(context.getApplicationContext(), OrderDetailActivity.class)
                                        .putExtra("orderId", orderId));
                                break;
                            case 4: //有老师抢单
                                context.startActivity(new Intent(context.getApplicationContext(), OrdersStatusActivity.class)
                                        .putExtra(OrdersStatusActivity.EXTRA_ORDERID, orderId));
                                break;
                        }
                        break;
                    case 2:     //上课提醒（已废）

                        break;
                    case 3:      //账户变动， 账户通知

                        break;
                    case 4:     //系统通知

                        break;
                    default:
                        break;

                }
            }catch (JSONException e) {
                e.printStackTrace();
            }


        } else if (Event.PLUGINRSP.equals(event)) {
            final int TYPE_LBS = 1;
            final int TYPE_TAG = 2;
            int reportType = extras.getInt(BOUND_KEY.PLUGINREPORTTYPE, -1);
            boolean isSuccess = extras.getBoolean(BOUND_KEY.PLUGINREPORTRESULT, false);
            String message = "";
            if (TYPE_LBS == reportType) {
                message = "LBS report result :";
            } else if(TYPE_TAG == reportType) {
                message = "TAG report result :";
            }
            L.d( message + isSuccess);
//            showPushMessage(PustDemoActivity.RECEIVE_TAG_LBS_MSG, message + isSuccess);
        }
        super.onEvent(context, event, extras);
    }

    private void refreshMyOrderCount(){
        IMyOrderInfoMessageView iAccountFragment= ((AccountFragment)FragmentFactory.getFragment(BaseFragment.ACCOUNT));
        MyOrderInfoMessagePresenter accountFragmentPresenter = new MyOrderInfoMessagePresenter(iAccountFragment);
        accountFragmentPresenter.getMyOrderInfoMessage();
    }
}
