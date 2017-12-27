package com.anhhoang.zoompoint.ui;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by anh.hoang on 23.12.17.
 */

public class ItemSpacingDecoration extends RecyclerView.ItemDecoration {
    private final boolean isHorizontal;
    private int spacing;

    public ItemSpacingDecoration(int spacing, boolean isHorizontal) {
        this.spacing = spacing;
        this.isHorizontal = isHorizontal;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

        boolean isGridLike = false;
        boolean isStart = false;

        if (layoutManager instanceof StaggeredGridLayoutManager) {
            isGridLike = ((StaggeredGridLayoutManager) layoutManager).getSpanCount() > 1;
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
            isStart = layoutParams.getSpanIndex() == 0;
        } else if (layoutManager instanceof GridLayoutManager) {
            isGridLike = ((GridLayoutManager) layoutManager).getSpanCount() > 1;
            GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();
            isStart = layoutParams.getSpanIndex() == 0;
        }

        // If is not Grid or first column of the grid then shouldn't add padding to start/left,
        // might break symmetry that is set from parent if do
        if ((!isGridLike && !isHorizontal) || isStart) {
            outRect.left = 0;
        } else {
            outRect.left = spacing;
        }

        outRect.bottom = isHorizontal ? 0 : spacing;
        outRect.right = 0;
        outRect.top = 0;
    }
}
