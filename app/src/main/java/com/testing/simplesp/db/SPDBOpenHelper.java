package com.testing.simplesp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SPDBOpenHelper extends SQLiteOpenHelper {

	private SPDBOpenHelper(Context context) {
		super(context, SPDB.NAME, null, SPDB.VERSION);
	}

	private static SPDBOpenHelper instance;

	public static SPDBOpenHelper getInstance(Context context) {
		if (instance == null) {
			synchronized (SPDBOpenHelper.class) {
				if (instance == null) {
					instance = new SPDBOpenHelper(context);
				}
			}
		}
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SPDB.DocumentItem.SQL_CREATE_TABLE);
		db.execSQL(SPDB.ScheduleItem.SQL_CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
