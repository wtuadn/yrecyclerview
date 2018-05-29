package com.wtuadn.yrecyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.support.v7.widget.RecyclerView.ViewHolder;

/**
 * Created by wtuadn on 15-12-22.
 */
public abstract class RecyclerAdapter<T, VH extends ViewHolder> extends Adapter {
    private static final int BASE_HEADER_FOOTER_TYPE = 100000;

    protected RecyclerItemListener recyclerItemListener;
    private List<View> headerList;
    private List<View> footerList;
    protected int headerSize;
    protected int footerSize;

    public final void clearHeaderFooter() {
        headerSize = 0;
        footerSize = 0;
        if (headerList != null) headerList.clear();
        if (footerList != null) footerList.clear();
    }

    private boolean isHeaderPosition(int position) {
        return position > -1 && position < headerSize;
    }

    private boolean isFooterPosition(int position) {
        return position >= headerSize + getNormalItemCount() && position < getItemCount();
    }

    public final int getHeaderSize() {
        return headerSize;
    }

    public final int getFooterSize() {
        return footerSize;
    }

    public List<View> getFooterList() {
        if (footerList == null) {
            footerList = new ArrayList<>();
        }
        return footerList;
    }

    public List<View> getHeaderList() {
        if (headerList == null) {
            headerList = new ArrayList<>();
        }
        return headerList;
    }

    public final void addHeaderView(View view) {
        addHeaderView(view, headerSize, true);
    }

    public final void addHeaderView(View view, boolean notify) {
        addHeaderView(view, headerSize, notify);
    }

    public final void addHeaderView(View view, int position) {
        addHeaderView(view, position, true);
    }

    public final void addHeaderView(View view, int position, boolean notify) {
        if (headerList == null) {
            headerList = new ArrayList<>();
        }
        headerList.add(position, view);
        headerSize = headerList.size();
        if (notify) {
            notifyItemInserted(position);
        }
    }

    public final void addFooterView(View view) {
        addFooterView(view, 0, true);
    }

    public final void addFooterView(View view, boolean notify) {
        addFooterView(view, 0, notify);
    }

    public final void addFooterView(View view, int position) {
        addFooterView(view, position, true);
    }

    public final void addFooterView(View view, int position, boolean notify) {
        if (footerList == null) {
            footerList = new ArrayList<>();
        }
        footerList.add(position, view);
        footerSize = footerList.size();
        if (notify) {
            notifyItemInserted(headerSize + getNormalItemCount() + position);
        }
    }

    public final void removeHeaderView(View view) {
        if (headerList != null) {
            int position = headerList.indexOf(view);
            if (isHeaderPosition(position)) {
                headerList.remove(view);
                headerSize = headerList.size();
                notifyItemRemoved(position);
            }
        }
    }

    public final void removeHeaderView(int position) {
        if (headerList != null) {
            if (isHeaderPosition(position)) {
                headerList.remove(position);
                headerSize = headerList.size();
                notifyItemRemoved(position);
            }
        }
    }

    public final void removeFooterView(View view) {
        if (footerList != null) {
            int position = footerList.indexOf(view);
            if (position > -1 && position < footerSize) {
                footerList.remove(view);
                footerSize = footerList.size();
                notifyItemRemoved(headerSize + getNormalItemCount() + position);
            }
        }
    }

    public final void removeFooterView(int position) {
        if (footerList != null) {
            if (position > -1 && position < footerSize) {
                footerList.remove(position);
                footerSize = footerList.size();
                notifyItemRemoved(headerSize + getNormalItemCount() + position);
            }
        }
    }

    public final View getHeaderView(int position) {
        if (headerList != null) {
            return headerList.get(position);
        } else {
            throw new RuntimeException("header is null!");
        }
    }

    public final View getFooterView(int position) {
        if (footerList != null) {
            return footerList.get(position);
        } else {
            throw new RuntimeException("footer is null!");
        }
    }

    @Override
    public final int getItemCount() {
        return headerSize + getNormalItemCount() + footerSize;
    }

    @Override
    public final int getItemViewType(int position) {
        if (isHeaderPosition(position)) {
            return BASE_HEADER_FOOTER_TYPE + headerList.get(position).hashCode();//header
        } else if (isFooterPosition(position)) {
            return BASE_HEADER_FOOTER_TYPE + footerList.get(position - headerSize - getNormalItemCount()).hashCode();//footer
        } else {
            return getNormalItemViewType(position);//normal
        }
    }

    @Override
    public final ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (headerList != null) {
            for (View view : headerList) {
                if (BASE_HEADER_FOOTER_TYPE + view.hashCode() == viewType) {
                    return new HeaderFooterViewHolder(view);
                }
            }
        }
        if (footerList != null) {
            for (View view : footerList) {
                if (BASE_HEADER_FOOTER_TYPE + view.hashCode() == viewType) {
                    return new HeaderFooterViewHolder(view);
                }
            }
        }
        return onCreateNormalViewHolder(parent, viewType);
    }

    @Override
    public final void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        if (viewHolder instanceof RecyclerAdapter.HeaderFooterViewHolder) return;
        onBindNormalViewHolder((VH) viewHolder, position);
    }

    public abstract T getItem(int position);

    public abstract int getNormalItemCount();

    public int getNormalItemViewType(int position) {
        return 0;
    }

    @NonNull
    protected abstract VH onCreateNormalViewHolder(@NonNull ViewGroup parent, int viewType);

    protected abstract void onBindNormalViewHolder(@NonNull VH viewHolder, int position);


    public final void notifyNormalItemInserted(int position) {
        notifyItemInserted(headerSize + position);
    }

    public final void notifyNormalItemRemoved(int position) {
        notifyItemRemoved(headerSize + position);
    }

    public final void notifyNormalItemMoved(int fromPosition, int toPosition) {
        notifyItemMoved(headerSize + fromPosition, headerSize + toPosition);
    }

    public final void notifyNormalItemChanged(int position) {
        notifyItemChanged(headerSize + position);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup oldLookup = gridLayoutManager.getSpanSizeLookup();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isHeaderPosition(position) || isFooterPosition(position)) {
                        return gridLayoutManager.getSpanCount();
                    } else if (oldLookup != null) {
                        return oldLookup.getSpanSize(position);
                    }
                    return 1;
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        int position = holder.getLayoutPosition();
        if (isHeaderPosition(position) || isFooterPosition(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                ((StaggeredGridLayoutManager.LayoutParams) lp).setFullSpan(true);
            }
        }
    }

    private class HeaderFooterViewHolder extends ViewHolder {
        private HeaderFooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
