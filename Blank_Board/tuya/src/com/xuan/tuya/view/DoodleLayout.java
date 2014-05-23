/* 
 * @(#)DoodleLayout.java    Created on 2014-4-2
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.tuya.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 涂鸦图片滚动布局
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-4-2 下午8:01:23 $
 */
public class DoodleLayout extends ViewGroup {
    private View childView;

    public DoodleLayout(Context context) {
        super(context);
    }

    public DoodleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DoodleLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() == 1) {
            measureChild(getChildAt(0), widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() == 1) {
            View childView = getChildAt(0);
            childView.layout(0, 0, childView.getWidth(), childView.getHeight());
        }
    }

    public View getChildView() {
        return childView;
    }

    public void setChildView(View childView) {
        this.childView = childView;
    }

}
