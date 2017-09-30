package com.yline.view.recycler.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Collection;
import java.util.List;

/**
 * @author yline 2017/9/30 -- 11:43
 * @version 1.0.0
 */
public class Callback {

    /**
     * Adapter 支持的数据操作
     *
     * @param <E> 数据类型
     */
    public interface IDataAdapterCallback<E> {
        /**
         * @return 不可修改的数据集
         */
        List<E> getDataList();

        /**
         * @param list     放入数据
         * @param isNotify 是否更新数据
         */
        void setDataList(List<E> list, boolean isNotify);

        /**
         * @param position 位置信息
         * @return 单个数据
         */
        E getItem(int position);

        /**
         * @return 数据长度
         */
        int getDataSize();

        /**
         * @param element 单个刷数据
         * @return 是否含有
         */
        boolean contains(E element);

        /**
         * @param collection 数据集
         * @return 是否含有
         */
        boolean containsAll(Collection<? extends E> collection);

        /**
         * @return 数据长度是否为空，或者长度为零
         */
        boolean isEmpty();

        /**
         * @param element  增加一条数据
         * @param isNotify 是否更新界面
         * @return 是否成功
         */
        boolean add(E element, boolean isNotify);

        /**
         * @param index    指定位置
         * @param element  添加数据
         * @param isNotify 是否更新界面
         */
        void add(int index, E element, boolean isNotify);

        /**
         * @param collection 添加一个数据集
         * @param isNotify   是否更新界面
         * @return 是否成功
         */
        boolean addAll(Collection<? extends E> collection, boolean isNotify);

        /**
         * @param index      指定位置
         * @param collection 添加一个数据集
         * @param isNotify   是否更新界面
         * @return 是否成功
         */
        boolean addAll(int index, Collection<? extends E> collection, boolean isNotify);

        /**
         * @param index    指定位置
         * @param isNotify 是否更新界面
         * @return 移除的数据
         */
        E remove(int index, boolean isNotify);

        /**
         * @param e        单个数据
         * @param isNotify 是否更新界面
         * @return 是否成功
         */
        boolean remove(E e, boolean isNotify);

        /**
         * @param collection 数据集
         * @param isNotify   是否更新界面
         * @return 是否成功
         */
        boolean removeAll(Collection<? extends E> collection, boolean isNotify);

        /**
         * @param isNotify 是否更新界面
         */
        void clear(boolean isNotify);

        /**
         * @param index    指定位置
         * @param e        单个新数据
         * @param isNotify 是否更新界面
         * @return 是否成功
         */
        boolean update(int index, E e, boolean isNotify);

        /**
         * @param index    指定位置
         * @param arrays   数据集
         * @param isNotify 是否更新界面
         * @return 是否成功
         */
        boolean update(int index[], E[] arrays, boolean isNotify);
    }

    /**
     * RecyclerAdapter, 头部和底部的个数
     */
    public interface IHeadFootCallback {
        /**
         * @return 头部个数
         */
        int getHeadersCount();

        /**
         * @return 底部个数
         */
        int getFootersCount();
    }

    /**
     * ViewHolder对应的接口
     *
     * @author yline 2017/5/9 -- 15:47
     * @version 1.0.0
     */
    public interface IViewHolderCallback {
        /**
         * @param viewId id
         * @return View控件的对象
         */
        <T extends View> T get(int viewId);

        /**
         * @return 所维持的控件本身
         */
        View getItemView();

        /**
         * @param viewId id
         * @return 文本内容
         */
        String getText(int viewId);

        /**
         * @param viewId   id
         * @param listener 监听器
         */
        void setOnClickListener(int viewId, View.OnClickListener listener);

        /**
         * @param viewId  id
         * @param content 文本内容
         * @return 控件本身
         */
        TextView setText(int viewId, String content);

        /**
         * @param viewId id
         * @param resId  资源id
         * @return 控件本身
         */
        ImageView setImageBackgroundResource(int viewId, int resId);

        /**
         * @param viewId id
         * @param resId  资源id
         * @return 控件本身
         */
        ImageView setImageResource(int viewId, int resId);

        /**
         * @param viewId   id
         * @param progress 进度
         * @return 控件本身
         */
        ProgressBar setProgress(int viewId, int progress);
    }

    public interface OnRecyclerItemClickListener<T> {
        /**
         * @param viewHolder Item布局
         * @param t          数据
         * @param position   当前位置
         */
        void onItemClick(RecyclerViewHolder viewHolder, T t, int position);
    }

    public interface onRecyclerItemLongClickListener<T> {
        /**
         * @param viewHolder Item布局
         * @param t          数据
         * @param position   当前位置
         */
        void onItemLongClick(RecyclerViewHolder viewHolder, T t, int position);
    }
}
