package com.xuan.tuya.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.text.TextUtils;

/**
 * IO工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-9-4 下午7:22:40 $
 */
public abstract class IOUtils {
    /**
     * 把input流存入文件中
     * 
     * @param is
     * @param fileName
     */
    public static void inputStreamToFile(InputStream is, String fileName) {
        if (null == is || TextUtils.isEmpty(fileName)) {
            return;
        }

        File file = new File(fileName);
        File fileParent = file.getParentFile();
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[4 * 1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
        }
        catch (Exception e) {
            LogUtils.e("", e);
        }
        finally {
            closeQuietly(is);
        }
    }

    /**
     * 默默的关闭可关闭流
     * 
     * @param closeable
     */
    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        }
        catch (IOException ioe) {
            // ignore
        }
    }

}
