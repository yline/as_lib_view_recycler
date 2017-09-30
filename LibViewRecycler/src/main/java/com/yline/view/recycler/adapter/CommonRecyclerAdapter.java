package com.yline.view.recycler.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.yline.view.recycler.holder.Callback;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * RecyclerView 公共的 Adapter
 *
 * @author yline 2017/5/23 -- 10:28
 * @version 1.0.0
 */
public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> implements Callback.IDataAdapterCallback<T> {
    protected List<T> sList;

    public CommonRecyclerAdapter() {
        this.sList = new ArrayList<>();
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
        return sList.size();
    }

    @Override
    public List<T> getDataList() {
        return Collections.unmodifiableList(sList);
    }

    @Override
    public void setDataList(List<T> tList, boolean isNotify) {
        if (null != tList) {
            this.sList = new ArrayList<>(tList);
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
    public int getDataSize() {
        return sList.size();
    }

    @Override
    public boolean contains(T element) {
        return sList.contains(element);
    }

    @Override
    public boolean containsAll(Collection collection) {
        return sList.containsAll(collection);
    }

    @Override
    public boolean isEmpty() {
        return sList.isEmpty();
    }

    @Override
    public boolean add(T object, boolean isNotify) {
        boolean result = sList.add(object);
        if (isNotify) {
            this.notifyItemInserted(sList.size() - 1);
        }
        return result;
    }

    @Override
    public void add(int index, T element, boolean isNotify) {
        sList.add(index, element);
        if (isNotify) {
            this.notifyItemInserted(index);
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
            this.notifyItemRangeInserted(index, collection.size());
        }
        return result;
    }

    @Override
    public T remove(int index, boolean isNotify) {
        if (sList.size() > index) {
            T t = sList.remove(index);
            if (isNotify) {
                this.notifyItemRemoved(index);
            }
            return t;
        }
        return null;
    }

    @Override
    public boolean remove(T t, boolean isNotify) {
        List<Integer> objectList = new ArrayList<>();
        for (int i = sList.size() - 1; i >= 0; i--) {
            if (null != t && sList.get(i).equals(t)) {
                objectList.add(i);
            }
        }

        boolean result = sList.removeAll(Arrays.asList(t));
        if (result && isNotify) {
            for (Integer integer : objectList) {
                this.notifyItemRemoved(integer);
            }
        }

        return result;
    }

    @Override
    public boolean removeAll(Collection<? extends T> collection, boolean isNotify) {
        int length = sList.size();
        if (null == collection || collection.size() > length) {
            return false;
        }

        List<Integer> objectList = new ArrayList<>();
        for (int i = sList.size() - 1; i >= 0; i--) {
            if (collection.contains(sList.get(i))) {
                objectList.add(i);
            }
        }

        boolean result = sList.removeAll(collection);
        if (result && isNotify) {
            for (Integer integer : objectList) {
                this.notifyItemRemoved(integer);
            }
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
            this.notifyItemChanged(index);
        }

        return true;
    }

    @Override
    public boolean update(int index[], T[] arrays, boolean isNotify) {
        if (index.length != arrays.length || index.length > sList.size()) {
            return false;
        }

        for (int i : index) {
            if (i >= sList.size()) {
                return false;
            }
        }

        for (int i = 0; i < arrays.length; i++) {
            update(index[i], arrays[i], isNotify);
        }

        return true;
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
