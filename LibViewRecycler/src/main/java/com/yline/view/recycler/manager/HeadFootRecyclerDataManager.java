package com.yline.view.recycler.manager;

import android.support.v7.widget.RecyclerView;

import com.yline.view.recycler.adapter.AbstractHeadFootRecyclerAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 对应HeadFootRecyclerAdapter
 *
 * @author yline 2017/10/18 -- 16:30
 * @version 1.0.0
 */
public class HeadFootRecyclerDataManager<Model> extends RecyclerDataManager<Model> {

    public HeadFootRecyclerDataManager(RecyclerView.Adapter adapter) {
        super(adapter);
    }

    @Override
    public boolean add(Model element, boolean isNotify) {
        boolean result = mList.add(element);
        if (isNotify) {
            mAdapter.notifyItemInserted(AbstractHeadFootRecyclerAdapter.HEAD_COUNT + mList.size() - 1);
        }
        return result;
    }

    @Override
    public void add(int index, Model element, boolean isNotify) {
        mList.add(index, element);
        if (isNotify) {
            mAdapter.notifyItemInserted(AbstractHeadFootRecyclerAdapter.HEAD_COUNT + index);
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends Model> collection, boolean isNotify) {
        boolean result = mList.addAll(index, collection);
        if (isNotify) {
            mAdapter.notifyItemRangeInserted(AbstractHeadFootRecyclerAdapter.HEAD_COUNT + index, collection.size());
        }
        return result;
    }

    @Override
    public Model remove(int index, boolean isNotify) {
        Model model = mList.remove(index);
        if (isNotify) {
            mAdapter.notifyItemRemoved(AbstractHeadFootRecyclerAdapter.HEAD_COUNT + index);
        }
        return model;
    }

    @Override
    public boolean remove(Model model, boolean isNotify) {
        int index = mList.indexOf(model);
        boolean result = mList.remove(model);
        if (result && index != -1 && isNotify) {
            mAdapter.notifyItemRemoved(AbstractHeadFootRecyclerAdapter.HEAD_COUNT + index);
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
                mAdapter.notifyItemRemoved(AbstractHeadFootRecyclerAdapter.HEAD_COUNT + integer);
            }
        }

        return result;
    }

    @Override
    public Model set(int index, Model element, boolean isNotify) {
        Model model = mList.set(index, element);
        if (isNotify) {
            mAdapter.notifyItemChanged(AbstractHeadFootRecyclerAdapter.HEAD_COUNT + index);
        }
        return model;
    }
}
