/* 
 * @(#)AppApplication.java    Created on 2015-3-23
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.app.yiba;

import android.app.Application;

/**
 * 程序入口
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2015-3-23 下午6:47:25 $
 */
public class AppApplication extends Application {
    public static AppApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

}
