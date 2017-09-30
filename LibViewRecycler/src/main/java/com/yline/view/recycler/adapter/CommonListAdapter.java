package com.yline.view.recycler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yline.view.recycler.holder.Callback;
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
public abstract class CommonListAdapter<T> extends BaseAdapter implements Callback.IDataAdapterCallback<T> {
    protected Context sContext;

    protected List<T> sList;

    public CommonListAdapter(Context context) {
        this.sContext = context;
        this.sList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return sList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(sContext).inflate(getItemRes(position), parent, false);
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
        return Collections.unmodifiableList(sList);
    }

    @Override
    public void setDataList(List<T> list, boolean isNotify) {
        if (null != list) {
            this.sList = new ArrayList<>(list);
            if (isNotify) {
                this.notifyDataSetChanged();
            }
        }
    }

    @Override
    public T getItem(int position) {
        if (position >= sList.size()) {
            throw new IllegalArgumentException("invalid position");
        }
        return sList.get(position);
    }

    @Override
    public boolean contains(T t) {
        return sList.contains(t);
    }

    @Override
    public boolean containsAll(Collection<? extends T> collection) {
        return sList.containsAll(collection);
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public boolean add(T element, boolean isNotify) {
        boolean result = sList.add(element);
        if (isNotify) {
            this.notifyDataSetChanged();
        }
        return result;
    }

    @Override
    public void add(int index, T element, boolean isNotify) {
        sList.add(index, element);
        if (isNotify) {
            this.notifyDataSetChanged();
        }
    }

    @Override
    public boolean addAll(Collection<? extends T> collection, boolean isNotify) {
        boolean result = sList.addAll(collection);
        if (isNotify) {
            this.notifyDataSetChanged();
        }
        return result;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> collection, boolean isNotify) {
        boolean result = sList.addAll(index, collection);
        if (isNotify) {
            this.notifyDataSetChanged();
        }
        return result;
    }

    @Override
    public T remove(int index, boolean isNotify) {
        if (index >= sList.size()) {
            return null;
        }

        T t = sList.remove(index);
        if (isNotify) {
            this.notifyDataSetChanged();
        }
        return t;
    }

    @Override
    public boolean remove(T object, boolean isNotify) {
        boolean result = sList.remove(object);
        if (isNotify) {
            this.notifyDataSetChanged();
        }
        return result;
    }

    @Override
    public int getDataSize() {
        return sList.size();
    }

    @Override
    public boolean removeAll(Collection<? extends T> collection, boolean isNotify) {
        boolean result = sList.removeAll(collection);
        if (isNotify) {
            this.notifyDataSetChanged();
        }
        return result;
    }

    @Override
    public void clear(boolean isNotify) {
        sList.clear();
        if (isNotify) {
            this.notifyDataSetChanged();
        }
    }

    @Override
    public boolean update(int index, T t, boolean isNotify) {
        if (index >= sList.size()) {
            return false;
        }

        sList.remove(index);
        sList.add(index, t);
        if (isNotify) {
            this.notifyDataSetChanged();
        }
        return true;
    }

    @Override
    public boolean update(int[] index, T[] arrays, boolean isNotify) {
        // 入参处理
        if (index.length != arrays.length) {
            return false;
        }

        // 越界处理
        for (int i:index) {
            if (i >= sList.size()) {
                return false;
            }
        }

        for (int i = 0; i < index.length; i++) {
            sList.remove(index[i]);
            sList.add(index[i], arrays[i]);
        }

        if (isNotify) {
            this.notifyDataSetChanged();
        }
        return true;
    }
}
