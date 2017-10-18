package com.yline.view.recycler.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.yline.view.recycler.holder.Callback;
import com.yline.view.recycler.holder.RecyclerViewHolder;
import com.yline.view.recycler.manager.CommonRecyclerDataManager;

import java.util.Collection;
import java.util.List;

/**
 * RecyclerView 公共的 Adapter
 *
 * @author yline 2017/5/23 -- 10:28
 * @version 1.0.0
 */
public abstract class AbstractCommonRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> implements Callback.IDataAdapterCallback<T> {
    private CommonRecyclerDataManager<T> mDataManager;

    public AbstractCommonRecyclerAdapter() {
        this.mDataManager = new CommonRecyclerDataManager<>(this);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(getItemRes(), parent, false));
    }

    /**
     * @return item 资源文件
     */
    public abstract int getItemRes();

    @Override
    public int getItemCount() {
        return mDataManager.getDataSize();
    }

    @Override
    public List<T> getDataList() {
        return mDataManager.getDataList();
    }

    @Override
    public void setDataList(List<T> tList, boolean isNotify) {
        mDataManager.setDataList(tList, isNotify);
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
    public boolean isEmpty() {
        return mDataManager.isEmpty();
    }

    @Override
    public boolean add(T t, boolean isNotify) {
        return mDataManager.add(t, isNotify);
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

    /* &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& 适配情形 &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& */

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return onConfigGridLayoutManager(gridLayoutManager, spanSizeLookup, position);
                }
            });

            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }

    /**
     * 适配 GridLayoutManager
     *
     * @param gridLayoutManager 方向器
     * @param spanSizeLookup    原本的spanSize
     * @param position          当前位置
     * @return 列数
     */
    protected int onConfigGridLayoutManager(GridLayoutManager gridLayoutManager, GridLayoutManager.SpanSizeLookup spanSizeLookup, int position) {
        /*int viewType = getItemViewType(position);
        if (viewType == EmptyType) {
            return gridLayoutManager.getSpanCount();
        }*/

        if (spanSizeLookup != null) {
            return spanSizeLookup.getSpanSize(position);
        }
        return 0;
    }

    @Override
    public void onViewAttachedToWindow(RecyclerViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (onConfigStaggeredGridLayoutManager(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) lp;

                params.setFullSpan(true);
            }
        }
    }

    /**
     * 适配 StaggeredGridLayoutManager
     *
     * @param position 当前位置
     * @return true 则处理为一行
     */
    protected boolean onConfigStaggeredGridLayoutManager(int position) {
        return false;
    }
}
