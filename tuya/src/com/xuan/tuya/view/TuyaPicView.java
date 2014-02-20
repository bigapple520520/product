package com.xuan.tuya.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.xuan.tuya.R;

/**
 * 涂鸦图片view
 * 
 * @author xuan
 */
public class TuyaPicView extends View {
    private static final float TOUCH_TOLERANCE = 4;// 路径纪录长度，例如在滑动中，点xy的坐标超过临时点坐标的这个值，就画线
    private static final int DEFAULT_COLOR = R.color.doodle_blank_bg;

    private Bitmap mBgPicBitmap;

    private Bitmap mBitmap;
    private Canvas mCanvas;

    private Path mPath;// 用来画图形的，例如可以用他画一个三角形等等
    private Paint mPaint;// 真实的画笔
    private float mX, mY;// 临时点坐标

    private static List<DrawPath> savePath;// 保存Path路径的集合,用List集合来模拟栈
    private DrawPath mDrawPath;// 记录Path路径的对象

    private boolean isInit = false;// 用来标记保证只被初始化一次

    // 布局高和宽
    private int screenWidth = 500;
    private int screenHeight = 500;

    public TuyaPicView(Context context) {
        super(context);
    }

    public TuyaPicView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TuyaPicView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // 初始化
    private void init(int w, int h) {
        if (!isInit) {
            mBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
            mCanvas.drawColor(getResources().getColor(DEFAULT_COLOR));

            initPaint(getResources().getColor(R.color.doodle_pan_black), 8);

            savePath = new ArrayList<DrawPath>();
            isInit = true;
        }
    }

    /**
     * 初始化不同颜色的画笔
     * 
     * @param resId
     * @param strokeWidth
     */
    public void initPaint(int color, float strokeWidth) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);// 设置外边缘
        mPaint.setStrokeCap(Paint.Cap.ROUND);// 形状
        mPaint.setStrokeWidth(strokeWidth);// 画笔宽度
    }

    /**
     * 获取涂鸦好的图片
     * 
     * @return
     */
    public Bitmap getTuyaBitmap() {
        return mBitmap;
    }

    /**
     * 重新初始化背景图片
     * 
     * @param bgPicBitmap
     */
    public void initBgPicBitmap(Bitmap bgPicBitmap) {
        this.mBgPicBitmap = bgPicBitmap;
        savePath.clear();
        redrawOnBitmap();
    }

    /**
     * 获取画笔
     * 
     * @return
     */
    public Paint getPaint() {
        return mPaint;
    }

    /**
     * 撤销上一步画线<br>
     * 撤销的核心思想就是将画布清空，将保存下来的Path路径最后一个移除掉，重新将路径画在画布上面。
     */
    public void undo() {
        if (savePath != null && savePath.size() > 0) {
            savePath.remove(savePath.size() - 1);
            redrawOnBitmap();
        }
    }

    /**
     * 重做,就是清空所有的画线<br>
     * 核心思想就是，清空Path路径后，进行重新绘制
     */
    public void redo() {
        if (null != savePath && savePath.size() > 0) {
            savePath.clear();
            redrawOnBitmap();
        }
    }

    // 重新绘制Path中的画线路径
    private void redrawOnBitmap() {
        if (null == mBgPicBitmap) {
            mBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
            mCanvas.setBitmap(mBitmap);
            mCanvas.drawColor(getResources().getColor(DEFAULT_COLOR));
        }
        else {
            mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tuya_bg).copy(Bitmap.Config.ARGB_8888,
                    true);
            mCanvas.setBitmap(mBitmap);
        }

        for (DrawPath drawPath : savePath) {
            mCanvas.drawPath(drawPath.path, drawPath.paint);
        }

        invalidate();// 刷新
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, null);// 把图片显示到画布上

        if (null != mPath) {
            canvas.drawPath(mPath, mPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        screenWidth = w;
        screenHeight = h;
        init(screenWidth, screenHeight);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            mPath = new Path();
            mDrawPath = new DrawPath(mPath, mPaint);

            mPath.moveTo(x, y);
            mX = x;
            mY = y;
            break;
        case MotionEvent.ACTION_MOVE:
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                // 从（mX,mY）到（x,y）画一条贝塞尔曲线，更平滑(直接用mPath.lineTo也是可以的)
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
            }
            break;
        case MotionEvent.ACTION_UP:
            mPath.lineTo(mX, mY);
            mCanvas.drawPath(mPath, mPaint);

            savePath.add(mDrawPath);// 算一笔
            mPath = null;

            break;
        }

        invalidate();
        return true;
    }

    /**
     * 每条路径封装类
     * 
     * @author xuan
     */
    private class DrawPath {
        private DrawPath(Path path, Paint paint) {
            this.path = path;
            this.paint = paint;
        }

        public Path path;// 路径
        public Paint paint;// 画笔
    }

}
