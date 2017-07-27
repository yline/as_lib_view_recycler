package com.yline.view.recycler.demo;

import android.os.Bundle;
import android.view.View;

import com.yline.application.BaseApplication;
import com.yline.log.LogFileUtil;
import com.yline.test.BaseTestActivity;
import com.yline.view.recycler.demo.simple.SimpleGridDecorationActivity;
import com.yline.view.recycler.demo.simple.SimpleHeadFootRecyclerActivity;
import com.yline.view.recycler.demo.simple.SimpleLinearDecorationActivity;
import com.yline.view.recycler.demo.simple.SimpleListActivity;
import com.yline.view.recycler.demo.simple.SimpleRecyclerActivity;

public class MainActivity extends BaseTestActivity
{
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
				SimpleListActivity.actionStart(MainActivity.this);
			}
		});

		addButton("SimpleRecycleAdapter", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(BaseApplication.TAG, "CommonRecyclerAdapter");
				SimpleRecyclerActivity.actionStart(MainActivity.this);
			}
		});

		addButton("SimpleGridItemDecoration", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				SimpleGridDecorationActivity.actionStart(MainActivity.this);
			}
		});

		addButton("SimpleLinearItemDecoration", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				SimpleLinearDecorationActivity.actionStart(MainActivity.this);
			}
		});

		addButton("SimpleHeadFootRecyclerActivity", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				SimpleHeadFootRecyclerActivity.actionStart(MainActivity.this);
			}
		});
	}
}
