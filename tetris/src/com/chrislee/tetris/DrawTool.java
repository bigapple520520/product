package com.chrislee.tetris;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * 绘图工具
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-1-13 下午7:28:47 $
 */
public class DrawTool {

    /**
     * 把图片绘制到画布上
     * 
     * @param canvas
     * @param bitmap
     * @param x
     * @param y
     */
    public static void paintImage(Canvas canvas, Bitmap bitmap, int x, int y) {
        canvas.drawBitmap(bitmap, x, y, null);
    }

}
