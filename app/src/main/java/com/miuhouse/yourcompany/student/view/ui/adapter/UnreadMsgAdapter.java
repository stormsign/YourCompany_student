package com.miuhouse.yourcompany.student.view.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.listener.OnListItemClick;
import com.miuhouse.yourcompany.student.model.CommentsBean;
import com.miuhouse.yourcompany.student.view.ui.base.BaseRVAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by khb on 2017/1/17.
 */
public class UnreadMsgAdapter extends BaseRVAdapter {

    private CommentsBean comment;

    public UnreadMsgAdapter(Context context, List list, OnListItemClick onListItemClick) {
        super(context, list, onListItemClick);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_unreadmsg, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView type;
        TextView content;
        TextView time;
        LinearLayout container;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.commenterName);
            type = (TextView) itemView.findViewById(R.id.commentType);
            content = (TextView) itemView.findViewById(R.id.commentContent);
            time = (TextView) itemView.findViewById(R.id.time);
            container = (LinearLayout) itemView.findViewById(R.id.container);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        comment = (CommentsBean) list.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.name.setText(comment.getLinkName());
        if (comment.getCommentType().equals("1")){  //提示有人评论
            viewHolder.type.setText(context.getResources().getString(R.string.be_commented));
            viewHolder.content.setText(comment.getComment());
            viewHolder.content.setVisibility(View.VISIBLE);
        }else if (comment.getCommentType().equals("2")){
            viewHolder.type.setText(context.getResources().getString(R.string.be_liked));
            viewHolder.content.setVisibility(View.GONE);
        }
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH:mm");
        viewHolder.time.setText(format.format(new Date(comment.getCommentTime())));
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onListItemClick!=null){
                    onListItemClick.onItemClick(list.get(position));
                }
            }
        });
    }
}
