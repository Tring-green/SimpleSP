package com.testing.simplesp.domain;

import java.util.List;

/**
 * Created by admin on 2016/6/7.
 */
public class ScheduleItem{

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
        public String classPos;
        public String date;

        public Data(String id, String name, String classes, String teacher, double credit, String checkType, String
                classroom, String time, String creditType, String comment) {
            this.id = id;
            this.name = name;
            this.classes = classes;
            this.teacher = teacher;
            this.credit = credit;
            this.checkType = checkType;
            this.classroom = classroom;
            this.time = time;
            this.creditType = creditType;
            this.comment = comment;
        }

        public Data(String couName, String week, String date, String classPos, int pos) {
            this.name = couName;
            this.week = week;
            this.date = date;
            this.classPos = classPos;
            this.pos = pos;
        }

        public static Data newInstance(Data original) {
            Data data = new Data(original.id, original.name, original.classes, original.teacher,
                    original.credit, original.checkType,original.classroom,original.time,
                    original.creditType,original.comment);
            return data;
        }
    }


}
