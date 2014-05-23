/* 
 * @(#)FileUtils.java    Created on 2014-2-19
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.tuya.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

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

    /**
     * 拷贝文件
     * 
     * @param srcFile
     * @param destFile
     * @throws IOException
     */
    public static void copyFile(File srcFile, File destFile) throws IOException {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel input = null;
        FileChannel output = null;
        try {
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(destFile);
            input = fis.getChannel();
            output = fos.getChannel();
            long size = input.size();
            long pos = 0;
            long count = 0;
            while (pos < size) {
                count = size - pos;
                pos += output.transferFrom(input, pos, count);
            }
        }
        finally {
            IOUtils.closeQuietly(output);
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(fis);
        }

        if (srcFile.length() != destFile.length()) {
            throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile + "'");
        }
    }

}
