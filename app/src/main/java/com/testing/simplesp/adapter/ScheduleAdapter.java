package com.testing.simplesp.adapter;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.testing.simplesp.R;
import com.testing.simplesp.base.BaseAdapter;
import com.testing.simplesp.base.BaseViewHolder;
import com.testing.simplesp.domain.ScheduleItem;
import com.testing.simplesp.lib.SP;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/6/8.
 */
public class ScheduleAdapter extends BaseAdapter {
    public static List<ScheduleItem.Data> mValues = new ArrayList<>();

    public static final int ROW_COUNT = 7;
    public static final int COLUMN_COUNT = 6;
    public static final int COUNT = ROW_COUNT * COLUMN_COUNT;

    static {
        String date = DateUtils.formatDateTime(SP.getContext(), System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_DATE);
        addItem(createScheduleItem(date, "", "", "", 0));
        addItem(createScheduleItem("一", "", "", "", 1));
        addItem(createScheduleItem("二", "", "", "", 2));
        addItem(createScheduleItem("三", "", "", "", 3));
        addItem(createScheduleItem("四", "", "", "", 4));
        addItem(createScheduleItem("五", "", "", "", 5));
        for (int i = COLUMN_COUNT; i <= COUNT - 1; i++) {
            addItem(createScheduleItem("", "", "", "", i));
        }
        for (int i = 1; i <= 6; i++) {
            String name = "" + (i * 2 - 1) + "、" + (i * 2) + "节";
            mValues.remove(i * COLUMN_COUNT);
            mValues.add(i * COLUMN_COUNT, createScheduleItem(name, "", "", "", i * COLUMN_COUNT));
        }
    }


    public void setValues(List<ScheduleItem.Data> datas) {
        for (ScheduleItem.Data data : datas) {
            int pos = data.pos;
            mValues.remove(pos);
            mValues.add(pos, data);
        }
    }

    private static void addItem(ScheduleItem.Data item) {
        mValues.add(item);
    }

    private static ScheduleItem.Data createScheduleItem(String couName, String week, String date,
                                                        String classpos, int pos) {
        return new ScheduleItem.Data(couName, week, date, classpos, pos);
    }

    public ScheduleAdapter() {
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_schedule_item_common, parent, false);
        return new CommonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

        if (holder instanceof CommonViewHolder) {
            final CommonViewHolder commonViewHolder = (CommonViewHolder) holder;
            ScheduleItem.Data data = mValues.get(position);
            commonViewHolder.mContentView.setText(data.name);
            if (position == 0) {

                commonViewHolder.mContentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String date = DateUtils.formatDateTime(SP.getContext(), System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_DATE);
                        String weekDay = DateUtils.formatDateTime(SP.getContext(), System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_WEEKDAY);
                        commonViewHolder.mContentView.setText(commonViewHolder.flag ? date : weekDay);
                        commonViewHolder.flag = commonViewHolder.flag ? false : true;
                    }
                });
            }
            //System.out.println(commonViewHolder.mContentView.getHeight());
            //commonViewHolder.mContentView.setLayoutParams(new LinearLayout.LayoutParams(ScheduleFragment.mWidth / 6,
            //        commonViewHolder.mContentView.getMaxHeight()));

        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class CommonViewHolder extends BaseViewHolder {
        public final View mView;
        public final TextView mContentView;
        public boolean flag = false;

        public CommonViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
