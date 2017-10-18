package com.yline.view.recycler.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 支持多种数据类型 的RecyclerAdapter
 *
 * @author yline 2017/10/18 -- 14:38
 * @version 1.0.0
 */
public class ItemMultiRecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    protected final ItemDelegateManager mItemDelegateManager;
    protected List<Object> mList;

    public ItemMultiRecyclerAdapter() {
        mList = new ArrayList<>();
        mItemDelegateManager = new ItemDelegateManager();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemDelegateCallback itemDelegateCallback = mItemDelegateManager.getItemDeleteGateCallback(viewType);
        if (null != itemDelegateCallback) {
            return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(itemDelegateCallback.getItemRes(), parent, false));
        }

        View itemView = new View(parent.getContext());
        itemView.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return mItemDelegateManager.getItemType(mList.get(position), position);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        mItemDelegateManager.onBindHolder(holder, mList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
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
