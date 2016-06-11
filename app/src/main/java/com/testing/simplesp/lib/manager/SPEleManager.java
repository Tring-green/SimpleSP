package com.testing.simplesp.lib.manager;

import android.content.Context;

import com.testing.simplesp.domain.BuildingName;
import com.testing.simplesp.domain.ElectricityItem;
import com.testing.simplesp.lib.SP;
import com.testing.simplesp.lib.SPHttpParams;
import com.testing.simplesp.lib.SPUrl;
import com.testing.simplesp.lib.callback.SPObjectCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/6/10.
 */
public class SPEleManager {
    private static SPEleManager instance;
    private Context mContext;

    private SPEleManager() {
        mContext = SP.getContext();
    }

    public static SPEleManager getInstance() {
        if (instance == null) {
            synchronized (SPEleManager.class) {
                if (instance == null) {
                    instance = new SPEleManager();
                }
            }
        }
        return instance;
    }

    public void getBuildingName(final SPELECallBack callBack) {
        SPHTTPManager.getInstance().sendRequest(SPUrl.URL_HTTP_ELECTRICITY_BUILDING_NAME, "get",
                new SPHttpParams(5000, 5000), null, null, true, new SPObjectCallBack<BuildingName>() {
                    @Override
                    public void onSuccess(BuildingName buildingName) {
                        List<BuildingName.Data> list = buildingName.data;
                        List<String> stringList = new ArrayList<>();
                        for (BuildingName.Data data : list) {
                            stringList.add(data.buildingName);
                        }
                        String[] array = stringList.toArray(new String[stringList.size()]);
                        callBack.onSuccess(array);
                    }

                    @Override
                    public void onError(int errorCode, String errorMessage) {
                        callBack.onError(errorCode, errorMessage);
                    }
                });

    }

    public void getEle(String buildingName, String roomName, final SPELECallBack callBack) {
        Map<String, String> body = new HashMap<>();
        body.put("buildingName", buildingName);
        body.put("roomName", roomName);
        SPHTTPManager.getInstance().sendRequest(SPUrl.URL_HTTP_ELECTRICITY_ELE, "post",
                new SPHttpParams(5000, 5000, true), null, body, true, new SPObjectCallBack<ElectricityItem>() {
                    @Override
                    public void onSuccess(ElectricityItem buildingName) {
                        List<ElectricityItem.Data> list = buildingName.data;
                        callBack.onSuccess(list);
                    }

                    @Override
                    public void onError(int errorCode, String errorMessage) {
                        callBack.onError(errorCode, errorMessage);
                    }
                });

    }

    public interface SPELECallBack {
        void onSuccess(Object list);

        void onError(int errorCode, String errorMessage);
    }
}
