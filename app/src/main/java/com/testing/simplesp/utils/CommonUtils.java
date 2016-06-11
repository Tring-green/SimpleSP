package com.testing.simplesp.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.SystemClock;
import android.view.Display;

import com.testing.simplesp.lib.SP;

/**
 * Created by admin on 2016/6/9.
 */
public class CommonUtils {

    private final Context mContext;
    private static CommonUtils instance;

    private CommonUtils() {
        mContext = SP.getContext();
    }

    public static CommonUtils getInstance() {
        if (instance == null) {
            synchronized (CommonUtils.class) {
                if (instance == null) {
                    instance = new CommonUtils();
                }
            }
        }
        return instance;
    }

    public int[] getWindowWidthAndHeight(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return new int[]{width, height};
    }

    /**
     * dip 转 px
     *
     * @param dipValue
     * @return
     */
    public int dip2px(float dipValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px 转 dip
     *
     * @param pxValue
     * @return
     */
    public int px2dip(float pxValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    long[] mHits;

    public CommonUtils setClickTime(int clickTime) {
        if (mHits == null || clickTime != mHits.length)
            mHits = new long[clickTime];
        return this;
    }

    public void multiClick(onMultiClickListener listener) {
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();// 开机后开始计算的时间
        if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
            listener.onMultiClick();
        }
    }

    public interface onMultiClickListener {
        void onMultiClick();
    }
}
