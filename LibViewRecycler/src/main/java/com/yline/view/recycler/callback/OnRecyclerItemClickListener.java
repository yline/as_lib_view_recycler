package com.yline.view.recycler.callback;

import com.yline.view.recycler.holder.RecyclerViewHolder;

/**
 * Recycler Item 点击事件接口
 *
 * @author yline 2018/9/5 -- 11:26
 */
public interface OnRecyclerItemClickListener<T> {
	/**
	 * 条目，点击
	 *
	 * @param viewHolder Item布局
	 * @param t          数据
	 * @param position   当前位置
	 */
	void onItemClick(RecyclerViewHolder viewHolder, T t, int position);
}