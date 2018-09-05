package com.yline.view.recycler.demo.decoration.grid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.yline.view.recycler.demo.R;
import com.yline.view.recycler.demo.adapter.SimpleHeadFootRecyclerActivity;

public class GridHeadFootRecyclerActivity extends SimpleHeadFootRecyclerActivity {
    public static void actionStart(Context context) {
        context.startActivity(new Intent(context, GridHeadFootRecyclerActivity.class));
    }

    @Override
    protected void initRecyclerView(RecyclerView recyclerView) {
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.addItemDecoration(new GridItemDecoration(this) {
            @Override
            public int getDivideResourceId() {
                return R.drawable.recycler_divider_black_normal;
            }

            @Override
            public boolean[] isSpanDraw() {
                return new boolean[]{false, false, false, false};
            }
        });
    }
}
