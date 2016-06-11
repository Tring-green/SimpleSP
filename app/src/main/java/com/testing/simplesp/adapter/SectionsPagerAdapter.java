package com.testing.simplesp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.testing.simplesp.base.BaseFragment;
import com.testing.simplesp.fragment.DocumentFragment;
import com.testing.simplesp.fragment.ElectricityFragment;
import com.testing.simplesp.fragment.ScheduleFragment;

import java.util.ArrayList;
import java.util.List;

public class SectionsPagerAdapter extends FragmentPagerAdapter{

    List<BaseFragment> mFragmentList = new ArrayList<>();

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentList.add(DocumentFragment.newInstance());
        mFragmentList.add(ScheduleFragment.newInstance());
        mFragmentList.add(ElectricityFragment.newInstance());
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentList.get(position).getPageName();
    }


}