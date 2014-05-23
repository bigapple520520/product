/* 
 * @(#)CutViewUtils.java    Created on 2014-4-2
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.tuya.utils;

import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.view.View;

/**
 * View截图工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-4-2 上午11:13:20 $
 */
public abstract class CutViewUtils {

    /**
     * view截图
     * 
     * @param view
     * @param saveFilePath
     */
    public static void cutView(View view, String saveFilePath, int bitmapWidth, int bitmapHeight) {
        if (null == view || TextUtils.isEmpty(saveFilePath)) {
            return;
        }

        Bitmap bitmap = null;
        FileOutputStream fileOutputStream = null;
        try {
            bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Config.ARGB_8888);
            Canvas canvas = new Canvas();
            canvas.setBitmap(bitmap);
            view.draw(canvas);

            fileOutputStream = new FileOutputStream(saveFilePath);
            bitmap.compress(CompressFormat.PNG, 100, fileOutputStream);
        }
        catch (Exception e) {
            LogUtils.e("view截图出错，原因：", e);
        }
        finally {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }

            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                }
                catch (IOException e) {
                    // Ignore
                }
            }
        }
    }

}
