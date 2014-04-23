/* 
 * @(#)CropImage.java    Created on 2014-4-11
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.tuya.view.crop;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.xuan.tuya.BasicActivity;
import com.xuan.tuya.R;
import com.xuan.tuya.common.Constants;
import com.xuan.tuya.ioc.InjectView;
import com.xuan.tuya.utils.BitmapDecoder;
import com.xuan.tuya.utils.FileUtils;
import com.xuan.tuya.utils.LogUtils;
import com.xuan.tuya.utils.ToastUtils;

/**
 * 截图界面
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-4-11 下午3:54:58 $
 */
public class CropImageActivity extends BasicActivity {
    @InjectView(R.id.returnBtn)
    private Button returnBtn;

    @InjectView(R.id.rightBtn)
    private Button rightBtn;

    // 截取的宽高比，例如要设置正方形就可以设置：mAspectX=1，mAspectY=1
    private int mAspectX = 0;
    private int mAspectY = 0;

    // 可以设置固定的输入或者输出宽高
    private int mOutputX = 0;
    private int mOutputY = 0;

    private CropImageView mImageView;

    private Bitmap mBitmap;// 原图
    private Bitmap mCroppedImage;// 裁剪后保存的图

    private HighlightView mCrop;// 裁剪区域

    private Handler mHandler = new Handler();

    // 输入输出文件路径
    public static final String PARAM_INPUT_FILENAME = "param.input.filename";
    public static final String PARAM_OUTPUT_FILENAME = "param.output.filename";
    private String inputFileName;
    private String outputFileName;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.doodle_crop);
        mImageView = (CropImageView) findViewById(R.id.image);

        inputFileName = getIntent().getStringExtra(PARAM_INPUT_FILENAME);
        outputFileName = getIntent().getStringExtra(PARAM_OUTPUT_FILENAME);
        if (TextUtils.isEmpty(inputFileName) || TextUtils.isEmpty(outputFileName)) {
            ToastUtils.displayTextShort(this, "输入或者输出文件地址不能为空");
            finish();
        }

        try {
            mBitmap = BitmapDecoder.decodeSampledBitmapFromFile(Constants.EDIT_PIC_TEMP, 1000, 1000);
        }
        catch (OutOfMemoryError e) {
            // IF OOM
            finish();
        }

        // mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test2).copy(
        // android.graphics.Bitmap.Config.ARGB_8888, true);
        try {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mImageView.setImageBitmapResetBase(mBitmap, true, true);
                    if (mImageView.getScale() == 1F) {
                        mImageView.center(true, true, false);
                    }
                    new Thread(mRunFaceDetection).start();
                }
            }, 100);
        }
        catch (Exception e) {
            LogUtils.e("图片加载失败，原因：" + e);
            finish();
        }

        // 返回
        returnBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImageActivity.this.finish();
            }
        });

        // 裁剪
        rightBtn.setText("裁剪");
        rightBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCroppedImage == null) {
                    if (mCrop == null) {
                        ToastUtils.displayTextShort(CropImageActivity.this, "截取区域为空");
                        return;
                    }

                    Rect r = mCrop.getCropRect();
                    int width = r.width();
                    int height = r.height();

                    mCroppedImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    Canvas c1 = new Canvas(mCroppedImage);
                    c1.drawBitmap(mBitmap, r, new Rect(0, 0, width, height), null);
                }

                // 输出的截取图片由自己定义，如果比例不对，会被截取掉
                if (mOutputX != 0 && mOutputY != 0) {
                    Bitmap b = Bitmap.createBitmap(mOutputX, mOutputY, Bitmap.Config.RGB_565);
                    Canvas c1 = new Canvas(b);

                    Rect r = mCrop.getCropRect();
                    int left = (mOutputX / 2) - (r.width() / 2);
                    int top = (mOutputY / 2) - (r.width() / 2);
                    c1.drawBitmap(mBitmap, r, new Rect(left, top, left + r.width(), top + r.height()), null);

                    mCroppedImage = b;
                }

                // 保存图片，如果耗时考虑起线程保存
                OutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(outputFileName);
                    FileUtils.initParentDir(outputFileName);
                    mCroppedImage.compress(Bitmap.CompressFormat.JPEG, 75, outputStream);
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
                finally {
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        }
                        catch (IOException ex) {
                        }
                    }
                }

                setResult(RESULT_OK, getIntent());
                CropImageActivity.this.finish();
            }
        });
    }

    /**
     * 脸部检测任务
     */
    private Runnable mRunFaceDetection = new Runnable() {
        private Matrix mImageMatrix;

        private HighlightView makeHighlightView() {
            return new HighlightView(mImageView);
        }

        private void makeDefault() {
            HighlightView hv = makeHighlightView();

            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();

            Rect imageRect = new Rect(0, 0, width, height);

            // make the default size about 4/5 of the width or height
            int cropWidth = Math.min(width, height) * 4 / 5;
            int cropHeight = cropWidth;

            if (mAspectX != 0 && mAspectY != 0) {
                if (mAspectX > mAspectY) {
                    cropHeight = cropWidth * mAspectY / mAspectX;
                }
                else {
                    cropWidth = cropHeight * mAspectX / mAspectY;
                }
            }

            int x = (width - cropWidth) / 2;
            int y = (height - cropHeight) / 2;

            RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
            hv.setup(mImageMatrix, imageRect, cropRect, false, mAspectX != 0 && mAspectY != 0);
            mImageView.add(hv);
        }

        @Override
        public void run() {
            mImageMatrix = mImageView.getImageMatrix();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    makeDefault();
                    mImageView.invalidate();
                    if (mImageView.mHighlightViews.size() == 1) {
                        mCrop = mImageView.mHighlightViews.get(0);
                        mCrop.setFocus(true);
                    }
                }
            });
        }
    };

}
