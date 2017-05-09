package com.miuhouse.yourcompany.student.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.miuhouse.yourcompany.student.R;

/**
 * Created by khb on 2016/8/31.
 */
public class MarkView extends FrameLayout {
    private Context context;
    private CircularImageViewHome icon;

    public MarkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    public MarkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public MarkView(Context context) {
        this(context, null);
    }

    private void initView() {
        View view = View.inflate(context, R.layout.layout_mapmarker, null);
        icon = (CircularImageViewHome) view.findViewById(R.id.icon);
        addView(view);
    }

    public void setIcon(String imgUrl) {
        Glide.with(context).load(imgUrl)
                .placeholder(R.mipmap.ico_head_default)
                .error(R.mipmap.ico_head_default)
                .into(icon);
    }

}
