package com.testing.simplesp.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.testing.simplesp.R;
import com.testing.simplesp.adapter.DocumentAdapter;
import com.testing.simplesp.base.BaseFragment;
import com.testing.simplesp.db.DocumentItemDao;
import com.testing.simplesp.domain.DocumentItem;
import com.testing.simplesp.lib.SPDocumentManager;
import com.testing.simplesp.utils.SharedPreferenceUtils;
import com.testing.simplesp.utils.ThreadUtils;

import java.util.List;


public class DocumentFragment extends BaseFragment {

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    public  RecyclerView mRv_list;
    private View mView;
    public static SwipeRefreshLayout mSrl;
    public static Activity mActivity;
    public static DocumentAdapter mAdapter;

    public DocumentFragment() {
    }


    @SuppressWarnings("unused")
    public static DocumentFragment newInstance(int columnCount) {
        DocumentFragment fragment = new DocumentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_document_item_list, container, false);
        initView();
        return mView;
    }
    private void initView() {
        mActivity = getActivity();
        mRv_list = (RecyclerView) mView.findViewById(R.id.rv_list);
        mSrl = (SwipeRefreshLayout) mView.findViewById(R.id.srl);
        if (mColumnCount <= 1) {
            mRv_list.setLayoutManager(new LinearLayoutManager(mActivity));
        } else {
            mRv_list.setLayoutManager(new GridLayoutManager(mActivity, mColumnCount));
        }
        int currentId = SharedPreferenceUtils.getInstance().getInt("currentId");
        if (currentId != 0) {
            List<DocumentItem.Data> list = DocumentItemDao.getInstance().getDocumentItemById(currentId);
            synchronized (DocumentAdapter.class) {
                DocumentAdapter.mValues.addAll(list);
            }
        }
        mAdapter = new DocumentAdapter(mActivity);
        mRv_list.setAdapter(mAdapter);
        mSrl.setColorSchemeColors(Color.GRAY);
        mSrl.setRefreshing(true);
        //下拉刷新逻辑
        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SPDocumentManager.getInstance().More(SPDocumentManager.LOAD_MORE, mSrl);
            }
        });
        //设置上划加载更多逻辑
        mRv_list.setOnScrollListener(new RecyclerView.OnScrollListener() {
            final LinearLayoutManager layoutManager = (LinearLayoutManager) mRv_list.getLayoutManager();
            public int lastVisibleItemPosition;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if ((newState == RecyclerView.SCROLL_STATE_IDLE) &&
                        (lastVisibleItemPosition + 1)
                                == mAdapter.getItemCount()) {
                    ThreadUtils.getInstance().execute(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(DocumentAdapter.LOADING_SIGN);
                            if (DocumentAdapter.LOADING_SIGN == DocumentAdapter.LOADING_DOWN)
                                mAdapter.addMore();
                        }
                    });
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            }
        });
    }


    @Override
    public String getPageName() {
        return "公文通";
    }
}
