package com.miuhouse.yourcompany.student.view.ui.activity;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.bumptech.glide.Glide;
import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.listener.OnGridItemClick;
import com.miuhouse.yourcompany.student.listener.OnListItemClick;
import com.miuhouse.yourcompany.student.view.ui.adapter.TeacherPhotoAlbumAdapter;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kings on 8/26/2016.
 */
public class PhotoAlbumActivity extends BaseActivity implements OnGridItemClick {

    private RecyclerView mRecyclerView;

    private List<String> photoLists = new ArrayList<>();
    private TeacherPhotoAlbumAdapter teacherPhotoAlbumAdapter;

    @Override
    protected String setTitle() {
        return "相册";
    }

    @Override
    protected String setRight() {
        return null;
    }

    @Override
    protected void initViewAndEvents() {
        photoLists.addAll(getIntent().getStringArrayListExtra("imageUrls"));
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_teacher_photo_album);
        final StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        teacherPhotoAlbumAdapter = new TeacherPhotoAlbumAdapter(this, photoLists);
        mRecyclerView.setAdapter(teacherPhotoAlbumAdapter);
        teacherPhotoAlbumAdapter.setmOnListItemClick(this);

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_teacher_photo_album;
    }

    @Override
    protected View getOverrideParentView() {
        return null;
    }

    @Override
    public void onItemClick(String urlPaht, View view) {
        startPictureActivity(urlPaht, view);
    }

    private void startPictureActivity(String url, View transitView) {
        Intent i = new Intent(this, PicActivity.class);
        i.putExtra(PicActivity.EXTRA_IMAGE_URL, url);

        ActivityOptionsCompat optionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, transitView,
                        PicActivity.TRANSIT_PIC);
        ActivityCompat.startActivity(this, i, optionsCompat.toBundle());
    }
}
