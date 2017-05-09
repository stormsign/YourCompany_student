package com.miuhouse.yourcompany.student.view.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.miuhouse.yourcompany.student.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by khb on 2017/1/20.
 */
public class SplashActivity extends AppCompatActivity {

    private ViewPager pager;
    private TimerTask task;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        final List<View> viewList = new ArrayList<>();
        pager = (ViewPager) findViewById(R.id.splashPager);
        ImageView loadingView = new ImageView(this);
        loadingView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(this).load(R.mipmap.splash_loading).into(loadingView);
//        if (SPUtils.getData(SPUtils.FIRST_START, true)){
//            SPUtils.saveData(SPUtils.FIRST_START, false);
//            ImageView splash1 = new ImageView(this);
//            Glide.with(this).load(R.mipmap.splash_jz_1).centerCrop().into(splash1);
//            ImageView splash2 = new ImageView(this);
//            Glide.with(this).load(R.mipmap.splash_2).centerCrop().into(splash2);
//            viewList.add(splash1);
//            viewList.add(splash2);
//            viewList.add(loadingView);
//            pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//                boolean toMain = false;
//
//                @Override
//                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                    if (toMain) {
//                        startActivity(new Intent(SplashActivity.this, LoginRegistActivity.class));
//                        finish();
//                    }
//                    if (position == viewList.size() - 1) {
//                        toMain = true;
//                    }
//                }
//
//                @Override
//                public void onPageSelected(int position) {
//                }
//
//                @Override
//                public void onPageScrollStateChanged(int state) {
//                }
//            });
//        }else {
            viewList.add(loadingView);
            task = new TimerTask() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            };
            timer = new Timer();
            timer.schedule(task, 2000);
//        }
        SplashAdapter adapter = new SplashAdapter(viewList);
        pager.setAdapter(adapter);
    }



    class SplashAdapter extends PagerAdapter {

        private List<View> viewList;

        public SplashAdapter(List<View> viewList){
            this.viewList = viewList;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
        if (task != null) {
            task.cancel();
        }
        super.onDestroy();
    }
}
