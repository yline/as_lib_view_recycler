package com.yline.view.recycler.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.yline.view.recycler.callback.IDataAdapterCallback;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * RecyclerView 公共的 Adapter
 *
 * @author yline 2017/5/23 -- 10:28
 * @version 1.0.0
 */
public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> implements IDataAdapterCallback<T>
{
	protected List<T> sList;
	
	public CommonRecyclerAdapter()
	{
		this.sList = new ArrayList<>();
	}
	
	@Override
	public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(getItemRes(), parent, false));
	}

	/**
	 * @return item 资源文件
	 */
	public abstract int getItemRes();
	
	@Override
	public int getItemCount()
	{
		return sList.size();
	}

	@Override
	public List<T> getDataList()
	{
		return Collections.unmodifiableList(sList);
	}
	
	@Override
	public void setDataList(List<T> tList)
	{
		if (null != tList)
		{
			this.sList = new ArrayList<>(tList);
			this.notifyDataSetChanged();
		}
	}

	/**
	 * 返回某项数据
	 *
	 * @param position 位置
	 * @return 某项数据
	 */
	@Override
	public T getItem(int position)
	{
		if (position >= sList.size())
		{
			throw new IllegalArgumentException("invalid position");
		}
		return sList.get(position);
	}

	@Override
	public int getDataSize()
	{
		int size = sList.size();
		return size;
	}

	@Override
	public boolean contains(Object object)
	{
		boolean result = sList.contains(object);
		return result;
	}

	@Override
	public boolean containsAll(Collection collection)
	{
		boolean result = sList.containsAll(collection);
		return result;
	}

	@Override
	public boolean isEmpty()
	{
		return sList.isEmpty();
	}

	@Override
	public boolean add(T object)
	{
		boolean result = sList.add(object);
		this.notifyItemInserted(sList.size() - 1);
		return result;
	}
	
	@Override
	public boolean add(int index, T element)
	{
		sList.add(index, element);
		this.notifyItemInserted(index);
		
		return true;
	}
	
	@Override
	public boolean addAll(Collection<? extends T> collection)
	{
		boolean result = sList.addAll(collection);
		this.notifyItemRangeInserted(sList.size() - 1, collection.size());
		return result;
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends T> collection)
	{
		boolean result = sList.addAll(index, collection);
		this.notifyItemRangeInserted(index, collection.size());
		return result;
	}

	@Override
	public T remove(int index)
	{
		if (sList.size() > index)
		{
			T t = sList.remove(index);
			this.notifyItemRemoved(index);
			
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
				this.notifyItemRemoved(integer);
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
				this.notifyItemRemoved(integer);
			}
		}
		
		return result;
	}
	
	@Override
	public void clear()
	{
		sList.clear();
		this.notifyDataSetChanged();
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
		this.notifyItemChanged(index);
		
		return true;
	}
	
	@Override
	public boolean update(int index[], T[] arrays)
	{
		if (index.length != arrays.length || index.length > sList.size())
		{
			return false;
		}

		for (int i = 0; i < index.length; i++)
		{
			if (index[i] >= sList.size())
			{
				return false;
			}
		}
		
		for (int i = 0; i < arrays.length; i++)
		{
			update(index[i], arrays[i]);
		}
		
		return true;
	}
}
