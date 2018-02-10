package com.yline.view.recycler.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yline.view.recycler.holder.RecyclerViewHolder;
import com.yline.view.recycler.manager.RecyclerDataManager;
import com.yline.view.recycler.manager.RecyclerSpanManager;

/**
 * 支持多种数据类型 的RecyclerAdapter
 *
 * @author yline 2017/10/18 -- 14:38
 * @version 1.0.0
 */
public class ItemMultiRecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    protected final ItemDelegateManager mItemDelegateManager;
    protected final RecyclerDataManager mDataManager;

    public ItemMultiRecyclerAdapter() {
        mDataManager = new RecyclerDataManager(this);
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
        return mItemDelegateManager.getItemType(mDataManager.get(position), position);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        mItemDelegateManager.onBindHolder(holder, mDataManager.get(position), position);
    }

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
}
