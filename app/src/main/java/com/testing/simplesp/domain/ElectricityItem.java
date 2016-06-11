package com.testing.simplesp.domain;

import java.util.List;

/**
 * Created by admin on 2016/6/10.
 */
public class ElectricityItem {
    public List<Data> data;
    public boolean flag;

    public static class Data {
        public double surplus;
        public String date;
        public double yesUse;
    }

}
