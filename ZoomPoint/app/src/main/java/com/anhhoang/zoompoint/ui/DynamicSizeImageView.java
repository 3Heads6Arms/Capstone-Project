package com.anhhoang.zoompoint.ui;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by anh.hoang on 21.12.17.
 */

public class DynamicSizeImageView extends AppCompatImageView {
    private float aspectRatio = 1.78f;
    private boolean hasMaxHeight;

    public DynamicSizeImageView(Context context) {
        super(context);
    }

    public DynamicSizeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicSizeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        hasMaxHeight = getMaxHeight() != Integer.MAX_VALUE;

        int measuredWidth = getMeasuredWidth();
        int measuredHeight = (int) (measuredWidth / aspectRatio);

        if (hasMaxHeight) {
            measuredHeight = getMaxHeight();
            measuredWidth = (int) (measuredHeight * aspectRatio);
        }

        setMeasuredDimension(measuredWidth, measuredHeight);
    }
}
