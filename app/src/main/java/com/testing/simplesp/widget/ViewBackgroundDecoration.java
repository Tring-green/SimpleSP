package com.testing.simplesp.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhy
 */
public class ViewBackgroundDecoration extends RecyclerView.ItemDecoration {

    Map<String, Paint> mMap = new HashMap<>();

    public ViewBackgroundDecoration(onDrawListener listener) {
        mListener = listener;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, State state) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            RectF rectF = new RectF();
            rectF.left = child.getLeft() - params.leftMargin;
            rectF.right = child.getRight() - params.rightMargin;
            rectF.top = child.getTop() + params.topMargin;
            rectF.bottom = child.getBottom() - params.bottomMargin;
            Paint paint = mListener.onDraw(mMap, child);
            c.drawRect(rectF, paint);
        }
    }

    onDrawListener mListener;

    public interface onDrawListener {
        /**
         * @param map    保存颜色信息的哈希图
         * @param child 子视图
         * @return 修改界面的画笔
         */
        Paint onDraw(Map<String, Paint> map, View child);
    }
}