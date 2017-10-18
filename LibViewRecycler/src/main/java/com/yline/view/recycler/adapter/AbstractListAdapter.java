package com.yline.view.recycler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yline.view.recycler.holder.Callback;
import com.yline.view.recycler.holder.ViewHolder;
import com.yline.view.recycler.manager.CommonListDataManager;

import java.util.Collection;
import java.util.List;

/**
 * ListView 公共的 Adapter
 *
 * @author yline 2017/5/23 -- 10:27
 * @version 1.0.0
 */
public abstract class AbstractListAdapter<T> extends BaseAdapter implements Callback.IDataAdapterCallback<T> {
    private CommonListDataManager<T> mDataManager;
    protected Context mContext;

    public AbstractListAdapter(Context context) {
        this.mContext = context;
        this.mDataManager = new CommonListDataManager<>(this);
    }

    @Override
    public int getCount() {
        return mDataManager.getDataSize();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(getItemRes(position), parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
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
    public List<T> getDataList() {
        return mDataManager.getDataList();
    }

    @Override
    public void setDataList(List<T> list, boolean isNotify) {
        mDataManager.setDataList(list, isNotify);
    }

    @Override
    public T getItem(int position) {
        return mDataManager.getItem(position);
    }

    @Override
    public int getDataSize() {
        return mDataManager.getDataSize();
    }

    @Override
    public boolean contains(T element) {
        return mDataManager.contains(element);
    }

    @Override
    public boolean containsAll(Collection<? extends T> collection) {
        return mDataManager.containsAll(collection);
    }

    @Override
    public boolean add(T element, boolean isNotify) {
        return mDataManager.add(element, isNotify);
    }

    @Override
    public void add(int index, T element, boolean isNotify) {
        mDataManager.add(index, element, isNotify);
    }

    @Override
    public boolean addAll(Collection<? extends T> collection, boolean isNotify) {
        return mDataManager.addAll(collection, isNotify);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> collection, boolean isNotify) {
        return mDataManager.addAll(index, collection, isNotify);
    }

    @Override
    public T remove(int index, boolean isNotify) {
        return mDataManager.remove(index, isNotify);
    }

    @Override
    public boolean remove(T t, boolean isNotify) {
        return mDataManager.remove(t, isNotify);
    }

    @Override
    public boolean removeAll(Collection<? extends T> collection, boolean isNotify) {
        return mDataManager.removeAll(collection, isNotify);
    }

    @Override
    public void clear(boolean isNotify) {
        mDataManager.clear(isNotify);
    }

    @Override
    public boolean update(int index, T t, boolean isNotify) {
        return mDataManager.update(index, t, isNotify);
    }

    @Override
    public boolean update(int[] index, T[] arrays, boolean isNotify) {
        return mDataManager.update(index, arrays, isNotify);
    }
}
