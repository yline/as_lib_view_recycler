package com.yline.view.recycler.demo;

import android.os.Bundle;
import android.view.View;

import com.yline.test.BaseTestActivity;
import com.yline.view.recycler.demo.adapter.SimpleEmptyRecyclerActivity;
import com.yline.view.recycler.demo.adapter.SimpleHeadFootRecyclerActivity;
import com.yline.view.recycler.demo.adapter.SimpleListActivity;
import com.yline.view.recycler.demo.adapter.SimpleRecyclerActivity;
import com.yline.view.recycler.demo.grid.GridEmptyRecyclerActivity;
import com.yline.view.recycler.demo.grid.GridHeadFootRecyclerActivity;
import com.yline.view.recycler.demo.grid.GridRecyclerActivity;
import com.yline.view.recycler.demo.linear.LinearEmptyRecyclerActivity;
import com.yline.view.recycler.demo.linear.LinearHeadFootRecyclerActivity;
import com.yline.view.recycler.demo.linear.LinearRecyclerActivity;

public class MainActivity extends BaseTestActivity
{
	@Override
	public void testStart(View view, Bundle savedInstanceState)
	{
		addTextView("Simple 系列，无ItemDecoration");
		addButton("SimpleListAdapter", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				SimpleListActivity.actionStart(MainActivity.this);
			}
		});
		
		addButton("SimpleRecyclerAdapter", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				SimpleRecyclerActivity.actionStart(MainActivity.this);
			}
		});

		addButton("SimpleEmptyRecyclerAdapter", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				SimpleEmptyRecyclerActivity.actionStart(MainActivity.this);
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

		addTextView("Linear ItemDecoration 系列");
		addButton("LinearRecyclerActivity", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LinearRecyclerActivity.actionStart(MainActivity.this);
			}
		});

		addButton("LinearEmptyRecyclerActivity", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LinearEmptyRecyclerActivity.actionStart(MainActivity.this);
			}
		});

		addButton("LinearHeadFootRecyclerActivity", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LinearHeadFootRecyclerActivity.actionStart(MainActivity.this);
			}
		});

		addTextView("Grid ItemDecoration 系列");
		addButton("GridRecyclerActivity", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				GridRecyclerActivity.actionStart(MainActivity.this);
			}
		});
		
		addButton("GridEmptyRecyclerActivity", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				GridEmptyRecyclerActivity.actionStart(MainActivity.this);
			}
		});

		addButton("GridHeadFootRecyclerActivity", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				GridHeadFootRecyclerActivity.actionStart(MainActivity.this);
			}
		});
	}
}
