package com.wtuadn.yrecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by wtuadn on 2017/8/31.
 */
public class MaterialProgressView extends ImageView {
    private MaterialProgressDrawable progressDrawable;

    public MaterialProgressDrawable getProgressDrawable() {
        return progressDrawable;
    }

    public MaterialProgressView(Context context) {
        this(context, null);
    }

    public MaterialProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressDrawable = new MaterialProgressDrawable(getContext(), this);
        progressDrawable.setColorSchemeColors(0xffff9500);
        progressDrawable.updateSizes(MaterialProgressDrawable.LARGE);
        progressDrawable.setProgressRotation(1f);
        progressDrawable.setStartEndTrim(0f, 0.9f);
        progressDrawable.showArrow(false);
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
