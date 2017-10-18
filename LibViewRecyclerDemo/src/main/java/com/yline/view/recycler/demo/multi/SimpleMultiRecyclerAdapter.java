package com.yline.view.recycler.demo.multi;

import com.yline.test.StrConstant;
import com.yline.view.recycler.adapter.ItemDelegateCallback;
import com.yline.view.recycler.adapter.ItemMultiRecyclerAdapter;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 多种 Item 的 RecyclerAdapter
 *
 * @author yline 2017/10/18 -- 14:43
 * @version 1.0.0
 */
public class SimpleMultiRecyclerAdapter extends ItemMultiRecyclerAdapter {
    public SimpleMultiRecyclerAdapter() {
        super();
        mItemDelegateManager.addItemDelegate(new ContentItemDelegate());
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    private class ContentItemDelegate implements ItemDelegateCallback<String> {
        @Override
        public int getItemType() {
            return 0;
        }

        @Override
        public boolean isItemType(String s, int position) {
            return true;
        }

        @Override
        public int getItemRes() {
            return android.R.layout.simple_list_item_1;
        }

        @Override
        public void onBindHolder(RecyclerViewHolder holder, String s, int position) {
            holder.setText(android.R.id.text1, "position = " + position);
        }
    }

    public void setData(){
        List<Object> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add(i, StrConstant.getListFive(20).get(i));
        }

        mList = data;
    }
}
