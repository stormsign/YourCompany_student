package com.miuhouse.yourcompany.student.factory;

import android.support.v4.app.Fragment;

import com.miuhouse.yourcompany.student.view.ui.base.BaseFragment;
import com.miuhouse.yourcompany.student.view.ui.fragment.AccountFragment;
import com.miuhouse.yourcompany.student.view.ui.fragment.AccountFragment2;
import com.miuhouse.yourcompany.student.view.ui.fragment.FragmentA;
import com.miuhouse.yourcompany.student.view.ui.fragment.FragmentB;
import com.miuhouse.yourcompany.student.view.ui.fragment.FragmentC;
import com.miuhouse.yourcompany.student.view.ui.fragment.FragmentD;
import com.miuhouse.yourcompany.student.view.ui.fragment.DongTaiFragment;
import com.miuhouse.yourcompany.student.view.ui.fragment.KzFragment;
import com.miuhouse.yourcompany.student.view.ui.fragment.MapFragment;
import com.miuhouse.yourcompany.student.view.ui.fragment.MessagesFragment;

import java.util.HashMap;
import java.util.Map;

//import com.miuhouse.yourcompany.student.view.ui.fragment.FragmentA;
//import com.miuhouse.yourcompany.student.view.ui.fragment.FragmentB;
//import com.miuhouse.yourcompany.student.view.ui.fragment.FragmentC;
//import com.miuhouse.yourcompany.student.view.ui.fragment.FragmentD;
//import com.miuhouse.yourcompany.student.view.ui.fragment.MyOrdersFragment;
//import com.miuhouse.yourcompany.student.view.ui.fragment.OrdersSquareFragment;

/**
 * Created by khb on 2016/7/6.
 */
public class FragmentFactory {

    private static Map<Integer, Fragment> map = new HashMap<>();

    public static Fragment getFragment(int position){
        BaseFragment baseFragment = null;
        if (map.containsKey(position)){
            if (map.get(position)!=null){
                baseFragment = (BaseFragment) map.get(position);
            }
        }else {
            switch (position){
                case BaseFragment.DONGTAI:
                    baseFragment = new DongTaiFragment();
                    break;
                case BaseFragment.KZ:
                    baseFragment = new KzFragment();
                    break;
                case BaseFragment.ACCOUNT2:
                    baseFragment = new AccountFragment2();
                    break;
                case BaseFragment.MESSAGES:
                    baseFragment = new MessagesFragment();
                    break;
                case BaseFragment.MAP:
                    baseFragment = new MapFragment();
                    break;
                case BaseFragment.ACCOUNT:
                    baseFragment = new AccountFragment();
                    break;
                case BaseFragment.A:
                        baseFragment = new FragmentA();
                        break;
                case BaseFragment.B:
                        baseFragment = new FragmentB();
                        break;
                case BaseFragment.C:
                        baseFragment = new FragmentC();
                        break;
                case BaseFragment.D:
                        baseFragment = new FragmentD();
                        break;
            }
            map.put(position, baseFragment);
        }
        return baseFragment;

    }
}
