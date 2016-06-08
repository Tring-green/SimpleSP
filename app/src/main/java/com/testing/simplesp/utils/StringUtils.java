package com.testing.simplesp.utils;

/**
 * Created by admin on 2016/3/17.
 */
public class StringUtils {
    public static String transWeek(String week) {
        if (week.equals("一"))
            week = "1";
        if (week.equals("二"))
            week = "2";
        if (week.equals("三"))
            week = "3";
        if (week.equals("四"))
            week = "4";
        if (week.equals("五"))
            week = "5";
        if (week.equals("六"))
            week = "6";
        if (week.equals("日"))
            week = "7";
        return week;
    }
}
