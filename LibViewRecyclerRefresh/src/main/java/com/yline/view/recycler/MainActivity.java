package com.yline.view.recycler;

import android.os.Bundle;
import android.view.View;

import com.yline.test.BaseTestActivity;
import com.yline.view.recycler.refresh.CircleProgressActivity;
import com.yline.view.recycler.refresh.CustomRefreshActivity;
import com.yline.view.recycler.refresh.SimpleRefreshActivity;

public class MainActivity extends BaseTestActivity{
	@Override
	public void testStart(View view, Bundle savedInstanceState) {
		addTextView("Refresh 系列");
		addButton("CircleProgressBar", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CircleProgressActivity.launcher(MainActivity.this);
			}
		});
		
		addButton("SimpleRefresh", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SimpleRefreshActivity.launcher(MainActivity.this);
			}
		});
		
		addButton("CustomRefresh", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CustomRefreshActivity.launcher(MainActivity.this);
			}
		});
	}
}
