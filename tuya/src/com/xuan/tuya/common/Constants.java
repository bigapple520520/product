package com.xuan.tuya.common;

import android.os.Environment;

/**
 * 常量类
 * 
 * @author xuan
 */
public abstract class Constants {

    // 手机SD卡的路径
    public static final String SDCARD = Environment.getExternalStorageDirectory().getPath();

    // 涂鸦图片存放路劲
    public static final String PIC_STORE_PATH = SDCARD + "/tuya/";

}
