package com.testing.simplesp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.testing.simplesp.R;
import com.testing.simplesp.activity.DetailDocumentActivity;
import com.testing.simplesp.base.BaseAdapter;
import com.testing.simplesp.base.BaseViewHolder;
import com.testing.simplesp.db.DocumentItemDao;
import com.testing.simplesp.domain.DocumentItem.Data;
import com.testing.simplesp.fragment.DocumentFragment;
import com.testing.simplesp.lib.SPDocumentManager;
import com.testing.simplesp.utils.ThreadUtils;

import java.util.ArrayList;
import java.util.List;

public class DocumentAdapter extends BaseAdapter {
    private static final int COMMON_VIEW = 0;
    private static final int MORE_VIEW = 1;
    private static final int ERROR_VIEW = 2;

    public static final int LOADING_NOW = 0;
    public static final int LOADING_ERROR = 1;
    public static final int LOADING_DOWN = 2;
    public static final int LOADING_NO_VALUE = 3;

    public static int LOADING_SIGN = LOADING_NO_VALUE;
    public static List<Data> mValues = new ArrayList<>();
    private Activity mActivity;

    public DocumentAdapter(Activity activity) {
        mActivity = activity;
        SPDocumentManager.getInstance().More(SPDocumentManager.LOAD_MORE, DocumentFragment.mSrl);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder holder = null;
        View view;
        switch (viewType) {
            case COMMON_VIEW:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_document_item_common, parent, false);
                holder = new CommonViewHolder(view);
                break;
            case MORE_VIEW:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .fragment_document_item_loading_more, parent, false);
                holder = new MoreViewHolder(view);
                break;
            case ERROR_VIEW:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .fragment_document_item_loading, parent, false);
                holder = new ErrorViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        if (holder instanceof CommonViewHolder) {
            final CommonViewHolder commonViewHolder = (CommonViewHolder) holder;
            Data item = mValues.get(position);
            commonViewHolder.mUnitView.setText(item.getUnit() + "  " + item.getTime());
            commonViewHolder.mTitleView.setText(item.getTitle());
            commonViewHolder.mContentView.setText(item.getContent());

            commonViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, DetailDocumentActivity.class);
                    intent.putExtra("currentPosition", position);
                    mActivity.startActivity(intent);
                }
            });
        } else if (holder instanceof MoreViewHolder) {
            final MoreViewHolder moreViewHolder = (MoreViewHolder) holder;
            moreViewHolder.mLoadError.setVisibility(
                    LOADING_SIGN == LOADING_ERROR ? View.VISIBLE : View.INVISIBLE);
            moreViewHolder.mLoadMore.setVisibility(
                    LOADING_SIGN == LOADING_ERROR ? View.INVISIBLE : View.VISIBLE);
            moreViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (LOADING_SIGN == LOADING_ERROR) {
                        System.out.println("error loading!");
                        LOADING_SIGN = LOADING_NOW;
                        ThreadUtils.getInstance().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                notifyDataSetChanged();
                            }
                        });
                        addMore();
                    }
                }
            });
        } else {
            ErrorViewHolder moreViewHolder = (ErrorViewHolder) holder;
            moreViewHolder.mProgressBar.setVisibility(
                    LOADING_SIGN == LOADING_NO_VALUE ? View.VISIBLE : View.INVISIBLE
            );
            moreViewHolder.mTextView.setVisibility(
                    LOADING_SIGN == LOADING_NO_VALUE ? View.INVISIBLE : View.VISIBLE
            );
        }
    }

    public void addMore() {
        DocumentAdapter.LOADING_SIGN = DocumentAdapter.LOADING_NOW;
        List<Data> newValues;
        System.out.println(mValues.size());
        newValues = mValues.size() == 0 ? null : DocumentItemDao.getInstance().getDocumentItemById(mValues.get
                (mValues.size() - 1).getId()-1);
        if (newValues == null) {
            SPDocumentManager.getInstance().More(SPDocumentManager.ADD_MORE, this);
            Log.d("DocumentAdapter", "no value");
        } else {
            mValues.addAll(newValues);
            ThreadUtils.getInstance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                    DocumentAdapter.LOADING_SIGN = LOADING_DOWN;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mValues.size() == 0) {
            return ERROR_VIEW;
        }
        if (position == mValues.size())
            return MORE_VIEW;
        else
            return COMMON_VIEW;
    }

    public class CommonViewHolder extends BaseViewHolder {
        public final View mView;
        public final TextView mUnitView;
        public final TextView mTitleView;
        public final TextView mContentView;

        public CommonViewHolder(View view) {
            super(view);
            mView = view;
            mUnitView = (TextView) view.findViewById(R.id.unit);
            mTitleView = (TextView) view.findViewById(R.id.title);
            mContentView = (TextView) view.findViewById(R.id.content);
        }


        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public class MoreViewHolder extends BaseViewHolder {
        public final View mView;
        public final RelativeLayout mLoadMore;
        public final RelativeLayout mLoadError;

        public MoreViewHolder(View view) {
            super(view);
            mView = view;
            mLoadMore = (RelativeLayout) view.findViewById(R.id.rl_load_more);
            mLoadError = (RelativeLayout) view.findViewById(R.id.rl_load_error);
        }
    }

    public class ErrorViewHolder extends BaseViewHolder {
        public final View mView;
        public final ProgressBar mProgressBar;
        public final TextView mTextView;

        public ErrorViewHolder(View view) {
            super(view);
            mView = view;
            mProgressBar = (ProgressBar) mView.findViewById(R.id.pb_loading);
            mTextView = (TextView) mView.findViewById(R.id.tv_load_error);
        }
    }

}
