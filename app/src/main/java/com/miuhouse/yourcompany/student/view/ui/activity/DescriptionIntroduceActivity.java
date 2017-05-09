package com.miuhouse.yourcompany.student.view.ui.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miuhouse.yourcompany.student.R;

/**
 * Created by kings on 9/13/2016.
 */
public class DescriptionIntroduceActivity extends AppCompatActivity {

    private TextView tvDescription;
    private Toolbar mToolbar;
    private String strDescriptionIntroduce;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_introduce);
        initData();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        tvDescription = (TextView) findViewById(R.id.tv_description);
        tvDescription.setText(strDescriptionIntroduce);
        supportActionBar(mToolbar);
    }

    private void initData() {
        strDescriptionIntroduce = getIntent().getStringExtra("context");
    }

    /**
     * @param toolbar The toolbar with go back button
     * @return ActionBar
     */
    protected ActionBar supportActionBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        return actionBar;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
