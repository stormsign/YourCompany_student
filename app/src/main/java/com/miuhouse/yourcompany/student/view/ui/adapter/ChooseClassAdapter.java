package com.miuhouse.yourcompany.student.view.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.listener.OnListItemClick;
import com.miuhouse.yourcompany.student.model.ClassEntity;
import com.miuhouse.yourcompany.student.view.ui.base.BaseRVAdapter;

import java.util.List;

/**
 * Created by khb on 2017/1/10.
 */
public class ChooseClassAdapter extends BaseRVAdapter {

    public ChooseClassAdapter(Context context, List list, OnListItemClick onListItemClick) {
        super(context, list, onListItemClick);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_chooseclass, parent, false));
    }

    class Holder extends RecyclerView.ViewHolder{

        ImageView selector;
        TextView className;
        LinearLayout content;

        public Holder(View itemView) {
            super(itemView);
            selector = (ImageView) itemView.findViewById(R.id.fuxuan);
            content = (LinearLayout) itemView.findViewById(R.id.content);
            className = (TextView) itemView.findViewById(R.id.className);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Holder mholder = (Holder) holder;
        final ClassEntity classEntity = (ClassEntity) list.get(position);
        mholder.selector.setSelected(classEntity.isSelected());
        mholder.className.setText(classEntity.getName());
        mholder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListItemClick.onItemClick(classEntity);
                classEntity.setSelected(!classEntity.isSelected());
                mholder.selector.setSelected(classEntity.isSelected());

//                if (position == 0) {
//                    for (int i=0; i<list.size(); i++){
//                        ((ClassEntity)list.get(i)).setSelected(!classEntity.isSelected());
//                    }
//                    notifyDataSetChanged();
//                }else {
//                    ((ClassEntity)list.get(0)).setSelected(false);
//                }
            }
        });
    }
}
