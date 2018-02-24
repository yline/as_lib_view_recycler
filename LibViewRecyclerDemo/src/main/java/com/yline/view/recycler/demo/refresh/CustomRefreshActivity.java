package com.yline.view.recycler.demo.refresh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yline.application.SDKManager;
import com.yline.base.BaseAppCompatActivity;
import com.yline.test.StrConstant;
import com.yline.view.recycler.demo.R;
import com.yline.view.recycler.refresh.adapter.DefaultRefreshAdapter;
import com.yline.view.recycler.refresh.SuperSwipeRefreshLayout;
import com.yline.view.recycler.simple.SimpleHeadFootRecyclerAdapter;

import java.util.Random;

public class CustomRefreshActivity extends BaseAppCompatActivity {
    private SuperSwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerView;

    private SimpleHeadFootRecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_refresh);

        swipeRefreshLayout = (SuperSwipeRefreshLayout) findViewById(R.id.super_swipe_refresh);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_super_swipe);
        recyclerAdapter = new SimpleHeadFootRecyclerAdapter(this);

        swipeRefreshLayout.setRefreshAdapter(new DefaultRefreshAdapter() {
            // 动画下拉时，距离变动
            @Override
            public void onCreating(float dragDistance, float targetDistance) {
                super.onCreating(dragDistance, targetDistance);
            }

            // 传递你需要定制的view;
            @NonNull
            @Override
            public View getView(Context context) {
                return super.getView(context);
            }

            // 正在刷新时，执行的操作
            @Override
            protected void onAnimate() {
                super.onAnimate();

                SDKManager.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        // 更新recyclerView
                        recyclerAdapter.add(0, "Refresh Item + " + new Random().nextInt(300), true);
                        recyclerView.scrollToPosition(0);
                    }
                }, 2000);
            }

            // 设置是否在表面；true:子View随下拉而一起滑动； false:子view不下滑，只是浮在上面
            @Override
            public boolean isTargetScroll() {
                return super.isTargetScroll();
            }

            // 返回背景颜色
            @Override
            public int getBackgroundResource() {
                return super.getBackgroundResource();
            }
        });

        // 支持自定义（view）
        swipeRefreshLayout.setLoadAdapter(null);
        /*swipeRefreshLayout.setOnLoadListener(new ViewSwipeRefreshLayout.OnSwipeListener()
        {
			@Override
			public void onRefresh()
			{
				new Handler().postDelayed(new Runnable()
				{

					@Override
					public void run()
					{
						swipeRefreshLayout.setLoadMore(false);
						// 更新recyclerView
						int itemNumber = recyclerAdapter.getItemCount();
						recyclerAdapter.add(itemNumber, "Loaded Item + " + new Random().nextInt(300));
						recyclerView.scrollToPosition(itemNumber);
					}
				}, 4000);
			}
		});*/

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

        recyclerAdapter.setDataList(StrConstant.getListSeven(20), true);
    }

    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, CustomRefreshActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }
}
