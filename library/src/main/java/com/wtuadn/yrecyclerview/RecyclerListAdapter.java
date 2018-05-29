package com.wtuadn.yrecyclerview;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by wtuadn on 15-12-28.
 */
public abstract class RecyclerListAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerAdapter<T, VH>  {
    public List<T> lists;

    public RecyclerListAdapter(List<T> lists) {
        this.lists = lists;
    }

    @Override
    public int getNormalItemCount() {
        return lists.size();
    }

    public T getItem(int position) {
        if (position - headerSize < 0 || position - headerSize > getNormalItemCount()-1) {
            return null;
        }
        return lists.get(position - headerSize);
    }
}