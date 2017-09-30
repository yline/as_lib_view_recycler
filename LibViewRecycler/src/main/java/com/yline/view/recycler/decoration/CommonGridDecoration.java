package com.yline.view.recycler.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;

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
	protected boolean isDrawDivide(RecyclerView.Adapter adapter, int totalCount, int currentPosition) {
		return super.isDrawDivide(adapter, totalCount, currentPosition);
	}

	@Override
	protected void setVerticalItemOffsets(RecyclerView parent, Rect outRect, boolean[] spanParam) {
		super.setVerticalItemOffsets(parent, outRect, spanParam);
	}

	@Override
	protected void drawVerticalDivider(Canvas c, RecyclerView parent, int[] drawParam, boolean[] spanParam) {
		super.drawVerticalDivider(c, parent, drawParam, spanParam);
	}

	@Override
	protected void setHorizontalItemOffsets(RecyclerView parent, Rect outRect, boolean[] spanParam) {
		super.setHorizontalItemOffsets(parent, outRect, spanParam);
	}

	@Override
	protected void drawHorizontalDivider(Canvas c, RecyclerView parent, int[] drawParam, boolean[] spanParam) {
		super.drawHorizontalDivider(c, parent, drawParam, spanParam);
	}

	@Override
	protected boolean[] isSpanDraw() {
		return super.isSpanDraw();
	}
}
