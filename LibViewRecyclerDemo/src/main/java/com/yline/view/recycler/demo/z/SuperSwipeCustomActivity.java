package com.yline.view.recycler.demo.z;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yline.base.BaseAppCompatActivity;
import com.yline.view.recycler.demo.InnerConstant;
import com.yline.view.recycler.demo.R;
import com.yline.view.recycler.simple.SimpleHeadFootRecyclerAdapter;
import com.yline.view.recycler.z.swiperefresh.SimpleSwipeRefreshAdapter;
import com.yline.view.recycler.z.swiperefresh.ViewSwipeRefreshLayout;

import java.util.Random;

public class SuperSwipeCustomActivity extends BaseAppCompatActivity
{
	private ViewSwipeRefreshLayout swipeRefreshLayout;
	
	private RecyclerView recyclerView;
	
	private SimpleHeadFootRecyclerAdapter recyclerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_super_swipe);
		
		swipeRefreshLayout = (ViewSwipeRefreshLayout) findViewById(R.id.super_swipe_refresh);
		recyclerView = (RecyclerView) findViewById(R.id.recycler_super_swipe);
		recyclerAdapter = new SimpleHeadFootRecyclerAdapter();

		swipeRefreshLayout.setRefreshAdapter(new SimpleSwipeRefreshAdapter(this)
		{
			// 动画下拉时，距离变动
			@Override
			protected void onCreating(float dragDistance, float targetDistance)
			{
				super.onCreating(dragDistance, targetDistance);
			}

			// 传递你需要定制的view;
			@NonNull
			@Override
			protected View getView(Context context)
			{
				return super.getView(context);
			}

			// 正在刷新时，执行的操作
			@Override
			protected void onAnimate()
			{
				super.onAnimate();

				new Handler().postDelayed(new Runnable()
				{

					@Override
					public void run()
					{
						swipeRefreshLayout.setRefreshing(false);
						// 更新recyclerView
						recyclerAdapter.add(0, "Refresh Item + " + new Random().nextInt(300));
						recyclerView.scrollToPosition(0);
					}
				}, 4000);
			}

			// 设置是否在表面；true:子View随下拉而一起滑动； false:子view不下滑，只是浮在上面
			@Override
			public boolean isTargetScroll()
			{
				return super.isTargetScroll();
			}

			// 返回背景颜色
			@Override
			public int getBackgroundResource()
			{
				return super.getBackgroundResource();
			}
		});

		// 支持自定义（view）
		swipeRefreshLayout.setLoadAdapter(null);
		swipeRefreshLayout.setOnLoadListener(new ViewSwipeRefreshLayout.OnSwipeListener()
		{
			@Override
			public void onAnimate()
			{
				new Handler().postDelayed(new Runnable()
				{

					@Override
					public void run()
					{
						swipeRefreshLayout.setLoadMore(false);
						// 更新recyclerView
						int itemNumber = recyclerAdapter.getItemCount();
						recyclerAdapter.add(itemNumber, "Loaded Item + " + new Random().nextInt(300));
						recyclerView.scrollToPosition(itemNumber);
					}
				}, 4000);
			}
		});

		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(recyclerAdapter);
		
		recyclerAdapter.setDataList(InnerConstant.getMvList());
	}
	
	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, SuperSwipeCustomActivity.class));
	}
}
