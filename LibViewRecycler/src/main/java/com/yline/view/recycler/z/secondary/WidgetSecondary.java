package com.yline.view.recycler.z.secondary;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yline.view.recycler.R;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 二级列表
 * 数据后驱动型
 *
 * @author yline 2017/3/16 -- 17:05
 * @version 1.0.0
 */
public class WidgetSecondary
{
	protected Context sContext;
	
	protected FirstRecyclerAdapter firstListAdapter;

	protected SecondRecyclerAdapter secondListAdapter;

	private OnSecondaryCallback onSecondaryCallback;

	public WidgetSecondary(Context context, List<View> viewList)
	{
		this.sContext = context;

		View view = initView();
		viewList.add(view);
	}

	public WidgetSecondary(Context context, ViewGroup viewGroup)
	{
		this.sContext = context;

		View view = initView();
		viewGroup.addView(view);
	}

	private View initView()
	{
		View parentView = LayoutInflater.from(sContext).inflate(getWidgetResourceId(), null);

		// 2
		RecyclerView secondRecyclerView = (RecyclerView) parentView.findViewById(R.id.recycler_widget_second);
		secondRecyclerView.setLayoutManager(new LinearLayoutManager(sContext));
		secondListAdapter = new SecondRecyclerAdapter()
		{
			@Override
			protected int getItemResource()
			{
				return getSecondItemResource();
			}
		};
		secondRecyclerView.setAdapter(secondListAdapter);
		secondListAdapter.setOnSecondListClickListener(new SecondRecyclerAdapter.OnSecondListClickListener()
		{
			@Override
			public void onSecondClick(RecyclerViewHolder viewHolder, String str, int position, boolean isSelected)
			{
				if (null != onSecondaryCallback)
				{
					onSecondaryCallback.onSecondSelected(str, position, isSelected);
				}
			}
		});

		// 1
		RecyclerView firstRecyclerView = (RecyclerView) parentView.findViewById(R.id.recycler_widget_first);
		firstRecyclerView.setLayoutManager(new LinearLayoutManager(sContext));
		firstListAdapter = new FirstRecyclerAdapter()
		{
			@Override
			protected int getItemResource()
			{
				return getFirstItemResource();
			}
		};
		firstRecyclerView.setAdapter(firstListAdapter);
		firstListAdapter.setOnFirstListClickListener(new FirstRecyclerAdapter.OnFirstListClickListener()
		{
			@Override
			public void onFirstClick(RecyclerViewHolder viewHolder, String str, int position)
			{
				if (null != onSecondaryCallback)
				{
					onSecondaryCallback.onFirstSelected(str, position);
				}
			}
		});

		// btn
		Button btnSure = (Button) parentView.findViewById(R.id.btn_widget_sure);
		btnSure.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (null != onSecondaryCallback)
				{
					List<String> secondResultList = secondListAdapter.getSelectedList();
					onSecondaryCallback.onSelectedConfirm(firstListAdapter.getSelectedString(), secondResultList);
				}
			}
		});
		
		return parentView;
	}

	public void setOnSecondaryCallback(OnSecondaryCallback onSecondaryCallback)
	{
		this.onSecondaryCallback = onSecondaryCallback;
	}

	/**
	 * @param firstList 第一列表所有值
	 */
	public void setData(List<String> firstList)
	{
		if (null == firstList)
		{
			return;
		}

		firstListAdapter.setDataList(firstList);
	}

	public boolean isSecondSelect(int position)
	{
		return secondListAdapter.isSelected(position);
	}

	public void setSecondSelect(int position, boolean isSelect)
	{
		secondListAdapter.setSelectPosition(position, isSelect);
	}

	public void setSecondSelectOnly(int position)
	{
		secondListAdapter.setSelectPositionAndCancelAll(position);
	}

	public void updateSecondList(List<String> strings)
	{
		secondListAdapter.setDataList(strings);
	}

	/**
	 * @param firstList          第一列表所有值
	 * @param firstSelectedValue 第一列表，默认的初始值
	 * @param secondList         在第一列表默认值下，第二列表的所有值
	 * @param secondSelectedList 第二列表，默认的初始值
	 */
	public void setData(List<String> firstList, String firstSelectedValue, List<String> secondList, List<String> secondSelectedList)
	{
		if (null == firstList)
		{
			return;
		}

		firstListAdapter.setDataList(firstList);

		int firstValuePosition = firstList.indexOf(firstSelectedValue);
		if (firstValuePosition != -1)
		{
			firstListAdapter.setSelectPosition(firstValuePosition);
			secondListAdapter.setDataList(secondList);

			List<Integer> secondPositionList = new ArrayList<>();
			for (String str : secondSelectedList)
			{
				int secondValuePosition = secondList.indexOf(str);
				if (secondValuePosition != -1)
				{
					secondPositionList.add(secondValuePosition);
				}
			}

			if (secondPositionList.size() > 0)
			{
				secondListAdapter.setSelectPositionList(secondPositionList);
			}
		}
	}

	public interface OnSecondaryCallback
	{
		/**
		 * 第一列表，单个选择
		 *
		 * @param firstName
		 * @param position
		 */
		void onFirstSelected(String firstName, int position);

		/**
		 * 第二列表，单个选择
		 *
		 * @param secondName
		 * @param position
		 * @param isSelected
		 */
		void onSecondSelected(String secondName, int position, boolean isSelected);

		/**
		 * 第二列表，多个选择后，确定
		 *
		 * @param firstName
		 * @param secondList
		 */
		void onSelectedConfirm(String firstName, List<String> secondList);
	}

	/* ---------------------------------------------------- 从这里开始设置参数；这些参数都是可以被重写的 ---------------------------------------------------- */
	protected int getWidgetResourceId()
	{
		return R.layout.lib_view_recycler_secondary;
	}

	@LayoutRes
	protected int getFirstItemResource()
	{
		return R.layout.lib_view_recycler_secondary_item_first;
	}

	@LayoutRes
	protected int getSecondItemResource()
	{
		return R.layout.lib_view_recycler_secondary_item_second;
	}
}
