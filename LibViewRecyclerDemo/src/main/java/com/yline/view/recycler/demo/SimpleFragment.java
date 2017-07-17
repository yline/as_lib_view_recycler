package com.yline.view.recycler.demo;

import android.os.Bundle;
import android.view.View;

import com.yline.application.BaseApplication;
import com.yline.log.LogFileUtil;
import com.yline.test.BaseTestFragment;
import com.yline.view.recycler.demo.simple.SimpleGridDecorationActivity;
import com.yline.view.recycler.demo.simple.SimpleHeadFootRecyclerActivity;
import com.yline.view.recycler.demo.simple.SimpleLinearDecorationActivity;
import com.yline.view.recycler.demo.simple.SimpleListActivity;
import com.yline.view.recycler.demo.simple.SimpleRecyclerActivity;

public class SimpleFragment extends BaseTestFragment
{
	public static SimpleFragment newInstance()
	{
		Bundle args = new Bundle();

		SimpleFragment fragment = new SimpleFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void testStart(View view, Bundle savedInstanceState)
	{
		// SimpleListActivity
		addButton("SimpleListAdapter", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(BaseApplication.TAG, "CommonListAdapter");
				SimpleListActivity.actionStart(getContext());
			}
		});

		addButton("SimpleRecycleAdapter", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(BaseApplication.TAG, "CommonRecyclerAdapter");
				SimpleRecyclerActivity.actionStart(getContext());
			}
		});

		addButton("SimpleGridItemDecoration", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				SimpleGridDecorationActivity.actionStart(getContext());
			}
		});

		addButton("SimpleLinearItemDecoration", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				SimpleLinearDecorationActivity.actionStart(getContext());
			}
		});

		addButton("SimpleHeadFootRecyclerActivity", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				SimpleHeadFootRecyclerActivity.actionStart(getContext());
			}
		});
	}
}
