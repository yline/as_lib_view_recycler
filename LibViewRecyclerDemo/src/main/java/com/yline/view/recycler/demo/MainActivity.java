package com.yline.view.recycler.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yline.test.BaseTestActivity;
import com.yline.utils.LogUtil;
import com.yline.view.recycler.demo.adapter.SimpleHeadFootRecyclerActivity;
import com.yline.view.recycler.demo.adapter.SimpleListActivity;
import com.yline.view.recycler.demo.adapter.SimpleRecyclerActivity;
import com.yline.view.recycler.demo.grid.GridHeadFootRecyclerActivity;
import com.yline.view.recycler.demo.grid.GridRecyclerActivity;
import com.yline.view.recycler.demo.linear.LinearHeadFootRecyclerActivity;
import com.yline.view.recycler.demo.linear.LinearRecyclerActivity;
import com.yline.view.recycler.demo.multi.SimpleLoadingRecyclerActivity;
import com.yline.view.recycler.demo.multi.SimpleMultiRecyclerActivity;
import com.yline.view.recycler.demo.refresh.CircleProgressActivity;
import com.yline.view.recycler.demo.refresh.CustomRefreshActivity;
import com.yline.view.recycler.demo.refresh.SimpleRefreshActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Set s;
        HashSet hs;

        Integer i;
        Byte bb;
        Short sss;
        Character c;

        Long l;

        Boolean b;
        Float f;
        Double d;
        String ss;
        HashMap h;
        Object o;

        addButton("test", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> dataList = new LinkedList<String>();
                for (int i = 0; i < 10; i++) {
                    dataList.add(String.valueOf(i));
                }

                List<String> dataList2 = new ArrayList<>(dataList);

                LogUtil.v("one = " + dataList.hashCode() + ", two = " + dataList2.hashCode() + ", isSame = " + (dataList.hashCode() == dataList2.hashCode()));
            }
        });
    }

    public int hashCode2(String str) {
        int h = 0;
        for (int i = 0; i < str.length(); i++) {
            h = 31 * h + str.charAt(i);
        }
        return h;
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

        addButton("SimpleRecyclerAdapter", new View.OnClickListener() {
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

        addButton("SimpleLoadingRecyclerAdapter", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleLoadingRecyclerActivity.launcher(MainActivity.this);
            }
        });

        addTextView("Test 系列");
    }
}
