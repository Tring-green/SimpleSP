package com.testing.simplesp.lib;

import android.content.Context;
import android.util.Log;

/**
 * Created by admin on 2016/6/4.
 */
public class SP {
    private static SP instance;
    private static Context context;

    public static SP getInstance() {
        if (instance == null) {
            synchronized (SP.class) {
                instance = new SP();
            }
        }
        return instance;
    }

    public static Context getContext() {
        if (SP.context == null) {
            throw new RuntimeException(
                    "请在Application的onCreate方法中调用SP.getInstance().init(context)初始化SP.");
        }
        return SP.context;
    }

    public void init(Context context) {
        SP.context = context;
        Log.d("SP", "init");
    }
}
