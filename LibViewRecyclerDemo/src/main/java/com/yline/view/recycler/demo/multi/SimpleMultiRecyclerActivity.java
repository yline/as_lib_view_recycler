package com.yline.view.recycler.demo.multi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yline.base.BaseAppCompatActivity;
import com.yline.test.StrConstant;
import com.yline.view.recycler.demo.R;
import com.yline.view.recycler.simple.SimpleMultiRecyclerAdapter;

/**
 * @author yline 2017/10/18 -- 14:50
 * @version 1.0.0
 */
public class SimpleMultiRecyclerActivity extends BaseAppCompatActivity {
    protected SimpleMultiRecyclerAdapter homeAdapter;

    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, SimpleMultiRecyclerActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_recycler);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        initRecyclerView(recyclerView);

        homeAdapter = new SimpleMultiRecyclerAdapter();
        recyclerView.setAdapter(homeAdapter);

        homeAdapter.setDataList(StrConstant.getListFive(20), true);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_recycler);
        tabLayout.setVisibility(View.GONE);
    }

    protected void initRecyclerView(RecyclerView recyclerView) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setSpanCount(3);
        recyclerView.setLayoutManager(gridLayoutManager);
    }
}
