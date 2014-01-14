package com.chrislee.tetris;

import android.os.Handler;
import android.os.Message;

/**
 * 刷新处理
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-1-14 下午6:34:26 $
 */
public class RefreshHandler extends Handler {
    final static int MESSAGE_REFRESH = 0xeeeeeeee;

    final static int DELAY_MILLIS = 100;
    TetrisView tetrisView = null;
    boolean isPaused = false;

    public RefreshHandler(TetrisView tetrisView) {
        super();
        this.tetrisView = tetrisView;
    }

    public void handleMessage(Message message) {
        if (!isPaused) {
            if (message.what == MESSAGE_REFRESH) {
                tetrisView.logic();
                tetrisView.invalidate();
            }
        }
    }

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
    }

}
