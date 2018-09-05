package com.yline.view.recycler.refresh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yline.application.SDKManager;
import com.yline.base.BaseAppCompatActivity;
import com.yline.test.StrConstant;
import com.yline.view.recycler.refresh.adapter.AbstractRefreshAdapter;
import com.yline.view.recycler.holder.RecyclerViewHolder;
import com.yline.view.recycler.test.SimpleHeadFootRecyclerAdapter;

import java.util.Random;

public class SimpleRefreshActivity extends BaseAppCompatActivity {
    private SuperSwipeRefreshLayout swipeRefreshLayout;

    private SimpleHeadFootRecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_refresh);

        swipeRefreshLayout = (SuperSwipeRefreshLayout) findViewById(R.id.super_swipe_refresh);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_super_swipe);
        recyclerAdapter = new SimpleHeadFootRecyclerAdapter(this){
            @Override
            public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
                super.onBindViewHolder(holder, position);

                holder.getItemView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SDKManager.toast("getItemView + " + position);
                    }
                });
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

        swipeRefreshLayout.setOnRefreshListener(new AbstractRefreshAdapter.OnSwipeListener() {
            @Override
            public void onRefresh() {
                SDKManager.getHandler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        // 更新recyclerView
                        recyclerAdapter.add(0, "Refresh Item + " + new Random().nextInt(300), true);
                        recyclerView.scrollToPosition(0);
                    }
                }, 4000);
            }
        });
        swipeRefreshLayout.setOnLoadListener(new AbstractRefreshAdapter.OnSwipeListener() {
            @Override
            public void onRefresh() {
                SDKManager.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setLoadMore(false);
                        // 更新recyclerView
                        int itemNumber = recyclerAdapter.size();
                        recyclerAdapter.add(itemNumber, "Loaded Item + " + new Random().nextInt(300), true);
                        recyclerView.scrollToPosition(recyclerAdapter.getItemCount() - 1);
                    }
                }, 4000);
            }
        });

        recyclerAdapter.setDataList(StrConstant.getListSeven(20), true);

        findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeRefreshLayout.setRefreshing(true);
//                SDKManager.getHandler().postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        swipeRefreshLayout.setRefreshing(false);
//                        // 更新recyclerView
//                        recyclerAdapter.add(0, "Refresh Item + " + new Random().nextInt(300), true);
//                        recyclerView.scrollToPosition(0);
//                    }
//                }, 4000);
            }
        });
    }

    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, SimpleRefreshActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }
}
