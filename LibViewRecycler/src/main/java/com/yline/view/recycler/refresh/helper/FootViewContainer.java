package com.yline.view.recycler.refresh.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.yline.view.recycler.R;

/**
 * 刷新，上拉刷新，容器
 *
 * @author yline 2018/2/12 -- 10:55
 * @version 1.0.0
 */
public class FootViewContainer extends RelativeLayout {
    private RelativeLayout mContainer;

    public static FootViewContainer attachViewContainer(@NonNull ViewGroup viewGroup) {
        FootViewContainer viewContainer = new FootViewContainer(viewGroup.getContext());
        viewGroup.addView(viewContainer);
        return viewContainer;
    }

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

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_recycler_foot_refresh_container, this, true);
        mContainer = findViewById(R.id.view_recycler_foot_refresh_container);
    }

    /**
     * 填充子控件
     *
     * @param childView 子控件
     */
    public void attachChild(@NonNull View childView) {
        mContainer.removeAllViews();
        mContainer.addView(childView);
    }

    public void resetLayout(@NonNull ViewGroup refreshLayout, int bottomOffset) {
        int left = (refreshLayout.getMeasuredWidth() - getMeasuredWidth()) / 2;
        int top = refreshLayout.getHeight() - bottomOffset;
        int right = (refreshLayout.getMeasuredWidth() + getMeasuredWidth()) / 2;
        layout(left, top, right, top + getMeasuredHeight());
    }
}












