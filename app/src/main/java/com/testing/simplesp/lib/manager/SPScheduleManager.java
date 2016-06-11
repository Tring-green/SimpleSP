package com.testing.simplesp.lib.manager;

import android.content.Context;

import com.testing.simplesp.domain.ScheduleItem;
import com.testing.simplesp.lib.SP;
import com.testing.simplesp.lib.SPHttpParams;
import com.testing.simplesp.lib.SPUrl;
import com.testing.simplesp.lib.callback.SPObjectCallBack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/6/7.
 */
public class SPScheduleManager {
    private static SPScheduleManager instance;
    private Context mContext;

    private SPScheduleManager() {
        mContext = SP.getContext();
    }

    public static SPScheduleManager getInstance() {
        if (instance == null) {
            synchronized (SPScheduleManager.class) {
                if (instance == null) {
                    instance = new SPScheduleManager();
                }
            }
        }
        return instance;
    }

    public void getCourse(String id, final SPScheduleCallBack callBack) {
        Map<String, String> body = new HashMap<>();
        body.put("id", id);
        SPHTTPManager.getInstance().sendRequest(SPUrl.URL_HTTP_SCHEDULE, "POST", new SPHttpParams(5000, 5000, true),
                null, body, true, new SPObjectCallBack<ScheduleItem>() {
                    @Override
                    public void onSuccess(ScheduleItem item) {
                        List<ScheduleItem.Data> list = item.data;
                        callBack.onSuccess(list);
                    }

                    @Override
                    public void onError(int errorCode, String errorMessage) {
                        callBack.onError(errorCode, errorMessage);
                    }
                });
    }


    public interface SPScheduleCallBack{
        void onSuccess(List<ScheduleItem.Data> list);

        void onError(int errorCode, String errorMessage);
    }
}
