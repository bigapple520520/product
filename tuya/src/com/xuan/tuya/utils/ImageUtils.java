/* 
 * @(#)ImageUtils.java    Created on 2014-2-21
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.tuya.utils;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.xuan.tuya.common.Constants;

/**
 * 获取图片工具
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-2-21 上午9:24:28 $
 */
public class ImageUtils {

    /**
     * 相册获取图片
     * 
     * @param context
     * @param requestCode
     */
    public static void getImageFromMediaStore(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 去相机拍照返回图片
     * 
     * @param activity
     * @param requestCode
     */
    public static Uri getImageFromCamera(Activity activity, int requestCode) {
        FileUtils.createDir(new File(Constants.PIC_STORE_PATH));

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = Uri.fromFile(new File(Constants.CAMERA_PIC_FILENAME));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        activity.startActivityForResult(intent, requestCode);
        return uri;
    }

    /**
     * 加载图片，会压缩，防止内存溢出
     * 
     * @param filename
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromFile(String filename, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPurgeable = true;
        BitmapFactory.decodeFile(filename, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        try {
            return BitmapFactory.decodeFile(filename, options);
        }
        catch (OutOfMemoryError e) {
            LogUtils.e(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 计算缩小比例
     * 
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            }
            else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }

            final float totalPixels = width * height;
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

}
