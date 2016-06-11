package com.testing.simplesp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.testing.simplesp.domain.ScheduleItem;
import com.testing.simplesp.lib.SP;
import com.testing.simplesp.lib.manager.SPDocumentManager;

import java.util.List;

/**
 * Created by admin on 2016/6/8.
 */
public class ScheduleDao {
    private SPDBOpenHelper helper;

    private static ScheduleDao instance;
    private Context mContext;

    private List<ScheduleItem.Data> mList;

    private ScheduleDao() {
        mContext = SP.getContext();
        helper = SPDBOpenHelper.getInstance(mContext);

    }

    public static ScheduleDao getInstance() {
        if (instance == null) {
            synchronized (ScheduleDao.class) {
                if (instance == null) {
                    instance = new ScheduleDao();
                }
            }
        }
        return instance;
    }

    public void addScheduleItem(ScheduleItem.Data item) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SPDB.ScheduleItem.COLUMN_ID, item.id);
        values.put(SPDB.ScheduleItem.COLUMN_NAME, item.name);
        values.put(SPDB.ScheduleItem.COLUMN_CLASS, item.classes);
        values.put(SPDB.ScheduleItem.COLUMN_TEACHER, item.teacher);
        values.put(SPDB.ScheduleItem.COLUMN_CREDIT, item.credit);
        values.put(SPDB.ScheduleItem.COLUMN_CHECK_TYPE, item.checkType);
        values.put(SPDB.ScheduleItem.COLUMN_CLASSROOM, item.classroom);
        values.put(SPDB.ScheduleItem.COLUMN_TIME, item.time);
        values.put(SPDB.ScheduleItem.COLUMN_CREDIT_TYPE, item.creditType);
        values.put(SPDB.ScheduleItem.COLUMN_COMMENT, item.comment);
        db.insert(SPDB.ScheduleItem.TABLE_NAME, null, values);
    }

}
