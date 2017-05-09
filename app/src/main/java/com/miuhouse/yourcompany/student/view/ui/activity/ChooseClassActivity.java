package com.miuhouse.yourcompany.student.view.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.db.AccountDBTask;
import com.miuhouse.yourcompany.student.listener.OnListItemClick;
import com.miuhouse.yourcompany.student.model.ClassEntity;
import com.miuhouse.yourcompany.student.presenter.ChooseClassPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.IChooseClassPresenter;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IChooseClass;
import com.miuhouse.yourcompany.student.view.ui.adapter.ChooseClassAdapter;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;
import com.miuhouse.yourcompany.student.view.widget.RecyclerViewItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khb on 2017/1/10.
 */
public class ChooseClassActivity extends BaseActivity implements IChooseClass, OnListItemClick, View.OnClickListener {

    private RecyclerView list;
    private List<ClassEntity> classList;
    private ChooseClassAdapter adapter;
    private ArrayList<Integer> selectedIds = new ArrayList<>();
    private ArrayList<String> classNames = new ArrayList<>();
    private IChooseClassPresenter presenter;

    public static final String SELECTED_CLASS_ID = "select_class_id";
    public static final String SELECTED_CLASS_NAME = "select_class_name";
    private LinearLayout all;
    private LinearLayout teachers;
    private ImageView ivAll;
    private ImageView ivTeachers;

    @Override
    protected String setTitle() {
        return "谁可以看";
    }

    @Override
    protected String setRight() {
        return null;
    }

    @Override
    protected void initViewAndEvents() {
        presenter = new ChooseClassPresenter(this);
        list = (RecyclerView) findViewById(R.id.list);
        classList = new ArrayList<>();
//        ClassEntity all = new ClassEntity();
//        all.setId(0);
//        all.setName("所有人");
//        ClassEntity teachers = new ClassEntity();
//        teachers.setId(-1);
//        teachers.setName("老师圈");
//        classList.add(0, all);
//        classList.add(1, teachers);
        adapter = new ChooseClassAdapter(this, classList, this);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.addItemDecoration(new RecyclerViewItemDecoration(this, R.drawable.list_divider));
        list.setAdapter(adapter);
        presenter.getAllClasses(AccountDBTask.getAccount().getId());
        all = (LinearLayout) findViewById(R.id.all);
        teachers = (LinearLayout) findViewById(R.id.teachers);
        ivAll = (ImageView) findViewById(R.id.ivAll);
        ivTeachers = (ImageView) findViewById(R.id.ivTeachers);
        all.setOnClickListener(this);
        teachers.setOnClickListener(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_chooseclass;
    }

    @Override
    protected View getOverrideParentView() {
        return null;
    }

    @Override
    public void onItemClick(Object data) {
        ClassEntity classEntity = (ClassEntity) data;
        ivAll.setSelected(false);
        if (selectedIds.contains(0)){
            selectedIds.remove((Integer) 0);
        }
        if (selectedIds.contains(classEntity.getId())){
            selectedIds.remove((Integer)classEntity.getId());
        }else {
            selectedIds.add(classEntity.getId());
        }

    }

//    @Override
//    public void controlSelector(ImageView imageView, int position) {
//
//    }

    @Override
    public void showChoices(List<ClassEntity> classlist) {
        classList.clear();
        classList.addAll(classlist);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackClick() {
        for (int i = 0 ; i < selectedIds.size(); i++){
            if (selectedIds.get(i) == 0){
                classNames.add("所有人");
            }else if (selectedIds.get(i) == -1){
                classNames.add("老师圈");
            }else {
                for (ClassEntity classEntity :
                        classList) {
                    if (classEntity.getId() == selectedIds.get(i)){
                        classNames.add(classEntity.getName());
                        break;
                    }
                }
            }
        }
        setResult(RESULT_OK, new Intent()
                .putIntegerArrayListExtra(SELECTED_CLASS_ID, selectedIds)
                .putStringArrayListExtra(SELECTED_CLASS_NAME, classNames));
        super.onBackClick();
    }

    @Override
    public void onBackPressed() {
        for (int i = 0 ; i < selectedIds.size(); i++){
            if (selectedIds.get(i) == 0){
                classNames.add("所有人");
            }else if (selectedIds.get(i) == -1){
                classNames.add("老师圈");
            }else {
                for (ClassEntity classEntity :
                        classList) {
                    if (classEntity.getId() == selectedIds.get(i)){
                        classNames.add(classEntity.getName());
                        break;
                    }
                }
            }
        }
        setResult(RESULT_OK, new Intent()
                .putIntegerArrayListExtra(SELECTED_CLASS_ID, selectedIds)
                .putStringArrayListExtra(SELECTED_CLASS_NAME, classNames));
        super.onBackPressed();
    }

    @Override
    public void showLoading(String msg) {
        super.showLoading(msg);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

    @Override
    public void showError(int type) {
//        super.showError(type);
        if (type == 1){
            Toast.makeText(this, "没有班级", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "加载错误", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.all:
                ivAll.setSelected(!ivAll.isSelected());
                if (ivAll.isSelected()){
                    selectedIds.clear();
                    selectedIds.add(0, 0);
                    selectedIds.add(1, -1);
                    ivTeachers.setSelected(true);
                    for (int i = 0; i<classList.size(); i++){
                        selectedIds.add(classList.get(i).getId());
                        classList.get(i).setSelected(true);
                    }
                    adapter.notifyDataSetChanged();
                }else {
                    selectedIds.clear();
                    ivTeachers.setSelected(false);
                    for (int i = 0; i<classList.size(); i++){
                        classList.get(i).setSelected(false);
                    }
                    adapter.notifyDataSetChanged();
                }

                break;
            case R.id.teachers:
                ivTeachers.setSelected(!ivTeachers.isSelected());
                ivAll.setSelected(false);
                if (selectedIds.contains(0)){
                    selectedIds.remove((Integer)0);
                }
                if (ivTeachers.isSelected()){
                    ivTeachers.setSelected(true);
                    selectedIds.add(-1);
                }else {
                    ivTeachers.setSelected(false);
                    selectedIds.remove((Integer)(-1));
                }
                break;
        }
    }
}
