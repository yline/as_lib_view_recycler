package com.yline.view.recycler.simple;

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

    public void setDataList(List<String> list, boolean isNotify) {
        if (null != list) {
            List<Object> dataList = new ArrayList<>();
            for (String tempStr : list) {
                dataList.add(tempStr);
            }
            mDataManager.setDataList(dataList, isNotify);
        }
    }
}
