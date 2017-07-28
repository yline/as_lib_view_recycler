package com.yline.view.recycler.decoration;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.yline.view.recycler.callback.IHeadFootCallback;

/**
 * 绘制 线性 分割线封装
 * 在 处理了空数据、头部和底部三种情况外，处理了最后一个是否绘制的情况
 *
 * @author yline 2017/5/9 -- 17:23
 * @version 1.0.0
 */
public class CommonLinearDecoration extends LinearItemDecoration
{
	public CommonLinearDecoration(Context context)
	{
		super(context);
	}

	@Override
	protected boolean isDrawDivide(RecyclerView.Adapter adapter, int totalCount, int currentPosition)
	{
		if (adapter instanceof IHeadFootCallback)
		{
			// 最后一个数据
			if ((currentPosition == totalCount - 1 - ((IHeadFootCallback) adapter).getFootersCount()) && !isDivideLastLine())
			{
				return false;
			}
		}
		return super.isDrawDivide(adapter, totalCount, currentPosition);
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
