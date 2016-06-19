package com.testing.simplesp.lib;

/**
 * Created by admin on 2016/6/4.
 */
public class SPUrl {
    public static String BASE_HTTP = "http://10.0.2.2:8000/SPServer";

    public final static String URL_HTTP_DOCUMENT = BASE_HTTP + "/Webs/document";
    public final static String URL_HTTP_SCHEDULE = BASE_HTTP + "/Webs/schedule";
    public final static String URL_HTTP_ELECTRICITY = BASE_HTTP + "/Webs/electricity";

    public final static String URL_HTTP_ELECTRICITY_BUILDING_NAME =
            BASE_HTTP + "/Webs/electricity/buildingName";
}
