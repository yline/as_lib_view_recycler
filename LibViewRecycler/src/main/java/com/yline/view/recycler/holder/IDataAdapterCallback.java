package com.yline.view.recycler.holder;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * 数据操作，所有支持的操作
 *
 * @author yline 2017/9/30 -- 11:43
 * @version 1.0.0
 */
public interface IDataAdapterCallback<E> {
	/* -----------------------查看数据内容（与Adapter关系不大，抽象类统一实现）------------------------- */
	
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
	
	/**
	 * 返回所有数据
	 *
	 * @return 不可修改的数据集
	 */
	List<E> getDataList();
	
	/* ---------------------------------------改变数据内容（需要对应Adapter调整）------------------------------------------- */
	
	/**
	 * 清空之前的数据，替换掉所有数据
	 *
	 * @param list     放入数据
	 * @param isNotify 是否更新数据
	 */
	void setDataList(List<E> list, boolean isNotify);
	
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
