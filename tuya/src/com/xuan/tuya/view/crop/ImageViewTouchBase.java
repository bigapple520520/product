/* 
 * @(#)ImageViewTouchBase.java    Created on 2014-4-11
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.tuya.view.crop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * 触摸图片控件的基类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-4-11 下午3:31:58 $
 */
abstract public class ImageViewTouchBase extends ImageView {
    protected Matrix mBaseMatrix = new Matrix();// 原始
    protected Matrix mSuppMatrix = new Matrix();// 操作
    private Matrix mDisplayMatrix = new Matrix();// 显示=原始+操作

    private float[] mMatrixValues = new float[9];// 临时存放Matrix数据的数组

    // The current bitmap being displayed.
    protected Bitmap mBitmapDisplayed;

    // The thumbnail bitmap.
    protected Bitmap mThumbBitmap;

    // The full size bitmap which should be used once we start zooming.
    private Bitmap mFullBitmap;

    // True if the image is the thumbnail.
    protected boolean mBitmapIsThumbnail;

    // True if the user is zooming -- use the full size image
    protected boolean mIsZooming;

    // Paint to use to clear the "mPerfectFitBitmap"
    protected Paint mPaint = new Paint();

    static boolean sNewZoomControl = false;

    // 本控件的宽和高
    private int mThisWidth = -1;
    private int mThisHeight = -1;

    float mMaxZoom;

    protected Handler mHandler = new Handler();

    protected int mLastXTouchPos;
    protected int mLastYTouchPos;

    private Runnable mOnLayoutRunnable = null;

