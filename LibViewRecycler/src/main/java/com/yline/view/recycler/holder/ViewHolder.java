package com.yline.view.recycler.holder;

import android.app.Activity;
import android.support.v4.util.SparseArrayCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yline.view.recycler.callback.IViewHolderCallback;

/**
 * 资源文件 帮助类 ViewHolder
 *
 * @author yline 2017/3/19 -- 3:03
 * @version 1.0.0
 */
public class ViewHolder implements IViewHolderCallback<View>
{
	private SparseArrayCompat<View> sArray;

	private View sView;

	public ViewHolder(View view)
	{
		this.sView = view;
		sArray = new SparseArrayCompat<>();
	}

	public ViewHolder(Activity activity)
	{
		this.sView = activity.getWindow().getDecorView();
		this.sArray = new SparseArrayCompat<>();
	}

	@Override
	public <T extends View> T get(int viewId)
	{
		if (sArray.get(viewId) == null)
		{
			View view = sView.findViewById(viewId);
			sArray.put(viewId, view);
		}
		return (T) sArray.get(viewId);
	}

	@Override
	public View getItemView()
	{
		return this.sView;
	}

	@Override
	public String getText(int viewId)
	{
		TextView textView = this.get(viewId);
		return textView.getText().toString();
	}

	@Override
	public TextView setText(int viewId, String content)
	{
		TextView textView = this.get(viewId);
		textView.setText(content);
		return textView;
	}

	@Override
	public ImageView setImageBackgroundResource(int viewId, int resId)
	{
		ImageView imageView = this.get(viewId);
		imageView.setBackgroundResource(resId);
		return imageView;
	}

	@Override
	public ImageView setImageResource(int viewId, int resId)
	{
		ImageView imageView = this.get(viewId);
		imageView.setImageResource(resId);
		return imageView;
	}

	@Override
	public void setOnClickListener(int viewId, View.OnClickListener listener)
	{
		this.get(viewId).setOnClickListener(listener);
	}

	@Override
	public ProgressBar setProgress(int viewId, int progress)
	{
		ProgressBar progressBar = this.get(viewId);
		progressBar.setProgress(progress);
		return progressBar;
	}
}
