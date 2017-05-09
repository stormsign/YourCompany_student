package com.miuhouse.yourcompany.student.view.ui.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.miuhouse.yourcompany.student.listener.OnListItemClick;

import com.miuhouse.yourcompany.student.listener.OnListItemLongClick;
import java.util.List;

/**
 * Created by khb on 2016/7/7.
 */
public abstract class BaseRVAdapter<T> extends RecyclerView.Adapter {

    public List<T> list;
    public Context context;
    public OnListItemClick onListItemClick;
    public OnListItemLongClick onListItemLongClick;


    public BaseRVAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
    }

    public BaseRVAdapter(Context context, List<T> list, OnListItemClick onListItemClick) {
        this.context = context;
        this.list = list;
        this.onListItemClick = onListItemClick;
    }


    public BaseRVAdapter(Context context, List<T> list, OnListItemClick onListItemClick,
        OnListItemLongClick onListItemLongClick) {
        this(context, list);
        this.onListItemClick = onListItemClick;
        this.onListItemLongClick = onListItemLongClick;
    }

    public final void addAll(List<T> lists) {
        if (lists != null) {

            this.list.addAll(lists);
            notifyItemRangeInserted(this.list.size(), lists.size());
//            notifyDataSetChanged();

        }
    }

    public final void resetData(List<T> lists) {
        if (lists != null) {
            clear();
            addAll(lists);
        }
    }

    public final void clear() {
        this.list.clear();
        notifyDataSetChanged();
    }

    @Override
    public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