    static final float sPanRate = 7;
    static final float sScaleRate = 1.05F;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mThisWidth = right - left;
        mThisHeight = bottom - top;
        Runnable r = mOnLayoutRunnable;
        if (r != null) {
            mOnLayoutRunnable = null;
            r.run();
        }
        if (mBitmapDisplayed != null) {
            setBaseMatrix(mBitmapDisplayed, mBaseMatrix);
            setImageMatrix(getImageViewMatrix());
        }
    }

    protected boolean doesScrolling() {
        return true;
    }

    // Translate a given point through a given matrix.
    static private void translatePoint(Matrix matrix, float[] xy) {
        matrix.mapPoints(xy);
    }

    // Return the mapped x coordinate through the matrix.
    static int mapXPoint(Matrix matrix, int point) {
        // Matrix's mapPoints takes an array of x/y coordinates.
        // That's why we have to allocte an array of length two
        // even though we don't use the y coordinate.
        float[] xy = new float[2];
        xy[0] = point;
        xy[1] = 0F;
        matrix.mapPoints(xy);
        return (int) xy[0];
    }

    @Override
    public void setImageBitmap(Bitmap bitmap) {
        throw new NullPointerException();
    }

    public void setImageBitmap(Bitmap bitmap, boolean isThumbnail) {
        super.setImageBitmap(bitmap);
        Drawable d = getDrawable();
        if (d != null) {
            d.setDither(true);
        }
        mBitmapDisplayed = bitmap;
        mBitmapIsThumbnail = isThumbnail;
    }

    public void recycleBitmaps() {
        if (mFullBitmap != null) {
            mFullBitmap.recycle();
            mFullBitmap = null;
        }
        if (mThumbBitmap != null) {
            mThumbBitmap.recycle();
            mThumbBitmap = null;
        }

        // mBitmapDisplayed is either mPerfectFitBitmap or mFullBitmap (in the case of zooming)
        setImageBitmap(null, true);
    }

    public void clear() {
        mBitmapDisplayed = null;
        recycleBitmaps();
    }

    public void setImageBitmapResetBase(final Bitmap bitmap, final boolean resetSupp, final boolean isThumb) {
        final int viewWidth = getWidth();

        if (viewWidth <= 0) {
            mOnLayoutRunnable = new Runnable() {
                @Override
                public void run() {
                    setImageBitmapResetBase(bitmap, resetSupp, isThumb);
                }
            };
            return;
        }

        if (isThumb && mThumbBitmap != bitmap) {
            if (mThumbBitmap != null) {
                mThumbBitmap.recycle();
            }
            mThumbBitmap = bitmap;
        }
        else if (!isThumb && mFullBitmap != bitmap) {
            if (mFullBitmap != null) {
                mFullBitmap.recycle();
            }
            mFullBitmap = bitmap;
        }
        mBitmapIsThumbnail = isThumb;

        if (null != bitmap) {
            setScaleType(ImageView.ScaleType.MATRIX);
            setBaseMatrix(bitmap, mBaseMatrix);
            setImageBitmap(bitmap, isThumb);
        }
        else {
            mBaseMatrix.reset();
            setImageBitmap(null, isThumb);
        }

        if (resetSupp) {
            mSuppMatrix.reset();
        }
        setImageMatrix(getImageViewMatrix());
        mMaxZoom = maxZoom();
    }

    // Center as much as possible in one or both axis. Centering is
    // defined as follows: if the image is scaled down below the
    // view's dimensions then center it (literally). If the image
    // is scaled larger than the view and is translated out of view
    // then translate it back into view (i.e. eliminate black bars).
    protected void center(boolean vertical, boolean horizontal, boolean animate) {
        if (mBitmapDisplayed == null) {
            return;
        }

        Matrix m = getImageViewMatrix();

        float[] topLeft = new float[] { 0, 0 };
        float[] botRight = new float[] { mBitmapDisplayed.getWidth(), mBitmapDisplayed.getHeight() };

        translatePoint(m, topLeft);
        translatePoint(m, botRight);

        float height = botRight[1] - topLeft[1];
        float width = botRight[0] - topLeft[0];

        float deltaX = 0, deltaY = 0;

        if (vertical) {
            int viewHeight = getHeight();
            if (height < viewHeight) {
                deltaY = (viewHeight - height) / 2 - topLeft[1];
            }
            else if (topLeft[1] > 0) {
                deltaY = -topLeft[1];
            }
            else if (botRight[1] < viewHeight) {
                deltaY = getHeight() - botRight[1];
            }
        }

        if (horizontal) {
            int viewWidth = getWidth();
            if (width < viewWidth) {
                deltaX = (viewWidth - width) / 2 - topLeft[0];
            }
            else if (topLeft[0] > 0) {
                deltaX = -topLeft[0];
            }
            else if (botRight[0] < viewWidth) {
                deltaX = viewWidth - botRight[0];
            }
        }

        postTranslate(deltaX, deltaY);
        if (animate) {
            Animation a = new TranslateAnimation(-deltaX, 0, -deltaY, 0);
            a.setStartTime(SystemClock.elapsedRealtime());
            a.setDuration(250);
            setAnimation(a);
        }
        setImageMatrix(getImageViewMatrix());
    }

    public void copyFrom(ImageViewTouchBase other) {
        mSuppMatrix.set(other.mSuppMatrix);
        mBaseMatrix.set(other.mBaseMatrix);

        if (mThumbBitmap != null) {
            mThumbBitmap.recycle();
        }

        if (mFullBitmap != null) {
            mFullBitmap.recycle();
        }

        // copy the data
        mThumbBitmap = other.mThumbBitmap;
        mFullBitmap = null;

        if (other.mFullBitmap != null) {
            other.mFullBitmap.recycle();
        }

        // transfer "ownership"
        other.mThumbBitmap = null;
        other.mFullBitmap = null;
        other.mBitmapIsThumbnail = true;

        setImageMatrix(other.getImageMatrix());
        setScaleType(other.getScaleType());

        setImageBitmapResetBase(mThumbBitmap, true, true);
    }

    @Override
    public void setImageDrawable(android.graphics.drawable.Drawable d) {
        super.setImageDrawable(d);
    }

    public ImageViewTouchBase(Context context) {
        super(context);
        init();
    }

    public ImageViewTouchBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setScaleType(ImageView.ScaleType.MATRIX);
        mPaint.setDither(true);
        mPaint.setFilterBitmap(true);
    }

    // 获取Matrix的某个数值
    protected float getValue(Matrix matrix, int whichValue) {
        matrix.getValues(mMatrixValues);
        return mMatrixValues[whichValue];
    }

    // 获取Matrix的缩放因子
    protected float getScale(Matrix matrix) {
        return getValue(matrix, Matrix.MSCALE_X);
    }

    // 获取mSuppMatrix的缩放因子
    protected float getScale() {
        return getScale(mSuppMatrix);
    }

    // 获取mSuppMatrix的X平移因子
    protected float getTranslateX() {
        return getValue(mSuppMatrix, Matrix.MTRANS_X);
    }

    // 获取mSuppMatrix的Y平移因子
    protected float getTranslateY() {
        return getValue(mSuppMatrix, Matrix.MTRANS_Y);
    }

    // Setup the base matrix so that the image is centered and scaled properly.
    private void setBaseMatrix(Bitmap bitmap, Matrix matrix) {
        float viewWidth = getWidth();
        float viewHeight = getHeight();

        matrix.reset();
        float widthScale = Math.min(viewWidth / bitmap.getWidth(), 1.0f);
        float heightScale = Math.min(viewHeight / bitmap.getHeight(), 1.0f);
        float scale;
        if (widthScale > heightScale) {
            scale = heightScale;
        }
        else {
            scale = widthScale;
        }
        matrix.setScale(scale, scale);
        matrix.postTranslate((viewWidth - (bitmap.getWidth() * scale)) / 2F,
                (viewHeight - (bitmap.getHeight() * scale)) / 2F);
    }

    // 原始mBaseMatrix和最新mSuppMatrix的合成
    protected Matrix getImageViewMatrix() {
        mDisplayMatrix.set(mBaseMatrix);
        mDisplayMatrix.postConcat(mSuppMatrix);
        return mDisplayMatrix;
    }

    private void onZoom() {
        mIsZooming = true;
        if (mFullBitmap != null && mFullBitmap != mBitmapDisplayed) {
            setImageBitmapResetBase(mFullBitmap, false, mBitmapIsThumbnail);
        }
    }

    // Sets the maximum zoom, which is a scale relative to the base matrix. It is calculated to show
    // the image at 400% zoom regardless of screen or image orientation. If in the future we decode
    // the full 3 megapixel image, rather than the current 1024x768, this should be changed down to
    // 200%.
    protected float maxZoom() {
        if (mBitmapDisplayed == null) {
            return 1F;
        }

        float fw = (float) mBitmapDisplayed.getWidth() / mThisWidth;
        float fh = (float) mBitmapDisplayed.getHeight() / mThisHeight;
        float max = Math.max(fw, fh) * 4;
        return max;
    }

    protected void zoomTo(float scale, float centerX, float centerY) {
        if (scale > mMaxZoom) {
            scale = mMaxZoom;
        }
        onZoom();

        float oldScale = getScale();
        float deltaScale = scale / oldScale;

        mSuppMatrix.postScale(deltaScale, deltaScale, centerX, centerY);
        setImageMatrix(getImageViewMatrix());
        center(true, true, false);
    }

    protected void zoomTo(final float scale, final float centerX, final float centerY, final float durationMs) {
        final float incrementPerMs = (scale - getScale()) / durationMs;
        final float oldScale = getScale();
        final long startTime = System.currentTimeMillis();

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();
                float currentMs = Math.min(durationMs, now - startTime);
                float target = oldScale + (incrementPerMs * currentMs);
                zoomTo(target, centerX, centerY);

                if (currentMs < durationMs) {
                    mHandler.post(this);
                }
            }
        });
    }

    protected void zoomTo(float scale) {
        float width = getWidth();
        float height = getHeight();

        zoomTo(scale, width / 2F, height / 2F);
    }

    protected void zoomIn() {
        zoomIn(sScaleRate);
    }

    protected void zoomOut() {
        zoomOut(sScaleRate);
    }

    protected void zoomIn(float rate) {
        if (getScale() >= mMaxZoom) {
            return; // Don't let the user zoom into the molecular level.
        }
        if (mBitmapDisplayed == null) {
            return;
        }
        float width = getWidth();
        float height = getHeight();

        mSuppMatrix.postScale(rate, rate, width / 2F, height / 2F);
        setImageMatrix(getImageViewMatrix());

        onZoom();
    }

    protected void zoomOut(float rate) {
        if (mBitmapDisplayed == null) {
            return;
        }

        float width = getWidth();
        float height = getHeight();

        Matrix tmp = new Matrix(mSuppMatrix);
        tmp.postScale(1F / sScaleRate, 1F / sScaleRate, width / 2F, height / 2F);
        if (getScale(tmp) < 1F) {
            mSuppMatrix.setScale(1F, 1F, width / 2F, height / 2F);
        }
        else {
            mSuppMatrix.postScale(1F / rate, 1F / rate, width / 2F, height / 2F);
        }
        setImageMatrix(getImageViewMatrix());
        center(true, true, false);

        onZoom();
    }

    protected void postTranslate(float dx, float dy) {
        mSuppMatrix.postTranslate(dx, dy);
    }

    protected void panBy(float dx, float dy) {
        postTranslate(dx, dy);
        setImageMatrix(getImageViewMatrix());
    }

}
