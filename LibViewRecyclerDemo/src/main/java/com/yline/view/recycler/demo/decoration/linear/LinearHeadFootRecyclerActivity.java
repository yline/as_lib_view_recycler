package com.yline.view.recycler.demo.decoration.linear;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yline.view.recycler.demo.R;
import com.yline.view.recycler.demo.adapter.SimpleHeadFootRecyclerActivity;

public class LinearHeadFootRecyclerActivity extends SimpleHeadFootRecyclerActivity {
    public static void actionStart(Context context) {
        context.startActivity(new Intent(context, LinearHeadFootRecyclerActivity.class));
    }

    @Override
    protected void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new LinearItemDecoration(this) {
            @Override
            protected int getDivideResourceId() {
                return R.drawable.recycler_divider_black_normal;
            }
        });
    }
}
