package com.testing.simplesp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.testing.simplesp.R;
import com.testing.simplesp.adapter.ScheduleAdapter;
import com.testing.simplesp.base.BaseFragment;
import com.testing.simplesp.db.ScheduleDao;
import com.testing.simplesp.domain.ScheduleItem;
import com.testing.simplesp.lib.manager.SPScheduleManager;
import com.testing.simplesp.utils.CommonUtils;
import com.testing.simplesp.utils.RegexUtils;
import com.testing.simplesp.utils.SharedPreferenceUtils;
import com.testing.simplesp.utils.StringUtils;
import com.testing.simplesp.utils.ToastUtils;
import com.testing.simplesp.widget.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends BaseFragment {


    private View mView;
    private RecyclerView mRv_list;
    private Button mBt_bind;
    private ProgressBar mPb_loading;
    private RelativeLayout mRl_input;
    private ScheduleAdapter mAdapter;
    public static int mWidth;
    public static int mHeight;

    public ScheduleFragment() {

    }

    public static ScheduleFragment newInstance() {
        ScheduleFragment fragment = new ScheduleFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_schedule_item_list, container, false);
        initView();
        initData();
        return mView;
    }

    private void initData() {
        String schedule = SharedPreferenceUtils.getInstance().getString("schedule");
        if (schedule != null) {
            ScheduleItem item = new Gson().fromJson(schedule, ScheduleItem.class);
            List<ScheduleItem.Data> values = item.data;
            setAdapter(values);
            mRl_input.setVisibility(View.INVISIBLE);
        }
    }

    private void initView() {
        int[] widthAndHeight = CommonUtils.getWindowWidthAndHeight(getActivity());
        mWidth = widthAndHeight[0];
        mHeight = widthAndHeight[1];
        mRv_list = (RecyclerView) mView.findViewById(R.id.rv_list);
        mRv_list.setLayoutManager(new GridLayoutManager(getActivity(), ScheduleAdapter.COLUMN_COUNT));
        mBt_bind = (Button) mView.findViewById(R.id.bt_bind);
        mPb_loading = (ProgressBar) mView.findViewById(R.id.pb_loading);
        mRl_input = (RelativeLayout) mView.findViewById(R.id.rl_input);
        mBt_bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_input = (EditText) mView.findViewById(R.id.et_input);
                final String stuNum = et_input.getText().toString();
                if (!TextUtils.isEmpty(stuNum)) {
                    if (stuNum.length() != 10) {
                        ToastUtils.getInstance().showTestShort("学号错误，请重新输入");
                        return;
                    }
                    mRl_input.setVisibility(View.INVISIBLE);
                    mPb_loading.setVisibility(View.VISIBLE);
                    SPScheduleManager.getInstance().getCourse(stuNum, new
                            SPScheduleManager.SPScheduleCallBack() {
                                @Override
                                public void onSuccess(List<ScheduleItem.Data> list) {
                                    handleData(list);
                                    mPb_loading.setVisibility(View.INVISIBLE);
                                    mRv_list.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onError(int errorCode, String errorMessage) {

                                }
                            });
                } else {
                    ToastUtils.getInstance().showTestShort("学号为空，请重新输入！");
                }
            }
        });
    }

    private void handleData(List<ScheduleItem.Data> list) {
        List<ScheduleItem.Data> values = new ArrayList<>();
        for (ScheduleItem.Data data : list) {
            ScheduleDao.getInstance().addScheduleItem(data);
            String couClass = data.classroom;
            String week = RegexUtils.RegexGroup(couClass, "周(.{1})", 1);
            week = StringUtils.transWeek(week);
            String date = RegexUtils.RegexGroup(couClass, "周(.{1})(.+?),", 2);
            String classpos = RegexUtils.RegexGroup(couClass, "\\((.+?)\\)", 1);
            int pos = getPos(week, date);
            data.pos = pos;
            data.classpos = classpos;
            data.date = date;
            data.week = week;
            values.add(data);

        }
        setAdapter(values);
        //将课程保存到本地
        ScheduleItem item = new ScheduleItem();
        item.data = values;
        String json = new Gson().toJson(item);
        SharedPreferenceUtils.getInstance().putString("schedule", json);
    }

    private void setAdapter(List<ScheduleItem.Data> values) {
        mAdapter = new ScheduleAdapter();
        mAdapter.setValues(values);
        mRv_list.setAdapter(mAdapter);
        mRv_list.addItemDecoration(new DividerGridItemDecoration(ScheduleAdapter.mValues.size()));
        mPb_loading.setVisibility(View.INVISIBLE);
        mRv_list.setVisibility(View.VISIBLE);
    }


    private int getPos(String week, String date) {
        return (ScheduleAdapter.COLUMN_COUNT * (Integer.parseInt(date) / 2 + 1) + Integer.parseInt(week));
    }

    @Override
    public String getPageName() {
        return "课程表";
    }
}
