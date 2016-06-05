package com.testing.simplesp.lib;

/**
 * Created by admin on 2016/5/28.
 */
public class SPHttpParams {
    private int ConnectTimeout;
    private int ReadTimeout;
    private boolean DoOutput;
    private boolean DoInput;

    public SPHttpParams(int connectTimeout, int readTimeout, boolean doOutput, boolean doInput) {
        ConnectTimeout = connectTimeout;
        ReadTimeout = readTimeout;
        DoOutput = doOutput;
        DoInput = doInput;
    }

    public SPHttpParams(int connectTimeout, int readTimeout, boolean doOutput) {
        this(connectTimeout, readTimeout, doOutput, true);
    }

    public SPHttpParams(int connectTimeout, int readTimeout) {
        this(connectTimeout, readTimeout, false, false);
    }

    public int getConnectTimeout() {
        return ConnectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        ConnectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return ReadTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        ReadTimeout = readTimeout;
    }

    public boolean isDoOutput() {
        return DoOutput;
    }

    public void setDoOutput(boolean doOutput) {
        DoOutput = doOutput;
    }

    public boolean isDoInput() {
        return DoInput;
    }

    public void setDoInput(boolean doInput) {
        DoInput = doInput;
    }
}
