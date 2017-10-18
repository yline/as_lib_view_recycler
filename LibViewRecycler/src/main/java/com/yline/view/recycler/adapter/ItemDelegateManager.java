package com.yline.view.recycler.adapter;

import android.util.SparseArray;

import com.yline.view.recycler.holder.RecyclerViewHolder;

/**
 * Recycler的 Item 分发器
 *
 * @author yline 2017/10/18 -- 10:48
 * @version 1.0.0
 */
public class ItemDelegateManager {
    public static final int DefaultItemType = -1024;
    private SparseArray<ItemDelegateCallback> mSparseArray;

    public ItemDelegateManager() {
        this.mSparseArray = new SparseArray<>();
    }

    public ItemDelegateCallback getItemDeleteGateCallback(int viewType) {
        return mSparseArray.get(viewType);
    }

    /**
     * 添加 某种数据类型，若重复，则添加失败
     *
     * @param delegateCallback 某种数据 类型
     */
    public boolean addItemDelegate(ItemDelegateCallback delegateCallback) {
        int viewType = delegateCallback.getItemType();
        if (null == mSparseArray.get(viewType)) {
            mSparseArray.put(viewType, delegateCallback);
            return true;
        }
        return false;
    }

    public <Model> int getItemType(Model model, int position) {
        for (int i = 0; i < mSparseArray.size(); i++) {
            ItemDelegateCallback<Model> delegateCallback = mSparseArray.valueAt(i);
            if (delegateCallback.isItemType(model, position)) {
                return mSparseArray.keyAt(i);
            }
        }
        return DefaultItemType;
    }

    public <Model> void onBindHolder(RecyclerViewHolder holder, Model model, int position) {
        for (int i = 0; i < mSparseArray.size(); i++) {
            ItemDelegateCallback<Model> delegateCallback = mSparseArray.valueAt(i);
            if (delegateCallback.isItemType(model, position)) {
                delegateCallback.onBindHolder(holder, model, position);
                return;
            }
        }
    }
}
