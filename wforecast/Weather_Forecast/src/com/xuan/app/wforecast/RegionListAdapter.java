/* 
 * @(#)DefaultLetterSortAdapter.java    Created on 2013-7-16
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.app.wforecast;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dazzle.bigappleui.lettersort.entity.ItemContent;
import com.dazzle.bigappleui.lettersort.entity.ItemDivide;
import com.dazzle.bigappleui.lettersort.view.LetterSortAdapter;

/**
 * 地区选择
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-7-16 下午5:00:36 $
 */
public class RegionListAdapter extends LetterSortAdapter {
    private final Context context;

    public RegionListAdapter(List<ItemContent> fromList, Context context) {
        super(fromList, context);
        this.context = context;
    }

    @Override
    public View drawItemContent(int position, View convertView, ViewGroup parent, ItemContent itemContent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.region_item, null);
        TextView leftText = (TextView) convertView.findViewById(R.id.leftText);
        leftText.setText(itemContent.getName());
        return convertView;
    }

    @Override
    public View drawItemDivide(int position, View convertView, ViewGroup parent, ItemDivide itemDivide) {
        TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.region_item_split, null);
        textView.setText(itemDivide.getLetter());
        return textView;
    }

}
