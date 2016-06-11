package com.testing.simplesp.lib.manager;

import android.os.Handler;

/**
 * Created by admin on 2016/6/4.
 */
public class SPAsyncManager {
    private static SPAsyncManager instance;
    private Handler mHandler;

    public static SPAsyncManager getInstance() {
        if (instance == null) {
            synchronized (SPAsyncManager.class) {
                instance = new SPAsyncManager();
            }
        }
        return instance;
    }

    private SPAsyncManager() {
        mHandler = new Handler();
    }


    public void runOnUiThread(Runnable runnable) {
        mHandler.post(runnable);
    }
}
