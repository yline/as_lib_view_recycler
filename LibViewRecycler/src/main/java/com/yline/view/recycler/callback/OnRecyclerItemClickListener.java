package com.yline.view.recycler.callback;

import com.yline.view.recycler.holder.RecyclerViewHolder;

/**
 * Recycler item点击的 接口，未使用
 *
 * @author yline 2017/5/23 -- 10:24
 * @version 1.0.0
 */
public interface OnRecyclerItemClickListener<T>
{
	/**
	 * Recycler Item 点击回调
	 *
	 * @param viewHolder Item 的 布局
	 * @param t          数据
	 * @param position   当前位置
	 */
	void onItemClick(RecyclerViewHolder viewHolder, T t, int position);
}
