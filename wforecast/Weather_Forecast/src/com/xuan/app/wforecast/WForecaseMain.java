package com.xuan.app.wforecast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dazzle.bigappleui.lettersort.entity.ItemContent;
import com.dazzle.bigappleui.lettersort.view.LetterSortView;
import com.dazzle.bigappleui.slidingmenu.SlidingMenu;
import com.dazzle.bigappleui.slidingmenu.SlidingMenu.OnCloseListener;
import com.dazzle.bigappleui.slidingmenu.SlidingMenu.OnOpenListener;
import com.winupon.andframe.bigapple.ioc.AnActivity;
import com.winupon.andframe.bigapple.ioc.InjectView;

/**
 * 天气预报主界面
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-5-9 下午4:26:18 $
 */
public class WForecaseMain extends AnActivity {
    @InjectView(R.id.titleLeftBtn)
    private Button titleLeftBtn;

    @InjectView(R.id.titleText)
    private TextView titleText;

    private Handler handler = new Handler();

    private SlidingMenu slidingMenu;
    private View above;
    private View behindLeft;
    private View behindRight;

    private LetterSortView letterSortView;
    private HashMap<String, String> regionSourceMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        titleLeftBtn.setText("杭州");

        titleLeftBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingMenu.toggle();
            }
        });
        initSlidingMenu();
        initRegion();
    }

    // 初始化地区选择
    private void initRegion() {
        // 初始化地区数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                regionSourceMap = RegionSource.getRegionSourceMap(WForecaseMain.this);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        List<ItemContent> dataList = new ArrayList<ItemContent>(regionSourceMap.size());
                        for (Entry<String, String> entry : regionSourceMap.entrySet()) {
                            ItemContent ic = new ItemContent(entry.getKey(), entry.getValue());
                            dataList.add(ic);
                        }

                        // 初始化控件
                        letterSortView = (LetterSortView) behindLeft.findViewById(R.id.letterSortView);
                        letterSortView.setLetterShow((TextView) LayoutInflater.from(WForecaseMain.this).inflate(
                                R.layout.region_item_letter, null));
                        letterSortView.getListView().setDividerHeight(0);
                        letterSortView.getListView().setAdapter(new RegionListAdapter(dataList, WForecaseMain.this));
                    }
                });
            }
        }).start();
    }

    // 初始化侧滑控件
    private void initSlidingMenu() {
        above = LayoutInflater.from(this).inflate(R.layout.above, null);
        behindLeft = LayoutInflater.from(this).inflate(R.layout.behind_left, null);
        behindRight = LayoutInflater.from(this).inflate(R.layout.behind_right, null);

        slidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);// 左右都可以侧滑模式
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 全屏都可以触发侧滑（指定view除外）

        // 把三个界面放入侧滑容器里
        slidingMenu.setContent(above);
        slidingMenu.setMenu(behindLeft);
        slidingMenu.setSecondaryMenu(behindRight);

        // 背景菜单出现比例，0表示不动
        slidingMenu.setBehindOffset(0);
        slidingMenu.setBehindScrollScale(0);

        // 侧滑时背景的渐变效果
        slidingMenu.setFadeEnabled(true);
        slidingMenu.setFadeDegree(0.8f);

        slidingMenu.setOnOpenListener(new OnOpenListener() {
            @Override
            public void onOpen() {
                titleText.setText("选下所在地区哦");
                titleLeftBtn.setText("返回");
            }
        });

        slidingMenu.setOnCloseListener(new OnCloseListener() {
            @Override
            public void onClose() {
                titleText.setText("天气预报");
                titleLeftBtn.setText("杭州");
            }
        });

        slidingMenu.setSecondaryOnOpenListner(new OnOpenListener() {
            @Override
            public void onOpen() {
                titleText.setText("彩蛋哦");
                titleLeftBtn.setText("返回");
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (slidingMenu.isMenuShowing()) {
            slidingMenu.showContent();
        }
        else {
            super.onBackPressed();
        }
    }

}
