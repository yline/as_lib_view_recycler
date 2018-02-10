package com.yline.view.recycler.demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yline.base.BaseAppCompatActivity;
import com.yline.view.recycler.demo.test.DataAdapterTest;
import com.yline.view.recycler.demo.R;
import com.yline.view.recycler.simple.SimpleRecyclerAdapter;

public class SimpleRecyclerActivity extends BaseAppCompatActivity {
    protected SimpleRecyclerAdapter homeAdapter;

    public static void actionStart(Context context) {
        context.startActivity(new Intent(context, SimpleRecyclerActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_recycler);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        initRecyclerView(recyclerView);

        homeAdapter = new SimpleRecyclerAdapter();
        recyclerView.setAdapter(homeAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_recycler);
        new DataAdapterTest(tabLayout, homeAdapter);
    }

    protected void initRecyclerView(RecyclerView recyclerView) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setSpanCount(3);
        recyclerView.setLayoutManager(gridLayoutManager);
    }
}
