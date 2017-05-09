package com.miuhouse.yourcompany.student.view.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.listener.OnGridItemClick;
import com.miuhouse.yourcompany.student.listener.OnListItemClick;
import com.miuhouse.yourcompany.student.view.widget.RatioImageView;

import java.util.List;

/**
 * Created by kings on 8/26/2016.
 */
public class TeacherPhotoAlbumAdapter extends RecyclerView.Adapter<TeacherPhotoAlbumAdapter.ViewHolder> {

    private OnGridItemClick mOnGridItemClick;
    private List<String> mLists;
    private Context mContext;

    public TeacherPhotoAlbumAdapter(Context context, List<String> mLists) {
        mContext = context;
        this.mLists = mLists;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.grid_item_teacher_photo, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String urlPath = mLists.get(position);
        holder.urlPath = urlPath;
        Glide.with(mContext).load(urlPath).centerCrop().into(holder.ratioImageView);
    }

    public void setmOnListItemClick(OnGridItemClick mOnGridItemClick) {
        this.mOnGridItemClick = mOnGridItemClick;
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RatioImageView ratioImageView;
        private String urlPath;

        public ViewHolder(View itemView) {
            super(itemView);
            ratioImageView = (RatioImageView) itemView.findViewById(R.id.img_photo_album);
            ratioImageView.setOnClickListener(this);
            ratioImageView.setOriginalSize(50, 50);
        }

        @Override
        public void onClick(View v) {

            mOnGridItemClick.onItemClick(urlPath, v);
        }
    }
}
