package com.yline.view.recycler.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yline.test.BaseTestActivity;
import com.yline.view.recycler.demo.adapter.SimpleHeadFootRecyclerActivity;
import com.yline.view.recycler.demo.adapter.SimpleListActivity;
import com.yline.view.recycler.demo.adapter.SimpleRecyclerActivity;
import com.yline.view.recycler.demo.grid.GridHeadFootRecyclerActivity;
import com.yline.view.recycler.demo.grid.GridRecyclerActivity;
import com.yline.view.recycler.demo.linear.LinearHeadFootRecyclerActivity;
import com.yline.view.recycler.demo.linear.LinearRecyclerActivity;
import com.yline.view.recycler.demo.multi.SimpleMultiRecyclerActivity;
import com.yline.view.recycler.demo.refresh.CircleProgressActivity;
import com.yline.view.recycler.demo.refresh.CustomRefreshActivity;
import com.yline.view.recycler.demo.refresh.SimpleRefreshActivity;
import com.yline.view.recycler.demo.test.MultiLevelActivity;

public class MainActivity extends BaseTestActivity {
    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, MainActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addTextView("Simple 系列，无ItemDecoration");
        addButton("SimpleListAdapter", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleListActivity.actionStart(MainActivity.this);
            }
        });

        addButton("SimpleCommonRecyclerAdapter", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleRecyclerActivity.actionStart(MainActivity.this);
            }
        });

        addButton("SimpleHeadFootRecyclerActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleHeadFootRecyclerActivity.actionStart(MainActivity.this);
            }
        });

        addTextView("Linear ItemDecoration 系列");
        addButton("LinearRecyclerActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearRecyclerActivity.actionStart(MainActivity.this);
            }
        });

        addButton("LinearHeadFootRecyclerActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearHeadFootRecyclerActivity.actionStart(MainActivity.this);
            }
        });

        addTextView("Grid ItemDecoration 系列");
        addButton("GridRecyclerActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GridRecyclerActivity.actionStart(MainActivity.this);
            }
        });

        addButton("GridHeadFootRecyclerActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GridHeadFootRecyclerActivity.actionStart(MainActivity.this);
            }
        });

        addTextView("Refresh 系列");
        addButton("CircleProgressBar", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CircleProgressActivity.launcher(MainActivity.this);
            }
        });

        addButton("SimpleRefresh", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleRefreshActivity.launcher(MainActivity.this);
            }
        });

        addButton("CustomRefresh", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomRefreshActivity.launcher(MainActivity.this);
            }
        });

        addButton("SimpleMultiRecyclerAdapter", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleMultiRecyclerActivity.launcher(MainActivity.this);
            }
        });

        addTextView("Test 系列");
        addButton("三级列表", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiLevelActivity.launcher(MainActivity.this);
            }
        });
    }
}
