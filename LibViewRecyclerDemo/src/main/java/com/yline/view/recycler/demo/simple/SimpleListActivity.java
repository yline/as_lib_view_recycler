package com.yline.view.recycler.demo.simple;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.yline.base.BaseActivity;
import com.yline.view.recycler.demo.InnerConstant;
import com.yline.view.recycler.demo.R;
import com.yline.view.recycler.simple.SimpleListAdapter;

public class SimpleListActivity extends BaseActivity
{
	private ListView lvDemo;

	private SimpleListAdapter listAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.global_list);

		lvDemo = (ListView) findViewById(R.id.list_view);

		listAdapter = new SimpleListAdapter(this);
		lvDemo.setAdapter(listAdapter);

		listAdapter.setDataList(InnerConstant.getMvList());
	}

	/**
	 * 开启该CommonListActivity
	 *
	 * @param context
	 */
	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, SimpleListActivity.class));
	}
}
