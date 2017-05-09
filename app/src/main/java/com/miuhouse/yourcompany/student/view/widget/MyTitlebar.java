package com.miuhouse.yourcompany.student.view.widget;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.miuhouse.yourcompany.student.R;


/**
 * 自定义标题栏
 * Created by khb on 2016/6/30.
 */
public class MyTitlebar extends FrameLayout {

    private Context context;
    private TextView title;
    private TextView right;
    private Toolbar titlebar;
    private ImageView back;

    public MyTitlebar(Context context) {
        this(context, null);
    }

    public MyTitlebar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public MyTitlebar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_mytitlebar, null);
        titlebar = (Toolbar) view.findViewById(R.id.titlebar);
        title = (TextView) view.findViewById(R.id.title);
        right = (TextView) view.findViewById(R.id.right);
        back = (ImageView) view.findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnMyTitlebarClickListener != null) {
                    mOnMyTitlebarClickListener.onBackClick();
                }
            }
        });
        right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnMyTitlebarClickListener != null) {
                    mOnMyTitlebarClickListener.onRightClick();
                }
            }
        });
        title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnMyTitlebarClickListener != null) {
                    mOnMyTitlebarClickListener.onBackClick();
                }
            }
        });
        addView(view);
    }

    /**
     * 设置标题栏标题
     *
     * @param titleText
     */
    public void setTitle(String titleText) {
        title.setText(titleText);
    }

    /**
     * 设置右键文本，如果为null则不显示右键
     *
     * @param text
     */
    public void setRightButtonText(String text) {
        if (TextUtils.isEmpty(text)) {
            right.setVisibility(GONE);
            return;
        }
        right.setText(text);
    }

    /**
     * 设置titlebar背景颜色
     *
     * @param resource
     */
    public void setTitlebarBackground(int resource) {
        titlebar.setBackgroundColor(resource);
    }

    /**
     * 设置back按钮背景图片
     *
     * @param resource
     */
    public void setBackImageResource(int resource) {
        back.setImageResource(resource);
    }

    /**
     * 设置title的文字颜色
     *
     * @param resource
     */
    public void setTitleTextColor(int resource) {
        title.setTextColor(resource);
    }

    public interface OnMyTitlebarClickListener {
        void onBackClick();

        void onRightClick();
    }

    private OnMyTitlebarClickListener mOnMyTitlebarClickListener;

    public void setOnMyTitlebarClickListener(OnMyTitlebarClickListener onMyTitlebarClickListener) {
        this.mOnMyTitlebarClickListener = onMyTitlebarClickListener;
    }

}
