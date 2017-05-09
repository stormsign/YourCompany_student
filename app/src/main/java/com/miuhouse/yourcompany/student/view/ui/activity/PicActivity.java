package com.miuhouse.yourcompany.student.view.ui.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by kings on 8/26/2016.
 */
public class PicActivity extends AppCompatActivity {
    public static final String EXTRA_IMAGE_URL = "image_url";
    public static final String TRANSIT_PIC = "picture";

    PhotoViewAttacher mPhotoViewAttacher;
    String mImageUrl;

    private ImageView picView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mImageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
        setContentView(R.layout.activity_pic);

        picView = (ImageView) findViewById(R.id.picture);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading);

        ViewCompat.setTransitionName(picView, TRANSIT_PIC);
        Glide.with(this).load(mImageUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                picView.setImageBitmap(resource);
                mProgressBar.setVisibility(View.GONE);
                picView.setVisibility(View.VISIBLE);
            }
        });
        mPhotoViewAttacher = new PhotoViewAttacher(picView);
        picView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
