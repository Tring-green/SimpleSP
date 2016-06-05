package com.testing.simplesp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.testing.simplesp.R;

public class SimpleFragment extends Fragment {

        public SimpleFragment() {
        }

        public static SimpleFragment newInstance(int sectionNumber) {
            SimpleFragment fragment = new SimpleFragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
