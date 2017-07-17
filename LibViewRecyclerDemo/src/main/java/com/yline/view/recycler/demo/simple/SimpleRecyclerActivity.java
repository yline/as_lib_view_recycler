package com.yline.view.recycler.demo.simple;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yline.base.BaseAppCompatActivity;
import com.yline.view.recycler.demo.R;
import com.yline.view.recycler.holder.RecyclerViewHolder;
import com.yline.view.recycler.simple.SimpleRecyclerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SimpleRecyclerActivity extends BaseAppCompatActivity
{
	private RecyclerView recyclerView;

	private List<String> data;

	private SimpleRecyclerAdapter homeAdapter;

	private Random random = new Random();

	private GridLayoutManager gridLayoutManager;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.global_recycler);

		recyclerView = (RecyclerView) findViewById(R.id.recycler);

		gridLayoutManager = new GridLayoutManager(this, 3);
		gridLayoutManager.setSpanCount(3);
		recyclerView.setLayoutManager(gridLayoutManager);

		homeAdapter = new DefineRecyclerAdapter();
		recyclerView.setAdapter(homeAdapter);

		initData();

		TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_recycler);
		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
		{
			@Override
			public void onTabSelected(TabLayout.Tab tab)
			{
				switch (tab.getPosition())
				{
					case 0:
						homeAdapter.add("add-" + random.nextInt(50));
						break;
					case 1:
						homeAdapter.add(0, "add-" + random.nextInt(50));
						break;
					case 2:
						homeAdapter.addAll(Arrays.asList("add-" + random.nextInt(50), "add-" + random.nextInt(50), "add-" + random.nextInt(50)));
						break;
					case 3:
						homeAdapter.addAll(0, Arrays.asList("add-" + random.nextInt(50), "add-" + random.nextInt(50), "add-" + random.nextInt(50)));
						break;
					case 4:
						homeAdapter.remove(0);
						break;
					case 5:
						if (homeAdapter.dataSize() > 1)
						{
							String object = homeAdapter.getItem(homeAdapter.dataSize() - 1);
							homeAdapter.remove(object);
						}
						break;
					case 6:
						if (homeAdapter.dataSize() > 3)
						{
							String objectA = homeAdapter.getItem(homeAdapter.dataSize() - 1);
							String objectB = homeAdapter.getItem(homeAdapter.dataSize() - 2);
							String objectC = homeAdapter.getItem(homeAdapter.dataSize() - 3);
							homeAdapter.removeAll(Arrays.asList(objectA, objectB, objectC));
						}
						break;
					case 7:
						homeAdapter.clear();
						gridLayoutManager.setSpanCount(1);
						break;
					case 8:
						homeAdapter.update(0, "update-" + random.nextInt(50));
						break;
					case 9:
						homeAdapter.update(new int[]{0, 1, 2}, new String[]{"update-" + random.nextInt(50), "update-" + random.nextInt(50), "update-" + random.nextInt(50)});
						break;
					case 10:
						initData();
						gridLayoutManager.setSpanCount(3);
						break;
				}
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab)
			{

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab)
			{

			}
		});
		tabLayout.addTab(tabLayout.newTab().setText("末尾+1"));
		tabLayout.addTab(tabLayout.newTab().setText("首位+1"));
		tabLayout.addTab(tabLayout.newTab().setText("末尾+n"));
		tabLayout.addTab(tabLayout.newTab().setText("首位+n"));
		tabLayout.addTab(tabLayout.newTab().setText("首位-1"));
		tabLayout.addTab(tabLayout.newTab().setText("末尾-1"));
		tabLayout.addTab(tabLayout.newTab().setText("末尾-n"));
		tabLayout.addTab(tabLayout.newTab().setText("清除所有"));
		tabLayout.addTab(tabLayout.newTab().setText("首位更新"));
		tabLayout.addTab(tabLayout.newTab().setText("前三位更新"));

		tabLayout.addTab(tabLayout.newTab().setText("重置初始化"));

		tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
		tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
	}

	private void initData()
	{
		data = new ArrayList<>();
		for (int i = 0; i < 22; i++)
		{
			String temp = 200 + random.nextInt(5) + "";
			data.add(temp);
		}

		homeAdapter.setDataList(data);
	}

	/**
	 * 开启该CommonListActivity
	 *
	 * @param context
	 */
	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, SimpleRecyclerActivity.class));
	}

	private class DefineRecyclerAdapter extends SimpleRecyclerAdapter
	{
		@Override
		public int getEmptyItemRes()
		{
			return R.layout.global_recycler_empty;
		}

		@Override
		public void onBindEmptyViewHolder(RecyclerViewHolder viewHolder, int position)
		{
			// super.setEmptyViewContent(viewHolder, position);

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
