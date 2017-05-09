package com.miuhouse.yourcompany.student.view.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.presenter.MessagePresenter;
import com.miuhouse.yourcompany.student.presenter.interf.IMessagePresenter;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.view.ui.activity.OrderMsgActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.SysMsgActivity;
import com.miuhouse.yourcompany.student.view.ui.base.BaseFragment;
import com.miuhouse.yourcompany.student.view.ui.fragment.interf.IMessagesFragment;

//import com.miuhouse.yourcompany.student.model.MsgEntity;
//import com.miuhouse.yourcompany.student.presenter.MessagePresenter;
//import com.miuhouse.yourcompany.student.presenter.interf.IMessagePresenter;
//import com.miuhouse.yourcompany.student.utils.Util;
//import com.miuhouse.yourcompany.student.view.ui.activity.OrderDetailActivity;
//import com.miuhouse.yourcompany.student.view.ui.activity.OrderMsgActivity;
//import com.miuhouse.yourcompany.student.view.ui.activity.SysMsgActivity;
//import com.miuhouse.yourcompany.student.view.ui.adapter.MsgAdapter;
//import com.miuhouse.yourcompany.student.view.ui.fragment.interf.IMessageFragment;

/**
 * Created by khb on 2016/7/6.
 */
public class MessagesFragment extends BaseFragment implements IMessagesFragment {

    private SwipeRefreshLayout refresh;
    private RecyclerView msgList;
//    private List<MsgEntity> list;

    private int page = 1;
//    private MsgAdapter adapter;
    private IMessagePresenter messagePresenter;
    private TextView unreadSysMsg;
    private RelativeLayout sysMsg;
    private TextView sysMsgSummary;
    private RelativeLayout purseMsg;
    private TextView purseMsgSummary;
    private TextView unreadPurseMsg;
//
//    @Override
    public int getFragmentResourceId() {
          return R.layout.fragment_message;
    }
//
//    @Override
    public void getViews(View view) {
        messagePresenter = new MessagePresenter(this);
//        list = new ArrayList<>();
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        msgList = (RecyclerView) view.findViewById(R.id.msgList);

        unreadSysMsg = (TextView) view.findViewById(R.id.unreadSysMsg);
        sysMsg = (RelativeLayout) view.findViewById(R.id.sysMsg);
        sysMsgSummary = (TextView) view.findViewById(R.id.sysMsgSummary);

        unreadPurseMsg = (TextView) view.findViewById(R.id.unreadPurseMsg);
        purseMsg = (RelativeLayout) view.findViewById(R.id.purseMsg);
        purseMsgSummary = (TextView) view.findViewById(R.id.purseMsgSummary);
//
    }

    @Override
    public void setupView() {
        sysMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, SysMsgActivity.class));
            }
        });
        purseMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, OrderMsgActivity.class));
            }
        });
    }
//
    @Override
    public void onResume() {
        super.onResume();
        messagePresenter.getTopMsgs();
//        refreshTop();
//        refreshList();
    }
//
//
//    @Override
    public View getOverrideParentView() {
        return null;
    }
//
    @Override
    public void refreshTop() {
        messagePresenter.getTopMsgs();
    }
//
//    @Override
//    public void refreshList() {
////        for (int i=0;i<10; i++){
////            list.add(new MsgEntity());
////        }
//        adapter.notifyDataSetChanged();
//    }
//
//    @Override
//    public void onRefresh() {
//
//    }
//
//    @Override
//    public void instantAdd() {
//
//    }
//
    @Override
    public void setTop(int sysCount, String sysMsg, int purseCount, String purseMsg) {
        showUnreadCount(sysCount,unreadSysMsg);
        showLatestMsg(sysMsg, sysMsgSummary);
        showUnreadCount(purseCount, unreadPurseMsg);
        showLatestMsg(purseMsg, purseMsgSummary);
    }

    private void showUnreadCount(int unreadCount, TextView unreadTextView){
        if (unreadCount>0){
            unreadTextView.setText("" + unreadCount);
        }else {
            unreadTextView.setVisibility(View.GONE);
        }
    }

    private void showLatestMsg(String msg, TextView summary){
        if (!Util.isEmpty(msg)){
            summary.setText(msg);
        }else {
            summary.setVisibility(View.INVISIBLE);
        }
    }
//
//    @Override
//    public void onItemClick(Object data) {
//        MsgEntity msg = (MsgEntity) data;
//        startActivity(new Intent(context, OrderDetailActivity.class)
//                .putExtra("orderId", msg.toString()));
//    }
//
//    @Override
//    public void showLoading(String msg) {
////        super.showLoading(msg);
//        if (!refresh.isRefreshing()) {
//            refresh.post(new Runnable() {
//                @Override
//                public void run() {
//                    refresh.setRefreshing(true);
//                }
//            });
//        }
//    }
//
//    @Override
//    public void showError(int type) {
////        super.showError(msg);
//    }
//
//    @Override
//    public void hideLoading() {
////        super.hideLoading();
//        if (refresh.isRefreshing()){
//            refresh.setRefreshing(false);
//        }
//    }
//
//    @Override
//    public void hideError() {
//        super.hideError();
//    }
}
