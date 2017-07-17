package com.yline.view.recycler.callback;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * ViewHolder对应的接口
 *
 * @author yline 2017/5/9 -- 15:47
 * @version 1.0.0
 */
public interface IViewHolderCallback<T>
{
	/**
	 * 获取viewId 对应的资源
	 *
	 * @param viewId
	 * @param <T>
	 * @return
	 */
	<T extends View> T get(int viewId);

	/**
	 * 获取当前ViewHolder的最底部View
	 *
	 * @return
	 */
	T getItemView();

	/**
	 * 获取文本内容
	 *
	 * @param viewId
	 * @return
	 */
	String getText(int viewId);

	/**
	 * 给控件设置监听事件
	 *
	 * @param viewId
	 * @param listener
	 */
	void setOnClickListener(int viewId, View.OnClickListener listener);

	/**
	 * 给TextView设置内容
	 *
	 * @param viewId
	 * @param content
	 * @return
	 */
	TextView setText(int viewId, String content);

	/**
	 * 给ImageView设置背景
	 *
	 * @param viewId
	 * @param resId
	 * @return
	 */
	ImageView setImageBackgroundResource(int viewId, int resId);

	/**
	 * 给ImageView 设置 图标
	 *
	 * @param viewId
	 * @param resId
	 * @return
	 */
	ImageView setImageResource(int viewId, int resId);

	/**
	 * 给 ProgressBar 设置进度
	 *
	 * @param viewId   view 的 id
	 * @param progress 进度
	 * @return
	 */
	ProgressBar setProgress(int viewId, int progress);
}
