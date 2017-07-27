package com.yline.view.recycler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yline.view.recycler.callback.IDataAdapterCallback;
import com.yline.view.recycler.holder.ViewHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * ListView 公共的 Adapter
 *
 * @author yline 2017/5/23 -- 10:27
 * @version 1.0.0
 */
public abstract class CommonListAdapter<T> extends BaseAdapter implements IDataAdapterCallback<T>
{
	protected Context sContext;

	protected List<T> sList;

	public CommonListAdapter(Context context)
	{
		this.sContext = context;
		this.sList = new ArrayList<>();
	}
	
	@Override
	public int getCount()
	{
		return sList.size();
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		if (convertView == null)
		{
			convertView = LayoutInflater.from(sContext).inflate(getItemRes(position), parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		onBindViewHolder(parent, holder, position);

		return convertView;
	}

	/**
	 * @param position 当前的位置
	 * @return item 资源文件
	 */
	protected abstract int getItemRes(int position);

	/**
	 * 对内容设置
	 *
	 * @param parent     副控件(一般不用)
	 * @param viewHolder ViewHolder
	 * @param position   当前item位置
	 */
	protected abstract void onBindViewHolder(ViewGroup parent, ViewHolder viewHolder, int position);

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
		return super.isEmpty();
	}

	@Override
	public boolean isEmptyViewShow()
	{
		return true;
	}

	@Override
	public boolean add(T object)
	{
		boolean result = sList.add(object);
		this.notifyDataSetChanged();
		return result;
	}

	@Override
	public boolean add(int index, T element)
	{
		sList.add(index, element);
		this.notifyDataSetChanged();
		return true;
	}

	@Override
	public boolean addAll(Collection collection)
	{
		boolean result = sList.addAll(collection);
		this.notifyDataSetChanged();
		return result;
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> collection)
	{
		boolean result = sList.addAll(index, collection);
		this.notifyDataSetChanged();
		return result;
	}

	@Override
	public T remove(int index)
	{
		if (index >= sList.size())
		{
			return null;
		}

		T t = sList.remove(index);
		this.notifyDataSetChanged();
		return t;
	}

	@Override
	public boolean remove(Object object)
	{
		boolean result = sList.remove(object);
		this.notifyDataSetChanged();
		return result;
	}

	@Override
	public int getDataSize()
	{
		int size = sList.size();
		return size;
	}

	@Override
	public boolean removeAll(Collection collection)
	{
		boolean result = sList.removeAll(collection);
		this.notifyDataSetChanged();
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
		this.notifyDataSetChanged();

		return true;
	}

	@Override
	public boolean update(int[] index, T[] arrays)
	{
		// 入参处理
		if (index.length != arrays.length)
		{
			return false;
		}

		// 越界处理
		for (int i = 0; i < index.length; i++)
		{
			if (index[i] >= sList.size())
			{
				return false;
			}
		}

		for (int i = 0; i < index.length; i++)
		{
			sList.remove(index[i]);
			sList.add(arrays[i]);
		}
		this.notifyDataSetChanged();

		return true;
	}
}
