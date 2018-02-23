package com.yline.view.recycler.refresh;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;

import com.yline.view.recycler.refresh.adapter.AbstractRefreshAdapter;
import com.yline.view.recycler.refresh.adapter.DefaultRefreshAdapter;
import com.yline.view.recycler.refresh.helper.FootViewContainer;
import com.yline.view.recycler.refresh.helper.HeadViewContainer;
import com.yline.view.recycler.refresh.helper.RefreshChildHelper;

/**
 * 支持下拉刷新和上拉加载更多
 * 非侵入式，对原来的ListView、RecyclerView没有任何影响,用法和SwipeRefreshLayout类似
 * 支持RecyclerView，ListView，ScrollView，GridView等等
 *
 * @author yline 2017/5/22 -- 10:16
 * @version 1.0.0
 */
@SuppressLint("ClickableViewAccessibility")
class RefreshLayout extends ViewGroup {
    private static final String LOG_TAG = "CustomSwipe";

    // 数据
    private final int mHeadViewHeight;
    private static final int HEAD_VIEW_HEIGHT = 50;// HeadView height (dp)

    private final int mFootViewHeight;
    private static final int FOOT_VIEW_HEIGHT = 50;// FootView height (dp)

    private final float mDefaultTargetDistance;
    private static final int DEFAULT_TARGET_DISTANCE = 64; // 默认刷新控件，偏移距离

    // 控件、帮助类
    private AbstractRefreshAdapter mHeadRefreshAdapter;
    private HeadViewContainer mHeadViewContainer; // 头部

    private AbstractRefreshAdapter mFootLoadAdapter;
    private FootViewContainer mFootViewContainer; // 底部

    private RefreshChildHelper mChildHelper;

    private static final int INVALID_POINTER = -1;
    private static final float DRAG_RATE = .5f;
    private static final int ANIMATE_TO_START_DURATION = 200;

    /* 下拉刷新[有新建,就代表有默认值] */
    private boolean isFootLoading = false; // 是否正在上拉加载
    private float mInitialMotionY;
    private boolean mIsBeingDragged;
    private int mActivePointerId = INVALID_POINTER;
    private boolean mNotify;
    private int pushDistance = 0;

    /* ---------------------------------- 常量 ---------------------------------- */
    private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;

    // 表示滑动的时候，手的移动要大于这个距离才开始移动控件。如果小于这个距离就不触发移动控件
    private final int touchSlop;

	/* ---------------------------------- 引用 ---------------------------------- */

