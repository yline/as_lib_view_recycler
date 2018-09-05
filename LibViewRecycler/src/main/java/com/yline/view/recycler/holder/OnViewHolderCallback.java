package com.yline.view.recycler.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * ViewHolder 支持的操作，仅仅限于 google api
 *
 * @author yline 2018/9/5 -- 11:27
 */
public interface OnViewHolderCallback {
	/**
	 * @param viewId id
	 * @return View控件的对象
	 */
	<T extends View> T get(int viewId);
	
	/**
	 * @return 所维持的控件本身
	 */
	View getItemView();
	
	/**
	 * @param viewId id
	 * @return 文本内容
	 */
	String getText(int viewId);
	
	/**
	 * @param viewId   id
	 * @param listener 监听器
	 */
	void setOnClickListener(int viewId, View.OnClickListener listener);
	
	/**
	 * @param viewId  id
	 * @param content 文本内容
	 * @return 控件本身
	 */
	TextView setText(int viewId, String content);
	
	/**
	 * @param viewId id
	 * @param resId  资源id
	 * @return 控件本身
	 */
	ImageView setImageBackgroundResource(int viewId, int resId);
	
	/**
	 * @param viewId id
	 * @param resId  资源id
	 * @return 控件本身
	 */
	ImageView setImageResource(int viewId, int resId);
	
	/**
	 * @param viewId   id
	 * @param progress 进度
	 * @return 控件本身
	 */
	ProgressBar setProgress(int viewId, int progress);
}