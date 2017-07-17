package com.yline.view.recycler.z.swiperefresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.yline.view.recycler.R;
import com.yline.view.recycler.z.progress.ViewCircleProgressBar;

/**
 * SuperSwipeRefreshLayout 默认的Adapter;
 *
 * @author yline 2017/3/17 -- 18:12
 * @version 1.0.0
 */
public class SimpleSwipeRefreshAdapter extends CommonSwipeRefreshAdapter
{
	private ViewCircleProgressBar circleProgressBar;

	private float percent;

	public SimpleSwipeRefreshAdapter(Context context)
	{
		super(context);
	}

	@Override
	protected void onCreating(float dragDistance, float targetDistance)
	{
		if (!circleProgressBar.isArrowShow())
		{
			circleProgressBar.stop();
			circleProgressBar.setRingAlpha(255);
			circleProgressBar.setArrowShow(true);
			circleProgressBar.setColorSchemeResources(getColorSchemeResources());
		}

		percent = (dragDistance / targetDistance) - 0.36f; // 0.5f是微调
		percent = percent > 1.0f ? 1.0f : percent;

		circleProgressBar.setAlpha(percent);
		circleProgressBar.setArrowScale(percent);
		circleProgressBar.setStartEndTrim(0, percent * 0.8f);
	}

	@Override
	protected void onAnimate()
	{
		circleProgressBar.setArrowShow(false);
		circleProgressBar.start();
	}

	@Override
	protected View getView(Context context)
	{
		View view = LayoutInflater.from(context).inflate(R.layout.lib_view_recycler_super_swipe_progress, null);
		circleProgressBar = (ViewCircleProgressBar) view.findViewById(R.id.widget_progress_bar);
		return view;
	}

	protected int[] getColorSchemeResources()
	{
		return new int[]{android.R.color.holo_red_light, android.R.color.holo_blue_bright, android.R.color.holo_green_light};
	}
}
