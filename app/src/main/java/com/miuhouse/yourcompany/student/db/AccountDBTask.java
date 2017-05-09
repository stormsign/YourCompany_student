package com.miuhouse.yourcompany.student.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.miuhouse.yourcompany.student.model.ParentInfo;
import com.miuhouse.yourcompany.student.utils.L;

import java.math.BigDecimal;
import mabeijianxi.camera.util.Log;

/**
 * Created by kings on 7/11/2016.
 */
public class AccountDBTask {
    private AccountDBTask() {

    }

    private static SQLiteDatabase getWsd() {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        return databaseHelper.getWritableDatabase();
    }

    private static SQLiteDatabase getRsd() {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        return databaseHelper.getReadableDatabase();
    }

    public static void saveUserBean(ParentInfo parentInfo) {
        clear();
        if (getWsd().isOpen()) {
            L.i("TAG", "saveUserBean");
            ContentValues contentValues = new ContentValues();
            contentValues.put(AccountTable.PARENT_ID, parentInfo.getId());
            contentValues.put(AccountTable.PNAME, parentInfo.getpName());
            contentValues.put(AccountTable.MOBILE, parentInfo.getMobile());
            contentValues.put(AccountTable.TOKEN, parentInfo.getToken());
            contentValues.put(AccountTable.SCHOOL_ID, parentInfo.getSchoolId());
            contentValues.put(AccountTable.CLASS_ID, parentInfo.getClassId());
            contentValues.put(AccountTable.ROLENAME, parentInfo.getRoleName());
            contentValues.put(AccountTable.GRADE, parentInfo.getGrade());
            contentValues.put(AccountTable.CNAME, parentInfo.getcName());
            contentValues.put(AccountTable.HEAD_URL, parentInfo.getHeadUrl());
            contentValues.put(AccountTable.SCHOOLNAME, parentInfo.getSchoolName());

            getWsd().insert(AccountTable.TABLE_NAME, null, contentValues);
        }
    }

    public static ParentInfo getAccount() {
        String sql = "select * from " + AccountTable.TABLE_NAME;
        Cursor c = getRsd().rawQuery(sql, null);
        if (c.moveToNext()) {
            ParentInfo account = new ParentInfo();
            int colid = c.getColumnIndex(AccountTable.PARENT_ID);
            account.setId(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.PNAME);
            account.setpName(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.MOBILE);
            account.setMobile(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.TOKEN);
            account.setToken(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.SCHOOL_ID);
            account.setSchoolId(c.getLong(colid));

            colid = c.getColumnIndex(AccountTable.CLASS_ID);
            account.setClassId(c.getLong(colid));

            colid = c.getColumnIndex(AccountTable.CNAME);
            account.setcName(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.GRADE);
            account.setGrade(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.HEAD_URL);
            account.setHeadUrl(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.ROLENAME);
            account.setRoleName(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.SCHOOLNAME);
            account.setSchoolName(c.getString(colid));
            return account;
        }
        c.close();
        return null;
    }

    public static void clear() {
        String sql = "delete from " + AccountTable.TABLE_NAME;

        getWsd().execSQL(sql);
    }

    public static void updateHeadUrl(String parentId, String headUrl) {
        if (getWsd().isOpen()) {
            ContentValues values = new ContentValues();
            values.put(AccountTable.HEAD_URL, headUrl);
            String[] where = {parentId};
            getWsd().update(AccountTable.TABLE_NAME, values, AccountTable.PARENT_ID + "=?", where);
        }
    }

    public static void upCName(String parentId, String cName) {
        if (getWsd().isOpen()) {
            ContentValues values = new ContentValues();
            values.put(AccountTable.CNAME, cName);
            String[] where = {parentId};
            getWsd().update(AccountTable.TABLE_NAME, values, AccountTable.PARENT_ID + "=?", where);
        }
    }
}
