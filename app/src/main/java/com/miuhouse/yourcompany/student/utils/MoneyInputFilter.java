package com.miuhouse.yourcompany.student.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 金额输入filter
 * Created by khb on 2016/8/26.
 */
public class MoneyInputFilter implements InputFilter {

    private Pattern mPattern;

    private final int MAX_VALUE = 999;

    public MoneyInputFilter(){
        mPattern = Pattern.compile("([0-9]|\\.)*");
    }

    /**
     * @param source    新输入的字符串
     * @param start     新输入的字符串起始下标，一般为0
     * @param end       新输入的字符串终点下标，一般为source长度-1
     * @param dest      输入之前文本框内容
     * @param dstart    原内容起始坐标，一般为0
     * @param dend      原内容终点坐标，一般为dest长度-1
     * @return          输入内容
     */
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String strSource = source.toString();
        String strDest = dest.toString();
        if (TextUtils.isEmpty(strSource)){
            return "";
        }
        Matcher matcher = mPattern.matcher(source);
        if (strDest.contains(".")){
            int index = strDest.indexOf(".");
            int length = dend - index;

            if (length > 2) {
                return dest.subSequence(dstart, dend);
            }
        }
        double sum = Double.parseDouble(strDest + strSource);
        if (sum>MAX_VALUE){
            return strDest.subSequence(dstart, dend);
        }

        return strDest.subSequence(dstart, dend)+strSource;
    }
}
