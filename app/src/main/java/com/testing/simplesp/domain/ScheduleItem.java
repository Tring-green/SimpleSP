package com.testing.simplesp.domain;

import java.util.List;

/**
 * Created by admin on 2016/6/7.
 */
public class ScheduleItem {

    public List<Data> data;
    public boolean flag;

    public static class Data {
        public String id;
        public String name;
        public String classes;
        public String teacher;
        public double credit;
        public String checkType;
        public String classroom;
        public String time;
        public String creditType;
        public String comment;
        public int pos;
        public String week;
        public String classpos;
        public String date;

        public Data(String couName, String week, String date, String classpos, int pos) {
            this.name = couName;
            this.week = week;
            this.date = date;
            this.classpos = classpos;
            this.pos = pos;
        }
    }
}
