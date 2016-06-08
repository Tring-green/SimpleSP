package com.testing.simplesp;

import android.app.Application;

import com.testing.simplesp.lib.SP;
import com.testing.simplesp.utils.ThreadUtils;

/**
 * Created by admin on 2016/6/4.
 */
public class SPApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        SP.getInstance().init(this);
        ThreadUtils.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

}
