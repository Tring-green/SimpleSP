package com.testing.simplesp.utils;

import com.testing.simplesp.lib.manager.AsyncManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by admin on 2016/6/4.
 */
public class ThreadUtils {
    private static ThreadUtils instance;
    private ExecutorService mExecutorService;
    private final int ThreadCount = 10;

    public static ThreadUtils getInstance() {
        if (instance == null) {
            synchronized (ThreadUtils.class) {
                if (instance == null) {
                    instance = new ThreadUtils();
                }
            }
        }
        return instance;
    }

    ThreadUtils() {
        mExecutorService = Executors.newFixedThreadPool(ThreadCount);
    }



    public void execute(Runnable runnable) {
        mExecutorService.execute(runnable);
    }

    public  void runOnUiThread(Runnable runnable) {
        AsyncManager.getInstance().runOnUiThread(runnable);
    }
}
