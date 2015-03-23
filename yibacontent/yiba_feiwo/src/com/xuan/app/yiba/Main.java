package com.xuan.app.yiba;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feiwoone.banner.AdBanner;
import com.feiwoone.banner.RecevieAdListener;
import com.umeng.analytics.MobclickAgent;
import com.xuan.app.yiba.slidingupdown.SlidingUpDown;
import com.xuan.app.yiba.slidingupdown.SlidingUpDown.OpenPercentListener;
import com.xuan.app.yiba.utils.Utils;

/**
 * 翻译吧主界面
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-5-5 下午1:45:55 $
 */
public class Main extends Activity {
    private Button button; // 走一个按钮
    private EditText fromWordEditText;// 输入
    private TextView toWordTextView;// 输出
    private TextView explain;
    private TextView explainWord;

    private final Handler handler = new Handler();
    private ProgressDialog progressDialog;

    private SlidingUpDown slidingUpDown;
    private View aboveView;
    private View behindView;

    private String appKey = "aucNzN727rnYDSH867J32iIl";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        // 上下滑动
        initSliding();

        // 广告
        initAd();

        button = (Button) aboveView.findViewById(R.id.button);
        fromWordEditText = (EditText) aboveView.findViewById(R.id.fromWord);
        toWordTextView = (TextView) aboveView.findViewById(R.id.toWord);
        explain = (TextView) aboveView.findViewById(R.id.explain);
        explainWord = (TextView) aboveView.findViewById(R.id.explainWord);

        progressDialog = new ProgressDialog(this);

        explainWord.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (TextUtils.isEmpty(explainWord.getText().toString())) {
                    return false;
                }

                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(explainWord.getText().toString());
                Toast.makeText(Main.this, "亲，别的解释已复制了哦，去别地黏贴吧", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        toWordTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (TextUtils.isEmpty(toWordTextView.getText().toString())) {
                    return false;
                }

                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(toWordTextView.getText().toString());
                Toast.makeText(Main.this, "亲，译文已复制了哦，去别地黏贴吧", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 判断网络
                if (!Utils.hasNetwork(Main.this)) {
                    Toast.makeText(Main.this, "亲，网络不可用哦", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 隐藏键盘输入
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }

                // 处理翻译
                final String fromWord = fromWordEditText.getEditableText().toString();

                if (TextUtils.isEmpty(fromWord)) {
                    Toast.makeText(Main.this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                Utils.showDialog("我在奋力翻译中...", progressDialog);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final List<String> explainsList = new ArrayList<String>();
                            final String toWord = Utils.getTranslateWord(fromWord, explainsList);// 查询有道翻译API
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    explain.setText("");
                                    explainWord.setText("");

                                    toWordTextView.setText(toWord);

                                    // 显示解释
                                    if (!explainsList.isEmpty()) {
                                        explain.setText("一些别的解释：");
                                        StringBuilder sb = new StringBuilder();
                                        for (String explainWord : explainsList) {
                                            sb.append(explainWord).append("，");
                                        }

                                        explainWord.setText(sb.toString().substring(0, sb.length() - 1));
                                    }
                                }
                            });
                        }
                        finally {
                            Utils.dismissDialog(handler, progressDialog);
                        }
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    // 向上滑动
    private void initSliding() {
        aboveView = LayoutInflater.from(this).inflate(R.layout.above, null);
        behindView = LayoutInflater.from(this).inflate(R.layout.behind, null);

        // 设置侧滑界面参数
        slidingUpDown = (SlidingUpDown) findViewById(R.id.slidingUpDown);
        slidingUpDown.setAboveContent(aboveView);
        slidingUpDown.setBehindContent(behindView);

        // 参数配置
        slidingUpDown.setMode(SlidingUpDown.MODE_UP);

        slidingUpDown.setFadeEnabled(true);
        slidingUpDown.setFadeDegree(0.99f);

        // 设置打开的渐变
        slidingUpDown.setOpenPercentListener(new OpenPercentListener() {
            @Override
            public void openPercent(float percent) {
                int padding = (int) (40 * Math.abs(1 - percent)) + 5;
                behindView.setPadding(padding, padding, padding, padding);
            }
        });
    }

    // 初始化有米广告
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

}
