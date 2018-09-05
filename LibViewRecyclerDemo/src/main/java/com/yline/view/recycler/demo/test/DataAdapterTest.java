package com.yline.view.recycler.demo.test;

import android.support.design.widget.TabLayout;

import com.yline.application.SDKManager;
import com.yline.log.LogFileUtil;
import com.yline.test.StrConstant;
import com.yline.view.recycler.holder.IDataAdapterCallback;

import java.util.List;

public class DataAdapterTest {
    public DataAdapterTest(TabLayout tabLayout, final IDataAdapterCallback<String> adapter) {
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
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        SDKManager.toast("getDataList = " + adapter.getDataList().toString());
                        LogFileUtil.v("getDataList = " + adapter.getDataList().toString());
                        break;
                    case 1:
                        if (adapter.isEmpty()) {
                            SDKManager.toast("list size is 0");
                            LogFileUtil.v("list size is 0");
                        } else {
                            SDKManager.toast("getItem(0) = " + adapter.get(0));
                            LogFileUtil.v("getItem(0) = " + adapter.get(0));
                        }
                        break;
                    case 2:
                        SDKManager.toast("getDataSize = " + adapter.size());
                        LogFileUtil.v("getDataSize = " + adapter.size());
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
                        adapter.setDataList(StrConstant.getListThree(5), true);
                        SDKManager.toast("setDataList ok");
                        LogFileUtil.v("setDataList ok");
                        break;
                    case 7:
                        boolean addResult = adapter.add(StrConstant.getListSix(2).get(0), true);
                        SDKManager.toast("add = " + addResult);
                        LogFileUtil.v("add = " + addResult);
                        break;
                    case 8:
                        adapter.add(0, StrConstant.getListFour(3).get(0), true);
                        SDKManager.toast("add(0) = ");
                        LogFileUtil.v("add(0) = ");
                        break;
                    case 9:
                        boolean addAll = adapter.addAll(StrConstant.getListOne(2), true);
                        SDKManager.toast("addAll = " + addAll);
                        LogFileUtil.v("addAll = " + addAll);
                        break;
                    case 10:
                        boolean addAll0 = adapter.addAll(0, StrConstant.getListOne(3), true);
                        SDKManager.toast("addAll0 = " + addAll0);
                        LogFileUtil.v("addAll0 = " + addAll0);
                        break;
                    case 11:
                        boolean remove = adapter.remove(StrConstant.getListFour(3).get(0), true);
                        SDKManager.toast("remove = " + remove);
                        LogFileUtil.v("remove = " + remove);
                        break;
                    case 12:
                        String remove0 = adapter.remove(0, true);
                        SDKManager.toast("remove0 = " + remove0);
                        LogFileUtil.v("remove0 = " + remove0);
                        break;
                    case 13:
                        boolean removeAll = adapter.removeAll(StrConstant.getListFour(3), true);
                        SDKManager.toast("removeAll = " + removeAll);
                        LogFileUtil.v("removeAll = " + removeAll);
                        break;
                    case 14:
                        adapter.clear(true);
                        SDKManager.toast("adapter ok");
                        LogFileUtil.v("adapter ok");
                        break;
                    case 15:
                        if (adapter.isEmpty()) {
                            SDKManager.toast("update0 is empty");
                            LogFileUtil.v("update0 is empty");
                        } else {
                            String oldString = adapter.set(0, StrConstant.getListEnglish(2).get(0), true);
                            SDKManager.toast("update0 = " + oldString);
                            LogFileUtil.v("update0 = " + oldString);
                        }
                        break;
                    default:
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
