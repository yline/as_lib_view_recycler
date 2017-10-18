package com.yline.view.recycler.manager;

import android.support.v7.widget.RecyclerView;

import com.yline.view.recycler.adapter.AbstractHeadFootRecyclerAdapter;
import com.yline.view.recycler.holder.Callback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 对应HeadFootRecyclerAdapter
 *
 * @author yline 2017/10/18 -- 16:30
 * @version 1.0.0
 */
public class HeadFootRecyclerDataManager<Model> implements Callback.IDataAdapterCallback<Model> {
    private RecyclerView.Adapter mAdapter;
    private Callback.IHeadFootCallback mHeadFootCallback;
    private List<Model> mList;

    public HeadFootRecyclerDataManager(AbstractHeadFootRecyclerAdapter adapter) {
        this.mAdapter = adapter;
        this.mHeadFootCallback = adapter;
        this.mList = new ArrayList<>();
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
    public int getDataSize() {
        return mList.size();
    }

    @Override
    public boolean contains(Model element) {
        return mList.contains(element);
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
            mAdapter.notifyItemInserted(mHeadFootCallback.getHeadersCount() + mList.size() - 1);
        }
        return result;
    }

    @Override
    public void add(int index, Model element, boolean isNotify) {
        mList.add(index, element);
        if (isNotify) {
            mAdapter.notifyItemInserted(mHeadFootCallback.getHeadersCount() + index);
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
            mAdapter.notifyItemRangeInserted(mHeadFootCallback.getFootersCount() + index, collection.size());
        }
        return result;
    }

    @Override
    public Model remove(int index, boolean isNotify) {
        if (mList.size() > index) {
            Model t = mList.remove(index);
            if (isNotify) {
                mAdapter.notifyItemRemoved(mHeadFootCallback.getHeadersCount() + index);
            }
            return t;
        }
        return null;
    }

    @Override
    public boolean remove(Model model, boolean isNotify) {
        List<Integer> objectList = new ArrayList<>();
        for (int i = mList.size() - 1; i >= 0; i--) {
            if (null != model && mList.get(i).equals(model)) {
                objectList.add(i);
            }
        }

        boolean result = mList.removeAll(Arrays.asList(model));
        if (result && isNotify) {
            for (Integer integer : objectList) {
                mAdapter.notifyItemRemoved(mHeadFootCallback.getHeadersCount() + integer);
            }
        }

        return result;
    }

    @Override
    public boolean removeAll(Collection<? extends Model> collection, boolean isNotify) {
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
                mAdapter.notifyItemRemoved(mHeadFootCallback.getHeadersCount() + integer);
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
    public boolean update(int index, Model model, boolean isNotify) {
        if (index >= mList.size()) {
            return false;
        }

        mList.remove(index);
        mList.add(index, model);
        if (isNotify) {
            mAdapter.notifyItemChanged(mHeadFootCallback.getHeadersCount() + index);
        }

        return true;
    }

    @Override
    public boolean update(int[] index, Model[] arrays, boolean isNotify) {
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
