package com.yline.view.recycler.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * @author yline 2017/9/30 -- 11:43
 * @version 1.0.0
 */
public class Callback {

    /**
     * 查看数据信息
     *
     * @param <E> 数据类型
     */
    public interface IDataInfoCallback<E> {
        /**
         * 数据大小
         *
         * @return 大小
         */
        int size();

        /**
         * 数据是否为空
         *
         * @return true or false
         */
        boolean isEmpty();

        /**
         * 是否包含某数据
         *
         * @param element 数据对象
         * @return true or false
         */
        boolean contains(E element);

        /**
         * 是否包含，所有数据
         *
         * @param collection 数据集合
         * @return true of false
         */
        boolean containsAll(Collection<? extends E> collection);

        /**
         * 获取单条数据
         *
         * @param index 数据位置
         * @return 单条数据
         * @throws IndexOutOfBoundsException if the index is out of range
         */
        E get(int index);

        /**
         * 返回数据的位置
         *
         * @param element 单个数据
         * @return 从前面遍历，第一个符合条件的位置
         */
        int indexOf(E element);

        /**
         * 返回数据的位置
         *
         * @param element 单个数据
         * @return 从后面遍历，第一个符合条件的位置
         */
        int lastIndexOf(E element);

        /**
         * 获取截取的数据
         *
         * @param fromIndex 开始位置
         * @param toIndex   结束位置
         * @return 截取的List
         * @throws IndexOutOfBoundsException for an illegal endpoint index value
         */
        List<E> subList(int fromIndex, int toIndex);
    }

    /**
     * Adapter 支持的数据操作
     *
     * @param <E> 数据类型
     */
    public interface IDataListCallback<E> {
        /**
         * 返回所有数据
         *
         * @return 不可修改的数据集
         */
        List<E> getDataList();

        /**
         * 清空之前的数据，替换掉所有数据
         *
         * @param list     放入数据
         * @param isNotify 是否更新数据
         */
        void setDataList(List<E> list, boolean isNotify);
    }

    public interface IDataAdapterCallback<E> extends IDataInfoCallback<E>, IDataListCallback<E> {
        /**
         * 添加数据
         *
         * @param element  单个数据
         * @param isNotify 是否更新界面
         * @return true or false
         */
        boolean add(E element, boolean isNotify);

        /**
         * 添加数据
         *
         * @param index    数据位置
         * @param element  单个数据
         * @param isNotify 是否更新界面
         */
        void add(int index, E element, boolean isNotify);

        /**
         * 添加数据
         *
         * @param collection 数据集合
         * @param isNotify   是否更新界面
         * @return true or false
         */
        boolean addAll(Collection<? extends E> collection, boolean isNotify);

        /**
         * 添加数据集合
         *
         * @param index      开始位置
         * @param collection 数据集合
         * @param isNotify   是否更新界面
         * @return true or false
         */
        boolean addAll(int index, Collection<? extends E> collection, boolean isNotify);

        /**
         * 删除数据
         *
         * @param index    指定位置
         * @param isNotify 是否更新界面
         * @return 删除的数据
         */
        E remove(int index, boolean isNotify);

        /**
         * 删除，遍历到符合条件的第一条数据
         *
         * @param element  单个数据
         * @param isNotify 是否更新界面
         * @return true or false
         */
        boolean remove(E element, boolean isNotify);

        /**
         * 删除数据集合，删除所有包含在该集合内的数据
         *
         * @param collection 数据集合
         * @param isNotify   是否更新界面
         * @return true or false
         */
        boolean removeAll(Collection<? extends E> collection, boolean isNotify);

        /**
         * 删除所有数据
         *
         * @param isNotify 是否更新界面
         */
        void clear(boolean isNotify);

        /**
         * 替换掉，某一条数据
         *
         * @param index    数据位置
         * @param element  替换之后的元素
         * @param isNotify 是否更新界面
         * @return 替换前的元素
         * @throws IndexOutOfBoundsException if the index is out of range
         */
        E set(int index, E element, boolean isNotify);

        /**
         * 按照传入的规则，替换掉所有数据
         *
         * @param operator 替换的规则
         * @param isNotify 是否更新界面
         */
        void replaceAll(UnaryOperator<E> operator, boolean isNotify);

        /**
         * 按照传入的规则，对所有数据排序
         *
         * @param comparator 排序的规则
         * @param isNotify   是否更新界面
         */
        void sort(Comparator<? super E> comparator, boolean isNotify);
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
         * 条目，点击
         *
         * @param viewHolder Item布局
         * @param t          数据
         * @param position   当前位置
         */
        void onItemClick(RecyclerViewHolder viewHolder, T t, int position);
    }

    public interface onRecyclerItemLongClickListener<T> {
        /**
         * 条目，长按
         *
         * @param viewHolder Item布局
         * @param t          数据
         * @param position   当前位置
         */
        void onItemLongClick(RecyclerViewHolder viewHolder, T t, int position);
    }
}
