package com.yline.view.recycler.demo;

import android.os.Bundle;
import android.view.View;

import com.yline.test.BaseTestFragment;
import com.yline.view.recycler.demo.z.ProgressCircleActivity;
import com.yline.view.recycler.demo.z.SecondMenuActivity;
import com.yline.view.recycler.demo.z.SuperSwipeCustomActivity;
import com.yline.view.recycler.demo.z.SuperSwipeSimpleActivity;

public class ZFragment extends BaseTestFragment
{
	public static ZFragment newInstance()
	{
		Bundle args = new Bundle();

		ZFragment fragment = new ZFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void testStart(View view, Bundle savedInstanceState)
	{
		addButton("Secondary 二级列表", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				SecondMenuActivity.actionStart(getContext());
			}
		});

		addButton("ViewCircleProgressBar", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ProgressCircleActivity.actionStart(getContext());
			}
		});

		addButton("SuperSwipeRefreshLayout Simple", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				SuperSwipeSimpleActivity.actionStart(getContext());
			}
		});

		addButton("SuperSwipeRefreshLayout Custom", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				SuperSwipeCustomActivity.actionStart(getContext());
			}
		});
	}
}
