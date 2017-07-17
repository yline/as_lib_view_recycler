package com.yline.view.recycler.demo.simple;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yline.base.BaseAppCompatActivity;
import com.yline.utils.UIScreenUtil;
import com.yline.view.recycler.demo.R;
import com.yline.view.recycler.simple.SimpleHeadFootRecyclerAdapter;
import com.yline.view.recycler.simple.SimpleLinearItemDecoration;

import java.util.Arrays;

public class SimpleLinearDecorationActivity extends BaseAppCompatActivity
{
	private SimpleHeadFootRecyclerAdapter recyclerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.global_recycler);
		
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.addItemDecoration(new SimpleLinearItemDecoration(this)
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
				return R.drawable.recycler_divider_blue_normal;
			}
		});

		recyclerAdapter = new SimpleHeadFootRecyclerAdapter();
		recyclerView.setAdapter(recyclerAdapter);
		
		View viewA = new View(this);
		viewA.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIScreenUtil.dp2px(this, 10)));
		viewA.setBackgroundColor(Color.GREEN);
		recyclerAdapter.addHeadView(viewA);
		
		View viewB = new View(this);
		viewB.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIScreenUtil.dp2px(this, 10)));
		viewB.setBackgroundColor(Color.RED);
		recyclerAdapter.addHeadView(viewB);
		
		View viewC = new View(this);
		viewC.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIScreenUtil.dp2px(this, 10)));
		viewC.setBackgroundColor(Color.GREEN);
		recyclerAdapter.addFootView(viewC);
		
		View viewD = new View(this);
		viewD.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIScreenUtil.dp2px(this, 10)));
		viewD.setBackgroundColor(Color.RED);
		recyclerAdapter.addFootView(viewD);
		
		recyclerAdapter.addAll(Arrays.asList("yline", "Simple", "English", "fatenliyer", "sin", "cos", "baby", "piano", "tree", "sky", "the world"));
	}
	
	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, SimpleLinearDecorationActivity.class));
	}
}
