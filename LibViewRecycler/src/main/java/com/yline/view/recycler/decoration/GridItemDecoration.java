package com.yline.view.recycler.decoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * 公众的 GridItemDecoration
 *
 * @author yline 2017/5/23 -- 10:30
 * @version 1.0.0
 */
public abstract class GridItemDecoration extends RecyclerView.ItemDecoration
{
	private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

	protected Drawable sDivider;

	public GridItemDecoration(Context context)
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
		final int childCount = parent.getChildCount();
		final int totalCount = parent.getAdapter().getItemCount();
		int spanCount = getSpanCount(parent);

		int currentPosition;
		for (int i = 0; i < childCount; i++)
		{
			final View child = parent.getChildAt(i);

			currentPosition = parent.getChildAdapterPosition(child);
			if (isDrawDivide(totalCount, currentPosition))
			{
				if (isLastLine(totalCount, currentPosition, spanCount))
				{
					int orientation = getOrientation(parent);
					if (orientation == LinearLayoutManager.VERTICAL)
					{
						drawVertical(c, child, currentPosition);
					}
					else
					{
						drawHorizontal(c, child, currentPosition);
					}
				}
				else
				{
					drawHorizontal(c, child, currentPosition);
					drawVertical(c, child, currentPosition);
				}
			}
		}
	}

	protected void drawHorizontal(Canvas c, View child, int currentPosition)
	{
		final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

		final int childTop = child.getTop() - params.topMargin;
		final int childBottom = child.getBottom() + params.bottomMargin;
		final int childLeft = child.getLeft() - params.leftMargin;
		final int childRight = child.getRight() + params.rightMargin;

		drawHorizontalDivider(c, sDivider, currentPosition, childLeft, childTop, childRight, childBottom);
	}

	protected void drawVertical(Canvas c, View child, int currentPosition)
	{
		final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
		final int childTop = child.getTop() - params.topMargin;
		final int childBottom = child.getBottom() + params.bottomMargin;
		final int childLeft = child.getLeft() - params.leftMargin;
		final int childRight = child.getRight() + params.rightMargin;

		drawVerticalDivider(c, sDivider, currentPosition, childLeft, childTop, childRight, childBottom);
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
	{
		super.getItemOffsets(outRect, view, parent, state);
		int spanCount = getSpanCount(parent);
		int totalCount = parent.getAdapter().getItemCount();

		int currentPosition = parent.getChildAdapterPosition(view);
		if (isDrawDivide(totalCount, currentPosition))
		{
			int orientation = getOrientation(parent);
			boolean isLastLine = isLastLine(totalCount, currentPosition, spanCount);
			boolean isLastSpan = isLastSpan(currentPosition, spanCount);

			setItemOffsets(outRect, sDivider, orientation, isLastLine, isLastSpan);
		}
	}

	/**
	 * 获取当前控件的方向
	 *
	 * @param parent
	 * @return
	 */
	protected int getOrientation(RecyclerView parent)
	{
		int orientation = -1;
		RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
		if (layoutManager instanceof GridLayoutManager)
		{
			orientation = ((GridLayoutManager) layoutManager).getOrientation();
		}
		else if (layoutManager instanceof StaggeredGridLayoutManager)
		{
			orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
		}
		return orientation;
	}

	/**
	 * 获取行数 或 列数
	 *
	 * @param parent
	 * @return
	 */
	protected int getSpanCount(RecyclerView parent)
	{
		// 列数
		int spanCount = -1;
		RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
		if (layoutManager instanceof GridLayoutManager)
		{
			spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
		}
		else if (layoutManager instanceof StaggeredGridLayoutManager)
		{
			spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
		}
		return spanCount;
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

	/**
	 * 校验是否是最后一行(可重写)
	 * 默认按照标准排列计算
	 * 默认最后一行，不绘制底部
	 *
	 * @param totalCount      标准排列情况下的总数
	 * @param currentPosition 标准排列下的当前位置
	 * @param spanCount       标准排列情况下的列数
	 */
	public boolean isLastLine(int totalCount, int currentPosition, int spanCount)
	{
		int tempTotalCount = totalCount / spanCount * spanCount + (totalCount % spanCount == 0 ? 0 : spanCount);

		if (currentPosition >= tempTotalCount - spanCount && currentPosition < tempTotalCount)
		{
			return true;
		}

		return false;
	}

	/**
	 * 校验是否是最后一列(可重写)
	 *
	 * @param currentPosition 标准排列情况下的总数
	 * @param spanCount       标准排列情况下的列数
	 * @return
	 */
	public boolean isLastSpan(int currentPosition, int spanCount)
	{
		boolean isLastSpan = currentPosition % spanCount == (spanCount - 1); // 判断是否是 最后一个span
		return isLastSpan;
	}

	/**
	 * 依据情形绘制偏移量
	 *
	 * @param outRect     设置对象
	 * @param divide      分割线资源
	 * @param orientation 布局方向
	 * @param isLastLine  是否是最后一行
	 * @param isLastSpan  是否是最后一列
	 */
	public void setItemOffsets(Rect outRect, Drawable divide, int orientation, boolean isLastLine, boolean isLastSpan)
	{
		if (isLastLine)
		{
			if (!isLastSpan)
			{
				if (LinearLayoutManager.VERTICAL == orientation)
				{
					outRect.set(0, 0, divide.getIntrinsicWidth(), 0);
				}
				else if (LinearLayoutManager.HORIZONTAL == orientation)
				{
					outRect.set(0, 0, 0, divide.getIntrinsicHeight());
				}
			}
		}
		else
		{
			if (isLastSpan)
			{
				if (LinearLayoutManager.VERTICAL == orientation)
				{
					outRect.set(0, 0, 0, divide.getIntrinsicHeight());
				}
				else if (LinearLayoutManager.HORIZONTAL == orientation)
				{
					outRect.set(0, 0, divide.getIntrinsicWidth(), 0);
				}
			}
			else
			{
				outRect.set(0, 0, divide.getIntrinsicWidth(), divide.getIntrinsicHeight());
			}
		}
	}

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
}
