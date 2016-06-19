package com.testing.simplesp.fragment;


import android.graphics.Color;
import android.graphics.Paint;
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
import android.widget.TextView;

import com.google.gson.Gson;
import com.testing.simplesp.R;
import com.testing.simplesp.adapter.ScheduleAdapter;
import com.testing.simplesp.base.BaseFragment;
import com.testing.simplesp.db.ScheduleDao;
import com.testing.simplesp.domain.ScheduleItem;
import com.testing.simplesp.lib.manager.SPScheduleManager;
import com.testing.simplesp.utils.ColorUtils;
import com.testing.simplesp.utils.RegexUtils;
import com.testing.simplesp.utils.SharedPreferenceUtils;
import com.testing.simplesp.utils.StringUtils;
import com.testing.simplesp.utils.ToastUtils;
import com.testing.simplesp.widget.ViewBackgroundDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        mRv_list = (RecyclerView) mView.findViewById(R.id.rv_list);
        mRv_list.setLayoutManager(new GridLayoutManager(getActivity(), ScheduleAdapter.COLUMN_COUNT));
        mBt_bind = (Button) mView.findViewById(R.id.bt_bind);
        mPb_loading = (ProgressBar) mView.findViewById(R.id.pb_loading);
        mRl_input = (RelativeLayout) mView.findViewById(R.id.rl_input);
        mBt_bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
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
                                    mPb_loading.setVisibility(View.INVISIBLE);
                                    mRl_input.setVisibility(View.VISIBLE);
                                    ToastUtils.getInstance().showTestShort(errorCode + ": " + errorMessage);
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
        int pos;
        String classPos;
        String date;
        String week;
        for (ScheduleItem.Data data : list) {
            ScheduleDao.getInstance().addScheduleItem(data);
            String[] couClasses = data.classroom.split(";");
            if (couClasses.length == 0 || TextUtils.isEmpty(couClasses[0])) {
                continue;
            }
            Map<Integer, ScheduleItem.Data> map = new HashMap<>();
            for (int i = 0; i < couClasses.length; i++) {
                String couClass = couClasses[i];
                week = RegexUtils.RegexGroup(couClass, "周(.{1})", 1);
                week = StringUtils.transWeek(week);
                date = RegexUtils.RegexGroup(couClass, "周(.{1})(.+?),", 2);
                classPos = RegexUtils.RegexGroup(couClass, "\\((.+?)\\)", 1);
                if (classPos == null)
                    classPos = "";
                pos = getPos(week, date);
                ScheduleItem.Data judge = map.get(pos);
                String result = RegexUtils.RegexGroup(couClass, "(.{1})周(.{1})(.+?),", 1);
                if (judge == null) {
                    ScheduleItem.Data newInstance = ScheduleItem.Data.newInstance(data);
                    newInstance.pos = pos;
                    newInstance.classPos = classPos;
                    newInstance.date = date;
                    newInstance.week = week;
                    //System.out.println(data.name + "-" + data.classPos);
                    newInstance.name += result != null ? "(" + result + ")" + newInstance.classPos : newInstance
                            .classPos;
                    map.put(pos, newInstance);
                } else {
                    //System.out.println(judge.name + "-" + data.classPos);
                    judge.name += result != null ? ";(" + result + ")" + classPos : classPos;
                    map.put(pos, judge);
                }
            }
            for (ScheduleItem.Data result : map.values()) {
                values.add(result);
            }
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
        mRv_list.addItemDecoration(new ViewBackgroundDecoration(new ViewBackgroundDecoration.onDrawListener() {
            @Override
            public Paint onDraw(Map<String, Paint> map, View child) {
                Paint paint = new Paint();//生成画笔
                TextView tv_content = (TextView) child.findViewById(R.id.content);
                String content = tv_content.getText().toString();
                if (TextUtils.isEmpty(content)) {//如果没有课，则将背景设置为黑色
                    paint.setColor(Color.GRAY);
                    paint.setAlpha(80);
                } else {//否则，如果该课程名存在，则使用相同的颜色，否则生成新的颜色
                    if (map.containsKey(content)) {
                        paint = map.get(content);
                    } else {
                        int[] color = ColorUtils.getInstance().createNewColor();
                        paint.setARGB(color[0], color[1], color[2], color[3]);
                        map.put(content, paint);
                    }
                }
                return paint;
            }
        }));
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
