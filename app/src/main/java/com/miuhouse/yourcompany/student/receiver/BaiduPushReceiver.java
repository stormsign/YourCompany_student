package com.miuhouse.yourcompany.student.receiver;

import android.content.Context;
import android.content.Intent;

import com.baidu.android.pushservice.PushMessageReceiver;
import com.miuhouse.yourcompany.student.factory.FragmentFactory;
import com.miuhouse.yourcompany.student.presenter.MyOrderInfoMessagePresenter;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.utils.SPUtils;
import com.miuhouse.yourcompany.student.view.ui.activity.DetailTwitterActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.OrderMsgActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.SysMsgActivity;
import com.miuhouse.yourcompany.student.view.ui.base.BaseFragment;
import com.miuhouse.yourcompany.student.view.ui.fragment.AccountFragment;
import com.miuhouse.yourcompany.student.view.ui.fragment.interf.IMyOrderInfoMessageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by khb on 2016/7/14.
 */
public class BaiduPushReceiver extends PushMessageReceiver {
    @Override
    public void onBind(Context context, int errorCode, String appid, String userId, String channelId, String requestId) {
        String responseString = "onBind errorCode=" + errorCode + " appid=" + appid + " userId=" + userId + " channelId=" + channelId + " requestId=" + requestId;

        SPUtils.saveData(SPUtils.DEVICE_CODE, channelId);

        L.i("TAG", responseString);
    }

    @Override
    public void onUnbind(Context context, int errorCode, String requestId) {

    }

    @Override
    public void onSetTags(Context context, int errorCode,
                          List<String> sucessTags, List<String> failTags, String requestId) {

    }

    @Override
    public void onDelTags(Context context, int errorCode,
                          List<String> sucessTags, List<String> failTags, String requestId) {

    }

    @Override
    public void onListTags(Context context, int errorCode, List<String> tags,
                           String requestId) {

    }

    @Override
    public void onMessage(Context context, String message,
                          String customContentString) {
        L.i(" ON MESSAGE " + message);
    }

