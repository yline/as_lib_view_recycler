package com.yline.view.recycler.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yline.view.recycler.R;
import com.yline.view.recycler.holder.Callback;
import com.yline.view.recycler.holder.RecyclerViewHolder;
import com.yline.view.recycler.manager.HeadFootRecyclerDataManager;
import com.yline.view.recycler.manager.RecyclerSpanManager;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * 添加头部和底部的Recycle
 *
 * @author yline 2017/5/23 -- 10:31
 * @version 1.0.0
 */
public abstract class AbstractHeadFootRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> implements Callback.IDataAdapterCallback<T> {
    public static final int HEAD_COUNT = 1; // 头部数量
    public static final int FOOT_COUNT = 1; // 底部数量

    public static final int TYPE_HEAD = 128; // 头部
    public static final int TYPE_FOOT = 256; // 底部

    private final LinearLayout mHeadView;
    private final LinearLayout mFootView;

    private final HeadFootRecyclerDataManager<T> mDataManager;

    public AbstractHeadFootRecyclerAdapter(Context context) {
        mHeadView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.view_recycler_head_container, null);
        mHeadView.setMinimumHeight(1); // 若头部高度为0，则外部刷新框架下拉刷新无效
        mFootView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.view_recycler_foot_container, null);
        mFootView.setMinimumHeight(1); // 若底部高度为0，则外部刷新框架下拉刷新无效

        mDataManager = new HeadFootRecyclerDataManager<>(this);
    }

    /**
     * 设置图片资源文件
     *
     * @return item 资源文件
     */
    public abstract int getItemRes();

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        } else if (position == mDataManager.size() + HEAD_COUNT) {
            return TYPE_FOOT;
        }

        return super.getItemViewType(position - HEAD_COUNT);
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEAD) {
            return new RecyclerViewHolder(mHeadView);
        } else if (viewType == TYPE_FOOT) {
            return new RecyclerViewHolder(mFootView);
        } else {
            return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(getItemRes(), parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position, List<Object> payloads) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_HEAD || viewType == TYPE_FOOT) {
            return;
        }

        super.onBindViewHolder(holder, position - HEAD_COUNT, payloads);
    }

    @Override
    public int getItemCount() {
        return HEAD_COUNT + mDataManager.size() + FOOT_COUNT;
    }

    public void addHeadView(View view) {
        if (null != mHeadView) {
            mHeadView.addView(view);
        }
    }

    public void addFootView(View view) {
        if (null != mFootView) {
            mFootView.addView(view);
        }
    }

    /* &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& 适配情形 &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerSpanManager.onAttachedToRecyclerView(recyclerView, new RecyclerSpanManager.OnGridCallback() {
            @Override
            public int onGridConfig(GridLayoutManager gridLayoutManager, GridLayoutManager.SpanSizeLookup spanSizeLookup, int position) {
                int viewType = getItemViewType(position);
                if (viewType == TYPE_HEAD || viewType == TYPE_FOOT) {
                    return gridLayoutManager.getSpanCount();
                }

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
                int viewType = getItemViewType(position);
                if (viewType == TYPE_HEAD || viewType == TYPE_FOOT) {
                    return true;
                }
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
