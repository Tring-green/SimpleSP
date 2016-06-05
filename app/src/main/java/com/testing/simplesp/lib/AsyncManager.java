package com.testing.simplesp.lib;

import android.os.Handler;

/**
 * Created by admin on 2016/6/4.
 */
public class AsyncManager{
    private static AsyncManager instance;
    private final Handler mHandler;

    public static AsyncManager getInstance() {
        if (instance == null) {
            synchronized (SP.class) {
                instance = new AsyncManager();
            }
        }
        return instance;
    }

    private  AsyncManager() {
        mHandler = new Handler();
    }
    public void runOnUiThread(Runnable runnable) {
        mHandler.post(runnable);
    }
}
