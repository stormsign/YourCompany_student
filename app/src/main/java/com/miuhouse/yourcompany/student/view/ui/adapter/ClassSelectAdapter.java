package com.miuhouse.yourcompany.student.view.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.model.ClassEntity;

import java.util.List;

/**
 * Created by khb on 2017/1/3.
 */
public class ClassSelectAdapter extends RecyclerView.Adapter {

  private Context context;
  private List<ClassEntity> list;

  public ClassSelectAdapter(Context context, List<ClassEntity> list) {
    this.context = context;
    list.add(0, new ClassEntity("午托班", 0));
    list.add(1, new ClassEntity("老师圈", -1));
    this.list = list;
  }

  public void setmClassSelectListener(ClassSelectListener listener) {
    mClassSelectListener = listener;
  }

  public void addData(List<ClassEntity> datas) {
    list.addAll(datas);
    notifyDataSetChanged();
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new Holder(LayoutInflater.from(context).inflate(R.layout.item_classselect, null));
  }

  class Holder extends RecyclerView.ViewHolder {

    TextView className;

    public Holder(View itemView) {
      super(itemView);
      className = (TextView) itemView.findViewById(R.id.className);
    }
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
    Holder mholder = (Holder) holder;
    final ClassEntity classEntity = list.get(position);
    mholder.className.setText(classEntity.getName());
    mholder.className.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mClassSelectListener != null) {
          mClassSelectListener.onClassSelect(classEntity);
        }
      }
    });
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  public ClassSelectListener mClassSelectListener;

  public interface ClassSelectListener {
    void onClassSelect(ClassEntity classEntity);
  }
}
