package com.yline.view.recycler.demo.refresh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.animation.Animation;

import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;
import com.yline.view.recycler.demo.R;
import com.yline.view.recycler.refresh.view.CircleProgressBar;

public class CircleProgressActivity extends BaseAppCompatActivity {

	private CircleProgressBar circleProgressBar;

	private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_progress);

		circleProgressBar = (CircleProgressBar) findViewById(R.id.view_circle_progress);
		circleProgressBar.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
		circleProgressBar.setTextDraw(true);
		// progressBar.setArrowShow(true);
		circleProgressBar.setOnAnimationListener(new Animation.AnimationListener()
		{
			@Override
			public void onAnimationStart(Animation animation)
			{
				LogFileUtil.v("onAnimationStart");
			}

			@Override
			public void onAnimationEnd(Animation animation)
			{
				LogFileUtil.v("onAnimationEnd");
			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{
				LogFileUtil.v("onAnimationRepeat");
			}
		});

		tabLayout = (TabLayout) findViewById(R.id.tab_progress);
		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
		{
			@Override
			public void onTabSelected(TabLayout.Tab tab)
			{
				switch (tab.getPosition())
				{
					case 0:
						circleProgressBar.stop();
						break;
					case 1:
						circleProgressBar.setAlpha(1.0f); // 设置 整体 透明度
						circleProgressBar.start();
						break;
					case 2:
						circleProgressBar.setProgressRotation(0.3f);
						circleProgressBar.setStartEndTrim(0, 0.3f);
						break;
					case 3:
						circleProgressBar.setArrowShow(true);
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
		tabLayout.addTab(tabLayout.newTab().setText("停止"));
		tabLayout.addTab(tabLayout.newTab().setText("开始"));
		tabLayout.addTab(tabLayout.newTab().setText("显示一半"));
		tabLayout.addTab(tabLayout.newTab().setText("显示箭头"));
    }
    
    public static void launcher(Context context)
    {
    	if (null != context)
    	{
    		Intent intent = new Intent(context, CircleProgressActivity.class);
    		if (!(context instanceof Activity))
    		{
    			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);	
    		}
    		context.startActivity(intent);
    	}
    }
}
