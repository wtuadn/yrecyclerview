package com.wtuadn.yrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wtuadn on 15-12-28.
 */
public class YRecyclerView extends RecyclerView {
    private View emptyView;
    private boolean hasToAdd;
    private EmptyAdapterDataObserver innerDataObserver;
    private List<View> headerList;
    private List<View> footerList;

    public YRecyclerView(Context context) {
        this(context, null);
    }

    public YRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter == null) {
            super.setAdapter(null);
            return;
        }
        if (!(adapter instanceof RecyclerAdapter)) {
            throw new RuntimeException("Unsupported Adapter used. Valid one is RecyclerAdapter！");
        }
        if (innerDataObserver == null) {
            innerDataObserver = new EmptyAdapterDataObserver();
        }
        Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(innerDataObserver);
        }
        super.setAdapter(adapter);
        adapter.registerAdapterDataObserver(innerDataObserver);

        checkIfEmpty();
    }

    /**
     * @param emptyView 数据为空时显示的view
     * @param hasToAdd  是否直接添加到recyclerView中，为false的话emptyView得添加到其他的viewGroup中
     */
    public void registerEmptyView(View emptyView, boolean hasToAdd) {
        this.emptyView = emptyView;
        this.hasToAdd = hasToAdd;
        if (getMeasuredWidth() > 0)
            checkIfEmpty();
    }

    private void checkIfEmpty() {
        RecyclerAdapter adapter = (RecyclerAdapter) getAdapter();
        if (emptyView != null && adapter != null) {
            final boolean emptyViewVisible = adapter.getNormalItemCount() == 0;
            if (hasToAdd) {
                if (headerList == null) headerList = new ArrayList<>(3);
                if (footerList == null) footerList = new ArrayList<>(1);
                if (emptyViewVisible) {
                    if (!adapter.getFooterList().contains(emptyView)) {
                        headerList.addAll(adapter.getHeaderList());
                        footerList.addAll(adapter.getFooterList());
                        adapter.clearHeaderFooter();
                        adapter.addFooterView(emptyView, false);
                        adapter.notifyDataSetChanged();
                    }
                } else if (adapter.getFooterList().contains(emptyView)) {
                    adapter.clearHeaderFooter();
                    for (int i = 0; i < headerList.size(); i++) {
                        adapter.addHeaderView(headerList.get(i), false);
                    }
                    for (int i = 0; i < footerList.size(); i++) {
                        adapter.addFooterView(footerList.get(i), false);
                    }
                    headerList.clear();
                    footerList.clear();
                    adapter.notifyDataSetChanged();
                }
            } else {
                setVisibility(emptyViewVisible ? GONE : VISIBLE);
                emptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);
            }
        }
    }

    private class EmptyAdapterDataObserver extends AdapterDataObserver {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    }
}
