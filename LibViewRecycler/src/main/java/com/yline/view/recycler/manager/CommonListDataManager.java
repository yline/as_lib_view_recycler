package com.yline.view.recycler.manager;

import android.widget.BaseAdapter;

import com.yline.view.recycler.holder.Callback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * ListView Data 管理类
 *
 * @author yline 2017/10/18 -- 17:02
 * @version 1.0.0
 */
public class CommonListDataManager<Model> implements Callback.IDataAdapterCallback<Model>{
    private List<Model> mList;
    private BaseAdapter mAdapter;

    public CommonListDataManager(BaseAdapter adapter) {
        this.mList = new ArrayList<>();
        this.mAdapter = adapter;
    }

    @Override
    public List<Model> getDataList() {
        return Collections.unmodifiableList(mList);
    }

    @Override
    public void setDataList(List<Model> list, boolean isNotify) {
        if (null != list) {
            this.mList = new ArrayList<>(list);
            if (isNotify) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public Model getItem(int position) {
        if (position >= mList.size()) {
            throw new IllegalArgumentException("invalid position");
        }
        return mList.get(position);
    }

    @Override
    public boolean contains(Model t) {
        return mList.contains(t);
    }

    @Override
    public boolean containsAll(Collection<? extends Model> collection) {
        return mList.containsAll(collection);
    }

    @Override
    public boolean isEmpty() {
        return mList.isEmpty();
    }

    @Override
    public boolean add(Model element, boolean isNotify) {
        boolean result = mList.add(element);
        if (isNotify) {
            mAdapter.notifyDataSetChanged();
        }
        return result;
    }

    @Override
    public void add(int index, Model element, boolean isNotify) {
        mList.add(index, element);
        if (isNotify) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean addAll(Collection<? extends Model> collection, boolean isNotify) {
        boolean result = mList.addAll(collection);
        if (isNotify) {
            mAdapter.notifyDataSetChanged();
        }
        return result;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Model> collection, boolean isNotify) {
        boolean result = mList.addAll(index, collection);
        if (isNotify) {
            mAdapter.notifyDataSetChanged();
        }
        return result;
    }

    @Override
    public Model remove(int index, boolean isNotify) {
        if (index >= mList.size()) {
            return null;
        }

        Model t = mList.remove(index);
        if (isNotify) {
            mAdapter.notifyDataSetChanged();
        }
        return t;
    }

    @Override
    public boolean remove(Model object, boolean isNotify) {
        boolean result = mList.remove(object);
        if (isNotify) {
            mAdapter.notifyDataSetChanged();
        }
        return result;
    }

    @Override
    public int getDataSize() {
        return mList.size();
    }

    @Override
    public boolean removeAll(Collection<? extends Model> collection, boolean isNotify) {
        boolean result = mList.removeAll(collection);
        if (isNotify) {
            mAdapter.notifyDataSetChanged();
        }
        return result;
    }

    @Override
    public void clear(boolean isNotify) {
        mList.clear();
        if (isNotify) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean update(int index, Model t, boolean isNotify) {
        if (index >= mList.size()) {
            return false;
        }

        mList.remove(index);
        mList.add(index, t);
        if (isNotify) {
            mAdapter.notifyDataSetChanged();
        }
        return true;
    }

    @Override
    public boolean update(int[] index, Model[] arrays, boolean isNotify) {
        // 入参处理
        if (index.length != arrays.length) {
            return false;
        }

        // 越界处理
        for (int i:index) {
            if (i >= mList.size()) {
                return false;
            }
        }

        for (int i = 0; i < index.length; i++) {
            mList.remove(index[i]);
            mList.add(index[i], arrays[i]);
        }

        if (isNotify) {
            mAdapter.notifyDataSetChanged();
        }
        return true;
    }
}
