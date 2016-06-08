package com.testing.simplesp.lib.manager;

import android.os.Handler;

import com.testing.simplesp.lib.SP;

/**
 * Created by admin on 2016/6/4.
 */
public class AsyncManager {
    private static AsyncManager instance;
    private Handler mHandler;

    public static AsyncManager getInstance() {
        if (instance == null) {
            synchronized (SP.class) {
                instance = new AsyncManager();
            }
        }
        return instance;
    }

    private AsyncManager() {
        mHandler = new Handler();
    }

    public void init() {

    }

    public void runOnUiThread(Runnable runnable) {
        mHandler.post(runnable);
    }
}
