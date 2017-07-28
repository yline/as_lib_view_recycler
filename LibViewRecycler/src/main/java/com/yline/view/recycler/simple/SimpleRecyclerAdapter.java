package com.yline.view.recycler.simple;

import com.yline.view.recycler.adapter.CommonRecyclerAdapter;
import com.yline.view.recycler.holder.RecyclerViewHolder;


public class SimpleRecyclerAdapter extends CommonRecyclerAdapter<String>
{
	@Override
	public void onBindViewHolder(RecyclerViewHolder viewHolder, int position)
	{
		viewHolder.setText(android.R.id.text1, sList.get(position));
	}

	@Override
	public int getItemRes()
	{
		return android.R.layout.simple_list_item_1;
	}
}
