package com.testing.simplesp.utils;

import android.content.Context;

import com.testing.simplesp.lib.SP;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by admin on 2016/6/11.
 */
public class ColorUtils {
    private static ColorUtils instance;
    private final Context mContext;

    private List<int[]> mList = new ArrayList<>();

    private ColorUtils() {
        mContext = SP.getContext();
    }

    public static ColorUtils getInstance() {
        if (instance == null) {
            synchronized (ColorUtils.class) {
                if (instance == null) {
                    instance = new ColorUtils();
                }
            }
        }
        return instance;
    }

    public int[] getColorInexistent() {
        Random random = new Random();
        int[] ints = new int[4];
        do {
            ints[0] = 80 + random.nextInt(80);
            ints[1] = 40 + random.nextInt(150);
            ints[2] = 40 + random.nextInt(150);
            ints[3] = 40 + random.nextInt(150);
        } while (mList.contains(ints));
        mList.add(ints);
        return ints;
    }
}
