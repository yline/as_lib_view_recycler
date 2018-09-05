package com.yline.view.recycler.refresh.helper;

import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ScrollView;

/**
 * 管理子类
 *
 * @author yline 2018/2/13 -- 10:19
 * @version 1.0.0
 */
public class RefreshChildHelper {
    private View mChildTarget; // 刷新控件的主内容

    public int checkChildIndex(@NonNull ViewGroup refreshLayout, @NonNull View childView) {
        for (int i = 0; i < refreshLayout.getChildCount(); i++) {
            if (refreshLayout.getChildAt(i) == childView) {
                return i;
            }
        }
        return -1;
    }

    public boolean checkChild(@NonNull ViewGroup refreshLayout, @NonNull View headView, @NonNull View footView) {
        if (null == mChildTarget) {
            for (int i = 0; i < refreshLayout.getChildCount(); i++) {
                View child = refreshLayout.getChildAt(i);
                if (!child.equals(headView) && !child.equals(footView)) {
                    mChildTarget = child;
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public void measure(int width, int height) {
        if (null != mChildTarget) {
            mChildTarget.measure(width, height);
        }
    }

    public void layout(@NonNull ViewGroup refreshLayout, int topOffset) {
        if (null != mChildTarget) {
            int childLeft = refreshLayout.getPaddingLeft();
            int childTop = refreshLayout.getPaddingTop() + topOffset;
            int childRight = refreshLayout.getMeasuredWidth() - refreshLayout.getPaddingRight();
            int childBottom = refreshLayout.getMeasuredHeight() - refreshLayout.getPaddingBottom();
            mChildTarget.layout(childLeft, childTop, childRight, childBottom);
        }
    }

    public void resetLayout(@NonNull ViewGroup refreshLayout) {
        if (null != mChildTarget) {
            int childLeft = refreshLayout.getPaddingLeft();
            int childTop = refreshLayout.getPaddingTop();
            int childRight = mChildTarget.getWidth() - refreshLayout.getPaddingRight();
            int childBottom = mChildTarget.getHeight() - refreshLayout.getPaddingBottom();
            mChildTarget.layout(childLeft, childTop, childRight, childBottom);
        }
    }

    /**
     * 经测试isChildInTop、isChildInBottom计算的结果如下
     * 在顶部：true、false
     * 顶部滑到中间：true、false
     * 中间：false、false
     * 在底部：false、true
     * 底部滑到中间：false、true
     * 结论：需要适配顶部或底部滑动到中间的情况
     * 适配方案：通过移动偏移量，排除情况
     * 判断目标View是否滑动到顶部-还能否继续滑动
     *
     * @return true 在顶部
     */
    public boolean isChildInTop() {
        return !mChildTarget.canScrollVertically(-1);
    }

    /**
     * 判断目标View是否滑动到底部-还能否继续滑动
     *
     * @return true 在底部
     */
    public boolean isChildInBottom() {
        if (isChildInTop()) {
            return false;
        }

        if (null != mChildTarget) {
            if (mChildTarget instanceof RecyclerView) {
                boolean canScrollVertically = mChildTarget.canScrollVertically(1); // 判断是否能向上滑动
                return !canScrollVertically;
            } else if (mChildTarget instanceof AbsListView) {
                AbsListView absListView = (AbsListView) mChildTarget;
                int count = absListView.getAdapter().getCount();
                int firstPosition = absListView.getFirstVisiblePosition();
                if (firstPosition == 0 && absListView.getChildAt(0).getTop() >= absListView.getPaddingTop()) {
                    return false;
                }
                int lastPos = absListView.getLastVisiblePosition();
                if (lastPos > 0 && count > 0 && lastPos == count - 1) {
                    return true;
                }
                return false;
            } else if (mChildTarget instanceof ScrollView) {
                ScrollView scrollView = (ScrollView) mChildTarget;
                View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
                if (view != null) {
                    int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));
                    if (diff == 0) {
                        return true;
                    }
                }
            } else if (mChildTarget instanceof NestedScrollView) {
                NestedScrollView nestedScrollView = (NestedScrollView) mChildTarget;
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
        return false;
    }
}
