package com.xuan.tuya.common;

import android.os.Environment;

/**
 * 常量类
 * 
 * @author xuan
 */
public abstract class Constants {

    /**
     * 手机SD卡的路径
     */
    public static final String SDCARD = Environment.getExternalStorageDirectory().getPath();

    /**
     * 项目文件夹路径
     */
    public static final String PIC_STORE_PATH = SDCARD + "/tuya/";

    /**
     * 要编辑的图片放在这里
     */
    public static final String EDIT_PIC = PIC_STORE_PATH + "edit_pic.jpg";

    /**
     * 编辑图片临时存放目录
     */
    public static final String EDIT_PIC_TEMP = PIC_STORE_PATH + "edit_pic_temp.jpg";
}
