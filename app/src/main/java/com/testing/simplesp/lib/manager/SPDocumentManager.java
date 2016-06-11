package com.testing.simplesp.lib.manager;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.testing.simplesp.adapter.DocumentAdapter;
import com.testing.simplesp.db.DocumentDao;
import com.testing.simplesp.domain.DocumentItem;
import com.testing.simplesp.domain.DocumentItem.Data;
import com.testing.simplesp.fragment.DocumentFragment;
import com.testing.simplesp.lib.SP;
import com.testing.simplesp.lib.SPHttpParams;
import com.testing.simplesp.lib.SPUrl;
import com.testing.simplesp.lib.callback.SPObjectCallBack;
import com.testing.simplesp.utils.SharedPreferenceUtils;
import com.testing.simplesp.utils.ThreadUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/6/4.
 */
public class SPDocumentManager {
    private static SPDocumentManager instance;
    private Context mContext;
    private boolean shouldLoad = false;

    public static final int LOAD_MORE = 0;
    public static final int ADD_MORE = 1;

    private SPDocumentManager() {
        mContext = SP.getContext();
    }

    public static SPDocumentManager getInstance() {
        if (instance == null) {
            synchronized (SPDocumentManager.class) {
                if (instance == null) {
                    instance = new SPDocumentManager();
                }
            }
        }
        return instance;
    }

    public void More(final int more, final Object obj) {

        int currentId = -1;
        if (more == LOAD_MORE) {
            currentId = SharedPreferenceUtils.getInstance().getInt("currentId");
            if (currentId > 0)
                DocumentAdapter.LOADING_SIGN = DocumentAdapter.LOADING_DOWN;
        }
        if (DocumentAdapter.mValues.size() != 0 && more == ADD_MORE)
            currentId = DocumentAdapter.mValues.get(DocumentAdapter.mValues.size() - 1).getId();
        final int finalCurrentId = currentId;

        Map<String, String> header = new HashMap<>();
        if (more == LOAD_MORE) {
            header.put("more", "0");
        }
        if (more == ADD_MORE) {
            header.put("more", "1");
        }
        Map<String, String> body = new HashMap<>();
        body.put("currentId", finalCurrentId + "");
        body.put("count", "20");
        SPHttpParams httpParams = new SPHttpParams(5000, 5000, true);
        SPHTTPManager.getInstance().sendRequest(SPUrl.URL_HTTP_DOCUMENT, "POST", httpParams, header, body, true,
                new SPObjectCallBack<DocumentItem>() {

                    @Override
                    public void onSuccess(DocumentItem documentItem) {
                        DocumentAdapter.LOADING_SIGN = DocumentAdapter.LOADING_NOW;
                        List<Data> list = documentItem.getData();
                        if (more == LOAD_MORE) {
                            Collections.reverse(list);
                            synchronized (DocumentAdapter.class) {
                                for (Data data : list) {
                                    DocumentDao.getInstance().addDocumentItem(data);
                                    DocumentAdapter.mValues.add(0, data);
                                }
                            }
                            SharedPreferenceUtils.getInstance().putInt("currentId", list.get(list.size() - 1)
                                    .getId());
                            if (obj != null) {
                                if (obj instanceof SwipeRefreshLayout)
                                    ((SwipeRefreshLayout) obj).setRefreshing(false);
                            }
                        }
                        if (more == ADD_MORE) {
                            synchronized (DocumentAdapter.class) {
                                for (Data data : list) {
                                    DocumentDao.getInstance().addDocumentItem(data);
                                    DocumentAdapter.mValues.add(data);
                                }
                            }
                            if (obj != null) {
                                if (obj instanceof DocumentAdapter) {
                                    ((DocumentAdapter) obj).notifyDataSetChanged();
                                    DocumentAdapter.LOADING_SIGN = DocumentAdapter.LOADING_DOWN;
                                }
                            }
                        }
                        DocumentFragment.mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(int errorCode, String errorMessage) {
                        DocumentAdapter.LOADING_SIGN = DocumentAdapter.LOADING_NOW;
                        Log.d("SPDocumentManager", errorCode + " :" + errorMessage);
                        if (obj != null) {
                            if (obj instanceof SwipeRefreshLayout) {
                                DocumentAdapter.LOADING_SIGN = DocumentAdapter.LOADING_DOWN;
                                ((SwipeRefreshLayout) obj).setRefreshing(false);
                            }
                            if (obj instanceof DocumentAdapter) {
                                DocumentAdapter.LOADING_SIGN = DocumentAdapter.LOADING_ERROR;
                                ((DocumentAdapter) obj).notifyDataSetChanged();
                            }
                        }
                        ThreadUtils.getInstance().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                        DocumentFragment.mAdapter.notifyDataSetChanged();
                            }
                        });
                    }

                });

    }


}
