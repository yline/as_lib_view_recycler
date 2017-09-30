package com.yline.view.recycler.holder;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * RecyclerViewHolder 公共的
 *
 * @author yline 2017/5/23 -- 10:33
 * @version 1.0.0
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder implements Callback.IViewHolderCallback {
    private SparseArrayCompat<View> sArray;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        sArray = new SparseArrayCompat<>();
    }

    @Override
    public <T extends View> T get(int viewId) {
        if (sArray.get(viewId) == null) {
            View view = itemView.findViewById(viewId);
            sArray.put(viewId, view);
        }
        return (T) sArray.get(viewId);
    }

    @Override
    public View getItemView() {
        return this.itemView;
    }

    @Override
    public String getText(int viewId) {
        TextView textView = this.get(viewId);
        return textView.getText().toString();
    }

    @Override
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        this.get(viewId).setOnClickListener(listener);
    }

    @Override
    public TextView setText(int viewId, String content) {
        TextView textView = this.get(viewId);
        textView.setText(content);
        return textView;
    }

    @Override
    public ImageView setImageBackgroundResource(int viewId, int resId) {
        ImageView imageView = this.get(viewId);
        imageView.setBackgroundResource(resId);
        return imageView;
    }

    @Override
    public ImageView setImageResource(int viewId, int resId) {
        ImageView imageView = this.get(viewId);
        imageView.setImageResource(resId);
        return imageView;
    }

    @Override
    public ProgressBar setProgress(int viewId, int progress) {
        ProgressBar progressBar = this.get(viewId);
        progressBar.setProgress(progress);
        return progressBar;
    }
}
