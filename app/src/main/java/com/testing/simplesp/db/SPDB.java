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

}
