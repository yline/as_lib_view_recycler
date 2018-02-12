package com.yline.view.recycler.refresh.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

import com.yline.view.recycler.R;

/**
 * 刷新，下拉刷新，容器
 *
 * @author yline 2018/2/12 -- 10:48
 * @version 1.0.0
 */
public class HeadViewContainer extends RelativeLayout {
    private static final int SCALE_DOWN_DURATION = 150;

    private OnHeadAnimationCallback mAnimationListener;
    private RelativeLayout mContainer;

    private Animation scaleUpAnimation;
    private Animation scaleDownAnimation;

    public static HeadViewContainer attachViewContainer(@NonNull ViewGroup viewGroup) {
        HeadViewContainer viewContainer = new HeadViewContainer(viewGroup.getContext());
        viewGroup.addView(viewContainer);
        return viewContainer;
    }

    public HeadViewContainer(Context context) {
        super(context);
        initView();
    }

    public HeadViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public HeadViewContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_recycler_head_refresh_container, this, true);
        mContainer = findViewById(R.id.view_recycler_head_refresh_container);
    }

    @Override
    protected void onAnimationStart() {
        super.onAnimationStart();
        if (null != mAnimationListener) {
            mAnimationListener.onAnimationStart(getAnimation());
        }
    }

    @Override
    protected void onAnimationEnd() {
        super.onAnimationEnd();
        if (null != mAnimationListener) {
            mAnimationListener.onAnimationEnd(getAnimation());
        }
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

    /**
     * 开启动画
     *
     * @param listener 回调
     */
    public void startScaleUpAnimation(HeadViewContainer.OnHeadAnimationCallback listener) {
        Animation scaleAnimation = getScaleUpAnimation();
        attachAnimation(scaleAnimation, listener);
    }

    public void startScaleDownAnimation(HeadViewContainer.OnHeadAnimationCallback listener) {
        Animation scaleAnimation = getScaleDownAnimation();
        attachAnimation(scaleAnimation, listener);
    }

    /**
     * 给 头部容器，设置动画和回调
     *
     * @param animation 动画
     * @param listener  回调
     */
    public void attachAnimation(Animation animation, OnHeadAnimationCallback listener) {
        this.mAnimationListener = listener;
        clearAnimation();
        startAnimation(animation);
    }

    private Animation getScaleUpAnimation() {
        if (null == scaleUpAnimation) {
            scaleUpAnimation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    super.applyTransformation(interpolatedTime, t);

                    setScaleX(interpolatedTime);
                    setScaleY(interpolatedTime);
                }
            };
            int duration = getResources().getInteger(android.R.integer.config_mediumAnimTime);
            scaleUpAnimation.setDuration(duration);
        }
        return scaleUpAnimation;
    }

    private Animation getScaleDownAnimation() {
        if (null == scaleDownAnimation) {
            scaleDownAnimation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    super.applyTransformation(interpolatedTime, t);

                    setScaleX(1 - interpolatedTime);
                    setScaleY(1 - interpolatedTime);
                }
            };
            scaleDownAnimation.setDuration(SCALE_DOWN_DURATION);
        }
        return scaleDownAnimation;
    }

    public interface OnHeadAnimationCallback {
        /**
         * 动画开始
         *
         * @param animation 动画
         */
        void onAnimationStart(Animation animation);

        /**
         * 动画结束
         *
         * @param animation 动画
         */
        void onAnimationEnd(Animation animation);
    }
}
















