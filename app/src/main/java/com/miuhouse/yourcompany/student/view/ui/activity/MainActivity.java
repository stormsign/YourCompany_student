package com.miuhouse.yourcompany.student.view.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RelativeLayout;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.factory.FragmentFactory;
import com.miuhouse.yourcompany.student.listener.OnReceiveListener;
import com.miuhouse.yourcompany.student.receiver.MyPushReceiver;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.DictManager;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IMainActivity;
import com.miuhouse.yourcompany.student.view.ui.adapter.MainPageAdapter;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;
import com.miuhouse.yourcompany.student.view.ui.base.BaseFragment;
import com.miuhouse.yourcompany.student.view.ui.fragment.DongTaiFragment;
import com.miuhouse.yourcompany.student.view.widget.CantScrollViewPager;
import com.miuhouse.yourcompany.student.view.widget.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.List;

//import com.miuhouse.yourcompany.student.receiver.MyPushReceiver;
//import com.miuhouse.yourcompany.student.view.ui.adapter.MainPageAdapter;
//import com.miuhouse.yourcompany.student.view.ui.fragment.MessagesFragment;
//import com.miuhouse.yourcompany.student.view.ui.fragment.MapFragment;

public class MainActivity extends BaseActivity implements IMainActivity, OnReceiveListener {

    private RelativeLayout content;
    private ViewPagerIndicator mPages;
    private CantScrollViewPager pager;
    private MyPushReceiver receiver;
    private DongTaiFragment dongtaiFragment;
    //    private MyPushReceiver receiver;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    protected void initTitle() {
                //super.initTitle();
    }

    @Override
    protected String setTitle() {
        return "主页";
    }

    @Override
    protected String setRight() {
        return "按钮";
    }

    @Override
    protected void initViewAndEvents() {
        L.i("SHA1:" + Util.sHA1(context));
        content = (RelativeLayout) findViewById(R.id.content);
        List<Integer> imgResList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        titleList.add("动态");
        titleList.add("小课桌");
        titleList.add("我");
        imgResList.add(R.mipmap.home_dongtai_n);
        imgResList.add(R.mipmap.home_xkz_n);
        imgResList.add(R.mipmap.home_account_n);

        final List<Fragment> fragmentList = new ArrayList<Fragment>();
        //        fragmentList.add(FragmentFactory.getFragment(BaseFragment.MESSAGES));
        dongtaiFragment = (DongTaiFragment) FragmentFactory.getFragment(BaseFragment.DONGTAI);
        fragmentList.add(dongtaiFragment);
        fragmentList.add(FragmentFactory.getFragment(BaseFragment.KZ));
        //        fragmentList.add(FragmentFactory.getFragment(BaseFragment.MAP));
        fragmentList.add(FragmentFactory.getFragment(BaseFragment.ACCOUNT2));

        mPages = (ViewPagerIndicator) findViewById(R.id.pageIndicator);
        pager = (CantScrollViewPager) findViewById(R.id.pages);
        pager.setOffscreenPageLimit(3);
        MainPageAdapter adapter = new MainPageAdapter(getSupportFragmentManager(), fragmentList);
        mPages.setTabItemImgs(imgResList, titleList);
        pager.setAdapter(adapter);
        changePage(0);

        //        if (Util.isHuaWeiEmui()) {
        //            //华为推送
        //            com.huawei.android.pushagent.api.PushManager.requestToken(this);
        //        } else {
        //            //百度推送
        //            PushSettings.enableDebugMode(this, true);
        //            PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, Constants.PUSH_APIKEY);
        //        }
        receiver = new MyPushReceiver(this);
        IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_RECEIVE);
        registerReceiver(receiver, filter);

        DictManager.getInstance().init(this);

        //        Intent intent = new Intent("CLASS_ALARM_ACTION");
        //        intent.putExtra("orderid", "orderid1111111");
        //        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        //        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        //        Calendar calendar = Calendar.getInstance();
        //        calendar.setTimeInMillis(System.currentTimeMillis());
        //        calendar.add(Calendar.SECOND, 10);
        //        am.set(AlarmManager.RTC_WAKEUP,
        //                calendar.getTimeInMillis(),
        //                pi);

        //        Intent intent2 = new Intent("CLASS_ALARM_ACTION");
        //        intent2.putExtra("orderid", "orderid222222");
        //        PendingIntent pi2 = PendingIntent.getBroadcast(this, 100, intent2, PendingIntent.FLAG_ONE_SHOT);
        //        calendar.add(Calendar.SECOND, 5);
        //        am.setRepeating(AlarmManager.RTC_WAKEUP,
        //                calendar.getTimeInMillis(),
        //                1000*10,
        //                pi2);

    }

    @Override
    public void changePage(int index) {
        mPages.setViewPager(pager, index);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected View getOverrideParentView() {
        return content;
    }

    @Override
    public void onBackClick() {
        L.i("back or finish!!!");
    }

    @Override
    public void onRightClick() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int type = intent.getIntExtra(Constants.MSG_TYPE, 0);
        if (type == 0) {
            return;
        }
        //        ((MessagesFragment) FragmentFactory.getFragment(BaseFragment.MESSAGES)).refreshTop();
        //        if (type == 1) {
        //            IMyOrderInfoMessageView iAccountFragment= ((AccountFragment)FragmentFactory.getFragment(BaseFragment.ACCOUNT));
        //            MyOrderInfoMessagePresenter accountFragmentPresenter = new MyOrderInfoMessagePresenter(iAccountFragment);
        //            accountFragmentPresenter.getMyOrderInfoMessage();
        //            mPages.showUnreadIcon(0, true);
        //        } else if (type == 3 ||
        //                type == 4) {
        ////            mPages.showUnreadIcon(0, true);
        //            ((MessagesFragment) FragmentFactory.getFragment(BaseFragment.MESSAGES)).refreshTop();
        //        }
        if (type == MyPushReceiver.TYPE_STUDENT_DONGTAI
                || type == MyPushReceiver.TYPE_DAYCARE_DONGTAI) {
            if (dongtaiFragment != null) {
                dongtaiFragment.unreadDisplayControll(true);
            }
        }
    }

    @Override
    protected void onResume() {
        //        if(SPUtils.getData(Constants.UNREAD_SYSMSG_COUNT, 0)
        //                + SPUtils.getData(Constants.UNREAD_ORDERMSG_COUNT, 0) > 0){
        //            mPages.showUnreadIcon(0 , true);
        //        }else {
        //            mPages.showUnreadIcon(0, false);
        //        }
        //        IMyOrderInfoMessageView iAccountFragment= ((AccountFragment)FragmentFactory.getFragment(BaseFragment.ACCOUNT));
        //        MyOrderInfoMessagePresenter accountFragmentPresenter = new MyOrderInfoMessagePresenter(iAccountFragment);
        //        accountFragmentPresenter.getMyOrderInfoMessage();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        //        if (FragmentFactory.getFragment(BaseFragment.MAP).getUserVisibleHint()
        //                && ((MapFragment) FragmentFactory.getFragment(BaseFragment.MAP)).isOptionExpanded()) {
        //            ((MapFragment) FragmentFactory.getFragment(BaseFragment.MAP)).hideMore();
        //            return;
        //        }
        super.onBackPressed();
    }
}
