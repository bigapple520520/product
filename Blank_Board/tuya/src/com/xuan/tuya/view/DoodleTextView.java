/* 
 * @(#)DoodleTextView.java    Created on 2014-4-2
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.tuya.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Scroller;

import com.xuan.tuya.utils.CutViewUtils;

/**
 * 涂鸦添加文本
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-4-2 下午1:26:00 $
 */
public class DoodleTextView extends ViewGroup {
    // 默认输入框的宽和高
    private static final int DEFAULT_INPUTWIDTH_DP = 100;
    private int inputWidth;
    private static final int DEFAULT_INPUTHEIGHT_DP = 50;
    private int inputHeight;

    // 默认文字的大小和颜色，可设置
    private static final int DEFAULT_TEXT_SIZE_SP = 13;
    private int textSize;
    private int textColor = Color.BLACK;

    private static final int DOUBLE_TIME = 500;
    private long lastDownTime;
    private MotionEvent lastDownEvent;

    private List<InputLayout> inputLayoutList = new ArrayList<InputLayout>();

    private boolean isEdit = false;// 是否编辑状态
    private float lastMotionX;
    private float lastMotionY;

    private ImageView bgImageView;// 添加文字的背景图

    private Scroller scroller;

    private int dragTempX;
    private int dragTempY;

    public DoodleTextView(Context context) {
        super(context);
        init();
    }

    public DoodleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DoodleTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        // 设置默认输入框大小
        final float density = getContext().getResources().getDisplayMetrics().density;
        inputWidth = (int) (DEFAULT_INPUTWIDTH_DP * density);
        inputHeight = (int) (DEFAULT_INPUTHEIGHT_DP * density);

        // 设置字体大小
        final float scaledDensity = getContext().getResources().getDisplayMetrics().scaledDensity;
        textSize = (int) (DEFAULT_TEXT_SIZE_SP * scaledDensity);

        // 可获取焦点
        setFocusable(true);
        setFocusableInTouchMode(true);

