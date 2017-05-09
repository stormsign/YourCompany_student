package com.miuhouse.yourcompany.student.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

//import com.miuhouse.yourcompany.student.model.AlarmEntity;

/** 上课提醒安排表
 * Created by khb on 2016/8/9.
 */
public class AlarmScheduleDao {
    public static final String TABLE_NAME = "AlarmScheduleTable";
    public static final String ORDERID = "orderId";
    public static final String ALARMTIME = "alarmtime";

    private DatabaseHelper dbHelper;
    public AlarmScheduleDao(Context context){
        dbHelper = DatabaseHelper.getInstance();
    }

//    public synchronized long addAlarm(AlarmEntity alarm){
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put(ORDERID, alarm.getOrderId());
//        cv.put(ALARMTIME, alarm.getAlarmTime());
//
//        long result = db.insert(TABLE_NAME, null, cv);
//        db.close();
//        return result;
//    }

//    /**
//     * 返回最近要开始的提醒
//     * @return
//     */
//    public synchronized AlarmEntity getNeaerestAlarm(){
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        Cursor cursor = db.rawQuery("select * from "
//                +TABLE_NAME
//                +" order by "
//                +ALARMTIME + "desc "
//                , null);
//        AlarmEntity alarm = null;
//        if (db.isOpen()){
//            alarm = new AlarmEntity();
//            if (cursor.moveToNext()){
//                alarm.setOrderId(cursor.getString(cursor.getColumnIndex(ORDERID)));
//                alarm.setAlarmTime(cursor.getLong(cursor.getColumnIndex(ALARMTIME)));
//            }
//            cursor.close();
//            db.close();
//            return alarm;
//        }
//        return alarm;
//    }

//    /**
//     * 删掉一个提醒
//     * @param alarm
//     * @return
//     */
//    public synchronized long deleteAlarm(AlarmEntity alarm){
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        long result = db.delete(TABLE_NAME, ORDERID + " = ? ", new String[]{alarm.getOrderId()});
//        db.close();
//        return result;
//    }



}
