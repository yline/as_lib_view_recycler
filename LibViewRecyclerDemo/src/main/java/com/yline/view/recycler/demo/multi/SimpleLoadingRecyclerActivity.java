package com.yline.view.recycler.demo.multi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yline.application.SDKManager;
import com.yline.base.BaseAppCompatActivity;
import com.yline.test.StrConstant;
import com.yline.view.recycler.adapter.AbstractHeadFootRecyclerAdapter;
import com.yline.view.recycler.demo.R;
import com.yline.view.recycler.holder.RecyclerViewHolder;

public class SimpleLoadingRecyclerActivity extends BaseAppCompatActivity {
    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, SimpleLoadingRecyclerActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    private SimpleLoadingRecyclerAdapter mLoadingRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_loading);

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SDKManager.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mLoadingRecyclerAdapter = new SimpleLoadingRecyclerAdapter(this){
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
        recyclerView.setAdapter(mLoadingRecyclerAdapter);

        mLoadingRecyclerAdapter.setDataList(StrConstant.getListRandom(25), true);
    }

    private class SimpleLoadingRecyclerAdapter extends AbstractHeadFootRecyclerAdapter<String> {
        public SimpleLoadingRecyclerAdapter(Context context) {
            super(context);
        }

        @Override
        public int getItemRes() {
            return android.R.layout.simple_list_item_1;
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            holder.setText(android.R.id.text1, get(position));
        }
    }
}
