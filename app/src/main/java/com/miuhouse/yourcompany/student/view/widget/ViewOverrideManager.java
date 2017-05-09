package com.miuhouse.yourcompany.student.view.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.utils.L;

/**
 * Created by khb on 2016/6/7.
 */
public class ViewOverrideManager implements ValueAnimator.AnimatorUpdateListener {
    private View parentView;
    private ViewGroup.LayoutParams layoutParams;
    private ViewGroup container;
    private int viewIndex;

    public static final int LOADING = 0;
    public static final int NO_MSG = 1;
    public static final int NO_COMMENT = 2;
    public static final int NO_TEACHER = 3;
    public static final int NO_ORDER = 4;
    public static final int NO_NETWORK = 5;
    public static final int NO_ADDRESS = 6;
    public static final int NO_CONTENT = 7;
    public static final int NO_PERSONAL_CONTENT = 8;

    private View view;
    private TextView tvMsg;
    private ImageView img;
    private TextView button;
    //    private ImageView shadow;

    private boolean isBgTransparent = false;

    public ViewOverrideManager(View view) {
        this.parentView = view;
        if (parentView != null) {
            this.view =
                LayoutInflater.from(parentView.getContext()).inflate(R.layout.view_override, null);
        }
        //        init();
    }

    private void init() {
        if (null == parentView) {
            return;
        }
        layoutParams = parentView.getLayoutParams();
        if (null != parentView.getParent()) {
            container = (ViewGroup) parentView.getParent();
        } else {
            ViewGroup root = (ViewGroup) parentView.getRootView();
            container = (ViewGroup) root.findViewById(android.R.id.content);
        }
        if (null == container) {
            //            container = (ViewGroup) parentView;
            L.i("内部对象为空");
            return;
        }
        for (int i = 0; i < container.getChildCount(); i++) {
            if (parentView == container.getChildAt(i)) {
                viewIndex = i;
                break;
            }
        }
    }

