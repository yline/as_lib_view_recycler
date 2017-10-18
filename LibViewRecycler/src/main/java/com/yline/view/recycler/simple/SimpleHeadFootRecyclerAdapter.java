package com.yline.view.recycler.simple;

import com.yline.view.recycler.adapter.AbstractHeadFootRecyclerAdapter;
import com.yline.view.recycler.holder.RecyclerViewHolder;

/**
 * 使用默认布局，简单的Recycler布局，支持添加头部和底部
 *
 * @author yline 2017/5/23 -- 10:20
 * @version 1.0.0
 */
public class SimpleHeadFootRecyclerAdapter extends AbstractHeadFootRecyclerAdapter<String>
{
	@Override
	public int getItemRes()
	{
		return android.R.layout.simple_list_item_1;
	}

	@Override
	public void onBindViewHolder(RecyclerViewHolder holder, int position)
	{
		holder.setText(android.R.id.text1, getItem(position));
	}
}
