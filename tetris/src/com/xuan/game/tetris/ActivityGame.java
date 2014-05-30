package com.xuan.game.tetris;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * 游戏界面
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-1-13 下午7:54:13 $
 */
public class ActivityGame extends Activity {
    private TetrisView tetrisView = null;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        tetrisView = new TetrisView(this);

        Intent intent = getIntent();

        // 设置游戏的难度级别
        int level = intent.getIntExtra(ActivityMain.LEVEL, 1);
        tetrisView.setLevel(level);

        // 判断是否从上次游戏开始
        if (intent.getFlags() == ActivityMain.FLAG_CONTINUE_LAST_GAME) {
            tetrisView.restoreGame();
        }

        // 判断是否开启声音
        boolean isVoice = intent.getBooleanExtra(ActivityMain.VOICE, true);
        tetrisView.setVoice(isVoice);

        setContentView(tetrisView);
    }

    @Override
    public void onPause() {
        super.onPause();
        tetrisView.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        tetrisView.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        tetrisView.saveGame();
        tetrisView.freeResources();
    }

}
