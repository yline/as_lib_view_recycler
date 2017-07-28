package com.yline.view.recycler.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

/**
 * 简单的绘制 表格 分割线封装
 *
 * @author yline 2017/5/10 -- 9:32
 * @version 1.0.0
 */
public class CommonGridDecoration extends GridItemDecoration
{
	public CommonGridDecoration(Context context)
	{
		super(context);
	}

	@Override
	public boolean isDrawDivide(int totalCount, int currentPosition)
	{
		// 头部
		if (currentPosition < getHeadNumber())
		{
			return false;
		}

		// 底部
		if (currentPosition > totalCount - 1 - getFootNumber())
		{
			return false;
		}

		return true;
	}

	@Override
	public void drawVerticalDivider(Canvas c, Drawable divide, int currentPosition, int childLeft, int childTop, int childRight, int childBottom)
	{
		divide.setBounds(childRight, childTop, childRight + divide.getIntrinsicWidth(), childBottom);
		divide.draw(c);
	}

	@Override
	public void drawHorizontalDivider(Canvas c, Drawable divide, int currentPosition, int childLeft, int childTop, int childRight, int childBottom)
	{
		divide.setBounds(childLeft, childBottom, childRight + divide.getIntrinsicWidth(), childBottom + divide.getIntrinsicHeight());
		divide.draw(c);
	}

	@Override
	public boolean isLastLine(int totalCount, int currentPosition, int spanCount)
	{
		totalCount = totalCount - getHeadNumber() - getFootNumber();
		currentPosition = currentPosition - getHeadNumber();
		return super.isLastLine(totalCount, currentPosition, spanCount);
	}

	@Override
	public boolean isLastSpan(int currentPosition, int spanCount)
	{
		currentPosition = currentPosition - getHeadNumber();
		return super.isLastSpan(currentPosition, spanCount);
	}

	/**
	 * 头部的数量
	 *
	 * @return
	 */
	protected int getHeadNumber()
	{
		return 0;
	}

	/**
	 * 底部的数量
	 *
	 * @return
	 */
	protected int getFootNumber()
	{
		return 0;
	}
}
