package com.yline.view.recycler.manager;

import android.support.v7.widget.RecyclerView;

import com.yline.view.recycler.holder.Callback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 复杂数据类型的，数据管理类
 * @author yline 2017/10/18 -- 19:22
 * @version 1.0.0
 */
public class AbstractMultiDataManager implements Callback.IDataAdapterCallback<Object>{
    protected RecyclerView.Adapter mAdapter;
    protected List<Object> mList;

    public AbstractMultiDataManager(RecyclerView.Adapter adapter) {
        this.mAdapter = adapter;
        this.mList = new ArrayList<>();
    }

    @Override
    public Object getItem(int position){
        if (position >= mList.size()) {
            throw new IllegalArgumentException("invalid position");
        }
        return mList.get(position);
    }

    @Override
    public int getDataSize(){
        return mList.size();
    }

    @Override
    public List<Object> getDataList() {
        return Collections.unmodifiableList(mList);
    }

    @Override
    public void setDataList(List<Object> list, boolean isNotify) {
        if (null != list) {
            this.mList = new ArrayList<>(list);
            if (isNotify) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean contains(Object element) {
        return mList.contains(element);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return mList.containsAll(collection);
    }

    @Override
    public boolean isEmpty() {
        return mList.isEmpty();
    }

    @Override
    public boolean add(Object element, boolean isNotify) {
        boolean result = mList.add(element);
        if (isNotify) {
            mAdapter.notifyItemInserted(mList.size() - 1);
        }
        return result;
    }

    @Override
    public void add(int index, Object element, boolean isNotify) {
        mList.add(index, element);
        if (isNotify) {
            mAdapter.notifyItemInserted(index);
        }
    }

    @Override
    public boolean addAll(Collection<?> collection, boolean isNotify) {
        boolean result = mList.addAll(collection);
        if (isNotify) {
            mAdapter.notifyDataSetChanged();
        }
        return result;
    }

    @Override
    public boolean addAll(int index, Collection<?> collection, boolean isNotify) {
        boolean result = mList.addAll(index, collection);
        if (isNotify) {
            mAdapter.notifyItemRangeInserted(index, collection.size());
        }
        return result;
    }

    @Override
    public Object remove(int index, boolean isNotify) {
        if (mList.size() > index) {
            Object t = mList.remove(index);
            if (isNotify) {
                mAdapter.notifyItemRemoved(index);
            }
            return t;
        }
        return null;
    }

    @Override
    public boolean remove(Object model, boolean isNotify) {
        List<Integer> objectList = new ArrayList<>();
        for (int i = mList.size() - 1; i >= 0; i--) {
            if (null != model && mList.get(i).equals(model)) {
                objectList.add(i);
            }
        }

        boolean result = mList.removeAll(Arrays.asList(model));
        if (result && isNotify) {
            for (Integer integer : objectList) {
                mAdapter.notifyItemRemoved(integer);
            }
        }

        return result;
    }

    @Override
    public boolean removeAll(Collection<?> collection, boolean isNotify) {
        int length = mList.size();
        if (null == collection || collection.size() > length) {
            return false;
        }

        List<Integer> objectList = new ArrayList<>();
        for (int i = mList.size() - 1; i >= 0; i--) {
            if (collection.contains(mList.get(i))) {
                objectList.add(i);
            }
        }

        boolean result = mList.removeAll(collection);
        if (result && isNotify) {
            for (Integer integer : objectList) {
                mAdapter.notifyItemRemoved(integer);
            }
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
    public boolean update(int index, Object o, boolean isNotify) {
        if (index >= mList.size()) {
            return false;
        }

        mList.remove(index);
        mList.add(index, o);
        if (isNotify) {
            mAdapter.notifyItemChanged(index);
        }

        return true;
    }

    @Override
    public boolean update(int[] index, Object[] arrays, boolean isNotify) {
        if (index.length != arrays.length || index.length > mList.size()) {
            return false;
        }

        for (int i : index) {
            if (i >= mList.size()) {
                return false;
            }
        }

        for (int i = 0; i < arrays.length; i++) {
            update(index[i], arrays[i], isNotify);
        }

        return true;
    }
}
