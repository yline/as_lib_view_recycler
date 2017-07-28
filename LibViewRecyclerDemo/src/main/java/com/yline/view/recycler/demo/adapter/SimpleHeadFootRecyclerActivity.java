package com.yline.view.recycler.demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;
import com.yline.utils.UIScreenUtil;
import com.yline.view.recycler.demo.DataAdapterTest;
import com.yline.view.recycler.demo.R;
import com.yline.view.recycler.holder.RecyclerViewHolder;
import com.yline.view.recycler.simple.SimpleHeadFootRecyclerAdapter;

public class SimpleHeadFootRecyclerActivity extends BaseAppCompatActivity
{
	private SimpleHeadFootAdapter simpleAdapter;

	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, SimpleHeadFootRecyclerActivity.class));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.global_recycler);

		initView();
	}

	private void initView()
	{
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
		simpleAdapter = new SimpleHeadFootAdapter();

		LogFileUtil.v("adapter isEmptyViewShow = " + simpleAdapter.isEmptyViewShow());
		initRecyclerView(recyclerView);
		recyclerView.setAdapter(simpleAdapter);

		View headViewA = new View(this);
		headViewA.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIScreenUtil.dp2px(this, 20)));
		headViewA.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
		simpleAdapter.addHeadView(headViewA);

		View headViewB = new View(this);
		headViewB.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIScreenUtil.dp2px(this, 20)));
		headViewB.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_blue_bright));
		simpleAdapter.addHeadView(headViewB);

		View footViewA = new View(this);
		footViewA.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIScreenUtil.dp2px(this, 20)));
		footViewA.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
		simpleAdapter.addFootView(footViewA);

		View footViewB = new View(this);
		footViewB.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIScreenUtil.dp2px(this, 20)));
		footViewB.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_blue_bright));
		simpleAdapter.addFootView(footViewB);

		TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_recycler);
		new DataAdapterTest(tabLayout, simpleAdapter);
	}

	protected void initRecyclerView(RecyclerView recyclerView)
	{
		StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(gridLayoutManager);
	}

	private class SimpleHeadFootAdapter extends SimpleHeadFootRecyclerAdapter
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
