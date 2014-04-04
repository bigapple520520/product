/* 
 * @(#)DoodleColorAdapter.java    Created on 2014-4-4
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.tuya.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xuan.tuya.R;

/**
 * 颜色值对应
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-4-4 上午10:01:58 $
 */
public class DoodleColorAdapter extends BaseAdapter {
    private Context context;
    private List<Integer> colorList = new ArrayList<Integer>();

    public DoodleColorAdapter(Context context) {
        this.context = context;
        Resources resources = context.getResources();
        colorList.add(resources.getColor(R.color.doodle_pen_color_ys1));
        colorList.add(resources.getColor(R.color.doodle_pen_color_ys2));
        colorList.add(resources.getColor(R.color.doodle_pen_color_ys3));
        colorList.add(resources.getColor(R.color.doodle_pen_color_ys4));
        colorList.add(resources.getColor(R.color.doodle_pen_color_ys5));
        colorList.add(resources.getColor(R.color.doodle_pen_color_ys6));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.doodle_line_pencolor_item, null);
        TextView textView = (TextView) convertView.findViewById(R.id.colorItem);
        textView.setBackgroundColor(colorList.get(position));
        return convertView;
    }

    @Override
    public int getCount() {
        return colorList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
