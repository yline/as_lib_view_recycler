package com.yline.view.recycler.refresh.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.yline.view.recycler.R;

/**
 * 刷新，上拉刷新，容器
 *
 * @author yline 2018/2/12 -- 10:55
 * @version 1.0.0
 */
public class FootViewContainer extends RelativeLayout{
    public FootViewContainer(Context context) {
        super(context);
        initView();
    }

    public FootViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public FootViewContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        LayoutInflater.from(getContext()).inflate(R.layout.view_recycler_foot_refresh_container, this, true);
    }
}
