package com.miuhouse.yourcompany.student.view.ui.activity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.presenter.OrderCommentPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.IOrderCommentPresenter;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IOrderEvaluateView;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kings on 8/31/2016.
 */
public class OrderCommentActivity extends BaseActivity implements IOrderEvaluateView {

    private LinearLayout linearCommentGrade;
    private ImageView imageView[] = new ImageView[5];
    private EditText editCommentContent;
    private TextView tvName;
    private TextView tvType;
    private TextView tvDate;
    private ImageView imgAvatar;

    private IOrderCommentPresenter orderCommentPresenter;

    private static final String TAG = "OrderCommentActivity";
    /**
     * 评价星级  数字 1-5
     */
    private String evaluateRank;
    private Button btnCommit;


    @Override
    protected String setTitle() {
        return "评价";
    }

    @Override
    protected String setRight() {
        return null;
    }

    @Override
    protected void initViewAndEvents() {

        String tName = getIntent().getStringExtra("tname");
        String tHeader = getIntent().getStringExtra("tHeader");
        String majorDemand = getIntent().getStringExtra("majorDemand");
        long classBeginTimePromise = getIntent().getLongExtra("classBeginTimePromise", 0);
        final String orderInfoId = getIntent().getStringExtra("orderInfoId");
        linearCommentGrade = (LinearLayout) findViewById(R.id.linear_comment_grade);
        editCommentContent = (EditText) findViewById(R.id.edit_comment);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvType = (TextView) findViewById(R.id.tv_type);
        tvDate = (TextView) findViewById(R.id.tv_date);
        btnCommit = (Button) findViewById(R.id.btn_commit);
        imgAvatar = (ImageView) findViewById(R.id.img_avatar);

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit(orderInfoId);
            }
        });

        initCommentGrade();
        fillView(tName, tHeader, majorDemand, classBeginTimePromise);
        orderCommentPresenter = new OrderCommentPresenter(this);

    }

    private void fillView(String name, String tHeader, String majorDemed, long classBeginTimePromise) {
        tvName.setText(name);
        tvType.setText(Util.getResourceId(this, String.format("order_type_%s", majorDemed), "string"));
        tvDate.setText(new SimpleDateFormat("课程安排 MM/dd HH:mm").format(new Date(classBeginTimePromise)));
        if (Util.isEmpty(tHeader)) {
            Glide.with(this).load(R.mipmap.ico_head_default).override(Util.dip2px(this, 55), Util.dip2px(this, 55)).centerCrop().into(imgAvatar);
        } else {
            Glide.with(this).load(tHeader).override(Util.dip2px(this, 55), Util.dip2px(this, 55)).error(R.mipmap.ico_head_default).centerCrop().into(imgAvatar);
        }
    }

    private void initCommentGrade() {
        for (int i = 0; i < linearCommentGrade.getChildCount(); i++) {
            imageView[i] = (ImageView) linearCommentGrade.getChildAt(i);
            imageView[i].setTag(i);
            imageView[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnCommit.setEnabled(true);
                    int index = (int) v.getTag();
                    evaluateRank = String.valueOf(index + 1);
                    handlerCommentGrade(index);
                }
            });
        }
    }

    private void handlerCommentGrade(int index) {
        for (int i = 0; i < imageView.length; i++) {
            if (i <= index) {
                imageView[i].setImageResource(R.mipmap.ico_star_org);
            } else {
                imageView[i].setImageResource(R.mipmap.ico_star_gray);
            }
        }

    }

    //未完待续，订单id还不知道
    private void commit(String orderInfoId) {
        String evaluateContent = editCommentContent.getText().toString();
        orderCommentPresenter.commitOrderComment(orderInfoId, evaluateRank, evaluateContent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_submit_comment;
    }

    @Override
    protected View getOverrideParentView() {
        return null;
    }

    @Override
    public void showLoading(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        finish();
    }
}
