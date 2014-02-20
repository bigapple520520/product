/* 
 * @(#)PointView.java    Created on 2014-2-20
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.tuya.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.xuan.tuya.R;

/**
 * 黑点
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-2-20 下午6:56:46 $
 */
public class PointView extends View {
    private Paint mPaint;
    private int strokeWidth;

    private int width;
    private int height;

    public PointView(Context context) {
        super(context);
        initPaint();
    }

    public PointView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public PointView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPaint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPoint(width / 2, height / 2, mPaint);
    }

    public void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.doodle_pan_black));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);// 设置外边缘
        mPaint.setStrokeCap(Paint.Cap.ROUND);// 形状
        mPaint.setStrokeWidth(8);// 画笔宽度
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;

        mPaint.setStrokeWidth(strokeWidth);
        invalidate();
    }

}
