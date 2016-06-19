package com.testing.simplesp.db;

/**
 * Created by admin on 2016/6/4.
 */
public interface SPDB {

    String NAME = "sp.db";
    int VERSION = 1;

    interface DocumentItem {
        String TABLE_NAME = "web";
        String COLUMN_ID = "id";
        String COLUMN_TITLE = "title";
        String COLUMN_UNIT = "unit";
        String COLUMN_TIME = "time";
        String COLUMN_CONTENT = "content";

        String SQL_CREATE_TABLE = "create table " + TABLE_NAME + " ("
                + COLUMN_ID + " integer primary key, "
                + COLUMN_TITLE + " text," + COLUMN_UNIT + " text,"
                + COLUMN_TIME + " text," + COLUMN_CONTENT + " text" + ")";
    }

    interface ScheduleItem{
        String TABLE_NAME = "course";
        String COLUMN_ID = "id";
        String COLUMN_NAME = "name";
        String COLUMN_CLASS= "class";
        String COLUMN_TEACHER = "teacher";
        String COLUMN_CREDIT = "credit";
        String COLUMN_CHECK_TYPE = "checktype";
        String COLUMN_CHOSEN = "choosen";
        String COLUMN_MAX = "max";
        String COLUMN_CLASSROOM = "classroom";
        String COLUMN_TIME = "time";
        String COLUMN_CREDIT_TYPE = "credittype";
        String COLUMN_COMMENT= "comment";
        String COLUMN_STUDENT = "student";
        String COLUMN_CHECKED= "checked";
        String SQL_CREATE_TABLE = "create table " + TABLE_NAME + " ("
                + COLUMN_ID + " text primary key, "
                + COLUMN_NAME + " text," + COLUMN_CLASS + " text,"
                + COLUMN_TEACHER + " text," + COLUMN_CREDIT + " double,"
                + COLUMN_CHECK_TYPE + " text," + COLUMN_CLASSROOM + " text,"
                + COLUMN_TIME + " text," + COLUMN_CREDIT_TYPE + " text,"
                + COLUMN_COMMENT + " text" + ")";
    }
}
