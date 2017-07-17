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

/**
 * 公共的 LinearItemDecoration
 *
 * @author yline 2017/5/23 -- 10:31
 * @version 1.0.0
 */
public abstract class LinearItemDecoration extends RecyclerView.ItemDecoration
{
	private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

	protected Drawable sDivider;

	public LinearItemDecoration(Context context)
	{
		if (-1 != getDivideResourceId())
		{
			sDivider = ContextCompat.getDrawable(context, getDivideResourceId());
		}
		else
		{
			final TypedArray a = context.obtainStyledAttributes(ATTRS);
			sDivider = a.getDrawable(0);
			a.recycle();
		}
	}

	@Override
	public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state)
	{
		super.onDraw(c, parent, state);

		if (getOrientation() == LinearLayoutManager.VERTICAL)
		{
			drawVertical(c, parent);
		}
		else if (getOrientation() == LinearLayoutManager.HORIZONTAL)
		{
			drawHorizontal(c, parent);
		}
	}

	private void drawVertical(Canvas c, RecyclerView parent)
	{
		final int childLeft = parent.getPaddingLeft();
		final int childRight = parent.getWidth() - parent.getPaddingRight();
		final int childTotalCount = parent.getAdapter().getItemCount();
		final int childCount = parent.getChildCount();

		int currentPosition;
		for (int i = 0; i < childCount; i++)
		{
			final View child = parent.getChildAt(i);
			currentPosition = parent.getChildAdapterPosition(child);

			if (isDrawDivide(childTotalCount, currentPosition))
			{
				final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
				final int childTop = child.getTop() - params.topMargin;
				final int childBottom = child.getBottom() + params.bottomMargin;

				drawVerticalDivider(c, sDivider, currentPosition, childLeft, childTop, childRight, childBottom);
			}
		}
	}

	private void drawHorizontal(Canvas c, RecyclerView parent)
	{
		final int childTop = parent.getPaddingTop();
		final int childBottom = parent.getHeight() - parent.getPaddingBottom();
		final int childCount = parent.getChildCount();
		final int childTotalCount = parent.getAdapter().getItemCount();

		int currentPosition;
		for (int i = 0; i < childCount; i++)
		{
			final View child = parent.getChildAt(i);
			currentPosition = parent.getChildAdapterPosition(child);

			if (isDrawDivide(childTotalCount, currentPosition))
			{
				final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
				final int childLeft = child.getLeft() - params.leftMargin;
				final int childRight = child.getRight() + params.rightMargin;

				drawHorizontalDivider(c, sDivider, currentPosition, childLeft, childTop, childRight, childBottom);
			}
		}
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
	{
		super.getItemOffsets(outRect, view, parent, state);

		final int currentPosition = parent.getChildAdapterPosition(view);
		final int totalCount = parent.getAdapter().getItemCount();
		if (isDrawDivide(totalCount, currentPosition))
		{
			if (getOrientation() == LinearLayoutManager.VERTICAL)
			{
				setVerticalOffsets(outRect, sDivider, currentPosition);
			}
			else if (getOrientation() == LinearLayoutManager.HORIZONTAL)
			{
				setHorizontalOffsets(outRect, sDivider, currentPosition);
			}
		}
	}

	/**
	 * 判断是否绘制分割线
	 *
	 * @param totalCount      数据的最大量
	 * @param currentPosition 当前的位置
	 * @return
	 */
	public abstract boolean isDrawDivide(int totalCount, int currentPosition);

	/**
	 * 设置 垂直 偏移量
	 *
	 * @param outRect
	 * @param divider
	 * @param currentPosition
	 */
	public abstract void setVerticalOffsets(Rect outRect, Drawable divider, int currentPosition);

	/**
	 * 设置 水平 偏移量
	 *
	 * @param outRect
	 * @param divider
	 * @param currentPosition
	 */
	public abstract void setHorizontalOffsets(Rect outRect, Drawable divider, int currentPosition);

	/**
	 * 绘制 分割线
	 *
	 * @param c
	 * @param divide
	 * @param currentPosition
	 * @param childLeft
	 * @param childTop
	 * @param childRight
	 * @param childBottom
	 */
	public abstract void drawVerticalDivider(Canvas c, Drawable divide, int currentPosition, int childLeft, int childTop, int childRight, int childBottom);

	/**
	 * 绘制 分割线
	 *
	 * @param c
	 * @param divide
	 * @param currentPosition
	 * @param childLeft
	 * @param childTop
	 * @param childRight
	 * @param childBottom
	 */
	public abstract void drawHorizontalDivider(Canvas c, Drawable divide, int currentPosition, int childLeft, int childTop, int childRight, int childBottom);

	/* %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 提供重写的参数 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% */

	/**
	 * 分割线资源
	 *
	 * @return
	 */
	protected int getDivideResourceId()
	{
		return -1;
	}

	/**
	 * 方向
	 *
	 * @return
	 */
	protected int getOrientation()
	{
		return LinearLayoutManager.VERTICAL;
	}
}
