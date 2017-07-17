package com.yline.view.recycler.simple;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.yline.view.recycler.decoration.LinearItemDecoration;

/**
 * 简单的绘制 线性 分割线封装
 *
 * @author yline 2017/5/9 -- 17:23
 * @version 1.0.0
 */
public class SimpleLinearItemDecoration extends LinearItemDecoration
{
	public SimpleLinearItemDecoration(Context context)
	{
		super(context);
	}

	@Override
	public boolean isDrawDivide(int totalCount, int currentPosition)
	{
		// 头部
		if (getHeadNumber() > currentPosition)
		{
			return false;
		}

		// 底部
		if (currentPosition > totalCount - 1 - getFootNumber())
		{
			return false;
		}

		// 最后一个
		if ((currentPosition == totalCount - 1 - getFootNumber()) && !isDivideLastLine())
		{
			return false;
		}

		return true;
	}

	@Override
	public void setVerticalOffsets(Rect outRect, Drawable divider, int currentPosition)
	{
		outRect.set(0, 0, 0, divider.getIntrinsicHeight());
	}

	@Override
	public void setHorizontalOffsets(Rect outRect, Drawable divider, int currentPosition)
	{
		outRect.set(0, 0, divider.getIntrinsicWidth(), 0);
	}

	@Override
	public void drawVerticalDivider(Canvas c, Drawable divide, int currentPosition, int childLeft, int childTop, int childRight, int childBottom)
	{
		divide.setBounds(childLeft, childBottom, childRight, childBottom + divide.getIntrinsicHeight());
		divide.draw(c);
	}

	@Override
	public void drawHorizontalDivider(Canvas c, Drawable divide, int currentPosition, int childLeft, int childTop, int childRight, int childBottom)
	{
		divide.setBounds(childRight, childTop, childRight + divide.getIntrinsicWidth(), childBottom);
		divide.draw(c);
	}

	/**
	 * 确定头部有几个不绘制分割线
	 *
	 * @return
	 */
	protected int getHeadNumber()
	{
		return 0;
	}

	/**
	 * 确定底部有几个不绘制分割线
	 *
	 * @return
	 */
	protected int getFootNumber()
	{
		return 0;
	}

	/**
	 * 最后一个分割线是否绘制
	 *
	 * @return
	 */
	protected boolean isDivideLastLine()
	{
		return false;
	}
}
