package com.yline.view.recycler.manager;

import com.yline.view.recycler.holder.IDataAdapterCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 抽象管理类，获取数据信息，不可以修改数据
 *
 * @author yline 2018/2/10 -- 10:41
 * @version 1.0.0
 */
public abstract class AbstractDataManager<E> implements IDataAdapterCallback<E> {
	List<E> mList;
	
	public AbstractDataManager() {
		this.mList = new ArrayList<>();
	}
	
	@Override
	public int size() {
		return mList.size();
	}
	
	@Override
	public boolean isEmpty() {
		return mList.isEmpty();
	}
	
	@Override
	public boolean contains(E element) {
		return mList.contains(element);
	}
	
	@Override
	public boolean containsAll(Collection<? extends E> collection) {
		return mList.containsAll(collection);
	}
	
	@Override
	public E get(int index) {
		return mList.get(index);
	}
	
	@Override
	public int indexOf(E element) {
		return mList.indexOf(element);
	}
	
	@Override
	public int lastIndexOf(E obj) {
		return mList.lastIndexOf(obj);
	}
	
	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return mList.subList(fromIndex, toIndex);
	}
	
	@Override
	public List<E> getDataList() {
		return Collections.unmodifiableList(mList);
	}
}
