/* 
 * @(#)ImageUtils.java    Created on 2014-2-21
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.tuya.utils;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
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
        Uri uri = Uri.fromFile(new File(Constants.EDIT_PIC_TEMP));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        activity.startActivityForResult(intent, requestCode);
        return uri;
    }

    /**
     * 裁剪图片
     * 
     * @param activity
     * @param uri
     * @param requestCode
     * @param output
     */
    public static void getCutImage(Activity activity, Uri uri, int requestCode, String output) {
        if (null == uri) {
            return;
        }

        // 如果没有文件夹，先创建之
        File file = new File(output);
        File fileDir = file.getParentFile();
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.putExtra("return-data", false);

        activity.startActivityForResult(intent, requestCode);
    }

}
