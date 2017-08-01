package com.wtuadn.yrecyclerview.lor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.wtuadn.yrecyclerview.LoadRecyclerView;

import static android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;

/**
 * Created by wtuadn on 15-11-9.
 */
public class LoadOrRefreshView extends YSwipeRefreshLayout implements LoadRecyclerView.OnLoadListener, OnRefreshListener {
    protected LoadRecyclerView loadRecyclerView;
    private OnLORListener onLORListener;
    private boolean isRefreshing;
    private boolean isLoading;

    public void setOnLORListener(OnLORListener onLORListener) {
        this.onLORListener = onLORListener;
    }

    public LoadRecyclerView getLoadRecyclerView() {
        return loadRecyclerView;
    }

    public LoadOrRefreshView(Context context) {
        this(context, null);
    }

    public LoadOrRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        loadRecyclerView = new LoadRecyclerView(getContext());
        loadRecyclerView.setHasFixedSize(true);
        loadRecyclerView.setLoadListener(this);
        setOnRefreshListener(this);
        addView(loadRecyclerView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 结束load或者是refresh
     */
    public void finishLOR() {
        if (isRefreshing) {
            refreshFinish();
        } else {
            loadFinish();
        }
    }

    public void loadFinish() {
        if (isLoading) {
            loadRecyclerView.loadFinish();
            setEnabled(true);
            isLoading = false;
        }
    }

    @Override
    public void refreshFinish() {
        if (isRefreshing) {
            super.refreshFinish();
            loadRecyclerView.setCanLoad(true);
            isRefreshing = false;
        }
    }

    @Override
    public void onLoad() {
        isLoading = true;
        if (onLORListener != null) {
            setEnabled(false);
            onLORListener.onLoad(this);
        }
    }

    @Override
    public void onRefresh() {
        isRefreshing = true;
        loadRecyclerView.setCanLoad(false);
        if (onLORListener != null) {
            onLORListener.onRefresh(this);
        }
    }

    public void disable() {
        setEnabled(false);
        loadRecyclerView.setDisableLoad(false);
    }

    public interface OnLORListener {
        /**
         * 下拉刷新
         */
        void onRefresh(LoadOrRefreshView lor);

        /**
         * 上拉加载
         */
        void onLoad(LoadOrRefreshView lor);
    }
}
