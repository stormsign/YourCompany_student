package com.miuhouse.yourcompany.student.view.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import mabeijianxi.camera.util.Log;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by kings on 11/17/2016.
 */
public class GalleryActivity extends AppCompatActivity
    implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private static ExecutorService EXECUTORS_INSTANCE;
    private ArrayList<String> urls = new ArrayList<>();
    private TextView tvIndex;
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_layout);

        //        getIntent().getStringArrayListExtra("urls");
        urls.addAll(getIntent().getStringArrayListExtra("urls"));
        currentIndex = getIntent().getIntExtra("position", 0);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        tvIndex = (TextView) findViewById(R.id.tv_index);
        ImageView imgSave = (ImageView) findViewById(R.id.img_save);

        imgSave.setOnClickListener(this);

        if (urls.size() == 1) {
            tvIndex.setVisibility(View.GONE);
        }

        if (currentIndex < 0 || currentIndex >= urls.size()) {
            currentIndex = 0;
        }

        pager.setAdapter(new ImagePagerAdapter());
        pager.setCurrentItem(currentIndex);
        pager.setOffscreenPageLimit(1);
        pager.addOnPageChangeListener(this);
        onPageSelected(currentIndex);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override public void onPageSelected(int position) {
        currentIndex = position;
        tvIndex.setText(String.format("%s/%s", position + 1, urls.size()));
    }

    @Override public void onPageScrollStateChanged(int state) {

    }

    @Override public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.img_save:
                saveImageToFile(urls.get(currentIndex));
                break;
        }
    }

    private class ImagePagerAdapter extends PagerAdapter {

        private LayoutInflater inflater;

        public ImagePagerAdapter() {
            inflater = getLayoutInflater();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (object instanceof ViewGroup) {
                ((ViewPager) container).removeView((View) object);
                ViewGroup viewGroup = (ViewGroup) object;
            }
        }

        @Override
        public int getCount() {
            return urls.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, final int position) {
            View contentView = inflater.inflate(R.layout.activity_pic, view, false);
            final ImageView picView = (ImageView) contentView.findViewById(R.id.picture);
            final ProgressBar mProgressBar =
                (ProgressBar) contentView.findViewById(R.id.pb_loading);
            //            handlePage(position, contentView, true);
            Glide.with(GalleryActivity.this)
                .load(urls.get(position))
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                        GlideAnimation<? super Bitmap> glideAnimation) {
                        picView.setImageBitmap(resource);
                        mProgressBar.setVisibility(View.GONE);
                        picView.setVisibility(View.VISIBLE);
                    }
                });
            ((ViewPager) view).addView(contentView, 0);
            //            unRecycledViews.add((ViewGroup) contentView);
            PhotoViewAttacher mPhotoViewAttacher = new PhotoViewAttacher(picView);
            mPhotoViewAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override public void onPhotoTap(View view, float v, float v1) {
                    GalleryActivity.this.finish();
                }
            });
            return contentView;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            View contentView = (View) object;
            if (contentView == null) {
                return;
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }
    }

    private void saveImageToFile(String path) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //Toast.makeText(this,"")
        }

        final Future<File> future =
            Glide.with(this).load(path).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);

        runOnThread(new Runnable() {
            @Override
            public void run() {
                try {
                    File sourceFile = future.get();
                    String extension = getExtension(sourceFile.getAbsolutePath());
                    String extDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).getAbsolutePath()
                        + File.separator
                        + "exuepei";
                    File extDirFile = new File(extDir);
                    if (!extDirFile.exists()) {
                        if (!extDirFile.mkdirs()) {
                            callSaveStatus(false, null);
                            return;
                        }
                    }
                    final File saveFile = new File(extDirFile,
                        String.format("img_%s.%s", System.currentTimeMillis(), extension));
                    final boolean isSuccess = Util.copyFile(sourceFile, saveFile);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callSaveStatus(isSuccess, saveFile);
                        }
                    });
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ExecutorService getExecutor() {

        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() > 0 ?
            Runtime.getRuntime().availableProcessors() : 2);
    }

    public void runOnThread(Runnable runnable) {
        getExecutor().execute(runnable);
    }

    public String getExtension(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        String mimeType = options.outMimeType;
        return mimeType.substring(mimeType.lastIndexOf("/") + 1);
    }

    private void callSaveStatus(boolean success, File savePath) {
        if (success) {
            Uri uri = Uri.fromFile(savePath);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            Toast.makeText(GalleryActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(GalleryActivity.this, "保存失败", Toast.LENGTH_LONG).show();
        }
    }
}