    private void showLayout(View view) {
        if (null == container) {
            init();
        }
        if (null == container) {
            //            container = (ViewGroup) parentView.getRootView();
            return;
        }
        if (container.getChildAt(viewIndex) != view) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            container.removeViewAt(viewIndex);
            container.addView(view, viewIndex, layoutParams);
        }
    }

    //    private void setBgTransparent(boolean isBgTransparent){
    //        this.isBgTransparent = isBgTransparent;
    //        if (isBgTransparent){
    //            view.setBackgroundResource(android.R.color.transparent);
    //        }else {
    ////            view.setBackgroundResource();
    //        }
    //    }

    public void showLoading() {
        showLoading(LOADING, null);
    }

    //    public void showLoadingTransparent(){
    //        showLoading(LOADING, null);
    //    }

    public void showLoading(String msg) {
        showLoading(msg, false);
    }

    public void showLoading(int type, final OnExceptionalClick onExceptionalClick) {
        showLoading(null, false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onExceptionalClick) {
                    restoreView();
                    onExceptionalClick.onExceptionalClick();
                }
            }
        });
        Context context = view.getContext();
        isContinue = false;
        tvMsg.setVisibility(View.VISIBLE);
        switch (type) {
            case LOADING:
                isContinue = true;
                view.setBackgroundResource(R.color.ebpay_transparent);
                tvMsg.setVisibility(View.GONE);
                animateLoading();
                break;
            case NO_MSG:
                view.setBackgroundResource(R.color.backgroundcolor_common);
                img.setImageResource(R.mipmap.img_no_msg);
                tvMsg.setText(context.getResources().getString(R.string.exception_no_msg));
                break;
            case NO_COMMENT:
                view.setBackgroundResource(R.color.backgroundcolor_common);
                img.setImageResource(R.mipmap.img_no_comment);
                tvMsg.setText(context.getResources().getString(R.string.exception_no_comment));
                break;
            case NO_TEACHER:
                view.setBackgroundResource(R.color.backgroundcolor_common);
                img.setImageResource(R.mipmap.img_no_address);
                //                tvMsg.setText(context.getResources().getString(R.string.exception_no_teacher));
                setTextView(tvMsg, context.getResources().getString(R.string.exception_no_teacher));
                button.setText("刷新");
                button.setVisibility(View.VISIBLE);
                break;
            case NO_ORDER:
                view.setBackgroundResource(R.color.backgroundcolor_common);
                img.setImageResource(R.mipmap.img_no_order);
                //                tvMsg.setText(context.getResources().getString(R.string.exception_no_order));
                setTextView(tvMsg, context.getResources().getString(R.string.exception_no_order));
                button.setText("立即前往");
                button.setVisibility(View.VISIBLE);
                break;
            case NO_NETWORK:
                view.setBackgroundResource(R.color.backgroundcolor_common);
                img.setImageResource(R.mipmap.img_no_network);
                tvMsg.setText(context.getResources().getString(R.string.exception_no_network));
                button.setText("重试");
                button.setVisibility(View.VISIBLE);
                break;
            case NO_ADDRESS:
                view.setBackgroundResource(R.color.backgroundcolor_common);
                img.setImageResource(R.mipmap.img_no_address);
                //                tvMsg.setText(context.getResources().getString(R.string.exception_no_address));
                setTextView(tvMsg, context.getResources().getString(R.string.exception_no_address));
                //                button.setText("添加新位置");
                //                button.setVisibility(View.VISIBLE);
                break;
            case NO_CONTENT:
                view.setBackgroundResource(R.color.backgroundcolor_common);
                img.setImageResource(R.drawable.prompt_no_content);
                tvMsg.setText("还没有内容");
                button.setText("我要发布");
                button.setVisibility(View.VISIBLE);
                break;
            case NO_PERSONAL_CONTENT:
                view.setBackgroundResource(R.color.backgroundcolor_common);
                img.setImageResource(R.drawable.pnrompt_no_content);
                setTextView(tvMsg,
                    context.getResources().getString(R.string.exception_no_personal_content));
                break;
            default:
                break;
        }
        if (onExceptionalClick == null) {
            button.setVisibility(View.GONE);
        }
    }

    public void showLoading(String msg, boolean isShowProgress) {
        //        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        img = (ImageView) view.findViewById(R.id.img);
        tvMsg = (TextView) view.findViewById(R.id.msg);
        button = (TextView) view.findViewById(R.id.button);
        //        shadow.setVisibility(View.GONE);

        if (msg != null) {
            tvMsg.setText(msg);
        }
        tvMsg.setTextColor(
            parentView.getContext().getResources().getColor(R.color.primary_material_dark));
        //        if (msg == null)
        showLayout(view);
    }

    private void setTextView(TextView tv, String text) {
        String[] splitedStr = text.split("\n");
        String title = splitedStr[0];
        String subTitle = splitedStr[1];
        tv.setText(Html.fromHtml("<font color='#706F6E'>" + title + "</font>"
            + "<br><font color='#B2B0AF'><small>" + subTitle + "</small></font></br>"));
    }

    public void restoreView() {
        showLayout(parentView);
    }

    private float startX;
    private float startY;
    private float width;
    private float height;
    private float shadowWidth;
    private float shadowHeight;

    private void animateLoading() {
        //        shadow.setVisibility(View.VISIBLE);
        //        img.setImageResource(R.mipmap.loading);
        //        RotateAnimation rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //        rotate.setFillAfter(true);
        //        rotate.setDuration(2000);
        //        rotate.setRepeatMode(Animation.RESTART);
        //        rotate.setRepeatCount(Animation.INFINITE);
        //        img.setAnimation(rotate);
        //        rotate.start();
    }

    private boolean isContinue = true;

    public void stopLoadingAnimation() {
        this.isContinue = false;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        img.invalidate();
        //        shadow.invalidate();
    }

    public interface OnExceptionalClick {
        void onExceptionalClick();
    }
}
