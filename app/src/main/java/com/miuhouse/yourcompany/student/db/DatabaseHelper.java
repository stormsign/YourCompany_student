package com.miuhouse.yourcompany.student.db;

import android.accounts.Account;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.miuhouse.yourcompany.student.application.App;

/**
 * Created by kings on 7/11/2016 Booean值以0(false)和1(true)来存储..
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper singleton = null;

    private static final String DATABASE_NAME = "yourcompanyteacher.db";

    private static final int DATABASE_VERSION = 3;
    //    public final static String TNAME = "tName";
    //    public final static String MOBILE = "mobile";
    //    public final static String TOKEN = "token";
    //    public final static String BALANCE = "balance";
    private final String CREATE_ACCOUNT_TABLE_SQL = "create table "
        + AccountTable.TABLE_NAME + "("
        + AccountTable.PARENT_ID + " text primary key,"
        + AccountTable.PNAME + " text,"
        + AccountTable.MOBILE + " text,"
        + AccountTable.TOKEN + " text,"
        + AccountTable.SCHOOL_ID + " INTEGER,"
        + AccountTable.CLASS_ID + " INTEGER,"
        + AccountTable.GRADE + " text,"
        + AccountTable.CNAME + " text,"
        + AccountTable.ROLENAME + " text,"
        + AccountTable.HEAD_URL + " text,"
        + AccountTable.SCHOOLNAME + " text"
        + ");";
    private final String CREATE_DICT_TABLE_SQL = "create table "
        + DictTable.TABLE_NAME + "("
        + DictTable.ID + " integer primary key autoincrement,"
        + DictTable.JSONDATA + " text" + ");";
    private final String CREATE_ALARM_SCHEDULE_TABLE_SQL =
        "create table "
            + AlarmScheduleDao.TABLE_NAME + "("
            + AlarmScheduleDao.ORDERID + " text primary key, "
            + AlarmScheduleDao.ALARMTIME + " time not null " + ");";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //    字段名称	数据类型	说明
    //    id	String	老师ID
    //    tName	String	用户姓名
    //    mobile	String	电话
    //    token	String
    //    tokenTime	long	token过期时间
    //    balance	BigDecimal	账号余额

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ACCOUNT_TABLE_SQL);
        db.execSQL(CREATE_DICT_TABLE_SQL);
        db.execSQL(CREATE_ALARM_SCHEDULE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            deleteAllTable(db);
            onCreate(db);
        }
    }

    public static synchronized DatabaseHelper getInstance() {
        if (singleton == null) {
            singleton = new DatabaseHelper(App.getContext());
        }
        return singleton;
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (singleton == null) {
            singleton = new DatabaseHelper(context);
        }
        return singleton;
    }

    private void deleteAllTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + AccountTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DictTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AlarmScheduleDao.TABLE_NAME);
    }

    public void closeDB() {
        if (singleton != null) {
            try {
                SQLiteDatabase db = singleton.getWritableDatabase();
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            singleton = null;
        }
    }
}
