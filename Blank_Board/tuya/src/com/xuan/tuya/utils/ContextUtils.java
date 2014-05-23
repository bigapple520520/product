/* 
 * @(#)ContextUtils.java    Created on 2012-5-7
 * Copyright (c) 2012 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.tuya.utils;

import android.os.Environment;

/**
 * 判断网络或者SD等之类的工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-3-25 上午9:22:02 $
 */
public abstract class ContextUtils {

    /**
     * SD卡是否可用
     * 
     * @return
     */
    public static boolean hasSdCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

}
