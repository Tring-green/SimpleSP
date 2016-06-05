package com.testing.simplesp.utils;

import android.content.Context;
import android.widget.Toast;

import com.testing.simplesp.lib.SP;
import com.testing.simplesp.lib.SPHTTPManager;

/**
 * Created by admin on 2016/3/16.
 */
public class ToastUtils {

    private static ToastUtils instance;
    private Context mContext;

    private ToastUtils() {
        mContext = SP.getContext();
    }

    public static ToastUtils getInstance() {
        if (instance == null) {
            synchronized (SPHTTPManager.class) {
                if (instance == null) {
                    instance = new ToastUtils();
                }
            }
        }
        return instance;
    }

    public void showTestShort(final String text) {
        ThreadUtils.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showTestLong(final String text) {
        ThreadUtils.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext,text, Toast.LENGTH_LONG).show();
            }
        });
    }
}
