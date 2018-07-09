package com.wtuadn.yrecyclerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;

/**
 * Created by wtuadn on 15-12-29.
 */
public class LoadRecyclerView extends YRecyclerView {
    private CircularProgressView progressView;
    private boolean canLoad = false;//能否上拉加载
    private boolean disableLoad = false;//彻底关闭上拉加载功能
    private boolean isLoading;
    private OnLoadListener onLoadListener;
    private int[] colors;

    public boolean isLoading() {
        return isLoading;
    }

    public CircularProgressView getProgressView() {
        return progressView;
    }

    public void setDisableLoad(boolean disableLoad) {
        this.disableLoad = disableLoad;
        if (disableLoad) {
//            hideProgress();
        }
    }

    public boolean isCanLoad() {
        return canLoad;
    }

    public void setCanLoad(boolean mCanLoad) {
        this.canLoad = mCanLoad;
        if (canLoad) showProgress();
        else hideProgress();
    }

    public void setLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    public void setColorSchemeColors(int... colors) {
        this.colors = colors;
    }

    public LoadRecyclerView(Context context) {
        this(context, null);
    }

    public LoadRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addOnScrollListener(new LoadOnScrollListener());
    }

    public void loadFinish() {
        if (isLoading) {
            isLoading = false;
        }
    }

    @Override
    public void setLayoutManager(final LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup oldLookup = gridLayoutManager.getSpanSizeLookup();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    RecyclerAdapter adapter = (RecyclerAdapter) getAdapter();
                    if (position < adapter.headerSize || position >= adapter.headerSize + adapter.getNormalItemCount()) {
                        return gridLayoutManager.getSpanCount();
                    } else if (oldLookup != null) {
                        return oldLookup.getSpanSize(position);
                    }
                    return 1;
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
            super.setLayoutManager(layoutManager);
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {

        }
        super.setLayoutManager(layoutManager);
    }

    private void showProgress() {
        if (progressView == null) {
            progressView = new CircularProgressView(getContext());
            progressView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        if (colors != null) progressView.getProgressDrawable().setColorSchemeColors(colors);
        RecyclerAdapter adapter = (RecyclerAdapter) getAdapter();
        if (adapter.getFooterList() != null && !adapter.getFooterList().contains(progressView)) {
            int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
            ViewGroup.LayoutParams lp = progressView.getLayoutParams();
            if (((LinearLayoutManager) getLayoutManager()).getOrientation() == LinearLayoutManager.VERTICAL) {
                lp.width = getWidth();
                lp.height = size;
                progressView.setLayoutParams(lp);
            } else {
                lp.width = size;
                lp.height = getHeight();
            }
            progressView.setLayoutParams(lp);
            adapter.addFooterView(progressView, adapter.getFooterSize());
        }
    }

    private void hideProgress() {
        ((RecyclerAdapter) getAdapter()).removeFooterView(progressView);
    }

    private class LoadOnScrollListener extends OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            if (totalItemCount > visibleItemCount && lastVisibleItem >= totalItemCount - 2) {
                if (!disableLoad && canLoad && onLoadListener != null && !isLoading) {
                    isLoading = true;
                    showProgress();
                    onLoadListener.onLoad();
                }
            }
        }
    }

    public interface OnLoadListener {
        void onLoad();
    }
}
