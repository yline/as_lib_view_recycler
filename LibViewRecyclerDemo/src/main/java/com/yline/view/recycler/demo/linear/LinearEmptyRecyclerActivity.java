package com.yline.view.recycler.demo.linear;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yline.view.recycler.decoration.CommonLinearDecoration;
import com.yline.view.recycler.demo.R;
import com.yline.view.recycler.demo.adapter.SimpleEmptyRecyclerActivity;

public class LinearEmptyRecyclerActivity extends SimpleEmptyRecyclerActivity
{
	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, LinearEmptyRecyclerActivity.class));
	}
	
	@Override
	protected void initRecyclerView(RecyclerView recyclerView)
	{
		// super.initRecyclerView(recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
		recyclerView.addItemDecoration(new CommonLinearDecoration(this)
		{
			@Override
			protected int getDivideResourceId()
			{
				return R.drawable.recycler_divider_black_normal;
			}
		});
	}
}
