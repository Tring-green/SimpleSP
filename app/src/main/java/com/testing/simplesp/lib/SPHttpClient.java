package com.testing.simplesp.lib;

import android.content.Context;

import com.testing.simplesp.utils.StreamUtils;
import com.testing.simplesp.utils.ThreadUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by admin on 2016/5/26.
 */
public class SPHttpClient {

    private static final String ERROR = "error";
    private HttpURLConnection conn;
    private static SPHttpClient instance;
    private Context mContext;
    //private SPHttpParams mHttpParams;

    public SPHttpClient() {
        mContext = SP.getContext();
    }

    public static SPHttpClient getInstance() {
        if (instance == null) {
            synchronized (SPHttpClient.class) {
                if (instance == null) {
                    instance = new SPHttpClient();
                }
            }
        }
        return instance;
    }

    private HttpURLConnection initClient(String targetUrl, String method, SPHttpParams httpParams, Map<String,
            String> header) throws
            IOException {
        URL url = new URL(targetUrl);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setConnectTimeout(httpParams.getConnectTimeout());
        conn.setReadTimeout(httpParams.getReadTimeout());
        conn.setDoOutput(httpParams.isDoOutput());
        conn.setDoInput(httpParams.isDoInput());
        if (header != null) {
            sendHeader(conn, header);
        }
        return conn;
    }


    public interface OnVisitingListener {
        void onSuccess(String result) throws UnsupportedEncodingException;

        void onError(IOException e);
    }

    public void startConnection(final String url, final String method, final SPHttpParams httpParams, final
    Map<String, String> header, final Map<String, String> body, boolean useThread, final OnVisitingListener listener) {
        if (useThread)
            ThreadUtils.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    judgeMethod(method, url, httpParams, header, body, listener);
                }
            });
        else
            judgeMethod(method, url, httpParams, header, body, listener);
    }

    public void judgeMethod(String method, String targetUrl, SPHttpParams httpParams, Map<String, String> headers,
                            Map<String, String> body, final OnVisitingListener listener) {
        switch (method.toUpperCase()) {
            case "POST":
                doPost(targetUrl, httpParams, headers, body, listener);
                break;
            case "GET":
                doGet(targetUrl, httpParams, headers, listener);
                break;
            case "PUT":
                break;
            case "DELETE":
                break;
            default:
                break;
        }

    }

    private void doGet(String targetUrl, SPHttpParams httpParams, Map<String, String> headers, final OnVisitingListener
            listener) {
        sendRequest(targetUrl, "GET", httpParams, headers, null, listener);
    }

    private void doPost(String targetUrl, SPHttpParams httpParams, Map<String, String> headers,
                        Map<String, String> body, final OnVisitingListener listener) {
        sendRequest(targetUrl, "POST", httpParams, headers, body, listener);
    }

    private void sendRequest(String targetUrl, String method, SPHttpParams httpParams, Map<String, String> headers,
                             Map<String, String> body, final OnVisitingListener listener) {
        try {
            initClient(targetUrl, method, httpParams, headers);
                    /* 发送请求并等待响应 */
            if (httpParams.isDoOutput() && body != null) {
                sendPostBody(conn, body);
            }
            conn.connect();
            if (conn.getResponseCode() == 200) {
                ThreadUtils.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            listener.onSuccess(StreamUtils.getString(conn.getInputStream()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else
                throw new IOException();
        } catch (final IOException e) {
            e.printStackTrace();
            ThreadUtils.getInstance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listener.onError(e);
                }
            });

        }
    }

    private void sendHeader(HttpURLConnection conn, Map<String, String> header) {
        for (Map.Entry<String, String> me : header.entrySet()) {
            String key = me.getKey();
            String value = me.getValue();
            conn.addRequestProperty(key, value);
        }
    }

    private void sendPostBody(HttpURLConnection conn, Map<String, String> body) throws IOException {
        OutputStream out = conn.getOutputStream();
        String pair = handlePair(body);
        if (pair == null)
            return;
        out.write(pair.getBytes());
        out.write("\r\n".getBytes());
        out.flush();
    }

    public static String handlePair(Map<String, String> body) throws UnsupportedEncodingException {
        String result = "";
        if (body == null) {
            return null;
        }
        for (Map.Entry<String, String> me : body.entrySet()) {
            String key = me.getKey();
            String value = me.getValue();
            result += key + "=" + URLEncoder.encode(value, "utf-8") + "&";
        }
        return result.substring(0, result.length() - 1);
    }

    public void disconnect() {
        if (conn != null) {
            conn.disconnect();
            conn = null;

        }
    }
}
