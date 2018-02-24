package com.yline.view.recycler.refresh.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

import com.yline.view.recycler.R;
import com.yline.view.recycler.refresh.SuperSwipeRefreshLayout;

/**
 * 刷新，下拉刷新，容器
 *
 * @author yline 2018/2/12 -- 10:48
 * @version 1.0.0
 */
public class HeadViewContainer extends RelativeLayout {
    private static final int SCALE_DOWN_DURATION = 150;
    private static final int ANIMATE_TO_TRIGGER_DURATION = 200;
    private static final int ANIMATE_TO_START_DURATION = 200;
    private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;

    private final int mDefaultTargetDistance;
    private static final int DEFAULT_TARGET_DISTANCE = 64; // 默认偏移距离

    private OnHeadAnimationCallback mAnimationListener;
    private RelativeLayout mContainer;

    private boolean mIsRefreshing;
    private boolean mIsOffsetInit; // 顶部初始化距离是否计算过了
    private int mOriginalOffset; // 顶部的初始距离，等于，负的头部高度
    private int mCurrentTargetOffset; // 距离顶部的实时偏移量

    private Animation mScaleUpAnimation;
    private Animation mScaleDownAnimation;

    private Animation mMoveTargetAnimation; // 滚动到指定位置
    private Animation mMoveDownAnimation; // 滚动到开始位置

    public static HeadViewContainer attachViewContainer(@NonNull ViewGroup viewGroup) {
        HeadViewContainer viewContainer = new HeadViewContainer(viewGroup.getContext());
        viewGroup.addView(viewContainer);
        return viewContainer;
    }

    public HeadViewContainer(Context context) {
        super(context);

        mDefaultTargetDistance = SuperSwipeRefreshLayout.dp2px(context, DEFAULT_TARGET_DISTANCE);
        initView();
    }

    public HeadViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);

        mDefaultTargetDistance = SuperSwipeRefreshLayout.dp2px(context, DEFAULT_TARGET_DISTANCE);
        initView();
    }

    public HeadViewContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mDefaultTargetDistance = SuperSwipeRefreshLayout.dp2px(context, DEFAULT_TARGET_DISTANCE);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_recycler_head_refresh_container, this, true);
        mContainer = findViewById(R.id.view_recycler_head_refresh_container);

        mIsOffsetInit = false;
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
     * 重新设定布局
     *
     * @param refreshLayout 父布局
     * @param topOffset     距离顶部距离
     */
    public void resetLayout(@NonNull ViewGroup refreshLayout, int topOffset) {
        int left = (refreshLayout.getWidth() - getMeasuredWidth()) / 2;
        int right = (refreshLayout.getWidth() + getMeasuredWidth()) / 2;
        layout(left, topOffset, right, topOffset + getMeasuredHeight());
    }

    /**
     * 非用户下拉时，实现放大动画背景，再加载动画效果
     *
     * @param listener 放大动画的回调
     */
    public void scaleUpAnimation(HeadViewContainer.OnHeadAnimationCallback listener) {
        // 动画
        if (null == mScaleUpAnimation) {
            mScaleUpAnimation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    super.applyTransformation(interpolatedTime, t);

                    setScaleX(interpolatedTime);
                    setScaleY(interpolatedTime);
                }
            };
            int duration = getResources().getInteger(android.R.integer.config_mediumAnimTime);
            mScaleUpAnimation.setDuration(duration);
        }

        // 初始偏移量、设置成可见
        int offset = mDefaultTargetDistance + mOriginalOffset - mCurrentTargetOffset;
        setTargetOffsetTopAndBottom(offset);

        setVisibility(VISIBLE);

        // 设置动画
        attachAnimation(mScaleUpAnimation, listener);
    }

    public void scaleDownAnimation(HeadViewContainer.OnHeadAnimationCallback listener) {
        if (null == mScaleDownAnimation) {
            mScaleDownAnimation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    super.applyTransformation(interpolatedTime, t);

                    setScaleX(1 - interpolatedTime);
                    setScaleY(1 - interpolatedTime);
                }
            };
            mScaleDownAnimation.setDuration(SCALE_DOWN_DURATION);
        }
        attachAnimation(mScaleDownAnimation, listener);
    }

    /**
     * 用户手指滑动，移动到指定位置，开始刷新动画
     *
     * @param listener 移动到指定位置的动画回调
     */
    public void moveTargetAnimation(HeadViewContainer.OnHeadAnimationCallback listener) {
        if (null == mMoveTargetAnimation) {
            mMoveTargetAnimation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    super.applyTransformation(interpolatedTime, t);

                    int endTarget = mDefaultTargetDistance - Math.abs(mOriginalOffset);
                    int offset = mCurrentTargetOffset + (int) ((endTarget - mCurrentTargetOffset) * interpolatedTime) - getTop();
                    setTargetOffsetTopAndBottom(offset);
                }
            };
        }
        mMoveTargetAnimation.reset();
        mMoveTargetAnimation.setDuration(ANIMATE_TO_TRIGGER_DURATION);
        mMoveTargetAnimation.setInterpolator(new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR));

        attachAnimation(mMoveTargetAnimation, listener);
    }

    /**
     * 快速移动到消失位置，消失后，停止刷新动画
     *
     * @param listener 指定位置移动消失的动画回调
     */
    public void moveDownAnimation(HeadViewContainer.OnHeadAnimationCallback listener) {
        if (null == mMoveDownAnimation) {
            mMoveDownAnimation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    super.applyTransformation(interpolatedTime, t);

                    int offset = mCurrentTargetOffset + (int) ((mOriginalOffset - mCurrentTargetOffset) * interpolatedTime) - getTop();
                    setTargetOffsetTopAndBottom(offset);
                }
            };
        }
        mMoveDownAnimation.reset();
        mMoveDownAnimation.setDuration(ANIMATE_TO_START_DURATION);
        mMoveDownAnimation.setInterpolator(new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR));

        attachAnimation(mMoveDownAnimation, listener);
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

    /**
     * 设置上下偏移量
     *
     * @param offset 偏移量
     */
    public void setTargetOffsetTopAndBottom(int offset) {
        bringToFront();
        offsetTopAndBottom(offset);
        setCurrentTargetOffset(getTop());
    }

    public void initOffset(int originalOffset) {
        if (!mIsOffsetInit) {
            this.mIsOffsetInit = true;
            this.mOriginalOffset = originalOffset;
            this.mCurrentTargetOffset = originalOffset;
        }
    }

    public int getOriginalOffset() {
        return mOriginalOffset;
    }

    public int getCurrentTargetOffset() {
        return mCurrentTargetOffset;
    }

    public void setCurrentTargetOffset(int targetOffset) {
        this.mCurrentTargetOffset = targetOffset;
    }

    public void setRefreshing(boolean isRefreshing) {
        this.mIsRefreshing = isRefreshing;
    }

    public boolean isRefreshing() {
        return mIsRefreshing;
    }

    public int getDefaultTargetDistance() {
        return mDefaultTargetDistance;
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
















