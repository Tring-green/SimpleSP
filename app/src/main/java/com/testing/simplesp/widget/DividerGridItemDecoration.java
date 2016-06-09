package com.testing.simplesp.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;
import android.widget.TextView;

import com.testing.simplesp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author zhy
 */
public class DividerGridItemDecoration extends RecyclerView.ItemDecoration {

    private List<int[]> mList;

    public DividerGridItemDecoration(int size) {
        Random random = new Random();
        mList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            mList.add(new int[]{80 + random.nextInt(80), 40 + random.nextInt(80),
                    40 + random.nextInt(80 + 30), 40 + random.nextInt(80)});
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, State state) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            TextView content = (TextView) child.findViewById(R.id.content);
            int position = parent.getChildAdapterPosition(child);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            int[] ints = mList.get(position);
            if (content.getText() == "") {
                paint.setColor(Color.GRAY);
                paint.setAlpha(80);
            } else
                paint.setARGB(ints[0], ints[1], ints[2], ints[3]);
            RectF rectF = new RectF();
            rectF.left = child.getLeft() - params.leftMargin;
            rectF.right = child.getRight() - params.rightMargin;
            rectF.top = child.getTop() - params.topMargin;
            rectF.bottom = child.getBottom() - params.bottomMargin;
            c.drawRoundRect(rectF, 10, 10, paint);
        }


    }
}