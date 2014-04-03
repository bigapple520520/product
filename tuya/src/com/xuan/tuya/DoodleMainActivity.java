/* 
 * @(#)DoodleMainActivity.java    Created on 2014-4-2
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.tuya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.xuan.tuya.ioc.InjectView;

/**
 * 截图界面
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-4-2 下午2:59:45 $
 */
public class DoodleMainActivity extends BasicActivity {
    @InjectView(R.id.tab0Layout)
    private RelativeLayout tab0Layout;

    @InjectView(R.id.tab1Layout)
    private RelativeLayout tab1Layout;

    @InjectView(R.id.tab2Layout)
    private RelativeLayout tab2Layout;

    @InjectView(R.id.tab3Layout)
    private RelativeLayout tab3Layout;

    @InjectView(R.id.tab4Layout)
    private RelativeLayout tab4Layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doodle_main);

        initTab();
    }

    private void initTab() {
        tab0Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        tab1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        tab2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        tab3Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(DoodleMainActivity.this, DoodleLineActivity.class);
                startActivity(intent);
            }
        });
        tab4Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

}
