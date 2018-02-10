package com.yline.view.recycler.manager;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import com.yline.view.recycler.holder.RecyclerViewHolder;

/**
 * RecyclerView Grid适配使用
 *
 * @author yline 2018/2/10 -- 14:29
 * @version 1.0.0
 */
public class RecyclerSpanManager {
    /**
     * RecyclerView 适配 GridLayoutManager
     *
     * @param recyclerView 控件
     * @param callback     回调
     */
    public static void onAttachedToRecyclerView(RecyclerView recyclerView, @NonNull final OnGridCallback callback) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return callback.onGridConfig(gridLayoutManager, spanSizeLookup, position);
                }
            });

            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }

    /**
     * RecyclerView 适配 StaggeredGridLayoutManager
     *
     * @param holder   组件
     * @param callback 回调
     */
    public static void onViewAttachedToWindow(RecyclerViewHolder holder, @NonNull final OnStaggerCallback callback) {
        int position = holder.getLayoutPosition();
        if (callback.onStaggerConfig(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) lp;

                params.setFullSpan(true);
            }
        }
    }

    public interface OnGridCallback {
        /**
         * 适配 GridLayoutManager
         *
         * @param gridLayoutManager 方向器
         * @param spanSizeLookup    原本的spanSize
         * @param position          当前位置
         * @return 列数
         */
        int onGridConfig(GridLayoutManager gridLayoutManager, GridLayoutManager.SpanSizeLookup spanSizeLookup, int position);
    }

    public interface OnStaggerCallback {
        /**
         * 适配 StaggeredGridLayoutManager
         *
         * @param position 当前位置
         * @return true 则处理为一行
         */
        boolean onStaggerConfig(int position);
    }
}
