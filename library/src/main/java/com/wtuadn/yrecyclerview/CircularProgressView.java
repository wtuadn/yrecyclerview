package com.wtuadn.yrecyclerview;

import android.content.Context;
import android.support.v4.widget.CircularProgressDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by wtuadn on 2017/8/31.
 */
public class CircularProgressView extends ImageView {
    public static int[] DEFAULT_COLORS;

    private CircularProgressDrawable progressDrawable;

    public CircularProgressDrawable getProgressDrawable() {
        return progressDrawable;
    }

    public CircularProgressView(Context context) {
        this(context, null);
    }

    public CircularProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressDrawable = new CircularProgressDrawable(getContext());
        if (DEFAULT_COLORS == null) progressDrawable.setColorSchemeColors(0xffF4B83F);
        else progressDrawable.setColorSchemeColors(DEFAULT_COLORS);
        progressDrawable.setStyle(CircularProgressDrawable.LARGE);
        progressDrawable.setProgressRotation(1f);
        progressDrawable.setStartEndTrim(0f, 0.9f);
        progressDrawable.setArrowEnabled(false);
        progressDrawable.setAlpha(255);
        setImageDrawable(progressDrawable);
        setScaleType(ScaleType.CENTER_INSIDE);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        if (isShown()) {
            if (!progressDrawable.isRunning()) progressDrawable.start();
        } else {
            progressDrawable.stop();
        }
        super.onVisibilityChanged(changedView, visibility);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        progressDrawable.stop();
    }
}
