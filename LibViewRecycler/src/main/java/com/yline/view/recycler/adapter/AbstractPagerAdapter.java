package com.yline.view.recycler.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewPager Adapter
 *
 * @author yline 2017/11/29 -- 16:30
 * @version 1.0.0
 */
public class AbstractPagerAdapter extends PagerAdapter {
    private List<View> mViewList;

    public AbstractPagerAdapter() {
        mViewList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mViewList.get(position);
        container.addView(view);
        return view;
    }

    /**
     * 这里不能重写原来的方法,因为一定环境下不支持
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void addView(View view, boolean isNotify) {
        if (null != view) {
            this.mViewList.add(view);
            if (isNotify) {
                notifyDataSetChanged();
            }
        }
    }

    /**
     * 添加一个View
     *
     * @param postition the index at which to insert.
     */
    public void addView(int postition, View view, boolean isNotify) {
        if (null != view) {
            this.mViewList.add(postition, view);
            if (isNotify) {
                notifyDataSetChanged();
            }
        }
    }

    /**
     * 移除一个View
     *
     * @param position the index at which to insert.
     */
    public void removeView(int position, boolean isNotify) {
        this.mViewList.remove(position);
        if (isNotify) {
            notifyDataSetChanged();
        }
    }

    public void removeView(View view, boolean isNotify) {
        this.mViewList.remove(view);
        if (isNotify) {
            notifyDataSetChanged();
        }
    }

    /**
     * 替换所有的view
     */
    public void setViews(List<View> viewList, boolean isNotify) {
        if (null != viewList && viewList.size() > 0) {
            this.mViewList = viewList;
            if (isNotify) {
                notifyDataSetChanged();
            }
        }
    }
}
