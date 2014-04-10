/* 
 * @(#)DialogUtils.java    Created on 2014-4-10
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.tuya.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;

/**
 * 对话框工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-4-10 上午11:27:14 $
 */
public abstract class DialogUtils {

    /**
     * 返回按钮提醒
     * 
     * @param activity
     */
    public static void returnAlert(final Activity activity) {
        Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(true);
        builder.setTitle("提示");
        builder.setMessage("返回后你的当前操作将不被保存，确定你要返回？");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

}
