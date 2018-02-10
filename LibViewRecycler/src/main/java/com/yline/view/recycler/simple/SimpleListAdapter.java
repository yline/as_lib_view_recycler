package com.yline.view.recycler.simple;

import android.content.Context;
import android.view.ViewGroup;

import com.yline.view.recycler.adapter.AbstractListAdapter;
import com.yline.view.recycler.holder.ViewHolder;

/**
 * 使用默认布局，简单的List列表
 *
 * @author yline 2017/5/23 -- 10:16
 * @version 1.0.0
 */
public class SimpleListAdapter extends AbstractListAdapter<String> {
    public SimpleListAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemRes(int position) {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    protected void onBindViewHolder(ViewGroup parent, ViewHolder viewHolder, int position) {
        viewHolder.setText(android.R.id.text1, getItem(position));
    }
}
