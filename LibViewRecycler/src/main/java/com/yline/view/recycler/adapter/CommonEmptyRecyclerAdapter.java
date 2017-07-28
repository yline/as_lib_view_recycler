package com.yline.view.recycler.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 继承于 CommonRecyclerAdapter
 * 加上：空数据处理功能
 *
 * @author yline 2017/7/27 -- 17:09
 * @version 1.0.0
 */
public abstract class CommonEmptyRecyclerAdapter<T> extends CommonRecyclerAdapter<T>
{
	public static final int EmptyType = -11111;

	protected static final int EmptyTypePosition = -1;

	@Override
	public int getItemCount()
	{
		if (sList.size() == 0 && isEmptyViewShow())
		{
			return 1;
		}
		return super.getItemCount();
	}

	@Override
	public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		if (viewType == EmptyType)
		{
			return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(getEmptyItemRes(), parent, false));
		}
		return super.onCreateViewHolder(parent, viewType);
	}

	@Override
	public void onBindViewHolder(RecyclerViewHolder holder, int position, List<Object> payloads)
	{
		if (holder.getItemViewType() == EmptyType)
		{
			onBindEmptyViewHolder(holder, position);
		}
		else
		{
			super.onBindViewHolder(holder, position, payloads);
		}
	}

	@Override
	public int getItemViewType(int position)
	{
		if (sList.size() == 0 && isEmptyViewShow())
		{
			return EmptyType;
		}
		return super.getItemViewType(position);
	}

	/**
	 * 数据为空时，显示内容
	 *
	 * @return
	 */
	public int getEmptyItemRes()
	{
		return android.R.layout.simple_list_item_1;
	}

	/**
	 * 数据为空时，显示内容
	 *
	 * @param viewHolder
	 * @param position
	 */
	public void onBindEmptyViewHolder(RecyclerViewHolder viewHolder, int position)
	{
		// TODO: 2017/5/23
	}

	/**
	 * 是否显示空数据
	 *
	 * @return
	 */
	public boolean isEmptyViewShow()
	{
		return true;
	}

	/* &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& 适配情形 &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& */

	@Override
	public void onAttachedToRecyclerView(RecyclerView recyclerView)
	{
		super.onAttachedToRecyclerView(recyclerView);

		RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
		if (layoutManager instanceof GridLayoutManager)
		{
			final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
			final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

			gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
			{
				@Override
				public int getSpanSize(int position)
				{
					return onConfigGridLayoutManager(gridLayoutManager, spanSizeLookup, position);
				}
			});

			gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
		}
	}

	/**
	 * 适配 GridLayoutManager
	 *
	 * @param gridLayoutManager
	 * @param spanSizeLookup
	 * @param position
	 * @return
	 */
	protected int onConfigGridLayoutManager(GridLayoutManager gridLayoutManager, GridLayoutManager.SpanSizeLookup spanSizeLookup, int position)
	{
		int viewType = getItemViewType(position);
		if (viewType == EmptyType)
		{
			return gridLayoutManager.getSpanCount();
		}

		if (spanSizeLookup != null)
		{
			return spanSizeLookup.getSpanSize(position);
		}
		return 0;
	}

	@Override
	public void onViewAttachedToWindow(RecyclerViewHolder holder)
	{
		super.onViewAttachedToWindow(holder);
		int position = holder.getLayoutPosition();
		if (onConfigStaggeredGridLayoutManager(position))
		{
			ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
			if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams)
			{
				StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) lp;

				params.setFullSpan(true);
			}
		}
	}

	/**
	 * 适配 StaggeredGridLayoutManager
	 *
	 * @param position
	 * @return
	 */
	protected boolean onConfigStaggeredGridLayoutManager(int position)
	{
		int viewType = getItemViewType(position);
		if (viewType == EmptyType)
		{
			return true;
		}

		return false;
	}

	/* &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& 兼容数据操作 &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& */

	@Override
	public boolean add(T object)
	{
		if (getItemViewType(EmptyTypePosition) == EmptyType)
		{
			setDataList(Arrays.asList(object));
			return true;
		}
		else
		{
			return super.add(object);
		}
	}

	@Override
	public boolean add(int index, T element)
	{
		if (getItemViewType(EmptyTypePosition) == EmptyType)
		{
			setDataList(Arrays.asList(element));
			return true;
		}
		else
		{
			return super.add(index, element);
		}
	}

	@Override
	public boolean addAll(Collection<? extends T> collection)
	{
		if (getItemViewType(EmptyTypePosition) == EmptyType)
		{
			setDataList(new ArrayList<T>(collection));
			return true;
		}
		else
		{
			return super.addAll(collection);
		}
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> collection)
	{
		if (getItemViewType(EmptyTypePosition) == EmptyType)
		{
			setDataList(new ArrayList<T>(collection));
			return true;
		}
		else
		{
			return super.addAll(index, collection);
		}
	}
}
