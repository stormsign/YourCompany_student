package com.miuhouse.yourcompany.student.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.miuhouse.yourcompany.student.model.DictBean;
import com.miuhouse.yourcompany.student.model.DictListBean;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.utils.Values;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kings on 7/19/2016.
 */
public class DictDBTask {
    private DictDBTask() {

    }

    private static SQLiteDatabase getWsd() {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        return databaseHelper.getWritableDatabase();
    }

    private static SQLiteDatabase getRsd() {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        return databaseHelper.getReadableDatabase();
    }

    public static void add(DictListBean list) {
        if (list == null || list.getDictionaries().size() == 0) {
            return;
        }
        clear();
        ContentValues cv = new ContentValues();
        cv.put(DictTable.JSONDATA, new Gson().toJson(list));
        getWsd().insert(DictTable.TABLE_NAME, DictTable.ID, cv);
    }

    public static int getDictListSize() {

        int result = 0;
        Gson gson = new Gson();
        String sql = "select * from " + DictTable.TABLE_NAME;
        Cursor c = getRsd().rawQuery(sql, null);
        if (c.moveToNext()) {
            String json = c.getString(c.getColumnIndex(DictTable.JSONDATA));
            DictListBean dictListBean = gson.fromJson(json, DictListBean.class);
            result = dictListBean.getDictionaries().size();
        }
        c.close();
        return result;
    }

    /**
     * 根据type返回 字典名称列表
     *
     * @param dcType
     * @return
     */
    public static List<String> getDcNameList(String dcType) {
        Gson gson = new Gson();
        List<String> result = new ArrayList<>();
        String sql = "select * from " + DictTable.TABLE_NAME;
        Cursor c = getRsd().rawQuery(sql, null);
        if (c.moveToNext()) {
            String json = c.getString(c.getColumnIndex(DictTable.JSONDATA));
            DictListBean dictListBean = gson.fromJson(json, DictListBean.class);
            for (DictBean dictBean : dictListBean.getDictionaries()) {
                if (dictBean.getDcType().equals(dcType)) {
                    result.add(dictBean.getDcName());
                }
            }
        }
        c.close();
        return result;
    }

    /**
     * 根据type返回 字典名称Map
     *
     * @param dcType
     * @return
     */
    public static Map<Integer, String> getDcNameMap(String dcType) {
        Gson gson = new Gson();
        Map<Integer, String> result = new HashMap<>();
        String sql = "select * from " + DictTable.TABLE_NAME;
        Cursor c = getRsd().rawQuery(sql, null);
        if (c.moveToNext()) {
            String json = c.getString(c.getColumnIndex(DictTable.JSONDATA));
            DictListBean dictListBean = gson.fromJson(json, DictListBean.class);
            for (DictBean dictBean : dictListBean.getDictionaries()) {
                if (dictBean.getDcType().equals(dcType)) {

                    result.put(Integer.parseInt(dictBean.getDcValue()), dictBean.getDcName());
                }
            }
        }
        c.close();
        return result;
    }

    /**
     * 根据type,和key返回 字典名称
     *
     * @param dcType
     * @param key
     * @return
     */
    public static String getDcName(String dcType, Integer key) {
        String result = null;
        Map<Integer, String> map = getDcNameMap(dcType);
        L.i("TAG", "map=" + map.size());
        for (Map.Entry<Integer, String> e : map.entrySet()) {
            if (e.getKey().equals(key)) {
                result = e.getValue();
                break;
            }
        }
        return result;
    }

    /**
     * 根据type,和value返回 字典值,就是1，2，3等
     *
     * @param dcType
     * @param value
     * @return
     */
    public static Integer getDcValue(String dcType, String value) {
        Integer result = null;
        Map<Integer, String> map = getDcNameMap(dcType);
        L.i("TAG", "entrySet=" + map.size());

        for (Map.Entry<Integer, String> e : map.entrySet()) {
            L.i("TAG", "entrySet=" + e.getValue());
            if (e.getValue().equals(value)) {
                result = e.getKey();
                break;
            }
        }
        return result;
    }

//    获取订单类型对应的价格
//    index==0 时类型为陪伴
//    index==1 时类型为作业辅导
//    index==2 时类型为兴趣培养
    public static double getOrderTypePrice(int index){
        String name = Values.getValue(Values.majorDemand, index+1);
        double price = 0;
        Gson gson = new Gson();
        String sql = "select * from " + DictTable.TABLE_NAME;
        SQLiteDatabase db = DatabaseHelper.getInstance().getWritableDatabase();
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToNext()) {
            String json = c.getString(c.getColumnIndex(DictTable.JSONDATA));
            DictListBean dictListBean = gson.fromJson(json, DictListBean.class);
            for (DictBean dictBean : dictListBean.getDictionaries()) {
                if (dictBean.getDcType().equals("order_type")
                        || dictBean.getDcName().equals(name)){
                    price = Double.parseDouble(dictBean.getDcValue());
                }
            }
        }
        c.close();
        db.close();
        return price;
    }


    public static void clear() {
        String sql = "delete from " + DictTable.TABLE_NAME;
        getWsd().execSQL(sql);
    }
}
