package com.yline.view.recycler.demo.simple;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yline.base.BaseAppCompatActivity;
import com.yline.test.TestConstant;
import com.yline.utils.UIScreenUtil;
import com.yline.view.recycler.demo.R;
import com.yline.view.recycler.holder.RecyclerViewHolder;
import com.yline.view.recycler.simple.SimpleGridItemDecoration;
import com.yline.view.recycler.simple.SimpleHeadFootRecyclerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SimpleHeadFootRecyclerActivity extends BaseAppCompatActivity
{
	private SimpleHeadFootAdapter simpleAdapter;

	private GridLayoutManager gridLayoutManager;

	private Random random = new Random();

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
		initData();
	}

	private void initView()
	{
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
		gridLayoutManager = new GridLayoutManager(this, 3);
		simpleAdapter = new SimpleHeadFootAdapter();

		recyclerView.setLayoutManager(gridLayoutManager);
		recyclerView.addItemDecoration(new SimpleGridItemDecoration(this)
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

		simpleAdapter.setDataList(TestConstant.getListFive(10));

		TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_recycler);
		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
		{
			@Override
			public void onTabSelected(TabLayout.Tab tab)
			{
				switch (tab.getPosition())
				{
					case 0:
						simpleAdapter.add("add-" + random.nextInt(50));
						break;
					case 1:
						simpleAdapter.add(0, "add-" + random.nextInt(50));
						break;
					case 2:
						simpleAdapter.addAll(Arrays.asList("add-" + random.nextInt(50), "add-" + random.nextInt(50), "add-" + random.nextInt(50)));
						break;
					case 3:
						simpleAdapter.addAll(0, Arrays.asList("add-" + random.nextInt(50), "add-" + random.nextInt(50), "add-" + random.nextInt(50)));
						break;
					case 4:
						simpleAdapter.remove(0);
						break;
					case 5:
						if (simpleAdapter.getDataSize() > 1)
						{
							String object = simpleAdapter.getItem(simpleAdapter.getDataSize() - 1);
							simpleAdapter.remove(object);
						}
						break;
					case 6:
						if (simpleAdapter.getDataSize() > 3)
						{
							String objectA = simpleAdapter.getItem(simpleAdapter.getDataSize() - 1);
							String objectB = simpleAdapter.getItem(simpleAdapter.getDataSize() - 2);
							String objectC = simpleAdapter.getItem(simpleAdapter.getDataSize() - 3);
							simpleAdapter.removeAll(Arrays.asList(objectA, objectB, objectC));
						}
						break;
					case 7:
						simpleAdapter.clear();
						gridLayoutManager.setSpanCount(1);
						break;
					case 8:
						simpleAdapter.update(0, "update-" + random.nextInt(50));
						break;
					case 9:
						simpleAdapter.update(new int[]{0, 1, 2}, new String[]{"update-" + random.nextInt(50), "update-" + random.nextInt(50), "update-" + random.nextInt(50)});
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
		List data = new ArrayList<>();
		for (int i = 0; i < 22; i++)
		{
			String temp = 200 + random.nextInt(5) + "";
			data.add(temp);
		}

		simpleAdapter.setDataList(data);
	}

	private class SimpleHeadFootAdapter extends SimpleHeadFootRecyclerAdapter
	{
		@Override
		public int getEmptyItemRes()
		{
			return R.layout.global_recycler_empty;
		}

		@Override
		public void onBindEmptyViewHolder(RecyclerViewHolder viewHolder, int position)
		{
			// super.onBindEmptyViewHolder(viewHolder, position);

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
