package com.yline.view.recycler.demo.refresh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yline.base.BaseAppCompatActivity;
import com.yline.test.StrConstant;
import com.yline.view.recycler.demo.R;
import com.yline.view.recycler.refresh.SuperSwipeRefreshLayout;
import com.yline.view.recycler.simple.SimpleHeadFootRecyclerAdapter;

import java.util.Random;

public class SimpleRefreshActivity extends BaseAppCompatActivity {
    private SuperSwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerView;

    private SimpleHeadFootRecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_refresh);

        swipeRefreshLayout = (SuperSwipeRefreshLayout) findViewById(R.id.super_swipe_refresh);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_super_swipe);
        recyclerAdapter = new SimpleHeadFootRecyclerAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SuperSwipeRefreshLayout.OnSwipeListener() {
            @Override
            public void onAnimate() {
                new Handler().postDelayed(new Runnable() {

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
        swipeRefreshLayout.setOnLoadListener(new SuperSwipeRefreshLayout.OnSwipeListener() {
            @Override
            public void onAnimate() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setLoadMore(false);
                        // 更新recyclerView
                        int itemNumber = recyclerAdapter.getItemCount();
                        recyclerAdapter.add(itemNumber, "Loaded Item + " + new Random().nextInt(300), true);
                        recyclerView.scrollToPosition(itemNumber);
                    }
                }, 4000);
            }
        });

        recyclerAdapter.setDataList(StrConstant.getListSeven(20), true);
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
