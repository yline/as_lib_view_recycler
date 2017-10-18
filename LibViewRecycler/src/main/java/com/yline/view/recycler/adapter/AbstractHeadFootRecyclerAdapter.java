package com.yline.view.recycler.adapter;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yline.view.recycler.holder.Callback;
import com.yline.view.recycler.holder.RecyclerViewHolder;
import com.yline.view.recycler.manager.HeadFootRecyclerDataManager;

import java.util.Collection;
import java.util.List;

/**
 * 添加头部和底部的Recycle
 *
 * @author yline 2017/5/23 -- 10:31
 * @version 1.0.0
 */
public abstract class AbstractHeadFootRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> implements Callback.IHeadFootCallback, Callback.IDataAdapterCallback<T> {
    private HeadFootRecyclerDataManager<T> mDataManager;

    // 头部的开始标签 1024
    private static final int BASE_ITEM_TYPE_HEADER = 1024;

    // 底部最大个数：1024
    private static final int BASE_ITEM_TYPE_FOOTER = Integer.MAX_VALUE - 1024;

    // 头布局
    private SparseArrayCompat<View> headViewArray;

    // 底部布局
    private SparseArrayCompat<View> footViewArray;

    public AbstractHeadFootRecyclerAdapter() {
        headViewArray = new SparseArrayCompat<>();
        footViewArray = new SparseArrayCompat<>();

        mDataManager = new HeadFootRecyclerDataManager<>(this);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (headViewArray.get(viewType) != null) {
            return new RecyclerViewHolder(headViewArray.get(viewType));
        } else if (footViewArray.get(viewType) != null) {
            return new RecyclerViewHolder(footViewArray.get(viewType));
        }

        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(getItemRes(), parent, false));
    }

    /**
     * @return item 资源文件
     */
    public abstract int getItemRes();

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            return headViewArray.keyAt(position);
        } else if (isFooterViewPos(position)) {
            return footViewArray.keyAt(position - getHeadersCount() - mDataManager.getDataSize());
        }
        return super.getItemViewType(position - getHeadersCount());
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position, List<Object> payloads) {
        if (isHeaderViewPos(position)) {
            return;
        }

        if (isFooterViewPos(position)) {
            return;
        }

        super.onBindViewHolder(holder, position - getHeadersCount(), payloads);
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + getFootersCount() + mDataManager.getDataSize();
    }

    private boolean isHeaderViewPos(int position) {
        return (position >= 0) && (position < getHeadersCount());
    }

    private boolean isFooterViewPos(int position) {
        return position >= getHeadersCount() + mDataManager.getDataSize();
    }

    public void addHeadView(View view) {
        headViewArray.put(headViewArray.size() + BASE_ITEM_TYPE_HEADER, view);
    }

    public void addFootView(View view) {
        footViewArray.put(footViewArray.size() + BASE_ITEM_TYPE_FOOTER, view);
    }

    @Override
    public int getHeadersCount() {
        return headViewArray.size();
    }

    @Override
    public int getFootersCount() {
        return footViewArray.size();
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

    protected int onConfigGridLayoutManager(GridLayoutManager gridLayoutManager, GridLayoutManager.SpanSizeLookup spanSizeLookup, int position) {
        int viewType = getItemViewType(position);
        if (headViewArray.get(viewType) != null) {
            return gridLayoutManager.getSpanCount();
        } else if (footViewArray.get(viewType) != null) {
            return gridLayoutManager.getSpanCount();
        }

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

    protected boolean onConfigStaggeredGridLayoutManager(int position) {
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            return true;
        }
        return false;
    }

	/* &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& 兼容数据操作 &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& */

    @Override
    public List<T> getDataList() {
        return mDataManager.getDataList();
    }

    @Override
    public void setDataList(List<T> list, boolean isNotify) {
        mDataManager.setDataList(list, isNotify);
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
    public void clear(boolean isNotify) {
        mDataManager.clear(isNotify);
    }

    @Override
    public boolean add(T object, boolean isNotify) {
        return mDataManager.add(object, isNotify);
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
    public boolean update(int index, T t, boolean isNotify) {
        return mDataManager.update(index, t, isNotify);
    }

    @Override
    public boolean update(int[] index, T[] arrays, boolean isNotify) {
        return mDataManager.update(index, arrays, isNotify);
    }
}