        scroller = new Scroller(getContext());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 1, n = getChildCount(); i < n; i++) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (null != bgImageView) {
            bgImageView.layout(0, 0, bgImageView.getBackground().getIntrinsicWidth(), bgImageView.getBackground()
                    .getIntrinsicHeight());
        }

        for (int i = 0, n = inputLayoutList.size(); i < n; i++) {
            InputLayout inputLayout = inputLayoutList.get(i);
            inputLayoutList.get(i).layout(inputLayout.getpLeft(), inputLayout.getpTop(), inputLayout.getpRight(),
                    inputLayout.getpBottom());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        requestFocus();

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            lastMotionX = x;
            lastMotionY = y;

            // 双击事件的处理
            long nowDownTime = System.currentTimeMillis();
            if (nowDownTime - lastDownTime < DOUBLE_TIME) {
                doubleClick(lastDownEvent, event);
                lastDownTime = 0;
                lastDownEvent = null;
            }
            else {
                lastDownTime = nowDownTime;
                lastDownEvent = event;
            }
            break;
        case MotionEvent.ACTION_MOVE:
            int deltaX = (int) (lastMotionX - x);
            int deltaY = (int) (lastMotionY - y);
            lastMotionX = x;
            lastMotionY = y;
            scrollBy(deltaX, deltaY);
            break;
        case MotionEvent.ACTION_UP:
            adjustPosition();
            break;
        }

        return true;
    }

    /**
     * 添加一个输入框
     * 
     * @param view
     * @param location
     */
    public void addInputLayout(InputLayout inputLayout) {
        inputLayoutList.add(inputLayout);
        addView(inputLayout);
    }

    /**
     * 重做，即清理所有添加的输入框
     */
    public void redo() {
        if (!inputLayoutList.isEmpty()) {
            for (int i = 0, n = inputLayoutList.size(); i < n; i++) {
                removeView(inputLayoutList.get(i));
            }
            inputLayoutList.clear();
            invalidate();
        }
    }

    /**
     * 撤销上一步
     */
    public void undo() {
        if (!inputLayoutList.isEmpty()) {
            int removePosition = inputLayoutList.size() - 1;
            InputLayout removedInputLayout = inputLayoutList.remove(removePosition);
            removeView(removedInputLayout);
            invalidate();
        }
    }

    /**
     * 添加背景图片
     * 
     * @param bitmap
     */
    public void initBgImageView(Bitmap bitmap) {
        bgImageView = new ImageView(getContext());
        bgImageView.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
        addView(bgImageView);
    }

    /**
     * 保存涂鸦结果到文件
     * 
     * @param fileName
     */
    public void cutViewToFile(String fileName) {
        for (InputLayout inputLayout : inputLayoutList) {
            inputLayout.getEditText().setBackgroundDrawable(null);
        }
        CutViewUtils.cutView(this, fileName, bgImageView.getBackground().getIntrinsicWidth(), bgImageView
                .getBackground().getIntrinsicHeight());

        // BitmapDrawable bd = (BitmapDrawable) bgImageView.getBackground();
        // Bitmap bitmap = bd.getBitmap().copy(Config.ARGB_8888, true);
        // Canvas canvas = new Canvas(bitmap);
        // Paint paint = new Paint();
        // for (InputLayout inputLayout : inputLayoutList) {
        // String text = inputLayout.getEditText().getEditableText().toString();
        // if (!TextUtils.isEmpty(text)) {
        // paint.setTextSize(inputLayout.getTextSize());
        // paint.setColor(inputLayout.getTextColor());
        // canvas.drawText(inputLayout.getEditText().getEditableText().toString(), inputLayout.getpLeft(),
        // inputLayout.getpTop(), paint);
        // }
        // }
        // FileUtils.saveToSDCard(fileName, bitmap);
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public int getTextSize() {
        return textSize;
    }

    /**
     * 设置文本的大小，单位是sp
     * 
     * @param textSize
     */
    public void setTextSizeSp(int textSize) {
        final float scaledDensity = getContext().getResources().getDisplayMetrics().scaledDensity;
        textSize = (int) (textSize * scaledDensity);
        this.textSize = textSize;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    // 双击事件，添加一个输入框
    private void doubleClick(MotionEvent firstEvent, MotionEvent secordEvent) {
        int startX = (int) (getScrollX() + secordEvent.getX());
        int startY = (int) (getScrollY() + secordEvent.getY());

        final InputLayout inputLayout = new InputLayout(getContext());
        inputLayout.setPosition(startX, startY, startX + inputWidth, startY + inputHeight);

        // 删除
        inputLayout.getCancelBtn().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                inputLayoutList.remove(inputLayout);
                removeView(inputLayout);
                invalidate();
            }
        });

        // 拖动事件
        inputLayout.getDragBtn().setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                int x = (int) event.getX();
                int y = (int) event.getY();

                switch (action) {
                case MotionEvent.ACTION_DOWN:
                    dragTempX = x;
                    dragTempY = y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int deltaX = x - dragTempX;
                    int deltaY = y - dragTempY;

                    inputLayout.setpRight(inputLayout.getRight() + deltaX);
                    inputLayout.setpBottom(inputLayout.getBottom() + deltaY);

                    dragTempX = x;
                    dragTempY = y;
                    requestLayout();
                    break;
                }

                return true;
            }
        });

        // 设置字体大小和颜色
        inputLayout.getEditText().setTextSize(textSize);
        inputLayout.getEditText().setTextColor(textColor);
        inputLayout.setTextSize(textSize);
        inputLayout.setTextColor(textColor);

        addInputLayout(inputLayout);
    }

    // 调整图片位置
    private void adjustPosition() {
        int rangeX = bgImageView.getBackground().getIntrinsicWidth() - getWidth();
        int rangeY = bgImageView.getBackground().getIntrinsicHeight() - getHeight();

        int scrollX = getScrollX();
        int scrollY = getScrollY();
        if (scrollX < 0) {
            scrollX = 0;
        }
        else if (scrollX > 0 && scrollX > rangeX) {
            scrollX = rangeX < 0 ? 0 : rangeX;
        }

        if (scrollY < 0) {
            scrollY = 0;
        }
        else if (scrollY > 0 && scrollY > rangeY) {
            scrollY = rangeY < 0 ? 0 : rangeY;
        }

        int moveDeltaX = scrollX - getScrollX();
        int moveDeltaY = scrollY - getScrollY();
        int durDistance = Math.max(Math.abs(moveDeltaX), Math.abs(moveDeltaY));

        scroller.startScroll(getScrollX(), getScrollY(), moveDeltaX, moveDeltaY, Math.abs(durDistance) * 2);
        invalidate();
    }

}
