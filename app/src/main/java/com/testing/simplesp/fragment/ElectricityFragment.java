package com.testing.simplesp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.testing.simplesp.R;
import com.testing.simplesp.base.BaseFragment;
import com.testing.simplesp.domain.ElectricityItem;
import com.testing.simplesp.lib.manager.SPEleManager;
import com.testing.simplesp.utils.SharedPreferenceUtils;
import com.testing.simplesp.utils.ToastUtils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ElectricityFragment extends BaseFragment {


    private View mView;
    private LinearLayout mLl_input;
    private ProgressBar mPb_loading;
    private LinearLayout mLl_show;
    private Spinner mSpinner_building;
    private String mBuildingName;
    private EditText mEt_input;
    private Button mBt_bind;
    private List<ElectricityItem.Data> mDataList;
    private Spinner mSpinner_date;
    private TextView mTv_surplus;
    private TextView mTv_yesUse;
    private Double[] mSurplus;
    private String[] mDate;
    private ArrayAdapter<String> mAdapter;
    private Double[] mYesUse;
    private TextView mTv_room_name;
    private Button mBt_refresh;
    private String mRoomName;

    public ElectricityFragment() {
    }

    public static ElectricityFragment newInstance() {
        ElectricityFragment fragment = new ElectricityFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_electricity, container, false);
        initView();
        initData();
        return mView;
    }

    private void initView() {
        mLl_input = (LinearLayout) mView.findViewById(R.id.ll_input);
        mSpinner_building = (Spinner) mView.findViewById(R.id.spinner_building);
        mEt_input = (EditText) mView.findViewById(R.id.et_input);
        mBt_bind = (Button) mView.findViewById(R.id.bt_bind);

        mPb_loading = (ProgressBar) mView.findViewById(R.id.pb_loading);

        mLl_show = (LinearLayout) mView.findViewById(R.id.ll_show);
        mTv_room_name = (TextView) mView.findViewById(R.id.tv_room_name);
        mSpinner_date = (Spinner) mView.findViewById(R.id.spinner_date);
        mTv_surplus = (TextView) mView.findViewById(R.id.tv_surplus);
        mTv_yesUse = (TextView) mView.findViewById(R.id.tv_yesUse);
        mBt_refresh = (Button) mView.findViewById(R.id.bt_refresh);
    }

    private void initData() {
        String eleUse = SharedPreferenceUtils.getInstance().getString("eleUse");
        mRoomName = SharedPreferenceUtils.getInstance().getString("roomName");
        mBuildingName = SharedPreferenceUtils.getInstance().getString("buildingName");
        mBt_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPb_loading.setVisibility(View.VISIBLE);
                getEle();
            }
        });
        if (eleUse == null) {
            String json = SharedPreferenceUtils.getInstance().getString("buildingNames");
            mLl_input.setVisibility(View.VISIBLE);
            mPb_loading.setVisibility(View.INVISIBLE);
            mLl_show.setVisibility(View.INVISIBLE);
            if (TextUtils.isEmpty(json)) {

                SPEleManager.getInstance().getBuildingName(new SPEleManager.SPELECallBack() {
                    @Override
                    public void onSuccess(Object list) {
                        String json = new Gson().toJson((String[]) list);
                        SharedPreferenceUtils.getInstance().putString("buildingNames", json);
                        initBuildingSpinner((String[]) list);
                    }

                    @Override
                    public void onError(int errorCode, String errorMessage) {
                        ToastUtils.getInstance().showTestShort(errorCode + ":" + errorMessage);
                    }
                });
            } else {
                String[] strings = new Gson().fromJson(json, String[].class);
                initBuildingSpinner(strings);
            }
            mBt_bind.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (TextUtils.isEmpty(ElectricityFragment.this.mBuildingName)) {
                        return;
                    }
                    mRoomName = mEt_input.getText().toString();
                    if (TextUtils.isEmpty(mRoomName)) {
                        ToastUtils.getInstance().showTestShort("请输入房间号");
                        return;
                    }
                    mLl_input.setVisibility(View.INVISIBLE);
                    mPb_loading.setVisibility(View.VISIBLE);
                    getEle();
                }
            });
        } else {
            mDataList = new Gson().fromJson(eleUse, new TypeToken<List<ElectricityItem.Data>>() {
            }.getType());
            showEle();
        }
    }

    private void getEle() {
        SPEleManager.getInstance().getEle(ElectricityFragment.this.mBuildingName, mRoomName, new SPEleManager
                .SPELECallBack() {
            @Override
            public void onSuccess(Object list) {
                mDataList = (List<ElectricityItem.Data>) list;
                if (mDataList.size() == 0) {
                    mPb_loading.setVisibility(View.INVISIBLE);
                    mLl_input.setVisibility(View.VISIBLE);
                    return;
                }
                showEle();
                SharedPreferenceUtils.getInstance().putString("eleUse", new Gson().toJson(mDataList));
                SharedPreferenceUtils.getInstance().putString("roomName", mRoomName);
                SharedPreferenceUtils.getInstance().putString("buildingName", mBuildingName);
            }

            @Override
            public void onError(int errorCode, String errorMessage) {
                mPb_loading.setVisibility(View.INVISIBLE);
                if (TextUtils.isEmpty(mRoomName)) {
                    mLl_input.setVisibility(View.VISIBLE);
                    mLl_show.setVisibility(View.INVISIBLE);
                } else {
                    mLl_input.setVisibility(View.INVISIBLE);
                    mLl_show.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void showEle() {
        mDate = new String[mDataList.size()];
        mSurplus = new Double[mDataList.size()];
        mYesUse = new Double[mDataList.size()];
        for (int i = 0; i < mDataList.size(); i++) {
            mSurplus[i] = mDataList.get(i).surplus;
            mDate[i] = mDataList.get(i).date;
            mYesUse[i] = mDataList.get(i).yesUse;
        }
        initAdapter(mDate, mSpinner_date);
        mTv_room_name.setText(mBuildingName.replaceAll("\"", "") + "\r\n" + mRoomName.replaceAll("\"", ""));
        mSpinner_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mTv_surplus.setText("剩余电费: \r\n" + mSurplus[position]);
                String yesUse = position == mSurplus.length - 1 ?
                        "---" : mYesUse[position] + "";
                mTv_yesUse.setText("昨日用电：\r\n" + yesUse);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mTv_surplus.setText("剩余电费: \r\n" + mSurplus[0]);
                mTv_yesUse.setText("昨日用电：\r\n" + mYesUse[0]);
            }
        });
        mPb_loading.setVisibility(View.INVISIBLE);
        mLl_show.setVisibility(View.VISIBLE);
    }

    private void initBuildingSpinner(String[] list) {
        initAdapter(list, mSpinner_building);
        mSpinner_building.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ElectricityFragment.this.mBuildingName = mAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ElectricityFragment.this.mBuildingName = null;
            }
        });
    }


    public void initAdapter(String[] strings, Spinner spinner) {
        mAdapter = new ArrayAdapter<>(getActivity(), R.layout
                .simple_spinner_item,
                strings);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(mAdapter);
    }


    @Override
    public String getPageName() {
        return "查电费";
    }
}
