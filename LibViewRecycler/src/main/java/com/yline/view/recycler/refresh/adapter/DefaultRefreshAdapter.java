package com.yline.view.recycler.refresh.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.yline.view.recycler.R;
import com.yline.view.recycler.refresh.view.CircleProgressBar;

/**
 * SuperSwipeRefreshLayout 默认的Adapter;
 *
 * @author yline 2017/3/17 -- 18:12
 * @version 1.0.0
 */
public class DefaultRefreshAdapter extends AbstractRefreshAdapter {
    private CircleProgressBar mCircleProgressBar;

    @Override
    public void onCreating(float dragDistance, float targetDistance) {
        if (null != mCircleProgressBar) {
            if (!mCircleProgressBar.isArrowShow()) {
                mCircleProgressBar.stop();
                mCircleProgressBar.setRingAlpha(255);
                mCircleProgressBar.setArrowShow(true);
                mCircleProgressBar.setColorSchemeResources(getColorSchemeResources());
            }

            float percent = (dragDistance / targetDistance) - 0.36f; // 0.36f是微调
            percent = percent > 1.0f ? 1.0f : percent;

            mCircleProgressBar.setAlpha(percent);
            mCircleProgressBar.setArrowScale(percent);
            mCircleProgressBar.setStartEndTrim(0, percent * 0.8f);
        }
    }

    @Override
    protected void onAnimate() {
        if (null != mCircleProgressBar) {
            mCircleProgressBar.setArrowShow(false);
            mCircleProgressBar.start();
        }
    }

    @NonNull
    @Override
    public View getView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_simple_refresh_progress, null);
        mCircleProgressBar = view.findViewById(R.id.widget_progress_bar);
        return view;
    }

    private int[] getColorSchemeResources() {
        return new int[]{android.R.color.holo_red_light, android.R.color.holo_blue_bright, android.R.color.holo_green_light};
    }
}
