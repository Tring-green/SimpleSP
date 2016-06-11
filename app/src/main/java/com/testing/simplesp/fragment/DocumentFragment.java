package com.testing.simplesp.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.testing.simplesp.R;
import com.testing.simplesp.adapter.DocumentAdapter;
import com.testing.simplesp.base.BaseFragment;
import com.testing.simplesp.db.DocumentDao;
import com.testing.simplesp.domain.DocumentItem;
import com.testing.simplesp.lib.manager.SPDocumentManager;
import com.testing.simplesp.utils.ColorUtils;
import com.testing.simplesp.utils.CommonUtils;
import com.testing.simplesp.utils.SharedPreferenceUtils;
import com.testing.simplesp.utils.ThreadUtils;
import com.testing.simplesp.widget.ViewBackgroundDecoration;

import java.util.List;
import java.util.Map;


public class DocumentFragment extends BaseFragment {


    private int mColumnCount = 1;
    public RecyclerView mRv_list;
    private View mView;
    public static SwipeRefreshLayout mSrl;
    public static Activity mActivity;
    public static DocumentAdapter mAdapter;

    public DocumentFragment() {
    }


    @SuppressWarnings("unused")
    public static DocumentFragment newInstance() {
        DocumentFragment fragment = new DocumentFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        mRv_list.addItemDecoration(new ViewBackgroundDecoration(new ViewBackgroundDecoration.onDrawListener() {
            @Override
            public Paint onDraw(Map<String, Paint> map, View child) {
                Paint paint = new Paint();
                TextView tv_content = (TextView) child.findViewById(R.id.unit);
                String content;
                if (tv_content == null) {
                    content = "";
                } else
                    content = tv_content.getText().toString().split(" ")[0];
                if (TextUtils.isEmpty(content)) {
                    paint.setColor(Color.GRAY);
                    paint.setAlpha(80);
                } else {
                    if (map.containsKey(content)) {
                        paint = map.get(content);
                    } else {
                        int[] color = ColorUtils.getInstance().getColorInexistent();
                        paint.setARGB(color[0], color[1], color[2], color[3]);
                        map.put(content, paint);
                    }
                }
                return paint;
            }
        }));
        mSrl = (SwipeRefreshLayout) mView.findViewById(R.id.srl);
        if (mColumnCount <= 1) {
            mRv_list.setLayoutManager(new LinearLayoutManager(mActivity));
        } else {
            mRv_list.setLayoutManager(new GridLayoutManager(mActivity, mColumnCount));
        }
        mSrl.setRefreshing(true);
        int currentId = SharedPreferenceUtils.getInstance().getInt("currentId");
        if (currentId != 0) {
            List<DocumentItem.Data> list = DocumentDao.getInstance().getDocumentItemById(currentId);
            synchronized (DocumentAdapter.class) {
                DocumentAdapter.mValues.addAll(list);
            }
            mSrl.setRefreshing(false);
        }
        mAdapter = new DocumentAdapter(mActivity);
        mRv_list.setAdapter(mAdapter);
        TabLayout tabLayout = (TabLayout) mActivity.findViewById(R.id.tabs);
        final ViewPager mViewPager = (ViewPager) mActivity.findViewById(R.id.container);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                CommonUtils.getInstance().setClickTime(2).multiClick(new CommonUtils.onMultiClickListener() {
                    @Override
                    public void onMultiClick() {
                        View child = mRv_list.getChildAt(0);
                        if (child != null)
                            mRv_list.smoothScrollToPosition(0);
                    }
                });
            }
        });
        mSrl.setColorSchemeColors(Color.GRAY);
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
