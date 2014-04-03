/* 
 * @(#)DoodleTextView.java    Created on 2014-4-2
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.tuya.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * 涂鸦添加文本
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-4-2 下午1:26:00 $
 */
public class DoodleTextView extends ViewGroup {
    private static final int DOUBLE_TIME = 500;
    private long lastDownTime;
    private MotionEvent lastDownEvent;

    private DoubleClickListener doubleClickListener;

    public DoodleTextView(Context context) {
        super(context);
    }

    public DoodleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DoodleTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            // 双击事件的处理
            long nowDownTime = System.currentTimeMillis();
            if (nowDownTime - lastDownTime < DOUBLE_TIME) {
                if (null != doubleClickListener) {
                    doubleClickListener.doubleClick(lastDownEvent, event);
                }

                lastDownTime = 0;
                lastDownEvent = null;
            }
            else {
                lastDownTime = nowDownTime;
                lastDownEvent = event;
            }
            break;
        case MotionEvent.ACTION_MOVE:

            break;
        case MotionEvent.ACTION_UP:

            break;
        }

        return true;
    }

    public DoubleClickListener getDoubleClickListener() {
        return doubleClickListener;
    }

    public void setDoubleClickListener(DoubleClickListener doubleClickListener) {
        this.doubleClickListener = doubleClickListener;
    }

}
