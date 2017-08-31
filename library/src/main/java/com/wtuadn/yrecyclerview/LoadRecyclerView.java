package com.wtuadn.yrecyclerview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by wtuadn on 15-12-29.
 */
public class LoadRecyclerView extends YRecyclerView {
    private MaterialProgressView progressView;
    private boolean canLoad = false;//能否上拉加载
    private boolean disableLoad = false;//彻底关闭上拉加载功能
    private boolean isLoading;
    private OnLoadListener onLoadListener;
    private int extraLayoutSpace;

    public void setExtraLayoutSpace(int extraLayoutSpace) {
        this.extraLayoutSpace = extraLayoutSpace;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setDisableLoad(boolean disableLoad) {
        this.disableLoad = disableLoad;
        if (disableLoad) {
            progressView.setVisibility(GONE);
        }
    }

    public boolean isCanLoad() {
        return canLoad;
    }

    public void setCanLoad(boolean mCanLoad) {
        if (this.canLoad != mCanLoad) {
            if (mCanLoad && isLoading && !disableLoad) {
                progressView.setVisibility(VISIBLE);
            } else {
                progressView.setVisibility(GONE);
            }
            this.canLoad = mCanLoad;
        }
    }

    public void setLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    public void setColorSchemeColors(int... colors) {
        progressView.getProgressDrawable().setColorSchemeColors(colors);
    }

    public LoadRecyclerView(Context context) {
        this(context, null);
    }

    public LoadRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        addOnScrollListener(new LoadOnScrollListener());
        setLayoutManager(new ExtraLinearLayoutManager(getContext()));
        initProgressBar();
    }

    private void initProgressBar() {
        progressView = new MaterialProgressView(getContext());
        progressView.setVisibility(GONE);
    }

    public void loadFinish() {
        if (isLoading) {
            isLoading = false;
        }
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter instanceof RecyclerAdapter) {
            super.setAdapter(adapter);
            ((RecyclerAdapter) adapter).removeFooterView(progressView);
            ((RecyclerAdapter) adapter).addFooterView(progressView);
        } else {
            throw new RuntimeException("Unsupported Adapter used. Valid one is RecyclerAdapter！");
        }
    }

    @Override
    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        if (layout instanceof LinearLayoutManager) {
            super.setLayoutManager(layout);
        } else {
            throw new RuntimeException("Unsupported LayoutManager used. Valid one is LinearLayoutManager！");
        }
    }

    private class LoadOnScrollListener extends RecyclerView.OnScrollListener {
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
                    progressView.setVisibility(VISIBLE);
                    onLoadListener.onLoad();
                }
            }
        }
    }

    private class ExtraLinearLayoutManager extends LinearLayoutManager {
        private ExtraLinearLayoutManager(Context context) {
            super(context);
        }

        @Override
        protected int getExtraLayoutSpace(RecyclerView.State state) {
            return super.getExtraLayoutSpace(state) + extraLayoutSpace;
        }
    }

    public interface OnLoadListener {
        void onLoad();
    }
}
