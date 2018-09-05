package com.yline.view.recycler.demo.multi.adapter;

import com.yline.view.recycler.holder.RecyclerViewHolder;

/**
 * Recycler 单个Item 实现公共类
 *
 * @author yline 2017/10/18 -- 11:01
 * @version 1.0.0
 */
public interface ItemDelegateCallback<Model> {
    /**
     * 获取 Item 的类型
     *
     * @return 单个常量
     */
    int getItemType();

    /**
     * 返回是否符合ItemType
     *
     * @param position 位置
     * @return 是否是该 Item
     */
    boolean isItemType(Model model, int position);

    /**
     * @return 绑定的 资源Id
     */
    int getItemRes();

    /**
     * 绑定数据，并显示数据
     */
    void onBindHolder(RecyclerViewHolder holder, Model model, int position);
}
