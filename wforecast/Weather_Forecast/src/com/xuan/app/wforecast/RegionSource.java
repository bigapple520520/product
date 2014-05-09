/* 
 * @(#)RegionSource.java    Created on 2014-5-9
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.app.wforecast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

import android.content.Context;

import com.winupon.andframe.bigapple.utils.log.LogUtils;

/**
 * 地区资源
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-5-9 下午6:12:25 $
 */
public abstract class RegionSource {
    private static HashMap<String, String> source;

    /**
     * 获取地区资源
     * 
     * @param context
     * @return
     */
    public static HashMap<String, String> getRegionSourceMap(Context context) {
        if (null == source) {
            source = new HashMap<String, String>();
            readRegionSource(context);
        }

        source = new HashMap<String, String>();
        readRegionSource(context);
        return source;
    }

    // 读取地区资源
    private static void readRegionSource(Context context) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(
                    "regionSource.txt")));

            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] region = line.split("=");
                source.put(region[1], region[0]);
            }
        }
        catch (Exception e) {
            LogUtils.e("", e);
        }
    }

}
