package com.yline.view.recycler.adapter;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.yline.view.recycler.callback.IHeadFootCallback;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 添加头部和底部的Recycle
 *
 * @author yline 2017/5/23 -- 10:31
 * @version 1.0.0
 */
public abstract class HeadFootRecyclerAdapter<T> extends CommonEmptyRecyclerAdapter<T> implements IHeadFootCallback
{
	// 头部的开始标签 1024
	private static final int BASE_ITEM_TYPE_HEADER = 1024;

	// 底部最大个数：1024
	private static final int BASE_ITEM_TYPE_FOOTER = Integer.MAX_VALUE - 1024;

	// 头布局
	private SparseArrayCompat<View> headViewArray = new SparseArrayCompat<>();

	// 底部布局
	private SparseArrayCompat<View> footViewArray = new SparseArrayCompat<>();

	@Override
	public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		if (headViewArray.get(viewType) != null)
		{
			return new RecyclerViewHolder(headViewArray.get(viewType));
		}
		else if (footViewArray.get(viewType) != null)
		{
			return new RecyclerViewHolder(footViewArray.get(viewType));
		}

		return super.onCreateViewHolder(parent, viewType);
	}

	@Override
	public int getItemViewType(int position)
	{
		if (isHeaderViewPos(position))
		{
			return headViewArray.keyAt(position);
		}
		else if (isFooterViewPos(position))
		{
			return footViewArray.keyAt(position - getHeadersCount() - super.getItemCount());
		}
		return super.getItemViewType(position - getHeadersCount());
	}

	@Override
	public void onBindViewHolder(RecyclerViewHolder holder, int position, List<Object> payloads)
	{
		if (isHeaderViewPos(position))
		{
			return;
		}

		if (isFooterViewPos(position))
		{
			return;
		}

		super.onBindViewHolder(holder, position - getHeadersCount(), payloads);
	}

	@Override
	public int getItemCount()
	{
		return getHeadersCount() + getFootersCount() + super.getItemCount();
	}

	private boolean isHeaderViewPos(int position)
	{
		return (position >= 0) && (position < getHeadersCount());
	}

	private boolean isFooterViewPos(int position)
	{
		return position >= getHeadersCount() + super.getItemCount();
	}

	public void addHeadView(View view)
	{
		headViewArray.put(headViewArray.size() + BASE_ITEM_TYPE_HEADER, view);
	}

	public void addFootView(View view)
	{
		footViewArray.put(footViewArray.size() + BASE_ITEM_TYPE_FOOTER, view);
	}

	@Override
	public int getHeadersCount()
	{
		return headViewArray.size();
	}
	
	@Override
	public int getFootersCount()
	{
		return footViewArray.size();
	}

	/* &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& 适配情形 &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& */
	@Override
	protected int onConfigGridLayoutManager(GridLayoutManager gridLayoutManager, GridLayoutManager.SpanSizeLookup spanSizeLookup, int position)
	{
		int viewType = getItemViewType(position);
		if (headViewArray.get(viewType) != null)
		{
			return gridLayoutManager.getSpanCount();
		}
		else if (footViewArray.get(viewType) != null)
		{
			return gridLayoutManager.getSpanCount();
		}
		return super.onConfigGridLayoutManager(gridLayoutManager, spanSizeLookup, position);
	}

	@Override
	protected boolean onConfigStaggeredGridLayoutManager(int position)
	{
		if (isHeaderViewPos(position) || isFooterViewPos(position))
		{
			return true;
		}
		return super.onConfigStaggeredGridLayoutManager(position);
	}

	/* &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& 兼容数据操作 &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& */

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
			boolean result = sList.add(object);
			this.notifyItemInserted(getHeadersCount() + sList.size() - 1);
			return result;
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
			sList.add(index, element);
			this.notifyItemInserted(getHeadersCount() + index);
			return true;
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
			boolean result = sList.addAll(collection);
			this.notifyItemRangeInserted(getHeadersCount() + sList.size() - 1, collection.size());
			return result;
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
			boolean result = sList.addAll(index, collection);
			this.notifyItemRangeInserted(getHeadersCount() + index, collection.size());
			return result;
		}
	}

	@Override
	public T remove(int index)
	{
		if (sList.size() > index)
		{
			T t = sList.remove(index);
			this.notifyItemRemoved(getHeadersCount() + index);

			return t;
		}
		return null;
	}

	@Override
	public boolean remove(T t)
	{
		List<Integer> objectList = new ArrayList<>();
		for (int i = sList.size() - 1; i >= 0; i--)
		{
			if (null != t && sList.get(i).equals(t))
			{
				objectList.add(i);
			}
		}

		boolean result = sList.removeAll(Arrays.asList(t));
		if (result)
		{
			for (Integer integer : objectList)
			{
				this.notifyItemRemoved(getHeadersCount() + integer);
			}
		}

		return result;
	}

	@Override
	public boolean removeAll(Collection<? extends T> collection)
	{
		int length = sList.size();
		if (null == collection || collection.size() > length)
		{
			return false;
		}

		List<Integer> objectList = new ArrayList<>();
		for (int i = sList.size() - 1; i >= 0; i--)
		{
			if (collection.contains(sList.get(i)))
			{
				objectList.add(i);
			}
		}

		boolean result = sList.removeAll(collection);
		if (result)
		{
			for (Integer integer : objectList)
			{
				this.notifyItemRemoved(getHeadersCount() + integer);
			}
		}

		return result;
	}

	@Override
	public boolean update(int index, T t)
	{
		if (index >= sList.size())
		{
			return false;
		}

		sList.remove(index);
		sList.add(index, t);
		this.notifyItemChanged(getHeadersCount() + index);

		return true;
	}
}
