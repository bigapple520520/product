/* 
 * @(#)DoubleClickListener.java    Created on 2014-3-31
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.tuya.view;

import android.view.MotionEvent;

/**
 * 双击监听接口
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-3-31 下午5:53:08 $
 */
public interface DoubleClickListener {

    /**
     * 双击事件
     * 
     * @param fistEvent
     * @param secondEvent
     */
    void doubleClick(MotionEvent fistEvent, MotionEvent secondEvent);

}
