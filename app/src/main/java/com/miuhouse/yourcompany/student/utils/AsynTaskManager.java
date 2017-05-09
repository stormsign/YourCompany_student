package com.miuhouse.yourcompany.student.utils;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by khb on 2017/1/6.
 */
public class AsynTaskManager extends AsyncTask<String, String, String>{

    private Context context;
    private NewTask mNewTask;
    private NewTaskForResult mNewTaskForResult;
    private TaskListener mTaskListener;

    public AsynTaskManager(Context context, NewTask newTask, TaskListener taskListener){
        this.context = context;
        mNewTask = newTask;
        mTaskListener = taskListener;
    }

    public AsynTaskManager(Context context, NewTaskForResult newTaskForResult, TaskListener taskListener){
        this.context = context;
        mNewTaskForResult = newTaskForResult;
        mTaskListener = taskListener;
    }

    public interface NewTask{
        void newTask();
    }

    public interface NewTaskForResult{
        String newTaskForResult();
    }

    public interface TaskListener{
        void onPreExrcute();
        void onPostExecute(String result);
        void onCancelled();
    }

    @Override
    protected void onPreExecute() {
        mTaskListener.onPreExrcute();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        if (mNewTask != null) {
            mNewTask.newTask();
            return null;
        }else {
            return mNewTaskForResult.newTaskForResult();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        mTaskListener.onPostExecute(s);
        super.onPostExecute(s);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(String s) {
        mTaskListener.onCancelled();
        super.onCancelled(s);
    }
}
