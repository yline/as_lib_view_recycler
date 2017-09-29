package com.yline.view.recycler.demo;

import android.support.design.widget.TabLayout;

import com.yline.application.SDKManager;
import com.yline.log.LogFileUtil;
import com.yline.test.StrConstant;
import com.yline.view.recycler.callback.IDataAdapterCallback;

import java.util.List;

public class DataAdapterTest
{
	public DataAdapterTest(TabLayout tabLayout, final IDataAdapterCallback<String> adapter)
	{
		tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
		tabLayout.addTab(tabLayout.newTab().setText("getDataList"));
		tabLayout.addTab(tabLayout.newTab().setText("getItem(0)"));
		tabLayout.addTab(tabLayout.newTab().setText("getDataSize"));
		tabLayout.addTab(tabLayout.newTab().setText("contains"));
		tabLayout.addTab(tabLayout.newTab().setText("containsAll"));
		tabLayout.addTab(tabLayout.newTab().setText("isEmpty"));
		tabLayout.addTab(tabLayout.newTab().setText("setDataList"));
		tabLayout.addTab(tabLayout.newTab().setText("add"));
		tabLayout.addTab(tabLayout.newTab().setText("add(0)"));
		tabLayout.addTab(tabLayout.newTab().setText("addAll"));
		tabLayout.addTab(tabLayout.newTab().setText("addAll(0)"));
		tabLayout.addTab(tabLayout.newTab().setText("remove"));
		tabLayout.addTab(tabLayout.newTab().setText("remove(0)"));
		tabLayout.addTab(tabLayout.newTab().setText("removeAll"));
		tabLayout.addTab(tabLayout.newTab().setText("clear"));
		tabLayout.addTab(tabLayout.newTab().setText("update(0)"));
		tabLayout.addTab(tabLayout.newTab().setText("update(0-1)"));
		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
		{
			@Override
			public void onTabSelected(TabLayout.Tab tab)
			{
				switch (tab.getPosition())
				{
					case 0:
						SDKManager.toast("getDataList = " + adapter.getDataList().toString());
						LogFileUtil.v("getDataList = " + adapter.getDataList().toString());
						break;
					case 1:
						try
						{
							SDKManager.toast("getItem(0) = " + adapter.getItem(0));
							LogFileUtil.v("getItem(0) = " + adapter.getItem(0));
						}
						catch (IllegalArgumentException ex)
						{
							SDKManager.toast("list size is 0");
							LogFileUtil.v("list size is 0");
						}
						break;
					case 2:
						SDKManager.toast("getDataSize = " + adapter.getDataSize());
						LogFileUtil.v("getDataSize = " + adapter.getDataSize());
						break;
					case 3:
						String str = StrConstant.getListThree(2).get(0);
						SDKManager.toast(String.format("adapter contains %s is ", str) + adapter.contains(str));
						LogFileUtil.v(String.format("adapter contains %s is ", str) + adapter.contains(str));
						break;
					case 4:
						List<String> strList = StrConstant.getListThree(2);
						SDKManager.toast(String.format("adapter containsAll %s is ", strList.toString()) + adapter.containsAll(strList));
						LogFileUtil.v(String.format("adapter containsAll %s is ", strList.toString()) + adapter.containsAll(strList));
						break;
					case 5:
						SDKManager.toast("isEmpty = " + adapter.isEmpty());
						LogFileUtil.v("isEmpty = " + adapter.isEmpty());
						break;
					case 6:
						adapter.setDataList(StrConstant.getListThree(5));
						SDKManager.toast("setDataList ok");
						LogFileUtil.v("setDataList ok");
						break;
					case 7:
						boolean addResult = adapter.add(StrConstant.getListSix(2).get(0));
						SDKManager.toast("add = " + addResult);
						LogFileUtil.v("add = " + addResult);
						break;
					case 8:
						boolean addResult0 = adapter.add(0, StrConstant.getListFour(3).get(0));
						SDKManager.toast("add(0) = " + addResult0);
						LogFileUtil.v("add(0) = " + addResult0);
						break;
					case 9:
						boolean addAll = adapter.addAll(StrConstant.getListOne(2));
						SDKManager.toast("addAll = " + addAll);
						LogFileUtil.v("addAll = " + addAll);
						break;
					case 10:
						boolean addAll0 = adapter.addAll(0, StrConstant.getListOne(3));
						SDKManager.toast("addAll0 = " + addAll0);
						LogFileUtil.v("addAll0 = " + addAll0);
						break;
					case 11:
						boolean remove = adapter.remove(StrConstant.getListFour(3).get(0));
						SDKManager.toast("remove = " + remove);
						LogFileUtil.v("remove = " + remove);
						break;
					case 12:
						String remove0 = adapter.remove(0);
						SDKManager.toast("remove0 = " + remove0);
						LogFileUtil.v("remove0 = " + remove0);
						break;
					case 13:
						boolean removeAll = adapter.removeAll(StrConstant.getListFour(3));
						SDKManager.toast("removeAll = " + removeAll);
						LogFileUtil.v("removeAll = " + removeAll);
						break;
					case 14:
						adapter.clear();
						SDKManager.toast("adapter ok");
						LogFileUtil.v("adapter ok");
						break;
					case 15:
						boolean update0 = adapter.update(0, StrConstant.getListEnglish(2).get(0));
						SDKManager.toast("update0 = " + update0);
						LogFileUtil.v("update0 = " + update0);
						break;
					case 16:
						boolean update01 = adapter.update(new int[]{0, 1}, StrConstant.getListEnglish(2).toArray(new String[]{"", ""}));
						SDKManager.toast("update01 = " + update01);
						LogFileUtil.v("update01 = " + update01);
						break;
				}
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab)
			{

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab)
			{

			}
		});
	}
}
