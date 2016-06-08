package com.testing.simplesp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.testing.simplesp.domain.DocumentItem.Data;
import com.testing.simplesp.lib.SP;
import com.testing.simplesp.lib.manager.SPDocumentManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/6/4.
 */
public class DocumentDao {
    private SPDBOpenHelper helper;

    private static DocumentDao instance;
    private Context mContext;

    private List<Data> mList;

    private DocumentDao() {
        mContext = SP.getContext();
        helper = SPDBOpenHelper.getInstance(mContext);
    }

    public static DocumentDao getInstance() {
        if (instance == null) {
            synchronized (SPDocumentManager.class) {
                if (instance == null) {
                    instance = new DocumentDao();
                }
            }
        }
        return instance;
    }

    public List<Data> getDocumentItemById(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        int maxId = getMaxId();
        mList = null;
        if (maxId != 0) {
            String sql = "select * from " + SPDB.DocumentItem.TABLE_NAME
                    + " where " + SPDB.DocumentItem.COLUMN_ID + " <= "
                    + id + " "
                    + " Order by " + SPDB.DocumentItem.COLUMN_ID + " desc"
                    + " limit " + 20;
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    if (mList == null) {
                        mList = new ArrayList<>();
                    }
                    Data item = new Data();
                    item.setId(cursor.getInt(cursor
                            .getColumnIndex(SPDB.DocumentItem.COLUMN_ID)));
                    item.setTitle(cursor.getString(cursor
                            .getColumnIndex(SPDB.DocumentItem.COLUMN_TITLE)));
                    item.setUnit(cursor.getString(cursor
                            .getColumnIndex(SPDB.DocumentItem.COLUMN_UNIT)));
                    item.setTime(cursor.getString(cursor
                            .getColumnIndex(SPDB.DocumentItem.COLUMN_TIME)));
                    item.setContent(cursor.getString(cursor
                            .getColumnIndex(SPDB.DocumentItem.COLUMN_CONTENT)));
                    mList.add(item);
                }
            }
        }
        return mList;
    }

    public void addDocumentItem(Data item) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SPDB.DocumentItem.COLUMN_ID, item.getId());
        values.put(SPDB.DocumentItem.COLUMN_TITLE, item.getTitle());
        values.put(SPDB.DocumentItem.COLUMN_TIME, item.getTime());
        values.put(SPDB.DocumentItem.COLUMN_UNIT, item.getUnit());
        values.put(SPDB.DocumentItem.COLUMN_CONTENT, item.getContent());
        db.insert(SPDB.DocumentItem.TABLE_NAME, null, values);
    }

    public int getMaxId() {
        String sql = "select max(" + SPDB.DocumentItem.COLUMN_ID + ") from "
                + SPDB.DocumentItem.TABLE_NAME;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            if (cursor.moveToNext())
                return cursor.getInt(0);
        }
        return 0;
    }

    public int getMinId() {
        String sql = "select min(" + SPDB.DocumentItem.COLUMN_ID + ") from "
                + SPDB.DocumentItem.TABLE_NAME;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            if (cursor.moveToNext())
                return cursor.getInt(0);
        }
        return 0;
    }
}
