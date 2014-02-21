/* 
 * @(#)FileUtils.java    Created on 2014-2-19
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.tuya.utils;

import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;

/**
 * 文件保存工具
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-2-19 上午11:51:05 $
 */
public abstract class FileUtils {

    /**
     * 保存图片
     * 
     * @param filePath
     * @param bitmap
     * @return
     */
    public static boolean saveToSDCard(String filePath, Bitmap bitmap) {
        if (!ContextUtils.hasSdCard()) {
            return false;
        }

        try {
            File file = new File(filePath);
            createDir(file.getParentFile());

            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        }
        catch (Exception e) {
            LogUtils.e("", e);
            return false;
        }

        return true;
    }

    /**
     * 文件夹不存在就创建
     * 
     * @param file
     */
    public static void createDir(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

}
