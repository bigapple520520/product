/* 
 * @(#)ImageActivity.java    Created on 2014-5-27
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.tuya;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.dazzle.bigappleui.view.ZoomImageView;
import com.winupon.andframe.bigapple.utils.Validators;
import com.xuan.tuya.ioc.InjectView;
import com.xuan.tuya.utils.BitmapDecoder;

/**
 * 显示大图
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-5-27 上午9:30:15 $
 */
public class ImageActivity extends BasicActivity {
    @InjectView(R.id.zoomImageView)
    private ZoomImageView zoomImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_activity);

        String filePath = getIntent().getStringExtra("image.path");
        if (Validators.isEmpty(filePath)) {
            return;
        }

        Bitmap bitmap = BitmapDecoder.decodeSampledBitmapFromFile(filePath, 1000, 1000);
        if (null == bitmap) {
            return;
        }

        zoomImageView.setImageBitmap(bitmap);
        zoomImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageActivity.this.finish();
            }
        });
    }

}
