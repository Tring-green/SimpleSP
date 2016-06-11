package com.testing.simplesp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.testing.simplesp.lib.SP;
import com.testing.simplesp.lib.manager.SPHTTPManager;

/**
 * Created by admin on 2016/2/18.
 */
public class SharedPreferenceUtils {

    private static SharedPreferences sp;
    private static String name = "config";
    private static SharedPreferenceUtils instance;
    private final Context mContext;

    private SharedPreferenceUtils() {
        mContext = SP.getContext();
    }

    public static SharedPreferenceUtils getInstance() {
        if (instance == null) {
            synchronized (SharedPreferenceUtils.class) {
                if (instance == null) {
                    instance = new SharedPreferenceUtils();
                }
            }
        }
        return instance;
    }


    public  String getString(String key) {
        sp = mContext.getSharedPreferences(name, Context
                .MODE_PRIVATE);
        return sp.getString(key, null);
    }

    public void putString(String key, String value) {
        sp = mContext.getSharedPreferences(name, Context
                .MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    public int getInt(String key) {
        sp = mContext.getSharedPreferences(name, Context
                .MODE_PRIVATE);
        return sp.getInt(key, 0);
    }

    public void putInt(String key, int value) {
        sp = mContext.getSharedPreferences(name, Context
                .MODE_PRIVATE);
        sp.edit().putInt(key, value).apply();
    }

    public  void delete(){
        sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }
}
