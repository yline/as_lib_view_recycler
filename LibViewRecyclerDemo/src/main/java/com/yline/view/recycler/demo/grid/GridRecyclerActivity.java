package com.yline.view.recycler.demo.grid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.yline.view.recycler.decoration.CommonGridDecoration;
import com.yline.view.recycler.demo.R;
import com.yline.view.recycler.demo.adapter.SimpleRecyclerActivity;

public class GridRecyclerActivity extends SimpleRecyclerActivity {
    public static void actionStart(Context context) {
        context.startActivity(new Intent(context, GridRecyclerActivity.class));
    }

    @Override
    protected void initRecyclerView(RecyclerView recyclerView) {
        super.initRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new CommonGridDecoration(this) {
            @Override
            protected int getDivideResourceId() {
                return R.drawable.recycler_divider_black_normal;
            }
        });
    }
}
