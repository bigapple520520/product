/* 
 * @(#)MainActivity.java    Created on 2014-4-9
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.tuya;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dazzle.bigappleui.viewpage.ViewPage;
import com.feiwoone.banner.AdBanner;
import com.feiwoone.banner.RecevieAdListener;
import com.winupon.andframe.bigapple.utils.HttpUtils;
import com.winupon.andframe.bigapple.utils.Validators;
import com.winupon.andframe.bigapple.utils.sharepreference.PreferenceModel;
import com.xuan.tuya.common.Constants;
import com.xuan.tuya.ioc.InjectView;
import com.xuan.tuya.utils.BitmapDecoder;
import com.xuan.tuya.utils.FileUtils;
import com.xuan.tuya.utils.ImageUtils;
import com.xuan.tuya.utils.LogUtils;

/**
 * 拍照或者从图库选择照片
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-4-9 下午3:12:10 $
 */
public class TestActivity extends BasicActivity {
    private static final boolean DEBUG = false;

    private static final int REQUEST_LOAD_IMAGE_CAMERA = 1;// 从拍照获取图片
    private static final int REQUEST_LOAD_IMAGE_ALBUM = 2;// 从相册获取图片

    private static final int REQUEST_DONE = 3;// 图片涂鸦完成

    @InjectView(R.id.button1)
    private Button button1;

    @InjectView(R.id.button2)
    private Button button2;

    @InjectView(R.id.imageView)
    private ImageView imageView;

    @InjectView(R.id.textView)
    private TextView textView;

    @InjectView(R.id.viewPage)
    private ViewPage viewPage;

    private String appKey = "UKuaw0JctkTymFub60pO4omi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        if (!DEBUG) {
            count();
        }

        initAd();
        initBitmap();

        // 拍照
        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUtils.getImageFromCamera(TestActivity.this, REQUEST_LOAD_IMAGE_CAMERA);
            }
        });

        // 图库选择
        button2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUtils.getImageFromMediaStore(TestActivity.this, REQUEST_LOAD_IMAGE_ALBUM);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
            case REQUEST_LOAD_IMAGE_CAMERA:
                // 拍照处理，已经存放在了Constants.EDIT_PIC_TEMP这个地址下
                break;
            case REQUEST_LOAD_IMAGE_ALBUM:
                // 图库选择图片
                try {
                    FileUtils.createDir(new File(Constants.PIC_STORE_PATH));
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    FileUtils.inputStreamToFile(new BufferedInputStream(in), Constants.EDIT_PIC);
                    FileUtils.copyFile(new File(Constants.EDIT_PIC), new File(Constants.EDIT_PIC_TEMP));
                }
                catch (Exception e) {
                    LogUtils.e("", e);
                }
                break;
            case REQUEST_DONE:
                // 涂鸦完成
                initBitmap();
                break;
            }

            if (REQUEST_DONE != requestCode) {
                // 完成后就不跳转
                Intent intent = new Intent();
                intent.setClass(TestActivity.this, DoodleMainActivity.class);
                startActivityForResult(intent, REQUEST_DONE);
            }
        }
    }

    private void initBitmap() {
        viewPage.removeAllViews();
        textView.setText("注意：编辑好的图片存放在下面这个文件夹下，你自己去取吧。\n" + Constants.PIC_STORE_PATH);
        try {
            File picDir = new File(Constants.PIC_STORE_PATH);
            if (picDir.exists()) {
                File[] files = picDir.listFiles();
                if (!Validators.isEmpty(files)) {
                    for (File file : files) {
                        final String filePath = file.getPath();
                        if (filePath.contains(Constants.EDIT_PIC) || filePath.contains(Constants.EDIT_PIC_TEMP)) {
                            continue;
                        }

                        ImageView imageView = (ImageView) LayoutInflater.from(this).inflate(R.layout.image, null);
                        Bitmap bitmap = BitmapDecoder.decodeSampledBitmapFromFile(filePath, 500, 500);
                        imageView.setImageBitmap(bitmap);
                        viewPage.addView(imageView);

                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setClass(TestActivity.this, ImageActivity.class);
                                intent.putExtra("image.path", filePath);
                                startActivity(intent);
                            }
                        });
                    }
                }
            }

        }
        catch (Exception e) {
            // Ignore
        }

        if (viewPage.getChildCount() == 0) {
            ImageView imageView = (ImageView) LayoutInflater.from(this).inflate(R.layout.image, null);
            viewPage.addView(imageView);
        }

        viewPage.setToScreen(0);
    }

    // 飞沃广告
    private void initAd() {
        final RelativeLayout adLayout = (RelativeLayout) findViewById(R.id.adLayout);
        AdBanner adBanner = new AdBanner(this);
        adLayout.addView(adBanner);
        adBanner.setAppKey(appKey);
        RecevieAdListener adListener = new RecevieAdListener() {
            @Override
            public void onSucessedRecevieAd(AdBanner adView) {
                adLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailedToRecevieAd(AdBanner adView) {
                adLayout.setVisibility(View.GONE);
            }
        };
        adBanner.setRecevieAdListener(adListener);
    }

    // 第一次安装通知服务器
    private void count() {
        try {
            boolean isUpLoad = PreferenceModel.instance(this).getBoolean("is.upload.tuya", false);
            if (!isUpLoad) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("key", "xuan15858178400");
                        map.put("apk", "tuya-anzhi");
                        String message = HttpUtils.requestURLPost("http://blog.xuanner.com/apk-count/up.php", map);
                        if (!Validators.isEmpty(message) && "1".equals(message.trim().substring(1))) {
                            PreferenceModel.instance(TestActivity.this).putBoolean("is.upload.tuya", true);
                        }
                    }
                }).start();
            }
        }
        catch (Exception e) {
            // Ignore
        }
    }

}
