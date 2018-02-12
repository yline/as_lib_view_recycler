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
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
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
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.yline.view.recycler.refresh.adapter.AbstractRefreshAdapter;
import com.yline.view.recycler.refresh.adapter.DefaultRefreshAdapter;
import com.yline.view.recycler.refresh.helper.HeadViewContainer;

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

    private final int mHeadViewHeight;
    private static final int HEAD_VIEW_HEIGHT = 50;// HeadView height (dp)

    private final int mFootViewHeight;
    private static final int FOOT_VIEW_HEIGHT = 50;// FootView height (dp)

    private final float mDefaultTargetDistance;
    private static final int DEFAULT_TARGET_DISTANCE = 64; // 默认刷新控件，偏移距离

    private AbstractRefreshAdapter mHeadRefreshAdapter;
    private HeadViewContainer mHeadViewContainer; // 头部

    private static final int INVALID_POINTER = -1;
    private static final float DRAG_RATE = .5f;
    private static final int SCALE_DOWN_DURATION = 150;
    private static final int ANIMATE_TO_START_DURATION = 200;

    // SuperSwipeRefreshLayout内的目标View，比如RecyclerView,ListView,ScrollView,GridView and etc.
    private View childTarget;

    /* 下拉刷新[有新建,就代表有默认值] */
    private boolean isHeadRefreshing = false; // 是否正在下拉刷新
    private int headViewIndex = -1; // 头部位置
    protected int headOriginalOffset; // 顶部一定的初始距离, 等于 负的头部高度
    private int headCurrentTargetOffset; // 容器，距离顶部的实时偏移量
    private boolean isHeadOriginalOffsetCalculated = false; // 顶部初始化距离是否计算过了

    /* 上拉加载[有新建,就代表有默认值] */
    private AbstractRefreshAdapter footLoadAdapter;
    private RelativeLayout footViewContainer; // 底部

    private boolean isFootLoading = false; // 是否正在上拉加载

    private int footViewIndex = -1; // 底部位置

    private float mInitialMotionY;

    private boolean mIsBeingDragged;

    private int mActivePointerId = INVALID_POINTER;

    private static final int[] LAYOUT_ATTRS = new int[]{android.R.attr.enabled};

    protected int mFrom;

    private boolean mNotify;

    private int pushDistance = 0;

    /* ---------------------------------- 常量 ---------------------------------- */
    private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;

    private final int screenWidth;

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

        final TypedArray a = context.obtainStyledAttributes(attrs, LAYOUT_ATTRS);
        setEnabled(a.getBoolean(0, true));
        a.recycle();

        /**
         * getScaledTouchSlop是一个距离，表示滑动的时候，手的移动要大于这个距离才开始移动控件。如果小于这个距离就不触发移动控件
         */
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        screenWidth = getScreenWidth(context);
        mHeadViewHeight = dp2px(context, HEAD_VIEW_HEIGHT);
        mFootViewHeight = dp2px(context, FOOT_VIEW_HEIGHT);
        mDefaultTargetDistance = dp2px(context, DEFAULT_TARGET_DISTANCE);

        // 添加 头布局和底布局
        mHeadViewContainer = HeadViewContainer.attachViewContainer(this);
        createFooterViewContainer();

        ViewCompat.setChildrenDrawingOrderEnabled(this, true);

        // 初始化 默认headRefreshAdapter
        mHeadRefreshAdapter = new DefaultRefreshAdapter();
        setRefreshAdapter(mHeadRefreshAdapter);

        // 初始化 footLoadAdapter
        footLoadAdapter = new DefaultRefreshAdapter();
        setLoadAdapter(footLoadAdapter);
    }

    /**
     * 添加底部布局
     */
    private void createFooterViewContainer() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, mFootViewHeight);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        footViewContainer = new RelativeLayout(getContext());
        footViewContainer.setVisibility(View.GONE);
        addView(footViewContainer, layoutParams);
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
            if (isHeadRefreshing) {
                if (mNotify) {
                    if (mHeadRefreshAdapter != null) {
                        mHeadRefreshAdapter.animate();
                    }
                }
            } else {
                mHeadViewContainer.setVisibility(View.GONE);
                setTargetOffsetTopAndBottom(headOriginalOffset - headCurrentTargetOffset, true);
            }
            headCurrentTargetOffset = mHeadViewContainer.getTop();
        }
    };

    /**
     * 下拉刷新
     *
     * @param headRefreshAdapter
     */
    public void setRefreshAdapter(AbstractRefreshAdapter headRefreshAdapter) {
        this.mHeadRefreshAdapter = headRefreshAdapter;
        if (null != headRefreshAdapter) {
            View child = headRefreshAdapter.getView(getContext());
            mHeadViewContainer.attachChild(child);
        }
    }

    @Override
    public void addOnLayoutChangeListener(OnLayoutChangeListener listener) {
        super.addOnLayoutChangeListener(listener);
    }

    /**
     * 监听动画
     *
     * @param onRefreshListener
     */
    public void setOnRefreshListener(AbstractRefreshAdapter.OnSwipeListener onRefreshListener) {
        if (null != mHeadRefreshAdapter) {
            mHeadRefreshAdapter.setSwipeAnimatingListener(onRefreshListener);
        }
    }

    /**
     * 上拉加载
     *
     * @param footLoadAdapter
     */
    public void setLoadAdapter(AbstractRefreshAdapter footLoadAdapter) {
        this.footLoadAdapter = footLoadAdapter;

        if (null != footLoadAdapter) {
            View child = footLoadAdapter.getView(getContext());

            if (child == null) {
                return;
            }
            if (footViewContainer == null) {
                return;
            }
            footViewContainer.removeAllViews();
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(screenWidth, mFootViewHeight);
            footViewContainer.addView(child, layoutParams);
        }
    }

    public void setOnLoadListener(AbstractRefreshAdapter.OnSwipeListener onLoadListener) {
        if (null != footLoadAdapter) {
            footLoadAdapter.setSwipeAnimatingListener(onLoadListener);
        }
    }

    /**
     * 孩子节点绘制的顺序
     *
     * @param childCount
     * @param i
     * @return
     */
    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        // 将新添加的View,放到最后绘制
        if (headViewIndex < 0 && footViewIndex < 0) {
            return i;
        }
        if (i == childCount - 2) {
            return headViewIndex;
        }
        if (i == childCount - 1) {
            return footViewIndex;
        }
        int bigIndex = footViewIndex > headViewIndex ? footViewIndex : headViewIndex;
        int smallIndex = footViewIndex < headViewIndex ? footViewIndex : headViewIndex;
        if (i >= smallIndex && i < bigIndex - 1) {
            return i + 1;
        }
        if (i >= bigIndex || (i == bigIndex - 1)) {
            return i + 2;
        }
        return i;
    }

    /**
     * Notify the widget that refresh state has changed. Do not call this when
     * refresh is triggered by a swipe gesture.
     *
     * @param refreshing Whether or not the view should show refresh progress.
     */
    public void setRefreshing(boolean refreshing) {
        if (refreshing && isHeadRefreshing != refreshing) {
            // scale and show
            isHeadRefreshing = refreshing;
            int endTarget = (int) (mDefaultTargetDistance + headOriginalOffset);
            setTargetOffsetTopAndBottom(endTarget - headCurrentTargetOffset, true /* requires update */);
            mNotify = false;

            mHeadViewContainer.setVisibility(View.VISIBLE);
            mHeadViewContainer.startScaleUpAnimation(mRefreshListener);
        } else {
            setRefreshing(refreshing, false /* notify */);
        }
    }

    private void setRefreshing(boolean refreshing, final boolean notify) {
        if (isHeadRefreshing != refreshing) {
            mNotify = notify;
            ensureTarget();
            isHeadRefreshing = refreshing;
            if (isHeadRefreshing) {
                animateOffsetToCorrectPosition(headCurrentTargetOffset, mRefreshListener);
            } else {
                animateOffsetToStartPosition(headCurrentTargetOffset, mRefreshListener);
            }
        }
    }

    public boolean isRefreshing() {
        return isHeadRefreshing;
    }

    /**
     * 确保childTarget不为空<br>
     * childTarget一般是可滑动的ScrollView,ListView,RecyclerView等
     */
    private void ensureTarget() {
        if (null == childTarget) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (!child.equals(mHeadViewContainer) && !child.equals(footViewContainer)) {
                    childTarget = child;
                    break;
                }
            }
        }
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 设置 控件的 改变的高度（动态改变，不再是原始高度）
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));

        ensureTarget();
        if (null == childTarget) {
            return;
        }

        int measureWidth = MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY);
        int measureHeight = MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY);

        childTarget.measure(measureWidth, measureHeight);
        mHeadViewContainer.measure(MeasureSpec.makeMeasureSpec(screenWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mHeadViewHeight, MeasureSpec.EXACTLY));
        footViewContainer.measure(MeasureSpec.makeMeasureSpec(screenWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mFootViewHeight, MeasureSpec.EXACTLY));

        if (!isHeadOriginalOffsetCalculated) {
            isHeadOriginalOffsetCalculated = true;
            headOriginalOffset = -mHeadViewContainer.getMeasuredHeight();
            headCurrentTargetOffset = headOriginalOffset;
        }

        headViewIndex = -1;
        for (int index = 0; index < getChildCount(); index++) {
            if (getChildAt(index) == mHeadViewContainer) {
                headViewIndex = index;
                break;
            }
        }

        footViewIndex = -1;
        for (int index = 0; index < getChildCount(); index++) {
            if (getChildAt(index) == footViewContainer) {
                footViewIndex = index;
                break;
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // 排除异常情况
        if (getChildCount() == 0) {
            return;
        }
        ensureTarget();
        if (childTarget == null) {
            return;
        }

        // 获取内容控件到顶部的距离
        int headTargetDistance = headCurrentTargetOffset + mHeadViewContainer.getMeasuredHeight();
        if (isHeadFloat()) {
            headTargetDistance = 0;
        }

        int footTargetDistance = pushDistance;
        if (isFootFloat()) {
            footTargetDistance = 0;
        }

        // 获取控件 原始宽高（依据Measure中的值获取）
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        final int originalHeight = getMeasuredHeight();

        final View child = childTarget;
        final int childLeft = getPaddingLeft();
        final int childTop = getPaddingTop() + headTargetDistance - footTargetDistance; // 根据偏移量headTargetDistance更新
        final int childWidth = width - getPaddingLeft() - getPaddingRight();
        final int childHeight = originalHeight - getPaddingTop() - getPaddingBottom();

        // 更新目标View的位置
        child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);

        // 更新  0
        int headViewWidth = mHeadViewContainer.getMeasuredWidth();
        int headViewHeight = mHeadViewContainer.getMeasuredHeight();
        mHeadViewContainer.layout(((width - headViewWidth) / 2), headCurrentTargetOffset,
                ((width + headViewWidth) / 2), headCurrentTargetOffset + headViewHeight);

        // 更新 底部布局位置
        int footViewWidth = footViewContainer.getMeasuredWidth();
        int footViewHeight = footViewContainer.getMeasuredHeight();
        footViewContainer.layout(((width - footViewWidth) / 2), height - pushDistance,
                ((width + footViewWidth) / 2), height + footViewHeight - pushDistance);
    }

    private boolean isHeadFloat() {
        return (null != mHeadRefreshAdapter && !mHeadRefreshAdapter.isTargetScroll());
    }

    private boolean isFootFloat() {
        return (null != footLoadAdapter && !footLoadAdapter.isTargetScroll());
    }

    /**
     * 判断目标View是否滑动到顶部-还能否继续滑动
     *
     * @return
     */
    private boolean isChildScrollToTop() {
        if (Build.VERSION.SDK_INT < 14) {
            if (childTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) childTarget;
                return !(absListView.getChildCount() > 0 && (absListView
                        .getFirstVisiblePosition() > 0 || absListView
                        .getChildAt(0).getTop() < absListView.getPaddingTop()));
            } else {
                return !(childTarget.getScrollY() > 0);
            }
        } else {
            return !ViewCompat.canScrollVertically(childTarget, -1);
        }
    }

    /**
     * 是否滑动到底部
     *
     * @return
     */
    private boolean isChildScrollToBottom() {
        if (isChildScrollToTop()) {
            return false;
        }

        if (childTarget instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) childTarget;
            /*LayoutManager layoutManager = recyclerView.getLayoutManager();*/

            boolean canScrollVertically = recyclerView.canScrollVertically(1); // 判断是否能向上滑动
            return !canScrollVertically;
            /*
            int count = recyclerView.getAdapter().getItemCount();
			if (layoutManager instanceof LinearLayoutManager && count > 0)
			{
				LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;

				if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == count - 1)
				{
					return true;
				}
			}
			else if (layoutManager instanceof StaggeredGridLayoutManager)
			{
				StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
				int[] lastItems = new int[2];
				staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(lastItems);
				int lastItem = Math.max(lastItems[0], lastItems[1]);
				if (lastItem == count - 1)
				{
					return true;
				}
			}
			return false;*/
        } else if (childTarget instanceof AbsListView) {
            final AbsListView absListView = (AbsListView) childTarget;
            int count = absListView.getAdapter().getCount();
            int firstPosition = absListView.getFirstVisiblePosition();
            if (firstPosition == 0
                    && absListView.getChildAt(0).getTop() >= absListView
                    .getPaddingTop()) {
                return false;
            }
            int lastPos = absListView.getLastVisiblePosition();
            if (lastPos > 0 && count > 0 && lastPos == count - 1) {
                return true;
            }
            return false;
        } else if (childTarget instanceof ScrollView) {
            ScrollView scrollView = (ScrollView) childTarget;
            View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
            if (view != null) {
                int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));
                if (diff == 0) {
                    return true;
                }
            }
        } else if (childTarget instanceof NestedScrollView) {
            NestedScrollView nestedScrollView = (NestedScrollView) childTarget;
            View view = (View) nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);
            if (view != null) {
                int diff = (view.getBottom() - (nestedScrollView.getHeight() + nestedScrollView.getScrollY()));
                if (diff == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 主要判断是否应该拦截子View的事件<br>
     * 如果拦截，则交给自己的OnTouchEvent处理<br>
     * 否者，交给子View处理<br>
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ensureTarget();

        final int action = MotionEventCompat.getActionMasked(ev);
        boolean isChildScrollToBottom = isChildScrollToBottom();
        if (!isEnabled() || isHeadRefreshing || isFootLoading || (!isChildScrollToTop() && !isChildScrollToBottom)) {
            // 如果子View可以滑动，不拦截事件，交给子View处理-下拉刷新
            // 或者子View没有滑动到底部不拦截事件-上拉加载更多
            return false;
        }

        // 下拉刷新判断
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                setTargetOffsetTopAndBottom(headOriginalOffset - mHeadViewContainer.getTop(), true);// 恢复HeaderView的初始位置
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
        final int index = MotionEventCompat.findPointerIndex(ev,
                activePointerId);
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

        boolean isChildScrollToBottom = isChildScrollToBottom();
        if (!isEnabled() || (!isChildScrollToTop() && !isChildScrollToBottom)) {
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

                    int targetY = headOriginalOffset
                            + (int) ((slingshotDist * dragPercent) + extraMove);
                    if (mHeadViewContainer.getVisibility() != View.VISIBLE) {
                        mHeadViewContainer.setVisibility(View.VISIBLE);
                    }

                    ViewCompat.setScaleX(mHeadViewContainer, 1f);
                    ViewCompat.setScaleY(mHeadViewContainer, 1f);

                    if (null != mHeadRefreshAdapter) {
                        mHeadRefreshAdapter.onCreating(overScrollTop, mDefaultTargetDistance);
                    }
                    setTargetOffsetTopAndBottom(targetY - headCurrentTargetOffset, true);
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
                    isHeadRefreshing = false;
                    HeadViewContainer.OnHeadAnimationCallback listener = new HeadViewContainer.OnHeadAnimationCallback() {

                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mHeadViewContainer.startScaleDownAnimation(null);
                        }
                    };
                    animateOffsetToStartPosition(headCurrentTargetOffset, listener);
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
                    if (null != footLoadAdapter) {
                        footLoadAdapter.onCreating(pushDistance, mFootViewHeight);
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
                if (overScrollBottom < mFootViewHeight || footLoadAdapter == null) {// 直接取消
                    pushDistance = 0;
                } else {// 下拉到mFooterViewHeight
                    pushDistance = mFootViewHeight;
                }
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    updateFooterViewPosition();
                    if (pushDistance == mFootViewHeight && footLoadAdapter != null) {
                        isFootLoading = true;
                        footLoadAdapter.animate();
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
                if (end > 0 && footLoadAdapter != null) {
                    // start loading more
                    isFootLoading = true;
                    footLoadAdapter.animate();
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

    private void animateOffsetToCorrectPosition(int from, HeadViewContainer.OnHeadAnimationCallback listener) {
        mFrom = from;
        mHeadViewContainer.startTargetAnimation(new HeadViewContainer.OnApplyAnimationCallback() {
            @Override
            public void onApply(float interpolatedTime) {
                int targetTop = 0;
                int endTarget = 0;
                endTarget = (int) (mDefaultTargetDistance - Math.abs(headOriginalOffset));
                targetTop = (mFrom + (int) ((endTarget - mFrom) * interpolatedTime));
                int offset = targetTop - mHeadViewContainer.getTop();
                setTargetOffsetTopAndBottom(offset, false /* requires update */);
            }
        }, listener);
    }

    private void animateOffsetToStartPosition(int from, HeadViewContainer.OnHeadAnimationCallback listener) {
        mFrom = from;
        mHeadViewContainer.startStartAnimation(new HeadViewContainer.OnApplyAnimationCallback() {
            @Override
            public void onApply(float interpolatedTime) {
                int targetTop = (mFrom + (int) ((headOriginalOffset - mFrom) * interpolatedTime));
                int offset = targetTop - mHeadViewContainer.getTop();
                setTargetOffsetTopAndBottom(offset, false /* requires update */);
            }
        }, listener);
        resetTargetLayoutDelay(ANIMATE_TO_START_DURATION);
    }

    private void setTargetOffsetTopAndBottom(int offset, boolean requiresUpdate) {
        mHeadViewContainer.bringToFront();
        mHeadViewContainer.offsetTopAndBottom(offset);
        headCurrentTargetOffset = mHeadViewContainer.getTop();
        if (requiresUpdate && Build.VERSION.SDK_INT < 11) {
            invalidate();
        }
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

    /**
     * 重置Target的位置
     */
    public void resetTargetLayout() {
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        final View child = childTarget;
        final int childLeft = getPaddingLeft();
        final int childTop = getPaddingTop();
        final int childWidth = child.getWidth() - getPaddingLeft() - getPaddingRight();
        final int childHeight = child.getHeight() - getPaddingTop() - getPaddingBottom();
        child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);

        int headViewWidth = mHeadViewContainer.getMeasuredWidth();
        int headViewHeight = mHeadViewContainer.getMeasuredHeight();
        mHeadViewContainer.layout((width / 2 - headViewWidth / 2), -headViewHeight,
                (width / 2 + headViewWidth / 2), 0);// 更新头布局的位置
        int footViewWidth = footViewContainer.getMeasuredWidth();
        int footViewHeight = footViewContainer.getMeasuredHeight();
        footViewContainer.layout((width / 2 - footViewWidth / 2), height,
                (width / 2 + footViewWidth / 2), height + footViewHeight);
    }

    /**
     * 修改底部布局的位置-敏感pushDistance
     */
    private void updateFooterViewPosition() {
        footViewContainer.setVisibility(View.VISIBLE);
        footViewContainer.bringToFront();
        //针对4.4及之前版本的兼容
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            footViewContainer.getParent().requestLayout();
        }
        footViewContainer.offsetTopAndBottom(-pushDistance);
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
