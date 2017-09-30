package com.yline.view.recycler.decoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yline.view.recycler.holder.Callback;

/**
 * 公共的 LinearItemDecoration
 * 处理了空数据、头部和底部三种情况
 *
 * @author yline 2017/5/23 -- 10:31
 * @version 1.0.0
 */
abstract class LinearItemDecoration extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    protected Drawable sDivider;

    LinearItemDecoration(Context context) {
        if (-1 != getDivideResourceId()) {
            sDivider = ContextCompat.getDrawable(context, getDivideResourceId());
        } else {
            final TypedArray a = context.obtainStyledAttributes(ATTRS);
            sDivider = a.getDrawable(0);
            a.recycle();
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) layoutManager).getOrientation() == LinearLayoutManager.VERTICAL) {
                drawVertical(c, parent);
            } else {
                drawHorizontal(c, parent);
            }
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        final int childLeft = parent.getPaddingLeft();
        final int childRight = parent.getWidth() - parent.getPaddingRight();
        final int childTotalCount = parent.getAdapter().getItemCount();
        final int childCount = parent.getChildCount();

        int currentPosition;
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            currentPosition = parent.getChildAdapterPosition(child);

            if (isDrawDivide(parent.getAdapter(), childTotalCount, currentPosition)) {
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int childTop = child.getTop() - params.topMargin;
                final int childBottom = child.getBottom() + params.bottomMargin;

                drawVerticalDivider(c, parent, currentPosition, childLeft, childTop, childRight, childBottom);
            }
        }
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        final int childTop = parent.getPaddingTop();
        final int childBottom = parent.getHeight() - parent.getPaddingBottom();
        final int childCount = parent.getChildCount();
        final int childTotalCount = parent.getAdapter().getItemCount();

        int currentPosition;
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            currentPosition = parent.getChildAdapterPosition(child);

            if (isDrawDivide(parent.getAdapter(), childTotalCount, currentPosition)) {
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int childLeft = child.getLeft() - params.leftMargin;
                final int childRight = child.getRight() + params.rightMargin;

                drawHorizontalDivider(c, parent, currentPosition, childLeft, childTop, childRight, childBottom);
            }
        }
    }

    /**
     * 判断是否绘制分割线
     *
     * @param totalCount      数据的最大量
     * @param currentPosition 当前的位置
     * @return true 则绘制
     */
    protected boolean isDrawDivide(RecyclerView.Adapter adapter, int totalCount, int currentPosition) {
        if (adapter instanceof Callback.IHeadFootCallback) {
            // 头部
            if (((Callback.IHeadFootCallback) adapter).getHeadersCount() > currentPosition) {
                return false;
            }

            // 底部
            if (currentPosition > totalCount - 1 - ((Callback.IHeadFootCallback) adapter).getFootersCount()) {
                return false;
            }
        }
        return true;
    }

    /**
     * RecyclerView.Vertical 时，绘制 底部 分割线
     *
     * @param c               画布
     * @param parent          RecycleView
     * @param currentPosition item 当前位置
     * @param childLeft       item left
     * @param childTop        item top
     * @param childRight      item right
     * @param childBottom     item bottom
     */
    protected void drawVerticalDivider(Canvas c, RecyclerView parent, int currentPosition, int childLeft, int childTop, int childRight, int childBottom) {
        sDivider.setBounds(childLeft, childBottom, childRight, childBottom + sDivider.getIntrinsicHeight());
        sDivider.draw(c);
    }

    /**
     * RecyclerView.Horizontal 时，绘制 右侧 分割线
     *
     * @param c               画布
     * @param parent          RecyclerView
     * @param currentPosition item 当前位置
     * @param childLeft       item left
     * @param childTop        item top
     * @param childRight      item right
     * @param childBottom     item bottom
     */
    protected void drawHorizontalDivider(Canvas c, RecyclerView parent, int currentPosition, int childLeft, int childTop, int childRight, int childBottom) {
        sDivider.setBounds(childRight, childTop, childRight + sDivider.getIntrinsicWidth(), childBottom);
        sDivider.draw(c);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            final int currentPosition = parent.getChildAdapterPosition(view);
            final int totalCount = parent.getAdapter().getItemCount();
            if (isDrawDivide(parent.getAdapter(), totalCount, currentPosition)) {
                if (((LinearLayoutManager) layoutManager).getOrientation() == LinearLayoutManager.VERTICAL) {
                    setVerticalOffsets(outRect, parent, currentPosition);
                } else {
                    setHorizontalOffsets(outRect, parent, currentPosition);
                }
            }
        }
    }

    /**
     * RecyclerView.Vertical 时，设置 底部 偏移量
     *
     * @param outRect         设置的对象
     * @param parent          父控件
     * @param currentPosition 当前位置
     */
    protected void setVerticalOffsets(Rect outRect, RecyclerView parent, int currentPosition) {
        outRect.set(0, 0, 0, sDivider.getIntrinsicHeight());
    }

    /**
     * RecyclerView.Horizontal 时，设置 右边 偏移量
     *
     * @param outRect         设置的对象
     * @param parent          父控件
     * @param currentPosition 当前位置
     */
    protected void setHorizontalOffsets(Rect outRect, RecyclerView parent, int currentPosition) {
        outRect.set(0, 0, sDivider.getIntrinsicWidth(), 0);
    }

	/* &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& 参数重写 &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& */

    /**
     * 分割线资源
     *
     * @return 分割线资源
     */
    protected int getDivideResourceId() {
        return -1;
    }
}
