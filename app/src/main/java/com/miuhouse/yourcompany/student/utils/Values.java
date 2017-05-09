package com.miuhouse.yourcompany.student.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 数字与对应文本的集合
 * Created by khb on 2016/7/12.
 */
public class Values {

    public static Map<Integer, String> teacherVerifyStatuses = new TreeMap<>();
    public static Map<String, String> orderStatuses = new TreeMap<>();
    public static Map<Integer, String> stars = new TreeMap<>();
    public static Map<Integer, String> studentGrades = new TreeMap<>();
    public static Map<Integer, String> teacherGrades = new TreeMap<>();
    public static Map<Integer, String> majorDemand = new TreeMap<>();
    public static Map<Integer, String> gendersDemand = new TreeMap<>();
    public static Map<Integer, String> msgTypes = new TreeMap<>();

    static {
        teacherVerifyStatuses.put(1, "未认证");
        teacherVerifyStatuses.put(2, "审核中");
        teacherVerifyStatuses.put(3, "已认证");
        teacherVerifyStatuses.put(4, "审核失败");

        orderStatuses.put("1", "待支付");
        orderStatuses.put("2", "待抢单");
        orderStatuses.put("3", "待上课");
        orderStatuses.put("4", "进行中");
        orderStatuses.put("5", "待评价");
        orderStatuses.put("6", "已完成");
        orderStatuses.put("7", "投诉");
        orderStatuses.put("8", "待退款");
        orderStatuses.put("0", "取消");

        stars.put(1, "一星");
        stars.put(2, "二星");
        stars.put(3, "三星");
        stars.put(4, "四星");
        stars.put(5, "五星");

        studentGrades.put(0, "学前");
        studentGrades.put(1, "一年级");
        studentGrades.put(2, "二年级");
        studentGrades.put(3, "三年级");
        studentGrades.put(4, "四年级");
        studentGrades.put(5, "五年级");
        studentGrades.put(6, "六年级");

        teacherGrades.put(1, "大一");
        teacherGrades.put(2, "大二");
        teacherGrades.put(3, "大三");
        teacherGrades.put(4, "大四");

        majorDemand.put(1, "陪伴");
        majorDemand.put(2, "作业辅导");
        majorDemand.put(3, "兴趣培养");

        gendersDemand.put(0, "女");
        gendersDemand.put(1, "男");

        msgTypes.put(6, "订单被取消");
        msgTypes.put(7, "老师被选中");
        msgTypes.put(8, "老师没被选中");
        msgTypes.put(11, "订单完成");
        msgTypes.put(12, "提现处理中");
        msgTypes.put(13, "提现完成");
        msgTypes.put(14, "认证通过");
        msgTypes.put(15, "认证失败");
    }

    /**
     * 根据值获取key
     *
     * @param map
     * @param value
     * @return
     */
    public static int getKey(Map<Integer, String> map, String value) {
        int key = 0;
        for (Map.Entry<Integer, String> e :
                map.entrySet()) {
            if (e.getValue().equals(value)) {
                key = e.getKey();
                break;
            }
        }
        return key;
    }

    /**
     * 根据值获取value
     *
     * @param map
     * @param
     * @return
     */
    public static String getValue(Map<Integer, String> map, Integer key) {
        String value = null;
        for (Map.Entry<Integer, String> e : map.entrySet()) {
            if (e.getKey().equals(key)) {
                value = e.getValue();
                break;
            }
        }
        return value;
    }

    public static String[] getArrayValue(Map<Integer, String> map) {
        String[] array = new String[map.size()];
        int i = 0;
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            array[i++] = entry.getValue();
        }
        return array;
    }

    public static List<String> getListValue(Map<Integer, String> map) {
        List<String> list = new ArrayList<>();
        for (Map.Entry<Integer, String> e : map.entrySet()) {
            list.add(e.getValue());
        }
        return list;
    }
}
