package com.yline.view.recycler.adapter;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.yline.view.recycler.holder.IDataAdapterCallback;
import com.yline.view.recycler.holder.RecyclerViewHolder;
import com.yline.view.recycler.manager.RecyclerDataManager;
import com.yline.view.recycler.manager.RecyclerSpanManager;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * RecyclerView 公共的 Adapter
 *
 * @author yline 2017/5/23 -- 10:28
 * @version 1.0.0
 */
public abstract class AbstractRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> implements IDataAdapterCallback<T> {
    private final RecyclerDataManager<T> mDataManager;

    public AbstractRecyclerAdapter() {
        this.mDataManager = new RecyclerDataManager<>(this);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(getItemRes(), parent, false));
    }

    /**
     * 设置图片，资源文件
     *
     * @return item 资源文件
     */
    public abstract int getItemRes();

    @Override
    public int getItemCount() {
        return mDataManager.size();
    }

    /* &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& 适配情形 &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& */

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerSpanManager.onAttachedToRecyclerView(recyclerView, new RecyclerSpanManager.OnGridCallback() {
            @Override
            public int onGridConfig(GridLayoutManager gridLayoutManager, GridLayoutManager.SpanSizeLookup spanSizeLookup, int position) {
                if (spanSizeLookup != null) {
                    return spanSizeLookup.getSpanSize(position);
                }
                return 0;
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(RecyclerViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        RecyclerSpanManager.onViewAttachedToWindow(holder, new RecyclerSpanManager.OnStaggerCallback() {
            @Override
            public boolean onStaggerConfig(int position) {
                return false;
            }
        });
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