    private final DecelerateInterpolator decelerateInterpolator;

    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        setWillNotDraw(false);
        decelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);

        final TypedArray typedArray = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.enabled});
        setEnabled(typedArray.getBoolean(0, true));
        typedArray.recycle();

        // getScaledTouchSlop是一个距离，表示滑动的时候，手的移动要大于这个距离才开始移动控件。如果小于这个距离就不触发移动控件
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mHeadViewHeight = dp2px(context, HEAD_VIEW_HEIGHT);
        mFootViewHeight = dp2px(context, FOOT_VIEW_HEIGHT);
        mDefaultTargetDistance = dp2px(context, DEFAULT_TARGET_DISTANCE);

        mChildHelper = new RefreshChildHelper();
        ViewCompat.setChildrenDrawingOrderEnabled(this, true);

        // 添加 头布局和底布局
        mHeadViewContainer = HeadViewContainer.attachViewContainer(this);
        mFootViewContainer = FootViewContainer.attachViewContainer(this);

        // 初始化 默认headRefreshAdapter
        mHeadRefreshAdapter = new DefaultRefreshAdapter();
        setRefreshAdapter(mHeadRefreshAdapter);

        // 初始化 footLoadAdapter
        mFootLoadAdapter = new DefaultRefreshAdapter();
        setLoadAdapter(mFootLoadAdapter);
    }

    /**
     * 设置，下拉加载适配器
     *
     * @param refreshAdapter 刷新控件的适配器
     */
    public void setRefreshAdapter(AbstractRefreshAdapter refreshAdapter) {
        this.mHeadRefreshAdapter = refreshAdapter;
        if (null != mHeadRefreshAdapter) {
            View child = mHeadRefreshAdapter.getView(getContext());
            mHeadViewContainer.attachChild(child);
        }
    }

    /**
     * 设置，下拉加载监听事件
     *
     * @param onRefreshListener 头部刷新，回调
     */
    public void setOnRefreshListener(AbstractRefreshAdapter.OnSwipeListener onRefreshListener) {
        if (null != mHeadRefreshAdapter) {
            mHeadRefreshAdapter.setSwipeAnimatingListener(onRefreshListener);
        }
    }

    /**
     * 设置，上拉加载适配器
     *
     * @param loadAdapter 底部上拉，适配器
     */
    public void setLoadAdapter(AbstractRefreshAdapter loadAdapter) {
        this.mFootLoadAdapter = loadAdapter;
        if (null != mFootLoadAdapter) {
            View child = mFootLoadAdapter.getView(getContext());
            mFootViewContainer.attachChild(child);
        }
    }

    /**
     * 设置，上拉加载监听事件
     *
     * @param onLoadListener 底部上拉，回调
     */
    public void setOnLoadListener(AbstractRefreshAdapter.OnSwipeListener onLoadListener) {
        if (null != mFootLoadAdapter) {
            mFootLoadAdapter.setSwipeAnimatingListener(onLoadListener);
        }
    }

    /**
     * Notify the widget that refresh state has changed. Do not call this when
     * refresh is triggered by a swipe gesture.
     *
     * @param refreshing Whether or not the view should show refresh progress.
     */
    public void setRefreshing(boolean refreshing) {
        if (refreshing && !mHeadViewContainer.isRefreshing()) {
            mHeadViewContainer.setRefreshing(true);
            mNotify = false;

            int endTarget = (int) (mDefaultTargetDistance + mHeadViewContainer.getOriginalOffset());
            mHeadViewContainer.setTargetOffsetTopAndBottom(endTarget - mHeadViewContainer.getCurrentTargetOffset());
            if (Build.VERSION.SDK_INT < 11) {
                invalidate();
            }


            mHeadViewContainer.setVisibility(View.VISIBLE);
            mHeadViewContainer.startScaleUpAnimation(mRefreshListener);
        } else {
            setRefreshing(false, false /* notify */);
        }
    }

    private void setRefreshing(boolean refreshing, final boolean notify) {
        if (mHeadViewContainer.isRefreshing() != refreshing) {
            mHeadViewContainer.setRefreshing(refreshing);

            mNotify = notify;
            mChildHelper.checkChild(this, mHeadViewContainer, mFootViewContainer);
            if (refreshing) {
                animateOffsetToCorrectPosition(mHeadViewContainer.getCurrentTargetOffset(), mRefreshListener);
            } else {
                animateOffsetToStartPosition(mHeadViewContainer.getCurrentTargetOffset(), mRefreshListener);
            }
        }
    }

    /**
     * 下拉时，超过距离之后，弹回来的动画监听器
     */
    private HeadViewContainer.OnHeadAnimationCallback mRefreshListener = new HeadViewContainer.OnHeadAnimationCallback() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (mHeadViewContainer.isRefreshing()) {
                if (mNotify) {
                    if (mHeadRefreshAdapter != null) {
                        mHeadRefreshAdapter.animate();
                    }
                }
            } else {
                mHeadViewContainer.setVisibility(View.GONE);
                mHeadViewContainer.setTargetOffsetTopAndBottom(mHeadViewContainer.getOriginalOffset() - mHeadViewContainer.getCurrentTargetOffset());
                if (Build.VERSION.SDK_INT < 11) {
                    invalidate();
                }
            }
            mHeadViewContainer.setCurrentTargetOffset(mHeadViewContainer.getTop());
        }
    };

    private void animateOffsetToCorrectPosition(int from, HeadViewContainer.OnHeadAnimationCallback listener) {
        final int targetFrom = from;
        mHeadViewContainer.startTargetAnimation(new HeadViewContainer.OnApplyAnimationCallback() {
            @Override
            public void onApply(float interpolatedTime) {
                int endTarget = (int) (mDefaultTargetDistance - Math.abs(mHeadViewContainer.getOriginalOffset()));
                int targetTop = (targetFrom + (int) ((endTarget - targetFrom) * interpolatedTime));
                int offset = targetTop - mHeadViewContainer.getTop();
                mHeadViewContainer.setTargetOffsetTopAndBottom(offset);
            }
        }, listener);
    }

    private void animateOffsetToStartPosition(int from, HeadViewContainer.OnHeadAnimationCallback listener) {
        final int startFrom = from;
        mHeadViewContainer.startStartAnimation(new HeadViewContainer.OnApplyAnimationCallback() {
            @Override
            public void onApply(float interpolatedTime) {
                int targetTop = (startFrom + (int) ((mHeadViewContainer.getOriginalOffset() - startFrom) * interpolatedTime));
                int offset = targetTop - mHeadViewContainer.getTop();
                mHeadViewContainer.setTargetOffsetTopAndBottom(offset);
            }
        }, listener);
        resetTargetLayoutDelay(ANIMATE_TO_START_DURATION);
    }

    /**
     * 重置Target位置
     *
     * @param delay
     */
    public void resetTargetLayoutDelay(int delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resetTargetLayout();
            }
        }, delay);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 设置 控件的 改变的高度（动态改变，不再是原始高度）
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));

        boolean isChildExist = mChildHelper.checkChild(this, mHeadViewContainer, mFootViewContainer);
        if (!isChildExist) {
            return;
        }

        int measureWidth = MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY);
        int measureHeight = MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY);
        mChildHelper.measure(measureWidth, measureHeight);

        mHeadViewContainer.measure(measureWidth, MeasureSpec.makeMeasureSpec(mHeadViewHeight, MeasureSpec.EXACTLY));
        mFootViewContainer.measure(measureWidth, MeasureSpec.makeMeasureSpec(mFootViewHeight, MeasureSpec.EXACTLY));

        mHeadViewContainer.initOffset(-mHeadViewContainer.getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // 排除异常情况
        boolean isChildExist = mChildHelper.checkChild(this, mHeadViewContainer, mFootViewContainer);
        if (!isChildExist) {
            return;
        }

        // 获取内容控件到顶部的距离
        int headTargetDistance = mHeadViewContainer.getCurrentTargetOffset() + mHeadViewContainer.getMeasuredHeight();
        if (isHeadFloat()) {
            headTargetDistance = 0;
        }

        int footTargetDistance = pushDistance;
        if (isFootFloat()) {
            footTargetDistance = 0;
        }

        // 获取控件 原始宽高（依据Measure中的值获取）
        mChildHelper.layout(this, headTargetDistance - footTargetDistance);
        mHeadViewContainer.resetLayout(this, mHeadViewContainer.getCurrentTargetOffset());
        mFootViewContainer.resetLayout(this, pushDistance);
    }

    /**
     * 重置Target的位置
     */
    public void resetTargetLayout() {
        mChildHelper.resetLayout(this);
        mHeadViewContainer.resetLayout(this, -mHeadViewContainer.getMeasuredHeight());
        mFootViewContainer.resetLayout(this, 0);
    }

    private boolean isHeadFloat() {
        return (null != mHeadRefreshAdapter && !mHeadRefreshAdapter.isTargetScroll());
    }

    private boolean isFootFloat() {
        return (null != mFootLoadAdapter && !mFootLoadAdapter.isTargetScroll());
    }

    /**
     * 主要判断是否应该拦截子View的事件<br>
     * 如果拦截，则交给自己的OnTouchEvent处理<br>
     * 否者，交给子View处理<br>
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isChildExist = mChildHelper.checkChild(this, mHeadViewContainer, mFootViewContainer);
        if (!isChildExist) {
            return false;
        }

        final int action = MotionEventCompat.getActionMasked(ev);
        boolean isChildScrollToBottom = mChildHelper.isChildScrollToBottom();
        if (!isEnabled() || mHeadViewContainer.isRefreshing() || isFootLoading || (!mChildHelper.isChildScrollToTop() && !isChildScrollToBottom)) {
            // 如果子View可以滑动，不拦截事件，交给子View处理-下拉刷新
            // 或者子View没有滑动到底部不拦截事件-上拉加载更多
            return false;
        }

        // 下拉刷新判断
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mHeadViewContainer.setTargetOffsetTopAndBottom(mHeadViewContainer.getOriginalOffset() - mHeadViewContainer.getTop());
                if (Build.VERSION.SDK_INT < 11) {
                    invalidate();
                }
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mIsBeingDragged = false;
                final float initialMotionY = getMotionEventY(ev, mActivePointerId);
                if (initialMotionY == -1) {
                    return false;
                }
                mInitialMotionY = initialMotionY;// 记录按下的位置

            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    Log.e(LOG_TAG, "Got ACTION_MOVE event but don't have an active pointer id.");
                    return false;
                }

                final float y = getMotionEventY(ev, mActivePointerId);
                if (y == -1) {
                    return false;
                }

                float yDiff = 0;
                if (isChildScrollToBottom) {
                    yDiff = mInitialMotionY - y;// 计算上拉距离
                    if (yDiff > touchSlop && !mIsBeingDragged) {// 判断是否下拉的距离足够
                        mIsBeingDragged = true;// 正在上拉
                    }
                } else {
                    yDiff = y - mInitialMotionY;// 计算下拉距离
                    if (yDiff > touchSlop && !mIsBeingDragged) {// 判断是否下拉的距离足够
                        mIsBeingDragged = true;// 正在下拉
                    }
                }
                break;

            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
            default:
                break;
        }

        return mIsBeingDragged;// 如果正在拖动，则拦截子View的事件
    }

    private float getMotionEventY(MotionEvent ev, int activePointerId) {
        final int index = MotionEventCompat.findPointerIndex(ev, activePointerId);
        if (index < 0) {
            return -1;
        }
        return MotionEventCompat.getY(ev, index);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        boolean isChildScrollToBottom = mChildHelper.isChildScrollToBottom();
        if (!isEnabled() || (!mChildHelper.isChildScrollToTop() && !isChildScrollToBottom)) {
            // 如果子View可以滑动，不拦截事件，交给子View处理
            return false;
        }

        if (isChildScrollToBottom) {// 上拉加载更多
            return handlerFootLoadTouchEvent(ev, action);
        } else {// 下拉刷新
            return handlerHeadRefreshTouchEvent(ev, action);
        }
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return super.onInterceptHoverEvent(event);
    }

    private boolean handlerHeadRefreshTouchEvent(MotionEvent ev, int action) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mIsBeingDragged = false;
                break;

            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
                    return false;
                }

                final float y = MotionEventCompat.getY(ev, pointerIndex);
                final float overScrollTop = (y - mInitialMotionY) * DRAG_RATE;
                if (mIsBeingDragged) {
                    float originalDragPercent = overScrollTop / mDefaultTargetDistance;
                    if (originalDragPercent < 0) {
                        return false;
                    }
                    float dragPercent = Math.min(1f, Math.abs(originalDragPercent));
                    float extraOS = Math.abs(overScrollTop) - mDefaultTargetDistance;
                    float slingshotDist = mDefaultTargetDistance;
                    float tensionSlingshotPercent = Math.max(0,
                            Math.min(extraOS, slingshotDist * 2) / slingshotDist);
                    float tensionPercent = (float) ((tensionSlingshotPercent / 4) - Math
                            .pow((tensionSlingshotPercent / 4), 2)) * 2f;
                    float extraMove = (slingshotDist) * tensionPercent * 2;

                    int targetY = mHeadViewContainer.getOriginalOffset()
                            + (int) ((slingshotDist * dragPercent) + extraMove);
                    if (mHeadViewContainer.getVisibility() != View.VISIBLE) {
                        mHeadViewContainer.setVisibility(View.VISIBLE);
                    }

                    ViewCompat.setScaleX(mHeadViewContainer, 1f);
                    ViewCompat.setScaleY(mHeadViewContainer, 1f);

                    if (null != mHeadRefreshAdapter) {
                        mHeadRefreshAdapter.onCreating(overScrollTop, mDefaultTargetDistance);
                    }
                    mHeadViewContainer.setTargetOffsetTopAndBottom(targetY - mHeadViewContainer.getCurrentTargetOffset());
                    if (Build.VERSION.SDK_INT < 11) {
                        invalidate();
                    }
                }
                break;
            }
            case MotionEventCompat.ACTION_POINTER_DOWN: {
                final int index = MotionEventCompat.getActionIndex(ev);
                mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                break;
            }

            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (mActivePointerId == INVALID_POINTER) {
                    if (action == MotionEvent.ACTION_UP) {
                        Log.e(LOG_TAG, "Got ACTION_UP event but don't have an active pointer id.");
                    }
                    return false;
                }
                final int pointerIndex = MotionEventCompat.findPointerIndex(ev,
                        mActivePointerId);
                final float y = MotionEventCompat.getY(ev, pointerIndex);
                final float overScrollTop = (y - mInitialMotionY) * DRAG_RATE;
                mIsBeingDragged = false;
                if (overScrollTop > mDefaultTargetDistance) {
                    setRefreshing(true, true /* notify */);
                } else {
                    mHeadViewContainer.setRefreshing(false);
                    animateOffsetToStartPosition(mHeadViewContainer.getCurrentTargetOffset(), new HeadViewContainer.OnHeadAnimationCallback() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mHeadViewContainer.startScaleDownAnimation(null);
                        }
                    });
                }
                mActivePointerId = INVALID_POINTER;
                return false;
            }
            default:
                break;
        }

        return true;
    }

    /**
     * 处理上拉加载更多的Touch事件
     *
     * @param ev
     * @param action
     * @return
     */
    private boolean handlerFootLoadTouchEvent(MotionEvent ev, int action) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mIsBeingDragged = false;
                break;
            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = MotionEventCompat.findPointerIndex(ev,
                        mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
                    return false;
                }
                final float y = MotionEventCompat.getY(ev, pointerIndex);
                final float overScrollBottom = (mInitialMotionY - y) * DRAG_RATE;
                if (mIsBeingDragged) {
                    pushDistance = (int) overScrollBottom;
                    updateFooterViewPosition();
                    if (null != mFootLoadAdapter) {
                        mFootLoadAdapter.onCreating(pushDistance, mFootViewHeight);
                    }
                }
                break;
            }
            case MotionEventCompat.ACTION_POINTER_DOWN: {
                final int index = MotionEventCompat.getActionIndex(ev);
                mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                break;
            }
            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (mActivePointerId == INVALID_POINTER) {
                    if (action == MotionEvent.ACTION_UP) {
                        Log.e(LOG_TAG, "Got ACTION_UP event but don't have an active pointer id.");
                    }
                    return false;
                }
                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                final float y = MotionEventCompat.getY(ev, pointerIndex);
                final float overScrollBottom = (mInitialMotionY - y) * DRAG_RATE;// 松手是下拉的距离
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                if (overScrollBottom < mFootViewHeight || mFootLoadAdapter == null) {// 直接取消
                    pushDistance = 0;
                } else {// 下拉到mFooterViewHeight
                    pushDistance = mFootViewHeight;
                }
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    updateFooterViewPosition();
                    if (pushDistance == mFootViewHeight && mFootLoadAdapter != null) {
                        isFootLoading = true;
                        mFootLoadAdapter.animate();
                    }
                } else {
                    animatorFooterToBottom((int) overScrollBottom, pushDistance);
                }
                return false;
            }
            default:
                break;
        }
        return true;
    }

    /**
     * 松手之后，使用动画将Footer从距离start变化到end
     *
     * @param start
     * @param end
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void animatorFooterToBottom(int start, final int end) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);
        valueAnimator.setDuration(150);
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                // update
                pushDistance = (Integer) valueAnimator.getAnimatedValue();
                updateFooterViewPosition();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (end > 0 && mFootLoadAdapter != null) {
                    // start loading more
                    isFootLoading = true;
                    mFootLoadAdapter.animate();
                } else {
                    resetTargetLayout();
                    isFootLoading = false;
                }
            }
        });
        valueAnimator.setInterpolator(decelerateInterpolator);
        valueAnimator.start();
    }

    /**
     * 设置停止加载
     *
     * @param loadMore
     */
    public void setLoadMore(boolean loadMore) {
        if (!loadMore && isFootLoading) {// 停止加载
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                isFootLoading = false;
                pushDistance = 0;
                updateFooterViewPosition();
            } else {
                animatorFooterToBottom(mFootViewHeight, 0);
            }
        }
    }

    /**
     * 修改底部布局的位置-敏感pushDistance
     */
    private void updateFooterViewPosition() {
        mFootViewContainer.setVisibility(View.VISIBLE);
        mFootViewContainer.bringToFront();
        //针对4.4及之前版本的兼容
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            mFootViewContainer.getParent().requestLayout();
        }
        mFootViewContainer.offsetTopAndBottom(-pushDistance);
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if (pointerId == mActivePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
        }
    }

    /**
     * 获得屏幕宽度
     *
     * @param context 上下文
     * @return such as 720 if success
     */
    public int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,
                context.getResources().getDisplayMetrics());
    }

    public static int getDefaultTargetDistance(Context context) {
        return dp2px(context, DEFAULT_TARGET_DISTANCE);
    }
}
