package com.xuan.tuya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.xuan.tuya.common.Constants;
import com.xuan.tuya.utils.FileUtils;
import com.xuan.tuya.utils.ImageUtils;
import com.xuan.tuya.utils.ToastUtils;
import com.xuan.tuya.view.PointView;
import com.xuan.tuya.view.TuyaPicView;
import com.xuan.tuya.vinject.InjectView;

public class TuyaMain extends BasicActivity {
    private static final int REQUEST_OPERATE_TRANSLUCENT_ACTIVITY = 1;

    @InjectView(R.id.tuYaPicView)
    private TuyaPicView tuyaPicView;// 涂鸦的view

    // 选择操作
    @InjectView(R.id.backBtn)
    private Button backBtn;// 撤消上一步按钮

    @InjectView(R.id.clearBtn)
    private Button clearBtn;// 清除所有

    @InjectView(R.id.saveBtn)
    private Button saveBtn;// 保存图片按钮

    @InjectView(R.id.brushBtn)
    private Button brushBtn;// 切换选择画笔颜色和大小

    @InjectView(R.id.picBtn)
    private Button picBtn;

    // 画笔设置
    @InjectView(R.id.penOperate)
    private View penOperate;

    @InjectView(R.id.pen_operate_color_layout)
    private View pen_operate_color_layout;

    @InjectView(R.id.pen_size_btn)
    private Button pen_size_btn;

    @InjectView(R.id.pen_operate_size_layout)
    private View pen_operate_size_layout;

    @InjectView(R.id.pen_color_btn)
    private Button pen_color_btn;

    // 颜色按钮
    @InjectView(R.id.colorBlue)
    private ImageButton colorBlue;

    @InjectView(R.id.colorGreen)
    private ImageButton colorGreen;

    @InjectView(R.id.colorYellow)
    private ImageButton colorYellow;

    @InjectView(R.id.colorOrange)
    private ImageButton colorOrange;

    @InjectView(R.id.colorRed)
    private ImageButton colorRed;

    @InjectView(R.id.colorBlack)
    private ImageButton colorBlack;

    private ImageButton lastColorBtn;

    // 笔画大小
    @InjectView(R.id.pen_size_6)
    private PointView pen_size_6;

    @InjectView(R.id.pen_size_5)
    private PointView pen_size_5;

    @InjectView(R.id.pen_size_4)
    private PointView pen_size_4;

    @InjectView(R.id.pen_size_3)
    private PointView pen_size_3;

    @InjectView(R.id.pen_size_2)
    private PointView pen_size_2;

    @InjectView(R.id.pen_size_1)
    private PointView pen_size_1;

    private PointView lastSizebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initWidgits();
        initPenColorBtn();
        initPenSizeBtn();

        lastColorBtn = colorBlack;
        colorBlack.setImageResource(R.drawable.doodle_pen_color_sel);

        lastSizebtn = pen_size_3;
        pen_size_3.setBackgroundResource(R.drawable.doodle_pen_size_sel);
    }

    private void initPenSizeBtn() {
        pen_size_1.setStrokeWidth(6);
        pen_size_1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshSizeBtn(pen_size_1, 4);
            }
        });

        pen_size_2.setStrokeWidth(10);
        pen_size_2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshSizeBtn(pen_size_2, 6);
            }
        });

        pen_size_3.setStrokeWidth(14);
        pen_size_3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshSizeBtn(pen_size_3, 8);
            }
        });

        pen_size_4.setStrokeWidth(18);
        pen_size_4.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshSizeBtn(pen_size_4, 10);
            }
        });

        pen_size_5.setStrokeWidth(22);
        pen_size_5.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshSizeBtn(pen_size_5, 12);
            }
        });

        pen_size_6.setStrokeWidth(26);
        pen_size_6.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshSizeBtn(pen_size_6, 14);
            }
        });
    }

    private void initPenColorBtn() {
        colorBlue.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshColorBtn(colorBlue, R.color.doodle_pan_blue);
            }
        });
        colorGreen.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshColorBtn(colorGreen, R.color.doodle_pan_green);
            }
        });
        colorYellow.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshColorBtn(colorYellow, R.color.doodle_pan_yellow);
            }
        });
        colorOrange.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshColorBtn(colorOrange, R.color.doodle_pan_orange);
            }
        });
        colorRed.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshColorBtn(colorRed, R.color.doodle_pan_red);
            }
        });
        colorBlack.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshColorBtn(colorBlack, R.color.doodle_pan_black);
            }
        });
    }

    private void initWidgits() {
        picBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // editPhotoBg.setVisibility(View.VISIBLE);
                Intent intent = new Intent();
                intent.setClass(TuyaMain.this, OperateTranslucentActivity.class);
                startActivityForResult(intent, REQUEST_OPERATE_TRANSLUCENT_ACTIVITY);
            }
        });

        pen_size_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pen_operate_size_layout.setVisibility(View.VISIBLE);
                pen_operate_color_layout.setVisibility(View.GONE);
            }
        });

        pen_color_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pen_operate_color_layout.setVisibility(View.VISIBLE);
                pen_operate_size_layout.setVisibility(View.GONE);
            }
        });

        // 撤销前一步
        backBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                tuyaPicView.undo();
            }
        });

        // 清除所有
        clearBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                tuyaPicView.redo();
            }
        });

        // 画笔设置
        brushBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (penOperate.getVisibility() == View.GONE) {
                    penOperate.setVisibility(View.VISIBLE);
                }
                else {
                    penOperate.setVisibility(View.GONE);
                }
            }
        });

        // 保存图片
        saveBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filePath = Constants.PIC_STORE_PATH + "test.jpg";
                boolean isSave = FileUtils.saveToSDCard(filePath, tuyaPicView.getTuyaBitmap());

                if (isSave) {
                    ToastUtils.displayTextShort(TuyaMain.this, "图片已经保存在：" + filePath);
                }
                else {
                    ToastUtils.displayTextShort(TuyaMain.this, "不好意思图片保存失败");
                }
            }
        });
    }

    // 选画笔颜色
    private void refreshColorBtn(ImageButton curImageBtn, int colorResid) {
        if (null != lastColorBtn) {
            lastColorBtn.setImageDrawable(null);
        }

        lastColorBtn = curImageBtn;
        curImageBtn.setImageResource(R.drawable.doodle_pen_color_sel);

        // 切换画笔
        tuyaPicView.initPaint(getResources().getColor(colorResid), tuyaPicView.getPaint().getStrokeWidth());
    }

    // 选画笔大小
    private void refreshSizeBtn(PointView curPointView, int penSize) {
        if (null != lastSizebtn) {
            lastSizebtn.setBackgroundDrawable(null);
        }

        lastSizebtn = curPointView;
        curPointView.setBackgroundResource(R.drawable.doodle_pen_size_sel);

        // 切换画笔
        tuyaPicView.initPaint(tuyaPicView.getPaint().getColor(), penSize);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (REQUEST_OPERATE_TRANSLUCENT_ACTIVITY == requestCode) {
                String fileName = data.getStringExtra(OperateTranslucentActivity.RET_FILENAME);
                tuyaPicView.initBgPicBitmap(ImageUtils.decodeSampledBitmapFromFile(fileName, tuyaPicView.getWidth(),
                        tuyaPicView.getHeight()));
            }
        }

    }

}
