package com.miuhouse.yourcompany.student.view.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.model.EvaluateBean;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.utils.Values;
import com.miuhouse.yourcompany.student.view.ui.base.BaseRVAdapter;
import com.miuhouse.yourcompany.student.view.widget.CircularImageViewHome;

import java.util.List;

/**
 * Created by kings on 8/23/2016.
 */
public class CommentAdapter extends BaseRVAdapter<EvaluateBean> {

    public CommentAdapter(Context mContext, List<EvaluateBean> list) {
        super(mContext, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_comment, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder mHolder = (ViewHolder) holder;
        EvaluateBean evaluateBean = list.get(position);

        mHolder.tvName.setText(getFormatPName(evaluateBean.getParentName()));
        mHolder.tvContent.setText(evaluateBean.getEvaluateContent());
        if (evaluateBean.getParentHeadUrl() != null) {
            Glide.with(context).load(evaluateBean.getParentHeadUrl()).override(Util.dip2px(context, 45), Util.dip2px(context, 45)).centerCrop().into(mHolder.imgAvatar);
        }
        mHolder.tvDate.setText(Util.formatTime(evaluateBean.getEvaluateTime()));
        mHolder.tvType.setText(Values.getValue(Values.majorDemand, Integer.parseInt(evaluateBean.getMajorDemand()))
                + " " + getOrderTypeStr(evaluateBean.getOrderType()));
        for (int i = 0; i < 5; i++) {
            if (i < Integer.parseInt(evaluateBean.getEvaluateRank())) {
                mHolder.imgRank[i].setImageResource(R.mipmap.lszl_ico_starorg);
            } else {
                mHolder.imgRank[i].setImageResource(R.mipmap.lszlico_stargray);
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CircularImageViewHome imgAvatar;
        private TextView tvName;
        private TextView tvDate;
        private TextView tvContent;
        private TextView tvType;
        private ImageView imgRank[] = new ImageView[5];

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            tvType = (TextView) itemView.findViewById(R.id.tv_type);
            imgAvatar = (CircularImageViewHome) itemView.findViewById(R.id.img_avatar);
            for (int i = 0; i < 5; i++) {
                if (i == 0)
                    imgRank[i] = (ImageView) itemView.findViewById(R.id.img_rank);
                else
                    imgRank[i] = (ImageView) itemView.findViewById(R.id.img_rank + i);

            }
        }
    }

    /**
     * @param orderType
     * @return
     */
    private String getOrderTypeStr(String orderType) {
        String result;
        if (orderType.equals("1")) {
            result = "短单";
        } else if (orderType.equals("2")) {
            result = "包周";
        } else {
            result = "包月";
        }
        return result;
    }

    private String getFormatPName(String pName) {
        String result;
        if (pName == null) {
            result = "**";
        } else if (pName.length() > 1) {
            result = pName.replace(pName.substring(1, pName.length()), "**");
        } else {
            result = pName;
        }
        return result;
    }

}
