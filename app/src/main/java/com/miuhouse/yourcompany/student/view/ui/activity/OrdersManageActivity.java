package com.miuhouse.yourcompany.student.view.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.factory.FragmentFactory;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IOrdersManageActivity;
import com.miuhouse.yourcompany.student.view.ui.adapter.MainPageAdapter;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;
import com.miuhouse.yourcompany.student.view.ui.base.BaseFragment;
import com.miuhouse.yourcompany.student.view.widget.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khb on 2016/7/18.
 */
public class OrdersManageActivity extends BaseActivity implements IOrdersManageActivity {

    private ViewPager orderpager;
    private ViewPagerIndicator orderIndicator;
    private int index;

    @Override
    protected String setTitle() {
        return "订单管理";
    }

    @Override
    protected String setRight() {
        return null;
    }

    @Override
    protected void initViewAndEvents() {
        index = getIntent().getIntExtra("index", 0);
        List<String> textList = new ArrayList<>();
        textList.add("全部");
        textList.add("待付款");
        textList.add("待上课");
        textList.add("待评价");
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(FragmentFactory.getFragment(BaseFragment.A));
        fragmentList.add(FragmentFactory.getFragment(BaseFragment.B));
        fragmentList.add(FragmentFactory.getFragment(BaseFragment.C));
        fragmentList.add(FragmentFactory.getFragment(BaseFragment.D));
        orderIndicator = (ViewPagerIndicator) findViewById(R.id.orderIndicator);
        orderpager = (ViewPager) findViewById(R.id.orderpager);
        orderpager.setAdapter(new MainPageAdapter(getSupportFragmentManager(), fragmentList));
        orderIndicator.setTabItemTitles(textList);
        orderIndicator.setViewPager(orderpager, index);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_ordersmanage;
    }

    @Override
    protected View getOverrideParentView() {
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//        if (requestCode == 0){
//
//        }else
        if (resultCode == 1){
//            FragmentFactory.getFragment(BaseFragment.B).onResume();
//            FragmentFactory.getFragment(BaseFragment.C).onResume();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    orderIndicator.setViewPager(orderpager, 2);
                }
            });

        }
    }
}
