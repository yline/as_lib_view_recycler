package com.yline.view.recycler.callback;

import com.yline.view.recycler.holder.RecyclerViewHolder;

/**
 * Recycler Item 长按事件
 *
 * @author yline 2018/9/5 -- 11:26
 */
public interface OnRecyclerItemLongClickListener<T> {
	/**
	 * 条目，长按
	 *
	 * @param viewHolder Item布局
	 * @param t          数据
	 * @param position   当前位置
	 */
	void onItemLongClick(RecyclerViewHolder viewHolder, T t, int position);
}
