package com.wtuadn.yrecyclerview;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;

/**
 * Created by wtu on 15/12/29.
 */
public abstract class RecyclerCursorAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerAdapter<T, VH> {
    protected Cursor mCursor;

    public Cursor getCursor() {
        return mCursor;
    }

    @Override
    public int getNormalItemCount() {
        return mCursor.getCount();
    }

    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null && !old.isClosed()) {
            old.close();
        }
    }

    private Cursor swapCursor(Cursor cursor) {
        if (mCursor == cursor) {
            return null;
        }
        Cursor oldCursor = mCursor;
        this.mCursor = cursor;
        return oldCursor;
    }

    public void moveCursorToPosition(int position) {
        mCursor.moveToPosition(position - headerSize);
    }
}
