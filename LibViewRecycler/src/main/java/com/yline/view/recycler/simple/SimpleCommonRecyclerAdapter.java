package com.yline.view.recycler.simple;

import com.yline.view.recycler.adapter.AbstractCommonRecyclerAdapter;
import com.yline.view.recycler.holder.RecyclerViewHolder;


public class SimpleCommonRecyclerAdapter extends AbstractCommonRecyclerAdapter<String>
{
	@Override
	public void onBindViewHolder(RecyclerViewHolder viewHolder, int position)
	{
		viewHolder.setText(android.R.id.text1, getItem(position));
	}

	@Override
	public int getItemRes()
	{
		return android.R.layout.simple_list_item_1;
	}
}
