package com.yline.view.recycler.demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;
import com.yline.view.recycler.demo.DataAdapterTest;
import com.yline.view.recycler.demo.R;
import com.yline.view.recycler.holder.RecyclerViewHolder;
import com.yline.view.recycler.simple.SimpleEmptyRecyclerAdapter;

public class SimpleEmptyRecyclerActivity extends BaseAppCompatActivity
{
	private RecyclerView recyclerView;

	private SimpleEmptyRecyclerAdapter homeAdapter;
	
	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, SimpleEmptyRecyclerActivity.class));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.global_recycler);

		recyclerView = (RecyclerView) findViewById(R.id.recycler);
		initRecyclerView(recyclerView);

		homeAdapter = new DefineRecyclerAdapter();
		recyclerView.setAdapter(homeAdapter);

		LogFileUtil.v("adapter isEmptyViewShow = " + homeAdapter.isEmptyViewShow());
		TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_recycler);
		new DataAdapterTest(tabLayout, homeAdapter);
	}

	protected void initRecyclerView(RecyclerView recyclerView)
	{
		GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
		gridLayoutManager.setSpanCount(3);
		recyclerView.setLayoutManager(gridLayoutManager);
	}

	private class DefineRecyclerAdapter extends SimpleEmptyRecyclerAdapter
	{
		@Override
		public int getEmptyItemRes()
		{
			return R.layout.global_empty;
		}

		@Override
		public void onBindEmptyViewHolder(RecyclerViewHolder viewHolder, int position)
		{
			viewHolder.setOnClickListener(R.id.btn_empty_recycler, new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					finish();
				}
			});
		}
	}
}
