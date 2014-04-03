/* 
 * @(#)DoodleLineActivity.java    Created on 2014-4-3
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.tuya;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xuan.tuya.ioc.InjectView;
import com.xuan.tuya.view.DoodleLineView;

/**
 * 涂鸦画线
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-4-3 下午3:25:09 $
 */
public class DoodleLineActivity extends BasicActivity {
    @InjectView(R.id.returnBtn)
    private Button returnBtn;

    @InjectView(R.id.doodleLayout)
    private RelativeLayout doodleLayout;

    @InjectView(R.id.penBtn)
    private ImageView penBtn;

    @InjectView(R.id.penBack)
    private ImageView penBack;

    private DoodleLineView doodleLineView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doodle_line);

        doodleLineView = new DoodleLineView(this);
        doodleLineView.initBgPicBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.test));
        doodleLayout.addView(doodleLineView);

        penBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doodleLineView.isDraw()) {
                    // 设置移动状态
                    penBtn.setImageResource(R.drawable.tabbtn5_imgedit_normal);
                    doodleLineView.setDraw(false);
                }
                else {
                    // 设置涂鸦状态
                    penBtn.setImageResource(R.drawable.tabbtn5_imgedit_sel);
                    doodleLineView.setDraw(true);
                }
            }
        });
    }

}
