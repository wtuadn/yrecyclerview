package com.wtuadn.yrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewParent;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 获取holder，可以在此设置点击事件
 * Created by wtuadn on 2018/5/30.
 */
public abstract class HolderListener<VH extends RecyclerView.ViewHolder> implements RecyclerView.OnChildAttachStateChangeListener {
    private Class vhClass;

    @Override
    public void onChildViewAttachedToWindow(View view) {
        if (view.getParent() instanceof RecyclerView) {
            if (vhClass == null) {
                Type type = getClass().getGenericSuperclass();
                if (type instanceof ParameterizedType) {
                    Type[] params = ((ParameterizedType) type).getActualTypeArguments();
                    if (params[0] instanceof Class) {
                        vhClass = (Class) params[0];
                    }
                }
            }
            if (vhClass != null) {
                RecyclerView.ViewHolder viewHolder = ((RecyclerView) view.getParent()).getChildViewHolder(view);
                if (vhClass.isInstance(viewHolder)) onHolderAttach((VH) viewHolder);
            }
        }
    }

    @Override
    public void onChildViewDetachedFromWindow(View view) {
    }

    public abstract void onHolderAttach(VH holder);

    public static int getLayoutPosition(View view) {
        ViewParent parent = view.getParent();
        while (true) {
            if (parent instanceof RecyclerView)
                return ((RecyclerView) parent).getChildLayoutPosition(view);
            if (parent == null || !(parent instanceof View)) return -1;
            view = (View) parent;
            parent = parent.getParent();
        }
    }

    public static int getAdapterPosition(View view) {
        ViewParent parent = view.getParent();
        while (true) {
            if (parent instanceof RecyclerView)
                return ((RecyclerView) parent).getChildAdapterPosition(view);
            if (parent == null || !(parent instanceof View)) return -1;
            view = (View) parent;
            parent = parent.getParent();
        }
    }
}
