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

import com.yline.view.recycler.adapter.CommonEmptyRecyclerAdapter;
import com.yline.view.recycler.callback.IHeadFootCallback;

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
		final int childCount = parent.getChildCount(); // 界面上 child 个数
		final int totalCount = parent.getAdapter().getItemCount(); // adapter child 总数
		int spanCount = getSpanCount(parent);

		int currentPosition;
		for (int i = 0; i < childCount; i++)
		{
			final View child = parent.getChildAt(i);
			currentPosition = parent.getChildAdapterPosition(child);
			
			// 处理 头部、底部、头部 特殊情况
			if (!isDrawDivide(parent.getAdapter(), totalCount, currentPosition))
			{
				continue;
			}
			
			int[] drawParam = initDrawParam(child);
			boolean[] spanParam = initSpanParam(parent.getAdapter(), totalCount, spanCount, currentPosition);

			if (getOrientation(parent.getLayoutManager()))
			{
				drawVerticalDivider(c, drawParam, spanParam);
			}
			else
			{
				drawHorizontalDivider(c, drawParam, spanParam);
			}
		}
	}

	/**
	 * 获取到 child 四个方位
	 *
	 * @param child
	 * @return {left, top, right, bottom}
	 */
	protected int[] initDrawParam(View child)
	{
		RecyclerView.LayoutParams childParams = (RecyclerView.LayoutParams) child.getLayoutParams();
		
		int childLeft = child.getLeft();
		int childTop = child.getTop();
		int childRight = child.getRight();
		int childBottom = child.getBottom();
		return new int[]{childLeft, childTop, childRight, childBottom};
	}

	/**
	 * 初始化，四方位判断
	 * 不需要考虑currentPos 为 头部和底部 的情况
	 *
	 * @param adapter
	 * @param totalCount 总数
	 * @param spanCount  行列数
	 * @param currentPos 当前位置
	 * @return {left, top, right, bottom}
	 */
	protected boolean[] initSpanParam(RecyclerView.Adapter adapter, int totalCount, int spanCount, int currentPos)
	{
		int headCount = 0, footCount = 0;
		if (adapter instanceof IHeadFootCallback)
		{
			headCount = ((IHeadFootCallback) adapter).getHeadersCount();
			footCount = ((IHeadFootCallback) adapter).getFootersCount();
		}

		boolean isSpanFirst = ((currentPos - headCount) % spanCount == 0);
		boolean isFirst = (currentPos - headCount >= 0 && currentPos - headCount < spanCount);
		boolean isSpanLast = ((currentPos - headCount) % spanCount == (spanCount - 1));

		int minPos = headCount + spanCount * ((totalCount - 1 - headCount - footCount) / spanCount);
		boolean isLast = (currentPos - minPos >= 0 && currentPos - minPos < spanCount);

		return new boolean[]{isSpanFirst, isFirst, isSpanLast, isLast};
	}

	/**
	 * 初始化 Vertical时，四个方向，偏移量
	 * @param spanParam
	 * @return
	 */
	protected int[] initVerticalDivider(boolean[] spanParam)
	{
		// 计算开始
		int outLeft = 0, outTop = 0, outRight = 0, outBottom = 0; // 每一个方向上，对应的宽度
		boolean isSpanFirst = spanParam[0], isFirst = spanParam[1], isSpanLast = spanParam[2], isLast = spanParam[3];
		boolean isDrawSpanFirst = isSpanDraw()[0], isDrawFirst = isSpanDraw()[1], isDrawSpanLast = isSpanDraw()[2], isDrawLast = isSpanDraw()[3];

		// 计算 外轮廓
		outLeft = isSpanFirst ? (isDrawSpanFirst ? sDivider.getIntrinsicWidth() : 0) : sDivider.getIntrinsicWidth() / 2;
		outTop = isFirst ? (isDrawFirst ? sDivider.getIntrinsicHeight() : 0) : sDivider.getIntrinsicHeight() / 2;
		outRight = isSpanLast ? (isDrawSpanLast ? sDivider.getIntrinsicHeight() : 0) : sDivider.getIntrinsicWidth() / 2;
		outBottom = isLast ? (isDrawLast ? sDivider.getIntrinsicHeight() : 0) : sDivider.getIntrinsicHeight() / 2;

		return new int[]{outLeft, outTop, outRight, outBottom};
	}

	protected void drawVerticalDivider(Canvas c, int[] drawParam, boolean[] spanParam)
	{
		// 参数排除
		if (drawParam.length != 4 || spanParam.length != 4)
		{
			return;
		}

		int[] outRect = initVerticalDivider(spanParam);
		int outLeft = outRect[0], outTop = outRect[1], outRight = outRect[2], outBottom = outRect[3]; // 每一个方向上，对应的宽度

		// 绘制开始
		int childLeft = drawParam[0], childTop = drawParam[1], childRight = drawParam[2], childBottom = drawParam[3];
		// 绘制左边
		sDivider.setBounds(childLeft - outLeft, childTop - outTop, childLeft, childBottom + outBottom);
		sDivider.draw(c);

		// 绘制顶部
		sDivider.setBounds(childLeft - outLeft, childTop - outTop, childRight + outRight, childTop);
		sDivider.draw(c);

		// 绘制右边
		sDivider.setBounds(childRight, childTop - outTop, childRight + outRight, childBottom + outBottom);
		sDivider.draw(c);

		// 绘制底部
		sDivider.setBounds(childLeft - outLeft, childBottom, childRight + outRight, childBottom + outBottom);
		sDivider.draw(c);
	}

	protected int[] initHorizontalDivider(boolean[] spanParam)
	{
		// 计算开始；换转方向上；is.
		int outLeft = 0, outTop = 0, outRight = 0, outBottom = 0; // 每一个方向上，对应的宽度
		boolean isSpanFirst = spanParam[0], isFirst = spanParam[1], isSpanLast = spanParam[2], isLast = spanParam[3];
		boolean isDrawSpanFirst = isSpanDraw()[0], isDrawFirst = isSpanDraw()[1], isDrawSpanLast = isSpanDraw()[2], isDrawLast = isSpanDraw()[3]; // 用户以为的 left,top,right,bottom

		// 计算 外轮廓
		outLeft = isFirst ? (isDrawSpanFirst ? sDivider.getIntrinsicWidth() : 0) : sDivider.getIntrinsicWidth() / 2;
		outTop = isSpanFirst ? (isDrawFirst ? sDivider.getIntrinsicHeight() : 0) : sDivider.getIntrinsicHeight() / 2;
		outRight = isLast ? (isDrawSpanLast ? sDivider.getIntrinsicHeight() : 0) : sDivider.getIntrinsicWidth() / 2;
		outBottom = isSpanLast ? (isDrawLast ? sDivider.getIntrinsicHeight() : 0) : sDivider.getIntrinsicHeight() / 2;

		return new int[]{outLeft, outTop, outRight, outBottom};
	}
	
	protected void drawHorizontalDivider(Canvas c, int[] drawParam, boolean[] spanParam)
	{
		if (drawParam.length != 4 || spanParam.length != 4)
		{
			return;
		}

		// 获取，横向方向上的，left,top,right,bottom
		int[] outRect = initHorizontalDivider(spanParam);
		int outLeft = outRect[0], outTop = outRect[1], outRight = outRect[2], outBottom = outRect[3]; // 每一个方向上，对应的宽度

		// 绘制开始
		int childLeft = drawParam[0], childTop = drawParam[1], childRight = drawParam[2], childBottom = drawParam[3];
		// 绘制左边
		sDivider.setBounds(childLeft - outLeft, childTop - outTop, childLeft, childBottom + outBottom);
		sDivider.draw(c);

		// 绘制顶部
		sDivider.setBounds(childLeft - outLeft, childTop - outTop, childRight + outRight, childTop);
		sDivider.draw(c);

		// 绘制右边
		sDivider.setBounds(childRight, childTop - outTop, childRight + outRight, childBottom + outBottom);
		sDivider.draw(c);

		// 绘制底部
		sDivider.setBounds(childLeft - outLeft, childBottom, childRight + outRight, childBottom + outBottom);
		sDivider.draw(c);
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
	{
		super.getItemOffsets(outRect, view, parent, state);
		int spanCount = getSpanCount(parent);
		int totalCount = parent.getAdapter().getItemCount();

		int currentPosition = parent.getChildAdapterPosition(view);
		if (!isDrawDivide(parent.getAdapter(), totalCount, currentPosition))
		{
			return;
		}

		boolean[] spanParam = initSpanParam(parent.getAdapter(), totalCount, spanCount, currentPosition);
		if (getOrientation(parent.getLayoutManager()))
		{
			setVerticalItemOffsets(outRect, spanParam);
		}
		else
		{
			setHorizontalItemOffsets(outRect, spanParam);
		}
	}

	protected void setVerticalItemOffsets(Rect outRect, boolean[] spanParam)
	{
		int[] initRect = initVerticalDivider(spanParam);
		outRect.set(initRect[0], initRect[1], initRect[2], initRect[3]);
	}

	protected void setHorizontalItemOffsets(Rect outRect, boolean[] spanParam)
	{
		int[] initRect = initHorizontalDivider(spanParam);
		outRect.set(initRect[0], initRect[1], initRect[2], initRect[3]);
	}

	/**
	 * 获取当前控件的方向
	 *
	 * @param parentLayoutManager
	 * @return true(Vertical); false(Horizontal)
	 */
	protected boolean getOrientation(RecyclerView.LayoutManager parentLayoutManager)
	{
		if (parentLayoutManager instanceof LinearLayoutManager)
		{
			return ((LinearLayoutManager) parentLayoutManager).getOrientation() == LinearLayoutManager.VERTICAL;
		}
		else if (parentLayoutManager instanceof GridLayoutManager)
		{
			return ((GridLayoutManager) parentLayoutManager).getOrientation() == LinearLayoutManager.VERTICAL;
		}
		else if (parentLayoutManager instanceof StaggeredGridLayoutManager)
		{
			return ((StaggeredGridLayoutManager) parentLayoutManager).getOrientation() == StaggeredGridLayoutManager.VERTICAL;
		}
		else
		{
			return true;
		}
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
		RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
		if (layoutManager instanceof GridLayoutManager)
		{
			return ((GridLayoutManager) layoutManager).getSpanCount();
		}
		else if (layoutManager instanceof StaggeredGridLayoutManager)
		{
			return ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
		}
		else
		{
			return 1;
		}
	}

	/**
	 * 判断是否绘制分割线
	 * 处理空数据、头部、底部，三种情况
	 *
	 * @param adapter
	 * @param totalCount      数据的最大量
	 * @param currentPosition 当前的位置
	 * @return
	 */
	protected boolean isDrawDivide(RecyclerView.Adapter adapter, int totalCount, int currentPosition)
	{
		if (adapter instanceof CommonEmptyRecyclerAdapter)
		{
			if (adapter.getItemViewType(currentPosition) == CommonEmptyRecyclerAdapter.EmptyType)
			{
				return false;
			}
		}
		
		if (adapter instanceof IHeadFootCallback)
		{
			// 头部
			if (currentPosition < ((IHeadFootCallback) adapter).getHeadersCount())
			{
				return false;
			}
			
			// 底部
			if (currentPosition > totalCount - 1 - ((IHeadFootCallback) adapter).getFootersCount())
			{
				return false;
			}
		}
		
		return true;
	}

	/* %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 提供重写的参数 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% */

	/**
	 * 初始化设置，是否绘制
	 *
	 * @return {left, top, right, bottom}
	 */
	protected boolean[] isSpanDraw()
	{
		return new boolean[]{true, true, true, true};
	}

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
