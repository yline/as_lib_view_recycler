package com.yline.view.recycler.refresh.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * SuperSwipeRefreshLayout 添加 刷新布局 适配器
 * 1，下拉刷新
 * 2，上拉加载
 *
 * @author yline 2017/5/23 -- 10:36
 * @version 1.0.0
 */
public abstract class AbstractRefreshAdapter {
    /**
     * 手势操作时，一直调用
     *
     * @param dragDistance   实时改变的内容
     * @param targetDistance 目标内容
     */
    public abstract void onCreating(float dragDistance, float targetDistance);

    /**
     * 开始动画时,被调用一次
     */
    protected void onAnimate() {
    }

    /**
     * 下拉刷新时，展示的View
     *
     * @param context 上下文
     * @return 展示的View对象
     */
    @NonNull
    public abstract View getView(Context context);

    /**
     * 子View是否跟随,手指的滑动,而移动
     * 【目前只有头布局实现了】
     *
     * @return true(RecyclerView跟随滚动而移动)，false(加载布局，悬浮在RecyclerView上方)
     */
    public boolean isTargetScroll() {
        return false;
    }

    /**
     * 设置，背景布局颜色
     * 【目前只有头布局实现了】
     *
     * @return
     */
    public int getBackgroundResource() {
        return android.R.color.transparent;
    }

    /* ---------------------------------------- 被SuperSwipeRefreshLayout调用;一般不重写 ---------------------------------------------------- */
    private OnSwipeListener mOnRefreshListener;

    public void setSwipeAnimatingListener(OnSwipeListener refreshListener) {
        this.mOnRefreshListener = refreshListener;
    }

    public void animate() {
        onAnimate();
        if (null != mOnRefreshListener) {
            mOnRefreshListener.onAnimate();
        }
    }

    /**
     * 给用户使用
     */
    public interface OnSwipeListener {
        /**
         * 动画中
         */
        void onAnimate();
    }
}
