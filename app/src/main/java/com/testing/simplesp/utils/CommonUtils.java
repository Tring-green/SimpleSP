package com.testing.simplesp.utils;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;

/**
 * Created by admin on 2016/6/9.
 */
public class CommonUtils {
    public static int[] getWindowWidthAndHeight(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return new int[]{width, height};
    }
}
