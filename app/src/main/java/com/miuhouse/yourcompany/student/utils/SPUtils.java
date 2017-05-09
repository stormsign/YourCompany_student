package com.miuhouse.yourcompany.student.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.miuhouse.yourcompany.student.application.App;

/**
 * Created by kings on 7/11/2016.
 */
public class SPUtils {
    public static final String TOKEN = "token";
    public static final String DEVICE_CODE = "deviceCode";
    public static final String DICT_VERSION = "dictVersion";
    public static final String FIRST_START = "app_first_start";
    public static final String CHECK_PERSONAL_DATA = "check_personal_data";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor sharedPreferencesEditor;
    private static SharedPreferences mSharedPreferences = null;

//    //写入
//    public static void saveData(String key, String value) {
//        Context context = App.getContext();
//        if (sharedPreferences == null) {
//            sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
//        }
//        sharedPreferences.edit().putString(key, value).commit();
//    }

    //写入
    public static void saveData(String key, String value) {
        Context context = App.getContext();
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        sharedPreferences.edit().putString(key, value).commit();
    }

    //读出
    public static String getData(String key, String defValue) {
        Context context = App.getInstance();
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return sharedPreferences.getString(key, defValue);
    }


    //写入
    public static void saveData(String key, int value) {
        Context context = App.getContext();
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        sharedPreferences.edit().putInt(key, value).commit();
    }

    //读出
    public static int getData(String key, int defValue) {
        Context context = App.getContext();
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return sharedPreferences.getInt(key, defValue);
    }

    //写入
    public static void saveData(String key, boolean value) {
        Context context = App.getContext();
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    //读出
    public static Boolean getData(String key, boolean defValue) {
        Context context = App.getContext();
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        }
        return sharedPreferences.getBoolean(key, defValue);
    }

    //写入
    public static void saveData(String key, long value) {
        Context context = App.getContext();
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().putLong(key, value).commit();
    }

    //读出
    public static long getData(String key, long defValue) {
        Context context = App.getContext();
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        }
        return sharedPreferences.getLong(key, defValue);
    }
}
