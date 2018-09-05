package com.yline.view.recycler.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yline.view.recycler.holder.IDataAdapterCallback;
import com.yline.view.recycler.holder.ViewHolder;
import com.yline.view.recycler.manager.ListDataManager;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * ListView 公共的 Adapter
 *
 * @author yline 2017/5/23 -- 10:27
 * @version 1.0.0
 */
public abstract class AbstractListAdapter<T> extends BaseAdapter implements IDataAdapterCallback<T> {
    private final ListDataManager<T> mDataManager;
    private final Context mContext;

    public AbstractListAdapter(Context context) {
        this.mContext = context;
        this.mDataManager = new ListDataManager<>(this);
    }

    @Override
    public int getCount() {
        return mDataManager.size();
    }

    @Override
    public T getItem(int position) {
        return mDataManager.get(position);
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

    Context getContext() {
        return mContext;
    }


    /* &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& 数据情形 &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& */
    @Override
    public int size() {
        return mDataManager.size();
    }

    @Override
    public boolean isEmpty() {
        return mDataManager.isEmpty();
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
    public T get(int index) {
        return mDataManager.get(index);
    }

    @Override
    public int indexOf(T element) {
        return mDataManager.indexOf(element);
    }

    @Override
    public int lastIndexOf(T element) {
        return mDataManager.lastIndexOf(element);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return mDataManager.subList(fromIndex, toIndex);
    }

    @Override
    public List<T> getDataList() {
        return mDataManager.getDataList();
    }

    @Override
    public void setDataList(List<T> list, boolean isNotify) {
        mDataManager.setDataList(list, isNotify);
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
    public boolean remove(T element, boolean isNotify) {
        return mDataManager.remove(element, isNotify);
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
    public T set(int index, T element, boolean isNotify) {
        return mDataManager.set(index, element, isNotify);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void replaceAll(UnaryOperator<T> operator, boolean isNotify) {
        mDataManager.replaceAll(operator, isNotify);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void sort(Comparator<? super T> comparator, boolean isNotify) {
        mDataManager.sort(comparator, isNotify);
    }
}
