package com.yline.view.recycler.manager;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;

import com.yline.view.recycler.holder.Callback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * 对应 AbstractRecyclerAdapter
 *
 * @author yline 2017/10/18 -- 15:49
 * @version 1.0.0
 */
public class RecyclerDataManager<Model> extends AbstractDataManager<Model> implements Callback.IDataAdapterCallback<Model> {
    final RecyclerView.Adapter mAdapter;

    public RecyclerDataManager(RecyclerView.Adapter adapter) {
        super();
        this.mAdapter = adapter;
    }

    @Override
    public void setDataList(List<Model> list, boolean isNotify) {
        if (null != list) {
            mList = new ArrayList<>(list);
            if (isNotify) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean add(Model element, boolean isNotify) {
        boolean result = mList.add(element);
        if (isNotify) {
            mAdapter.notifyItemChanged(mList.size() - 1);
        }
        return result;
    }

    @Override
    public void add(int index, Model element, boolean isNotify) {
        mList.add(index, element);
        if (isNotify) {
            mAdapter.notifyItemInserted(index);
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
            mAdapter.notifyItemRangeInserted(index, collection.size());
        }
        return result;
    }

    @Override
    public Model remove(int index, boolean isNotify) {
        Model model = mList.remove(index);
        if (isNotify) {
            mAdapter.notifyItemRemoved(index);
        }
        return model;
    }

    @Override
    public boolean remove(Model element, boolean isNotify) {
        int index = mList.indexOf(element);
        boolean result = mList.remove(element);
        if (result && index != -1 && isNotify) {
            mAdapter.notifyItemRemoved(index);
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
    public Model set(int index, Model element, boolean isNotify) {
        Model model = mList.set(index, element);
        if (isNotify) {
            mAdapter.notifyItemChanged(index);
        }
        return model;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void replaceAll(UnaryOperator<Model> operator, boolean isNotify) {
        mList.replaceAll(operator);
        if (isNotify) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void sort(Comparator<? super Model> comparator, boolean isNotify) {
        mList.sort(comparator);
        if (isNotify) {
            mAdapter.notifyDataSetChanged();
        }
    }
}
