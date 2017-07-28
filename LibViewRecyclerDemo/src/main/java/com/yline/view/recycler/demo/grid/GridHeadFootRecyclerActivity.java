package com.yline.view.recycler.demo.grid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.yline.view.recycler.decoration.CommonGridDecoration;
import com.yline.view.recycler.demo.R;
import com.yline.view.recycler.demo.adapter.SimpleHeadFootRecyclerActivity;

public class GridHeadFootRecyclerActivity extends SimpleHeadFootRecyclerActivity
{
	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, GridHeadFootRecyclerActivity.class));
	}

	@Override
	protected void initRecyclerView(RecyclerView recyclerView)
	{
		super.initRecyclerView(recyclerView);
		recyclerView.addItemDecoration(new CommonGridDecoration(this)
		{
			@Override
			protected int getHeadNumber()
			{
				return 2;
			}

			@Override
			protected int getFootNumber()
			{
				return 2;
			}
			
			@Override
			protected int getDivideResourceId()
			{
				return R.drawable.recycler_divider_black_normal;
			}
		});
	}
}
