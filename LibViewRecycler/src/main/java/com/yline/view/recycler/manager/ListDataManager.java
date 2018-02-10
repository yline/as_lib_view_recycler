package com.yline.view.recycler.manager;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.BaseAdapter;

import com.yline.view.recycler.holder.Callback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * ListView Data 管理类
 *
 * @author yline 2017/10/18 -- 17:02
 * @version 1.0.0
 */
public class ListDataManager<Model> extends AbstractDataManager<Model> implements Callback.IDataAdapterCallback<Model> {
    private BaseAdapter mAdapter;

    public ListDataManager(BaseAdapter adapter) {
        super();

        this.mAdapter = adapter;
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
        Model model = mList.remove(index);
        if (isNotify) {
            mAdapter.notifyDataSetChanged();
        }
        return model;
    }

    @Override
    public boolean remove(Model model, boolean isNotify) {
        boolean result = mList.remove(model);
        if (isNotify) {
            mAdapter.notifyDataSetChanged();
        }
        return result;
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
    public Model set(int index, Model element, boolean isNotify) {
        Model model = mList.set(index, element);
        if (isNotify) {
            mAdapter.notifyDataSetChanged();
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
