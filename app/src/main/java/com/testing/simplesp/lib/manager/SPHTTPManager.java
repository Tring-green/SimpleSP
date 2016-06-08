package com.testing.simplesp.lib.manager;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.testing.simplesp.domain.SPError;
import com.testing.simplesp.lib.SP;
import com.testing.simplesp.lib.SPHttpClient;
import com.testing.simplesp.lib.SPHttpParams;
import com.testing.simplesp.lib.callback.SPObjectCallBack;
import com.testing.simplesp.utils.ThreadUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * Created by admin on 2016/6/4.
 */
public class SPHTTPManager {
    private Context mContext;
    private Thread mainThread;
    private static SPHTTPManager instance;

    private SPHTTPManager() {
        mContext = SP.getContext();
        mainThread = Thread.currentThread();
    }

    public static SPHTTPManager getInstance() {
        if (instance == null) {
            synchronized (SPHTTPManager.class) {
                if (instance == null) {
                    instance = new SPHTTPManager();
                }
            }
        }
        return instance;
    }

    public SPHttpClient sendRequest(String url, String method, SPHttpParams httpParams, Map<String, String> header,
                                    Map<String, String> body, boolean useThread, final SPObjectCallBack callback) {
        SPHttpClient httpClass = new SPHttpClient();
        httpClass.startConnection(url, method, httpParams, header, body, useThread, new SPHttpClient
                .OnVisitingListener() {
            @Override
            public void onSuccess(String result) throws UnsupportedEncodingException {
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(URLDecoder.decode(result, "utf-8"));
                JsonObject root = element.getAsJsonObject();
                JsonPrimitive flagJson = root.getAsJsonPrimitive("flag");
                boolean flag = flagJson.getAsBoolean();
                if (flag) {
                    JsonObject dataObject = element.getAsJsonObject();
                    if (dataObject != null) {
                        if (callback != null) {
                            final Object obj = new Gson().fromJson(dataObject, callback.getClazz());
                            ThreadUtils.getInstance().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onSuccess(obj);
                                }
                            });
                        }
                    }

                } else {
                    final JsonPrimitive errorCodeJson = root.getAsJsonPrimitive("errorCode");
                    final JsonPrimitive errorMessageJson = root.getAsJsonPrimitive("errorMessage");
                    if (callback != null) {
                        ThreadUtils.getInstance().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onError(errorCodeJson.getAsInt(), errorMessageJson.getAsString()+"123");
                            }
                        });
                    }

                }

            }


            @Override
            public void onError(IOException e) {
                e.printStackTrace();
                ThreadUtils.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null)
                            callback.onError(SPError.ERROR_SERVER, "服务器连接问题！");
                    }
                });
            }

        });
        return httpClass;
    }
}
