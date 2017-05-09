package com.miuhouse.yourcompany.student.view.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by khb on 2016/7/6.
 */
public class MainPageAdapter extends FragmentPagerAdapter {

    private Object context;
    private List<Fragment> fragmentList;
    private FragmentManager fm;

    public MainPageAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fm = fm;
        this.fragmentList = fragmentList;
    }


    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

}