    @Override
    public void onNotificationClicked(Context context, String title,
                                      String description, String customContentString) {
        String notifyString = "通知点击 onNotificationClicked  title=\"" + title
                + "\" description=\"" + description + "\" customContent="
                + customContentString;
        L.i(notifyString);
        try {
            JSONObject jObject = new JSONObject(customContentString);
            int type = jObject.getInt("type");
            switch (type) {
                case 1:     //订单通知
//                    int orderMsgType = jObject.getInt("orderMsgType");
//                    String orderId = jObject.getString("orderId");
                    Intent intent = new Intent();
                    intent.setClass(context.getApplicationContext(), OrderMsgActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
//                    switch (orderMsgType){
//                        case 1: //发布成功
//                            context.startActivity(new Intent(context.getApplicationContext(), OrdersStatusActivity.class)
//                                    .putExtra(OrdersStatusActivity.EXTRA_ORDERID, orderId));
//                            break;
//                        case 2: //订单取消
//                            context.startActivity(new Intent(context.getApplicationContext(), OrderDetailActivity.class)
//                                    .putExtra("orderId", orderId));
//                            break;
//                        case 3: //订单完成
//                            context.startActivity(new Intent(context.getApplicationContext(), OrderDetailActivity.class)
//                                    .putExtra("orderId", orderId));
//                            break;
//                        case 4: //有老师抢单
//                            context.startActivity(new Intent(context.getApplicationContext(), OrdersStatusActivity.class)
//                                    .putExtra(OrdersStatusActivity.EXTRA_ORDERID, orderId));
//                            break;
//                    }

                    break;
                case 2:     //上课提醒（已废）

                    break;
                case 3:      //账户变动， 账户通知

                    break;
                case 4:     //系统通知
//                    int count = SPUtils.getData(Constants.UNREAD_SYSMSG_COUNT, 0);
//                    count += 1;
//                    SPUtils.saveData(Constants.UNREAD_SYSMSG_COUNT, count);
                    Intent intent4 = new Intent();
                    intent4.setClass(context.getApplicationContext(), SysMsgActivity.class);
                    intent4.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent4);
                    break;
                case 10:        //动态被点赞或被评论的消息
                    String id = jObject.getString("schoolNewsId");
                    Intent intent5 = new Intent();
                    intent5.setClass(context.getApplicationContext(), DetailTwitterActivity.class);
                    intent5.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent5.putExtra("schoolNewsId", SPUtils.getData(Constants.LATEST_DONGTAI, null));
                    context.startActivity(intent5);
                    break;
                default:
                    break;

            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
//        Intent intent = new Intent();
//        intent.setClass(context.getApplicationContext(), SysMsgActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        context.startActivity(intent);
    }

    @Override
    public void onNotificationArrived(Context context, String title,
                                      String description, String customContentString) {
        String notifyString = "通知到达 onNotificationArrived  title=\"" + title
                + "\" description=\"" + description + "\" customContent="
                + customContentString;
        L.i(notifyString);
        try {
            JSONObject jObject = new JSONObject(customContentString);
            int type = Integer.parseInt(jObject.getString("type"));
//            关于未读消息提示这里，以后改成从后台获取
            switch (type) {
                case 1:     //订单通知
                    L.i("订单通知");
                    int count = SPUtils.getData(Constants.UNREAD_ORDERMSG_COUNT, 0);
                    count += 1;
                    SPUtils.saveData(Constants.UNREAD_ORDERMSG_COUNT, count);
                    SPUtils.saveData(Constants.LATEST_ORDERMSG, description);
                    Intent intent = new Intent(Constants.INTENT_ACTION_RECEIVE);
                    intent.putExtra(Constants.MSG_TYPE, type);
                    context.sendBroadcast(intent);
                    refreshMyOrderCount();
                    break;
                case 2:     //上课提醒（已废）
                    L.i("上课提醒");
                    break;
                case 3:      //账户变动， 账户通知
//                    int count3 = SPUtils.getData(Constants.UNREAD_ORDERMSG_COUNT, 0);
//                    count3 += 1;
//                    SPUtils.saveData(Constants.UNREAD_ORDERMSG_COUNT, count3);
//                    SPUtils.saveData(Constants.LATEST_ORDERMSG, description);
//                    Intent intent3 = new Intent(Constants.INTENT_ACTION_RECEIVE);
//                    intent3.putExtra("type", type);
//                    context.sendBroadcast(intent3);
                    break;
                case 4:     //系统通知
                    int count4 = SPUtils.getData(Constants.UNREAD_SYSMSG_COUNT, 0);
                    count4 += 1;
                    SPUtils.saveData(Constants.UNREAD_SYSMSG_COUNT, count4);
                    SPUtils.saveData(Constants.LATEST_SYSMSG, description);
                    Intent intent4 = new Intent(Constants.INTENT_ACTION_RECEIVE);
                    intent4.putExtra("type", type);
                    context.sendBroadcast(intent4);
                    break;
                case MyPushReceiver.TYPE_STUDENT_DONGTAI:    //动态有回复或点赞，来自家长端
                    String id = jObject.getString("schoolNewsId");
                    int count5 = SPUtils.getData(Constants.UNREAD_DONGTAI, 0);
                    count5+=1;
                    SPUtils.saveData(Constants.LATEST_DONGTAI, id);
                    SPUtils.saveData(Constants.UNREAD_DONGTAI, count5);
                    L.i("unread count = " + SPUtils.getData(Constants.UNREAD_DONGTAI, 0));
                    Intent intent5 = new Intent(Constants.INTENT_ACTION_RECEIVE);
                    intent5.putExtra("type", type);
                    context.sendBroadcast(intent5);
                    break;
                case MyPushReceiver.TYPE_DAYCARE_DONGTAI:    //动态有回复或点赞，来自机构端
                    String id2 = jObject.getString("schoolNewsId");
                    int count6 = SPUtils.getData(Constants.UNREAD_DONGTAI, 0);
                    count6+=1;
                    SPUtils.saveData(Constants.LATEST_DONGTAI, id2);
                    SPUtils.saveData(Constants.UNREAD_DONGTAI, count6);
                    L.i("unread count = " + SPUtils.getData(Constants.UNREAD_DONGTAI, 0));
                    Intent intent6 = new Intent(Constants.INTENT_ACTION_RECEIVE);
                    intent6.putExtra("type", type);
                    context.sendBroadcast(intent6);
                    break;
            }


        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    private void refreshMyOrderCount(){
        IMyOrderInfoMessageView iAccountFragment= ((AccountFragment)FragmentFactory.getFragment(BaseFragment.ACCOUNT));
        MyOrderInfoMessagePresenter accountFragmentPresenter = new MyOrderInfoMessagePresenter(iAccountFragment);
        accountFragmentPresenter.getMyOrderInfoMessage();
    }
}
