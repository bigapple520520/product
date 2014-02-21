/* 
 * @(#)OperateTranslucentActivity.java    Created on 2014-2-21
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.tuya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xuan.tuya.common.Constants;
import com.xuan.tuya.utils.ImageUtils;
import com.xuan.tuya.vinject.InjectView;

/**
 * 半透明Activity，显示操作界面
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-2-21 下午1:51:40 $
 */
public class OperateTranslucentActivity extends BasicActivity {
    private static final int LOAD_IMAGE_FROM_ALBUM = 1;// 从相册获取图片
    private static final int LOAD_IMAGE_FROM_CAMERA = 2;// 从拍照获取图片

    public static final String RET_FILENAME = "ret.filename";

    @InjectView(R.id.takePicBtn)
    private Button takePicBtn;

    @InjectView(R.id.localPicBtn)
    private Button localPicBtn;

    @InjectView(R.id.cancelBtn)
    private Button cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operate_translucent_activity);

        cancelBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                OperateTranslucentActivity.this.finish();
            }
        });

        localPicBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUtils.getImageFromMediaStore(OperateTranslucentActivity.this, LOAD_IMAGE_FROM_ALBUM);
            }
        });

        takePicBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUtils.getImageFromCamera(OperateTranslucentActivity.this, LOAD_IMAGE_FROM_CAMERA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String fileName = null;
            if (LOAD_IMAGE_FROM_ALBUM == requestCode) {
                fileName = data.getData().getPath();
            }
            else if (LOAD_IMAGE_FROM_CAMERA == requestCode) {
                fileName = Constants.CAMERA_PIC_FILENAME;
            }

            Intent intent = new Intent();
            intent.putExtra(RET_FILENAME, fileName);
            setResult(RESULT_OK, intent);
        }

        OperateTranslucentActivity.this.finish();
    }

}
