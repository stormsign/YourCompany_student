package com.miuhouse.yourcompany.student.view.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.db.DictDBTask;
import com.miuhouse.yourcompany.student.model.TeacherInfoBean;
import com.miuhouse.yourcompany.student.utils.Values;
import com.miuhouse.yourcompany.student.view.ui.base.BaseRVAdapter;
import com.miuhouse.yourcompany.student.view.widget.CircularImageViewHome;

import java.util.List;

/**
 * Created by khb on 2016/9/18.
 */
public class TeachersAdapter extends BaseRVAdapter{
    public TeachersAdapter(Context context, List list, TeachersItemOnclickListener onListItemClick) {
        super(context, list);
        this.listener = onListItemClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TeachersHolder(LayoutInflater.from(context).inflate(R.layout.item_teacher, null));
    }

    class TeachersHolder extends RecyclerView.ViewHolder{
        CircularImageViewHome tHeader;
        TextView tName;
        TextView tDesc;
        TextView tEducation;
        TextView tGrade;
        TextView tSchool;
        LinearLayout content;
        TextView select;
        ImageView tGender;
        public TeachersHolder(View view){
            super(view);
            tHeader = (CircularImageViewHome) view.findViewById(R.id.teacherHeader);
            tName = (TextView) view.findViewById(R.id.tName);
            tDesc = (TextView) view.findViewById(R.id.desc);
            tEducation = (TextView) view.findViewById(R.id.education);
            tGrade = (TextView) view.findViewById(R.id.grade);
            tSchool = (TextView) view.findViewById(R.id.school);
            content = (LinearLayout) view.findViewById(R.id.content);
            select = (TextView) view.findViewById(R.id.select);
            tGender = (ImageView) view.findViewById(R.id.gender);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        TeachersHolder mholder = (TeachersHolder) holder;
        final TeacherInfoBean teacher = (TeacherInfoBean) list.get(position);
//        final TeacherInfoBean teacher = teacherDetail.getTeacherInfo();
        mholder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onItemClick(teacher, position);
                }
            }
        });
        mholder.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onButtonClick(teacher, position);
                }
            }
        });
        Glide.with(context).load(teacher.getHeadUrl())
        .placeholder(R.mipmap.ico_head_default)
        .error(R.mipmap.ico_head_default)
        .into(mholder.tHeader);
        mholder.tName.setText(teacher.gettName());
        setTeacherInfo(teacher, mholder);
        if (!TextUtils.isEmpty(teacher.getIntroduction())){
            mholder.tDesc.setVisibility(View.VISIBLE);
            mholder.tDesc.setText(teacher.getIntroduction());
        }else {
            mholder.tDesc.setVisibility(View.GONE);
        }
        setSex(mholder.tGender, teacher.getSex());
    }

    private void setSex(ImageView tGender, String sex) {
        if (sex.equals("男")){
            tGender.setImageResource(R.mipmap.gender);
        }else {
            tGender.setImageResource(R.mipmap.gender_woman);
        }
    }

    private void setTeacherInfo(TeacherInfoBean teacher, TeachersHolder holder){
        String grade = "";
        try {
            grade = Values.getValue(Values.teacherGrades, Integer.parseInt(teacher.getGrade()));
        }catch (NumberFormatException e){
            grade = context.getResources().getString(R.string.data_unknown);
        }
        holder.tGrade.setText(grade);
        String edu = "";
        try{
            edu = DictDBTask.getDcName("college_grade", Integer.parseInt(teacher.getEducation()));
        }catch (NumberFormatException e){
            edu = context.getResources().getString(R.string.data_unknown);
        }
        holder.tEducation.setText(edu);
        String school = TextUtils.isEmpty(teacher.getCollege())
                ? context.getResources().getString(R.string.data_unknown)
                : teacher.getCollege();
        holder.tSchool.setText(school);

    }

    public interface TeachersItemOnclickListener{
        void onItemClick(Object data, int position);
        void onButtonClick(Object data, int position);
    }

    private TeachersItemOnclickListener listener;
}
