package com.yline.view.recycler.test;

import com.yline.view.recycler.adapter.AbstractRecyclerAdapter;
import com.yline.view.recycler.holder.RecyclerViewHolder;


public class SimpleRecyclerAdapter extends AbstractRecyclerAdapter<String> {
    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, int position) {
        viewHolder.setText(android.R.id.text1, get(position));
    }

    @Override
    public int getItemRes() {
        return android.R.layout.simple_list_item_1;
    }
}
