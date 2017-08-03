## LibViewRecycler
`LibViewRecycler` 是一个Android 滚动控件基类库，提供一些基础功能；主要功能分为：

* ViewHolder 类似注解的作用，管理findViewById()
* ListView的公共Adapter，提供数据管理功能；暂时有16个数据管理的Api
* RecyclerView的公共Adapter，提供数据管理功能；暂时有16个数据管理的Api
* 提供处理Recycler空数据、添加头部和底部的情况
* ItemDecoration，RecyclerView的分割线处理

技术交流群：[644213963](https://jq.qq.com/?_wv=1027&k=4ETdgdJ)   
个人邮箱：[957339173@qq.com](https://jq.qq.com/?_wv=1027&k=4B0yi1n)  
个人博客：[csdn.com/yline](http://blog.csdn.net/u014803950)  

## 依赖
* Gradle：
```compile 'com.yline.lib:LibViewRecycler:1.0.2'```
* Maven:
```
    <dependency>
      <groupId>com.yline.lib</groupId>
      <artifactId>LibViewRecycler</artifactId>
      <version>1.0.2</version>
      <type>pom</type>
    </dependency>
```
* Eclipse 请放弃治疗。

## 权限
无

## 使用教程
### Adapter的使用
> CommonListAdapter 和 CommonRecyclerAdapter 数据操作   
> 支持的数据操作，有以下

	// 获取数据信息
	List<E> getDataList();
	E getItem(int position);
	int getDataSize();
	boolean contains(Object object);
	boolean containsAll(Collection<?> collection);
	boolean isEmpty();
	// 添加数据
	void setDataList(List<E> list);
	boolean add(E object);
	boolean add(int index, E element);
	boolean addAll(Collection<? extends E> collection);
	boolean addAll(int index, Collection<? extends E> c);
	// 移除数据
	E remove(int index);
	boolean remove(E e);
	boolean removeAll(Collection<? extends E> collection);
	void clear();
	// 更新数据
	boolean update(int index, E e);
	boolean update(int index[], E[] arrays);

> CommonEmptyRecyclerAdapter   
> 重写以下两个函数即可

	int getEmptyItemRes() // 空数据时，布局文件
	void onBindEmptyViewHolder(RecyclerViewHolder viewHolder, int position) // 空数据时，进行的操作

> HeadFootRecyclerAdapter

	simpleAdapter = new SimpleHeadFootAdapter();
	recyclerView.setAdapter(simpleAdapter);

	// 添加 头部
	View headViewA = new View(this);
	...
	simpleAdapter.addHeadView(headViewA);

	View headViewB = new View(this);
	...
	simpleAdapter.addHeadView(headViewB);

	// 添加 底部
	View footViewA = new View(this);
	...
	simpleAdapter.addFootView(footViewA);

	View footViewB = new View(this);
	...
	simpleAdapter.addFootView(footViewB);

### ItemDecoration
	recyclerView.addItemDecoration(new CommonGridDecoration(this)
	{
		// 重写 间隔 的资源文件 
		@Override
		protected int getDivideResourceId()
		{
			return R.drawable.recycler_divider_black_normal;
		}
		
		// left、top、right、bottom 四个方向最边上是否绘制间隔
		@Override
		protected boolean[] isSpanDraw()
		{
			return new boolean[]{false, false, false, false};
		}
	});

### ViewHolder
新建

	ViewHolder(View view)
	ViewHolder(Activity activity)

使用API

	// 基础
	<T extends View> T get(int viewId)
	
	// 扩充
	View getItemView()
	String getText(int viewId)
	TextView setText(int viewId, String content)
	ImageView setImageBackgroundResource(int viewId, int resId)
	ImageView setImageResource(int viewId, int resId)
	setOnClickListener(int viewId, View.OnClickListener listener)
	ProgressBar setProgress(int viewId, int progress)
	...

添加头部、底部、空数据、带item时，效果   
![](https://github.com/yline/as_lib_view_recycler/blob/master/LibViewRecyclerDemo/src/main/assets/empty.jpeg)

添加头部、底部、有数据、带item时，效果   
![](https://github.com/yline/as_lib_view_recycler/blob/master/LibViewRecyclerDemo/src/main/assets/data.jpeg)

## 版本    
### Version 1.0.2
> ViewHolder View控件操作   
> 空数据时，RecyclerView显示   
> RecyclwView添加头部和底部   
> ItemDecoration 适配了Linear、Grid、StaggerGrid




