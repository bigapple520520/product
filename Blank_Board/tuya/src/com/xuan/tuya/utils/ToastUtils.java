/* 
 * @(#)ToastUtils.java    Created on 2011-5-31
 * Copyright (c) 2011 ZDSoft Networks, Inc. All rights reserved.
 * $Id: ToastUtils.java 31799 2012-10-25 04:59:34Z xuan $
 */
package com.xuan.tuya.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 吐司信息工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-3-25 下午7:40:05 $
 */
public abstract class ToastUtils {

    /**
     * 显示吐司信息（较短时间）
     * 
     * @param context
     * @param text
     */
    public static void displayTextShort(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
