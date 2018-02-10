package com.yline.view.recycler.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;

import com.yline.view.recycler.adapter.AbstractHeadFootRecyclerAdapter;

/**
 * 绘制 线性 分割线封装
 * 在 处理了空数据、头部和底部三种情况外，处理了最后一个是否绘制的情况
 *
 * @author yline 2017/5/9 -- 17:23
 * @version 1.0.0
 */
public class CommonLinearDecoration extends LinearItemDecoration {
    public CommonLinearDecoration(Context context) {
        super(context);
    }

    @Override
    protected boolean isDrawDivide(RecyclerView.Adapter adapter, int totalCount, int currentPosition) {
        if (adapter instanceof AbstractHeadFootRecyclerAdapter) {
            // 最后一个数据
            if ((currentPosition == totalCount - 1 - AbstractHeadFootRecyclerAdapter.FOOT_COUNT) && !isDivideLastLine()) {
                return false;
            }
        }
        return super.isDrawDivide(adapter, totalCount, currentPosition);
    }

    @Override
    protected void setVerticalOffsets(Rect outRect, RecyclerView parent, int currentPosition) {
        super.setVerticalOffsets(outRect, parent, currentPosition);
    }

    @Override
    protected void drawVerticalDivider(Canvas c, RecyclerView parent, int currentPosition, int childLeft, int childTop, int childRight, int childBottom) {
        super.drawVerticalDivider(c, parent, currentPosition, childLeft, childTop, childRight, childBottom);
    }

    @Override
    protected void setHorizontalOffsets(Rect outRect, RecyclerView parent, int currentPosition) {
        super.setHorizontalOffsets(outRect, parent, currentPosition);
    }

    @Override
    protected void drawHorizontalDivider(Canvas c, RecyclerView parent, int currentPosition, int childLeft, int childTop, int childRight, int childBottom) {
        super.drawHorizontalDivider(c, parent, currentPosition, childLeft, childTop, childRight, childBottom);
    }

    protected boolean isDivideLastLine() {
        return false;
    }
}
